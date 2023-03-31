package com.example.segrau

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.text.SimpleDateFormat

enum class Mode{
    PRODUKT_EINZEl,
    PRODUKT_GRAFIK
}

class ProduktAuflistungAdapter(context: Context, resourceID: Int,
                               produkte: ArrayList<TempProdukt>, mode: Mode,
                               private val database: Database):
    ArrayAdapter<TempProdukt>(context, resourceID, produkte) {
    private val thisContext = context
    private val mode = mode
    private val size = produkte.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertView: View?
        val inflater = LayoutInflater.from(thisContext)
        convertView = if (mode == Mode.PRODUKT_EINZEl) {
            einzelMode(inflater, parent, position)
        } else{
            grafikMode(inflater, parent, position)
        }
        return convertView!!
    }

    private fun einzelMode(
        inflater: LayoutInflater,
        parent: ViewGroup,
        position: Int
    ): View? {
        var convertView = inflater.inflate(R.layout.produktauflisten_adapter_einzel, parent, false)
        var thisProdukt = getItem(position)!!

        var name = convertView!!.findViewById<TextView>(R.id.produktauflisten_adapter_name)
        name.text = thisProdukt.thisName

        var preis = convertView!!.findViewById<TextView>(R.id.produktauflisten_adapter_preis)
        preis.text = (thisProdukt.thisPreis.toDouble()/100).toString() + "€"
        return convertView
    }

    private fun grafikMode(
        inflater: LayoutInflater,
        parent: ViewGroup,
        position: Int
    ): View? {
        var convertView = inflater.inflate(R.layout.produktauflisten_adapter_grafik, parent, false)
        var thisProdukt = getItem(position)!!

        var name = convertView!!.findViewById<TextView>(R.id.produktauflisten_adapter_name)
        name.text = thisProdukt.thisName

        var graph = convertView!!.findViewById<GraphView>(R.id.produktauflisten_adapter_graph)
        val data1 = LineGraphSeries(getDataPointsPrices(thisProdukt.thisName).toTypedArray())
        graph.removeAllSeries()
        graph.addSeries(data1)
        graph.gridLabelRenderer.isHorizontalLabelsVisible = false
        graph.gridLabelRenderer.numHorizontalLabels = size

        return convertView
    }

    private fun getDataPointsPrices(name: String): ArrayList<DataPoint>{
        var temp: ArrayList<DataPoint> = java.util.ArrayList()
        var counter = 0.0
        for (produkt in database.getProduktFromKassenzettel(name)){
            temp.add(DataPoint(counter,produkt.thisPreis.toDouble()/100))
            counter += 1
        }

        return temp
    }


}
