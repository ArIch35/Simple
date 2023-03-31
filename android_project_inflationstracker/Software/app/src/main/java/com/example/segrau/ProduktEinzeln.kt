package com.example.segrau

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.text.SimpleDateFormat

class ProduktEinzeln : AppCompatActivity() {
    private lateinit var thisProdukt: ArrayList<Produkt>
    private val thisDatabase= Database(this)

    internal class FilterAttributeHolder {
        var popupWindow: PopupWindow? = null
        var dropDown: Spinner? = null
        var aufsteigen: CheckBox? = null
        var absteigen: CheckBox? = null
        var apply: Button? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produkt_einzeln)
        setupValues()
        setupGraph_preis()
        show_preis_table()
        val change_btn = findViewById<Button>(R.id.switchPreisGewicht)

        change_btn.setOnClickListener{
            controlAll(change_btn)
        }
        backButtonControl()
        filterControl()
    }

    private fun controlAll(
        change_btn: Button
    ) {
        val labelText =  findViewById<TextView>(R.id.preisOrGewicht)
        val table_labelText = findViewById<TextView>(R.id.table_gewichtOrPreis)

        if (labelText.text == "Preis") {
            table_labelText.text = "Gewicht"
            change_btn.text = "Preis Anzeigen"
            setupGraph_gewicht()
            delete_row()
            show_gewicht_table()
            labelText.text = "Gewicht"
        } else {
            table_labelText.text = "Preis"
            change_btn.text = "Gewicht Anzeigen"
            setupGraph_preis()
            delete_row()
            show_preis_table()
            labelText.text = "Preis"
        }
    }

    private fun filterControl() {
        val filterToggle =findViewById<ImageView>(R.id.filter_einzel)
        filterToggle.setOnClickListener {
            popupFilterControl()
        }
    }

    private fun inflateView(): View? {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return inflater.inflate(R.layout.activity_popup_filter_main_screen, null)
    }

    private fun setupPopupWindow(view: View): PopupWindow {
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        return PopupWindow(view,width,height,focusable)
    }

    private fun initializeHolder(popupView: View): KassenzettelAuflistenBildschirm.FilterAttributeHolder {
        val holder = KassenzettelAuflistenBildschirm.FilterAttributeHolder()
        holder.popupWindow = setupPopupWindow(popupView)
        holder.dropDown = popupView.findViewById(R.id.drop_down_category)
        holder.aufsteigen = popupView.findViewById(R.id.checkbox_aufsteigen)
        holder.absteigen = popupView.findViewById(R.id.checkbox_absteigen)
        holder.apply = popupView.findViewById(R.id.apply_filter)
        return holder
    }

    private fun popupFilterControl() {
        val popupView: View = inflateView()!!
        val holder = initializeHolder(popupView)

        holder.popupWindow!!.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        setupDropDownCategory(holder,popupView)

        dropDownControl(holder)

        applyButtonControl(holder)
    }

    private fun applyButtonControl(holder: KassenzettelAuflistenBildschirm.FilterAttributeHolder?){
        var queryDecider = ""
        var isPreis = true
        holder!!.apply!!.setOnClickListener {
            when(holder.dropDown!!.selectedItem.toString()){
                in "Laden" -> queryDecider = "LadenName "
                in "Datum" -> queryDecider = "Datum "
                in "Gewicht" ->{queryDecider = "Gewichts "; isPreis = false}
                in "Preis" -> {queryDecider = "Preis "; isPreis = true}
            }
            val ascending = holder.aufsteigen!!.isChecked
            val descending = holder.absteigen!!.isChecked

            queryDecider += if (ascending || !descending){
                "ASC"
            } else{
                "DESC"
            }
            thisProdukt = thisDatabase.getSortedProductArray(queryDecider,
                intent.getStringExtra("produkt_name")!!)

            val table_labelText = findViewById<TextView>(R.id.table_gewichtOrPreis)
            val labelText =  findViewById<TextView>(R.id.preisOrGewicht)

            delete_row()
            if (isPreis) {
                labelText.text = "Preis"
                table_labelText.text = "Preis"
                show_preis_table()
                setupGraph_preis()
            } else {
                labelText.text = "Gewicht"
                table_labelText.text = "Gewicht"
                show_gewicht_table()
                setupGraph_gewicht()
            }
            holder.popupWindow!!.dismiss()
        }
    }

    private fun dropDownControl(holder: KassenzettelAuflistenBildschirm.FilterAttributeHolder?) {
        holder!!.dropDown!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                holder.dropDown!!.setSelection(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }

    private fun setupDropDownCategory(holder: KassenzettelAuflistenBildschirm.FilterAttributeHolder?, popupView: View) {
        val sortCategory = arrayOf("Datum", "Laden", "Gewicht", "Preis")
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(popupView.context, android.R.layout.simple_spinner_dropdown_item, sortCategory)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder!!.dropDown!!.adapter = adapter

    }

    private fun backButtonControl(){
        val back = findViewById<ImageView>(R.id.back_button_produkt)
        back.setOnClickListener {
            val newIntent = decidePrevScreen()
            startActivity(newIntent) }
    }

    fun decidePrevScreen(): Intent{
        val prev = intent.getStringExtra("prev_screen")

        if (prev.equals("kassen_einzel")){
            val new_intent = Intent(this,KassenzettelEinzel::class.java)
            val datum = intent.getIntExtra("einkaufsDatum",0).toString()
            new_intent.putExtra("ladenName",intent.getStringExtra("ladenName"))
            new_intent.putExtra("einkaufsDatum",datum)
            return new_intent
        }
        return Intent(this,ProduktAuflistung::class.java)
    }


    private fun delete_row(){
        val tl = findViewById<TableLayout>(R.id.table_produkt)
        while (tl.getChildCount() > 1) {
            val row = tl.getChildAt(1)
            tl.removeView(row);
            //j=tl.getChildCount();
        }
    }

    private fun show_gewicht_table(){
        val tl = findViewById<TableLayout>(R.id.table_produkt)
        for (produkt in thisProdukt){
            val lp = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
            val textLayout = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1F)
            val tr = TableRow(this)

            tr.setBackgroundColor(ContextCompat.getColor(this, R.color.white))

            tr.layoutParams = lp

            //dates
            val date = TextView(this)
            date.layoutParams  = textLayout
            date.text = produkt.getDate()
            tr.addView(date)
            //ladenname
            val ladenName = TextView(this)
            ladenName.layoutParams = textLayout
            ladenName.text = produkt.thisLadenName
            tr.addView(ladenName)
            //preis
            val gewicht = TextView(this)
            gewicht.layoutParams = textLayout
            gewicht.text = produkt.thisGewicht.toString() + " g"
            tr.addView(gewicht)
            //
            tl.addView(tr, 1)
        }
    }

    private fun show_preis_table(){
        val tl = findViewById<TableLayout>(R.id.table_produkt)

        for (produkt in thisProdukt){
            val tr = TableRow(this)
            tr.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            val lp = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
            val textLayout = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1F)
            tr.layoutParams = lp

            //dates
            val date = TextView(this)
            date.layoutParams  = textLayout
            date.text = produkt.getDate()
            tr.addView(date)
            //ladenname
            val ladenName = TextView(this)
            ladenName.layoutParams = textLayout
            ladenName.text = produkt.thisLadenName
            tr.addView(ladenName)
            //preis
            val preis = TextView(this)
            preis.layoutParams = textLayout
            preis.text = (produkt.thisPreis.toDouble() / 100).toString() + " €"
            tr.addView(preis)
            //
            tl.addView(tr, 1)
        }
    }

    private fun setupValues() {
        val produktName = intent.getStringExtra("produkt_name")
        findViewById<TextView>(R.id.produkt_title).text = produktName
        thisProdukt = thisDatabase.getProduktFromKassenzettel(produktName!!)
    }

    private fun setupGraph_preis() {
        val graph = findViewById<GraphView>(R.id.graph)
        graph.removeAllSeries()
        val values = getDataPointsPrices().toTypedArray()
        val data1 = LineGraphSeries(values)
        graph.addSeries(data1)

        graph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter(){
            override fun formatLabel(value: Double, isValueX: Boolean): String {
                if (isValueX){
                    return thisProdukt[value.toInt()].getDate()
                }
                return super.formatLabel(value, isValueX)
            }
        }
        graph.gridLabelRenderer.numHorizontalLabels = values.size
    }

    private fun setupGraph_gewicht() {
        val graph = findViewById<GraphView>(R.id.graph)
        graph.removeAllSeries()
        val values = getDataPointsWeight().toTypedArray()
        val data1 = LineGraphSeries(values)
        graph.addSeries(data1)


        graph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter(){
            override fun formatLabel(value: Double, isValueX: Boolean): String {
                if (isValueX){
                    return thisProdukt[value.toInt()].getDate()
                }
                return super.formatLabel(value, isValueX)
            }
        }

        graph.gridLabelRenderer.numHorizontalLabels = values.size
    }

    private fun getDataPointsPrices(): ArrayList<DataPoint>{
        var temp: ArrayList<DataPoint> = java.util.ArrayList()
        var counter = 0.0
        for (produkt in thisProdukt){
            temp.add(DataPoint(counter,produkt.thisPreis.toDouble()/100))
            counter += 1
        }

        return temp
    }

    private fun getDataPointsWeight(): ArrayList<DataPoint>{
        var temp: ArrayList<DataPoint> = java.util.ArrayList()
        var counter = 0.0
        for (produkt in thisProdukt){
            temp.add(DataPoint(counter,produkt.thisGewicht.toDouble()))
            counter += 1
        }

        return temp
    }
}