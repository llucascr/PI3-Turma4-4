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
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import java.util.Locale


class QrCodeActivity : ComponentActivity() {
    private var allowManualInput = false
    private var enableAutoZoom = false
    private var barcodeResultView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        barcodeResultView = findViewById(R.id.barcode_result_view)
    }

    fun onAllowManualInputCheckboxClicked(view: View) {
        allowManualInput = (view as CheckBox).isChecked
    }

    fun onEnableAutoZoomCheckboxClicked(view: View) {
        enableAutoZoom = (view as CheckBox).isChecked
    }

    fun onScanButtonClicked(view: View) {
        val optionsBuilder = GmsBarcodeScannerOptions.Builder()
        if (allowManualInput) {
            optionsBuilder.allowManualInput()
        }
        if (enableAutoZoom) {
            optionsBuilder.enableAutoZoom()
        }
        val gmsBarcodeScanner = GmsBarcodeScanning.getClient(this, optionsBuilder.build())
        gmsBarcodeScanner
            .startScan()
            .addOnSuccessListener { barcode: Barcode ->
                barcodeResultView!!.text = getSuccessfulMessage(barcode)
            }
            .addOnFailureListener { e: Exception -> barcodeResultView!!.text = getErrorMessage(e) }
            .addOnCanceledListener {
                barcodeResultView!!.text = getString(R.string.error_scanner_cancelled)
            }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean(KEY_ALLOW_MANUAL_INPUT, allowManualInput)
        savedInstanceState.putBoolean(KEY_ENABLE_AUTO_ZOOM, enableAutoZoom)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        allowManualInput = savedInstanceState.getBoolean(KEY_ALLOW_MANUAL_INPUT)
        enableAutoZoom = savedInstanceState.getBoolean(KEY_ENABLE_AUTO_ZOOM)
    }

    private fun getSuccessfulMessage(barcode: Barcode): String {
        val barcodeValue =
            String.format(
                Locale.US,
                "Display Value: %s\nRaw Value: %s\nFormat: %s\nValue Type: %s",
                barcode.displayValue,
                barcode.rawValue,
                barcode.format,
                barcode.valueType
            )
        return getString(R.string.barcode_result, barcodeValue)
    }

    private fun getErrorMessage(e: Exception): String? {
        return if (e is MlKitException) {
            when (e.errorCode) {
                MlKitException.CODE_SCANNER_CAMERA_PERMISSION_NOT_GRANTED ->
                    getString(R.string.error_camera_permission_not_granted)
                MlKitException.CODE_SCANNER_APP_NAME_UNAVAILABLE ->
                    getString(R.string.error_app_name_unavailable)
                else -> getString(R.string.error_default_message, e)
            }
        } else {
            e.message
        }
    }

    companion object {
        private const val KEY_ALLOW_MANUAL_INPUT = "allow_manual_input"
        private const val KEY_ENABLE_AUTO_ZOOM = "enable_auto_zoom"
    }
}