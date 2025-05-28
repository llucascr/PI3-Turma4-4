package br.edu.puccampinas.pi3.turma4.superid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.MaterialTheme
import br.edu.puccampinas.pi3.turma4.superid.screens.AutoDismissPopup
import br.edu.puccampinas.pi3.turma4.superid.screens.PopUpScreen
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.ktx.Firebase
import android.util.Log

class QrCodeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startBarcodeScanner()
    }

    private fun startBarcodeScanner() {
        val options = GmsBarcodeScannerOptions.Builder()
            .build()

        val scanner = GmsBarcodeScanning.getClient(this, options)

        scanner.startScan()
            .addOnSuccessListener { barcode: Barcode ->
                val loginToken = barcode.rawValue
                if (!loginToken.isNullOrEmpty()) {
                    updateLoginDocument(loginToken)
                } else {
                    showFailurePopup("QR Code inválido: Token vazio")
                }
            }
            .addOnCanceledListener {
                showFailurePopup("Escaneamento de QR Code cancelado.")
            }
            .addOnFailureListener { e ->
                Log.e("QRCODE_SCANNER", "Erro ao escanear QR Code: ${e.message}")
                showFailurePopup("Erro ao escanear QR Code: ${e.message}")
            }
    }

    private fun updateLoginDocument(loginToken: String) {
        val db = Firebase.firestore
        val currentUser = Firebase.auth.currentUser

        if (currentUser == null) {
            showFailurePopup("Usuário não autenticado no aplicativo.")
            return
        }

        val userUid = currentUser.uid
        val loginDocRef = db.collection("login").document(loginToken)

        // Update the document to include the user's UID and a timestamp for when the login occurred
        val updates = hashMapOf<String, Any>(
            "user" to userUid,
            "loggedInAt" to FieldValue.serverTimestamp()
        )

        loginDocRef.update(updates)
            .addOnSuccessListener {
                Log.d("FIRESTORE", "Documento de login atualizado com sucesso para token: $loginToken com UID: $userUid")
                showSuccessPopup("Login realizado com sucesso via QR Code!")
            }
            .addOnFailureListener { e ->
                Log.e("FIRESTORE", "Erro ao atualizar documento de login: ${e.message}", e)
                showFailurePopup("Erro ao finalizar login via QR Code: ${e.message}")
            }
    }

    private fun showSuccessPopup(message: String) {
        setContent {
            SuperIDTheme {
                PopUpScreen(message, Icons.Default.CheckCircle, MaterialTheme.colorScheme.primary) {
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
    }

    private fun showFailurePopup(message: String) {
        setContent {
            SuperIDTheme {
                AutoDismissPopup(
                    message = message,
                    icon = Icons.Default.Cancel,
                    iconColor = MaterialTheme.colorScheme.error
                ) {
                    setResult(RESULT_CANCELED)
                    finish()
                }
            }
        }
    }
}