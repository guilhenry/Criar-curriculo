package com.merc.criarcurriculo

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Pessoal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pessoal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        escSigla()
        seleData()


    }
    object InfoPessoal {
        var nome: String = ""
        var dtnasc: String = ""
        var natu: String = ""
        var sig: String = ""
        var resposta: String? = ""
        var cont: String = ""

        var end: String = ""

        var grau: String = ""

        var curso: String =""
        var experi: String = ""

    }

    fun seleData(){
        val editNasci = findViewById<EditText>(R.id.editNasci)

        editNasci.setOnClickListener {
            val calendario = Calendar.getInstance()
            val ano = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val dataFormatada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                    editNasci.setText(dataFormatada)
                },
                ano, mes, dia
            )
            datePicker.show()
        }
    }
    fun escSigla(){
        val estados = arrayOf(
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
            "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
            "RS", "RO", "RR", "SC", "SP", "SE", "TO"
        )

        val spinner = findViewById<Spinner>(R.id.spinnerEstados)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

    }
    fun salvInf(){
        val ednome = findViewById<EditText>(R.id.editNome)
        val eddatanas = findViewById<EditText>(R.id.editNasci)
        val ednatu = findViewById<EditText>(R.id.editnat)
        val edsig = findViewById<Spinner>(R.id.spinnerEstados)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            InfoPessoal.resposta = radioButton.text.toString()
        }


        InfoPessoal.sig = edsig.selectedItem.toString()
        InfoPessoal.nome = ednome.text.toString()
        InfoPessoal.dtnasc = eddatanas.text.toString()
        InfoPessoal.natu = ednatu.text.toString()
    }
    fun pagContato(view: View){
        salvInf()

        val intent = Intent(this, ContatoActivity::class.java)
        startActivity(intent)
    }
}