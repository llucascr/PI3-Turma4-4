package br.edu.puccampinas.pi3.turma4.superid.functions

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
data class EncryptedData(
    val cipherText: String,
    val salt: String,
    val iv: String
)

@RequiresApi(Build.VERSION_CODES.O)
fun encrypt(password: String, plaintext: String): EncryptedData {
    val salt = ByteArray(saltLength).also { random.nextBytes(it) }
    val iv = ByteArray(ivLength).also { random.nextBytes(it) }

    val key = deriveKey(password, salt)
    val cipher = Cipher.getInstance(cipherAlgorithm)
    cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(iv))
    val encrypted = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))

    // Codificamos em Base64 os dados
    val cipherTextBase64 = Base64.getEncoder().encodeToString(encrypted)
    val saltBase64 = Base64.getEncoder().encodeToString(salt)
    val ivBase64 = Base64.getEncoder().encodeToString(iv)

    return EncryptedData(
        cipherText = cipherTextBase64,
        salt = saltBase64,
        iv = ivBase64
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun decrypt(password: String, cipherText: String, saltBase64: String, ivBase64: String): String {
    val salt = Base64.getDecoder().decode(saltBase64)
    val iv = Base64.getDecoder().decode(ivBase64)
    val ciphertextBytes = Base64.getDecoder().decode(cipherText)

    val key = deriveKey(password, salt)
    val cipher = Cipher.getInstance(cipherAlgorithm)
    cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))

    val decrypted = cipher.doFinal(ciphertextBytes)
    return String(decrypted, Charsets.UTF_8)
}

private fun deriveKey(password: String, salt: ByteArray): SecretKeySpec {
    val factory = SecretKeyFactory.getInstance(algorithm)
    val spec: KeySpec = PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength)
    val tmp = factory.generateSecret(spec)
    return SecretKeySpec(tmp.encoded, "AES")
}