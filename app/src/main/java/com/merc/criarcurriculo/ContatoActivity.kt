package com.merc.criarcurriculo

import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ContatoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contato)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        sig()
    }



    fun sig(){
        val estados = arrayOf(
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
            "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
            "RS", "RO", "RR", "SC", "SP", "SE", "TO"
        )

        val spinner = findViewById<Spinner>(R.id.spinnerEs)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
    }

    fun infocont(){

        val info = Pessoal.InfoPessoal

        val tel = findViewById<EditText>(R.id.editTelefone)
        tel.addTextChangedListener(PhoneNumberFormattingTextWatcher("BR"))
        val mail = findViewById<EditText>(R.id.editEmail).text.toString()

        info.cont = "Telefone: $tel\n    Email: $mail"

        val cidade = findViewById<EditText>(R.id.editCidade).text.toString()

        val bairro = findViewById<EditText>(R.id.editBairro).text.toString()

        val rua = findViewById<EditText>(R.id.editRua).text.toString()

        val numero = findViewById<EditText>(R.id.editnume).text.toString()

        val sig = findViewById<Spinner>(R.id.spinnerEs).selectedItem.toString()

        info.end  =  "Data de Nascimento: ${info.dtnasc} \nEstado Civil: ${info.resposta}\nNaturalidade: ${info.natu} - ${info.sig}\nRua: $rua Numero: $numero\nEndereço: $bairro -  $cidade - $sig"


    }
    fun pagForm(view: View){
        infocont()
        val intent = Intent(this, FormaActivity::class.java)
        startActivity(intent)
    }
}