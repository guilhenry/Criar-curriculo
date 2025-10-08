package com.merc.criarcurriculo

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ExperiActivity : AppCompatActivity() {

    private var dini1 = ""
    private var dsai1 = ""
    private var dini2 = ""
    private var dsai2 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_experi)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val ini1 = findViewById<EditText>(R.id.editdataini1)
        val sai1 = findViewById<EditText>(R.id.editdatasai1)
        val ini2 = findViewById<EditText>(R.id.editdataini2)
        val sai2 = findViewById<EditText>(R.id.editdatasai2)
        ini1.setOnClickListener {
            data(ini1)
        }
        ini2.setOnClickListener {
            data(ini2)
        }
        sai1.setOnClickListener {
            data(sai1)
        }
        sai2.setOnClickListener {
            data(sai2)
        }

          dini1 = findViewById<EditText>(R.id.editdataini1).text.toString()
          dsai1 = findViewById<EditText>(R.id.editdatasai1).text.toString()
          dini2 = findViewById<EditText>(R.id.editdataini2).text.toString()
          dsai2 = findViewById<EditText>(R.id.editdatasai2).text.toString()

    }



    fun salvExp(){
        val nmEmp1 = findViewById<EditText>(R.id.editnmEmp1).text.toString()
        val cargo1 = findViewById<EditText>(R.id.editcargo1).text.toString()


        val nmEmp2 = findViewById<EditText>(R.id.editnmEmp2).text.toString()
        val cargo2 = findViewById<EditText>(R.id.editcargo2).text.toString()

        if (nmEmp1 != "" || nmEmp2 != "" ){
            val infoexp = "• $nmEmp1\n Cargo: $cargo1\n Período: $dini1 - $dsai1\n • $nmEmp2\n Cargo: $cargo2\n Período: $dini2 - $dsai2"
            Pessoal.InfoPessoal.experi = infoexp

        }else if (nmEmp1 != ""){
            val infoexp ="• $nmEmp1\n Cargo: $cargo1\n Período: $dini1 - $dsai1"
            Pessoal.InfoPessoal.experi = infoexp
        }else{
            val infoexp ="Sem experiência profissional a procura da primeira oportunidade"
            Pessoal.InfoPessoal.experi = infoexp
        }



    }

    fun data(campo: EditText) {
        val calendario = Calendar.getInstance()
        val ano = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val dataFormatada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                campo.setText(dataFormatada)
            },
            ano, mes, dia
        )
        datePicker.show()
    }


    fun exp(view: View){
        salvExp()
        val intent = Intent(this, ReviwActivity::class.java)
        startActivity(intent)
    }
}