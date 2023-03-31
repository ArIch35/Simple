package com.example.segrau

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EditierenAdapter(context: Context, resourceID: Int, kassenzettel: ArrayList<Produkt>,listener: ClickListener):
    ArrayAdapter<Produkt>(context, resourceID, kassenzettel) {
    private val thisContext = context
    private val resourceId = resourceID
    private val thisListener= listener

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertView: View?
        val inflater = LayoutInflater.from(thisContext)
        convertView = inflater.inflate(resourceId, parent, false)
        initAttribute(position, convertView)
        return convertView!!
    }

    private fun initAttribute(position: Int, convertView: View?) {
        val kassenzettel = getItem(position)
        setupText(convertView, kassenzettel)
    }

    private fun setupText(
        convertView: View?,
        produkt: Produkt?
    ) {
        val name = convertView!!.findViewById<TextView>(R.id.name_editieren)
        name.text = produkt!!.thisName

        var preise = produkt!!.thisPreis.toFloat() / 100
        var preisString = preise.toString() + " €"
        val preis = convertView.findViewById<TextView>(R.id.preis_editieren)
        preis.text = preisString

        val gewicht = convertView.findViewById<TextView>(R.id.gewicht_editieren)
        gewicht.text = produkt!!.thisGewicht.toString() + " g"

        val button = convertView.findViewById<FloatingActionButton>(R.id.del_editieren)
        button.setOnClickListener{
            thisListener.minusButtonClicked(produkt)
        }
    }

}