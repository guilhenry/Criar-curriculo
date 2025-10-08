package com.merc.criarcurriculo

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.OutputStream


class ReviwActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reviw)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        trocainfpess()
       /* val scroll = findViewById<ScrollView>(R.id.scrollCurriculo)
        val btnExport = findViewById<Button>(R.id.btnExportPdf)

        btnExport.setOnClickListener {
            exportToPdf(scroll, btnExport)
        }*/


    }
    fun trocainfpess(){
        val ddpes = Pessoal.InfoPessoal

        val trocnm = findViewById<TextView>(R.id.tvName)
        val trocpes = findViewById<TextView>(R.id.tvInfoPessoal)
        val trctt = findViewById<TextView>(R.id.tvContact)
        val trform = findViewById<TextView>(R.id.tvFormacao)
        val trcurs = findViewById<TextView>(R.id.tvCursos)
        val trexp = findViewById<TextView>(R.id.tvExperiencia)

        if (ddpes.curso == "") {
            val fixcu = findViewById<TextView>(R.id.fixcr)
            val minhaView = findViewById<View>(R.id.fixcr1)
            trcurs.visibility = View.GONE
            fixcu.visibility = View.GONE
            minhaView.visibility = View.GONE
        }




        trocnm.setText(ddpes.nome)
        trctt.setText(ddpes.cont)
        trocpes.setText(ddpes.end)
        trform.setText(ddpes.grau)
        trcurs.setText(ddpes.curso)
        trexp.setText(ddpes.experi)
    }



         private fun exportToPdf(scrollView: ScrollView, hiddenView: View) {
             // Esconde o botão
             hiddenView.visibility = View.GONE

             val content = scrollView.getChildAt(0)

             // Mede todo o conteúdo do ScrollView
             val widthSpec = View.MeasureSpec.makeMeasureSpec(scrollView.width, View.MeasureSpec.EXACTLY)
             val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
             content.measure(widthSpec, heightSpec)
             content.layout(0, 0, content.measuredWidth, content.measuredHeight)

             val totalWidth = content.measuredWidth
             val totalHeight = content.measuredHeight

             // Renderiza todo o conteúdo em um Bitmap
             val fullBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888)
             val canvas = Canvas(fullBitmap)
             content.draw(canvas)

             // Tamanho de uma folha A4 (72 dpi → 595x842 px)
             val pageWidth = 595
             val pageHeight = 842

             // Define margem em px (ajuste se quiser mais ou menos)
             val margin = 40
             val usableWidth = pageWidth - margin * 2
             val usableHeight = pageHeight - margin * 2

             val document = PdfDocument()
             val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
             val page = document.startPage(pageInfo)
             val pdfCanvas = page.canvas

             // Calcula escala proporcional para caber dentro da área útil
             val scaleX = usableWidth.toFloat() / totalWidth.toFloat()
             val scaleY = usableHeight.toFloat() / totalHeight.toFloat()
             val scale = minOf(scaleX, scaleY)

             // Aplica margens e centraliza
             val offsetX = margin + (usableWidth - totalWidth * scale) / 2f
             val offsetY = margin + (usableHeight - totalHeight * scale) / 2f

             pdfCanvas.save()
             pdfCanvas.translate(offsetX, offsetY)
             pdfCanvas.scale(scale, scale)
             pdfCanvas.drawBitmap(fullBitmap, 0f, 0f, null)
             pdfCanvas.restore()

             document.finishPage(page)
             fullBitmap.recycle()

             val filename = "curriculo_${System.currentTimeMillis()}.pdf"

             try {
                 val out: OutputStream? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                     val contentValues = ContentValues().apply {
                         put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                         put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                         put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/Curriculos")
                     }
                     val uri = contentResolver.insert(
                         MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                         contentValues
                     )
                     uri?.let { contentResolver.openOutputStream(it) }
                 } else {
                     openFileOutput(filename, MODE_PRIVATE)
                 }

                 out?.use {
                     document.writeTo(it)
                     Toast.makeText(this, "PDF salvo em Download/Curriculos", Toast.LENGTH_LONG).show()
                 }
             } catch (e: Exception) {
                 Toast.makeText(this, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
             } finally {
                 document.close()
                 hiddenView.visibility = View.VISIBLE
             }
         }

}


