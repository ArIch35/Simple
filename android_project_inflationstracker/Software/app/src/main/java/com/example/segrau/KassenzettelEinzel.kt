package com.example.segrau

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.segrau.Database
import com.example.segrau.KassenzettelAdapter
import com.example.segrau.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class KassenzettelEinzel : AppCompatActivity() {
    private var thisDatabase= Database(this)
    private var laden:String = ""
    private var datum:Int = 0
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kassenzettel_einzel)
        getIntentFromPrevious()
        editButtonControl()
        backButtonControl()
        titleControl()
        controlList()
    }

    private fun titleControl(){
        findViewById<TextView>(R.id.laden_edit).text = laden
    }

    private fun backButtonControl(){
        findViewById<ImageView>(R.id.back_button).setOnClickListener {
            startActivity(Intent(this,KassenzettelAuflistenBildschirm::class.java))
        }
    }

    private fun getIntentFromPrevious(){
        laden = intent.getStringExtra("ladenName").toString()
        datum = intent.getStringExtra("einkaufsDatum")!!.toInt()
        setupList(laden,datum)
    }

    private fun setupList(laden: String,datum: Int){
        val listView = findViewById<ListView>(R.id.listViewProdukt)
        val adapter = ProduktAdapter(this,R.layout.produkt_adapter,thisDatabase.getProduktFromKassenzettel(laden,datum,true))
        listView.adapter = adapter
        listView.isClickable = true
    }

    private fun controlList(){
        val mainList = findViewById<ListView>(R.id.listViewProdukt)
        mainList.onItemClickListener =
            AdapterView.OnItemClickListener { _: AdapterView<*>?, _: View?, i: Int, _: Long ->
                switchScreenToProduktEinzel(i, thisDatabase.getTemporaryProduct())
            }
    }

    private fun switchScreenToProduktEinzel(position: Int, list: List<Produkt> ){
        val toProduktEinzel = Intent(this, ProduktEinzeln::class.java)
        toProduktEinzel.putExtra("produkt_name",list[position].thisName)
        toProduktEinzel.putExtra("prev_screen","kassen_einzel")
        toProduktEinzel.putExtra("ladenName",laden)
        toProduktEinzel.putExtra("einkaufsDatum",datum)
        startActivity(toProduktEinzel)
    }

    private fun editButtonControl(){
        val editButton = findViewById<FloatingActionButton>(R.id.editKnopf)
        editButton.setOnClickListener{
            switchScreenToEdit()
        }
    }

    private fun switchScreenToEdit(){
        val toKassenEdit = Intent(this, KassenzettelEditieren::class.java)
        putValuesToIntent(toKassenEdit)
        startActivity(toKassenEdit)
    }

    private fun putValuesToIntent(toKassenEdit: Intent){
        val laden:String = intent.getStringExtra("ladenName").toString()
        val datum:String = intent.getStringExtra("einkaufsDatum").toString()

        toKassenEdit.putExtra("ladenName",laden)
        toKassenEdit.putExtra("einkaufsDatum",datum)
    }

}