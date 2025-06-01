package br.edu.puccampinas.pi3.turma4.superid.functions

import com.google.mlkit.vision.barcode.common.Barcode

data class QrCodeData(
    val urlSite: String?,
    val apiKey: String?
)

fun extractQrCodeData(barcode: Barcode): QrCodeData {
    return QrCodeData(
        urlSite = barcode.displayValue,
        apiKey = barcode.displayValue
    )
}