package com.merc.criarcurriculo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FormaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forma)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        escolari()
    }

    fun escolari(){

            val grau = arrayOf(
                "Ensino Fundamental Completo", "Ensino Médio Incompleto", "Ensino Médio Completo",
                "Ensino Superior Incompleto", "Ensino Superior Completo"
            )

            val spinner = findViewById<Spinner>(R.id.spinnerEnsi)
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, grau)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinner.adapter = adapter


    }

    fun salvInf(){
        val cursSu = findViewById<EditText>(R.id.editEnsSup).text.toString()
        val aninsu = findViewById<EditText>(R.id.editinisu).text.toString()
        var ancmsu = findViewById<EditText>(R.id.edittermfacu).text.toString()
        val nmFacu = findViewById<EditText>(R.id.editnmfacu).text.toString()
        val graues = findViewById<Spinner>(R.id.spinnerEnsi).selectedItem.toString()
        val curs1 = findViewById<EditText>(R.id.editNomeCur1).text.toString()
        val curs2 = findViewById<EditText>(R.id.editNomeCur2).text.toString()
        val curs3 = findViewById<EditText>(R.id.editNomeCur3).text.toString()


        if (cursSu != ""){
            if (ancmsu == ""){
                ancmsu = "Cursando"
            }
            val infoSu = "• $graues \n• $cursSu - $nmFacu, $aninsu - $ancmsu"
            Pessoal.InfoPessoal.grau = infoSu

        }else{
            val infoSu = "• $graues "
            Pessoal.InfoPessoal.grau = infoSu
        }





        if (curs1 != "" && curs2 != "" && curs3 != ""  ){
            val infcur = "• $curs1 \n • ${curs2}\n• $curs3   "
            Pessoal.InfoPessoal.curso = infcur

        }else if(curs1 != "" && curs2 != ""){
            val infcur = "• $curs1 \n • ${curs2}"
            Pessoal.InfoPessoal.curso = infcur

        }else if (curs1 != ""){
            val infcur = "• $curs1"
            Pessoal.InfoPessoal.curso = infcur
        }

    }

    fun exp(view: View){
        salvInf()
        val intent = Intent(this, ExperiActivity::class.java)
        startActivity(intent)
    }
}