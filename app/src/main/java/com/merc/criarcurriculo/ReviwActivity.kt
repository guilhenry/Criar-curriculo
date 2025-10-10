package com.merc.criarcurriculo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
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
      val scroll = findViewById<ScrollView>(R.id.scrollCurriculo)
        val btnExport = findViewById<Button>(R.id.btnExportPdf)

        btnExport.setOnClickListener {
            exportToPdf(scroll, btnExport)
        }

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
        hiddenView.visibility = View.GONE

        val content = scrollView.getChildAt(0)
        val widthSpec = View.MeasureSpec.makeMeasureSpec(scrollView.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        content.measure(widthSpec, heightSpec)
        content.layout(0, 0, content.measuredWidth, content.measuredHeight)

        val totalWidth = content.measuredWidth
        val totalHeight = content.measuredHeight

        val fullBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(fullBitmap)
        content.draw(canvas)

        val pageWidth = 595
        val pageHeight = 842
        val margin = 40
        val usableWidth = pageWidth - margin * 2
        val usableHeight = pageHeight - margin * 2

        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = document.startPage(pageInfo)
        val pdfCanvas = page.canvas

        val scaleX = usableWidth.toFloat() / totalWidth.toFloat()
        val scaleY = usableHeight.toFloat() / totalHeight.toFloat()
        val scale = minOf(scaleX, scaleY)
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
        var fileUri: Uri? = null

        try {
            val out: OutputStream? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/Curriculos")
                }
                fileUri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                fileUri?.let { contentResolver.openOutputStream(it) }
            } else {
                val file = File(getExternalFilesDir(null), filename)
                fileUri = FileProvider.getUriForFile(this, "${packageName}.provider", file)
                openFileOutput(filename, MODE_PRIVATE)
            }

            out?.use {
                document.writeTo(it)
                Toast.makeText(this, "PDF salvo em Download/Curriculos", Toast.LENGTH_LONG).show()

                if (fileUri != null) {
                    sendClickableNotification(filename, fileUri!!)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            document.close()
            hiddenView.visibility = View.VISIBLE
        }
    }

    private fun sendClickableNotification(filename: String, fileUri: Uri) {
        val channelId = "pdf_export_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Exportação de PDF",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(fileUri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Download concluído")
            .setContentText("Currículo salvo como $filename")
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}


