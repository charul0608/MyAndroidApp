package com.example.myandroidapp.ui.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.util.FitPolicy
import java.net.URL
import android.os.StrictMode
import android.util.Log
import com.example.myandroidapp.R
import java.io.File
import java.io.FileOutputStream

class PdfViewerActivity : AppCompatActivity() {

    private lateinit var pdfView: PDFView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        pdfView = findViewById(R.id.pdfView)

//        val url = "https://fssservices.bookxpert.co/GeneratedPDF/Companies/nadc/2024%202025/BalanceSheet.pdf"
        val url = "https://www.aeee.in/wp-content/uploads/2020/08/Sample-pdf.pdf"

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        try {
            val input = URL(url).openStream()
            val file = File.createTempFile("balance_sheet", ".pdf", cacheDir)
            val output = FileOutputStream(file)
            input.copyTo(output)
            output.close()
            input.close()

            pdfView.fromFile(file)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .spacing(10)
                .pageFitPolicy(FitPolicy.BOTH)
                .load()
        } catch (e: Exception) {
            Log.e("PDF_VIEWER", "Error loading PDF", e)
        }
    }
}