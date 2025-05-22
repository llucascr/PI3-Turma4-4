package br.edu.puccampinas.pi3.turma4.superid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.edu.puccampinas.pi3.turma4.superid.ui.theme.SuperIDTheme
import com.example.cameraapp2.permissions.WithPermission
import android.Manifest
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import br.edu.puccampinas.pi3.turma4.superid.screens.AutoDismissPopup
import br.edu.puccampinas.pi3.turma4.superid.screens.PopUpScreen
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import org.json.JSONObject
import java.util.Locale


class QrCodeActivity : ComponentActivity() {
    private var siteUrl: String? = null
    private var apiKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startBarcodeScanner()
    }

    private fun startBarcodeScanner() {
        val options = GmsBarcodeScannerOptions.Builder()
            .enableAutoZoom()
            .build()

        val scanner = GmsBarcodeScanning.getClient(this, options)

        scanner.startScan()
            .addOnSuccessListener { barcode: Barcode ->
                val rawValue = barcode.rawValue
                try {
                    val json = JSONObject(rawValue)
                    if (json.has("siteUrl") && json.has("apiKey")) {
                        siteUrl = json.getString("siteUrl")
                        apiKey = json.getString("apiKey")

                        showSuccessPopup()
                    } else {
                        showFailurePopup()
                    }
                } catch (e: Exception) {
                    showFailurePopup()
//                    Toast.makeText(this, "QR Code inválido", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnCanceledListener {
                showFailurePopup()
//                Toast.makeText(this, "QR Code cancelado", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showSuccessPopup() {
        setContent {
            SuperIDTheme {
                PopUpScreen("QrCode encontrado!", Icons.Default.CheckCircle, MaterialTheme.colorScheme.primary) {
                    val resultIntent = intent.apply {
                        putExtra("siteUrl", siteUrl)
                        putExtra("apiKey", apiKey)
                    }
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            }
        }
    }

    private fun showFailurePopup() {
        setContent {
            SuperIDTheme {
                AutoDismissPopup(
                    message = "QrCode não encontrado!",
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