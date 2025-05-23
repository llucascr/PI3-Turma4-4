import android.os.Build
import androidx.annotation.RequiresApi
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import java.util.Base64


private const val iterationCount = 65536
    private const val keyLength = 256
    private const val algorithm = "PBKDF2WithHmacSHA256"
    private const val cipherAlgorithm = "AES/CBC/PKCS5Padding"
    private const val saltLength = 16
    private const val ivLength = 16

    private val random = SecureRandom()

    @RequiresApi(Build.VERSION_CODES.O)
    fun encrypt(password: String, plaintext: String): String {
        val salt = ByteArray(saltLength).also { random.nextBytes(it) }
        val iv = ByteArray(ivLength).also { random.nextBytes(it) }

        val key = deriveKey(password, salt)
        val cipher = Cipher.getInstance(cipherAlgorithm)
        cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(iv))
        val encrypted = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))

        // Vamos concatenar salt + iv + ciphertext e codificar em Base64
        val combined = ByteArray(salt.size + iv.size + encrypted.size)
        System.arraycopy(salt, 0, combined, 0, salt.size)
        System.arraycopy(iv, 0, combined, salt.size, iv.size)
        System.arraycopy(encrypted, 0, combined, salt.size + iv.size, encrypted.size)

        return Base64.getEncoder().encodeToString(combined)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decrypt(password: String, base64CipherText: String): String {
        val combined = Base64.getDecoder().decode(base64CipherText)

        // extrair salt, iv e ciphertext
        val salt = combined.copyOfRange(0, saltLength)
        val iv = combined.copyOfRange(saltLength, saltLength + ivLength)
        val ciphertext = combined.copyOfRange(saltLength + ivLength, combined.size)

        val key = deriveKey(password, salt)
        val cipher = Cipher.getInstance(cipherAlgorithm)
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))

        val decrypted = cipher.doFinal(ciphertext)
        return String(decrypted, Charsets.UTF_8)
    }

    private fun deriveKey(password: String, salt: ByteArray): SecretKeySpec {
        val factory = SecretKeyFactory.getInstance(algorithm)
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength)
        val tmp = factory.generateSecret(spec)
        return SecretKeySpec(tmp.encoded, "AES")
    }


@RequiresApi(Build.VERSION_CODES.O)
fun main() {
    val password = "minhaSenhaSuperSecreta"
    val textoOriginal = "Texto para criptografar com PBE + AES + PKCS5Padding"

    val criptografado = encrypt(password, textoOriginal)
    println("Texto criptografado: $criptografado")

    val textoDescriptografado = decrypt(password, criptografado)
    println("Texto descriptografado: $textoDescriptografado")
}
