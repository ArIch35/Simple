package com.example.segrau

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.util.ArrayList

class ProduktAdapter(context: Context, resourceID: Int, kassenzettel: ArrayList<Produkt>):
    ArrayAdapter<Produkt>(context, resourceID, kassenzettel) {
    private val thisContext = context;
    private val resourceID = resourceID;

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertView: View?
        val inflater = LayoutInflater.from(thisContext)
        convertView = inflater.inflate(resourceID, parent, false)
        var thisProdukt = getItem(position)
        var name = convertView!!.findViewById<TextView>(R.id.name_editieren)
        var gewicht = convertView!!.findViewById<TextView>(R.id.gewicht_editieren)
        var preis = thisProdukt!!.thisPreis.toFloat() / 100
        var preisString = preis.toString() + " €"
        var preisTextView = convertView!!.findViewById<TextView>(R.id.preis_editieren)
        name.text = thisProdukt!!.thisName
        gewicht.text = thisProdukt!!.thisGewicht.toString() + " g"
        preisTextView.text = preisString

        return convertView
    }

}
