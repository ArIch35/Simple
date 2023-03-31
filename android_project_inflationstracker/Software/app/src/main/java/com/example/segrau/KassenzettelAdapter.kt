package com.example.segrau

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

@Suppress("NAME_SHADOWING")
class KassenzettelAdapter(context: Context, resourceID: Int, kassenzettel: ArrayList<Kassenzettel>):
    ArrayAdapter<Kassenzettel>(context, resourceID, kassenzettel) {
    private val thisContext= context
    private val resourceId = resourceID

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertView: View?
        val inflater = LayoutInflater.from(thisContext)
        convertView = inflater.inflate(resourceId, parent, false)
        initAttribute(position,convertView)
        return convertView!!
    }

    private fun initAttribute(position: Int,convertView: View?){
        val kassenzettel = getItem(position)
        setupImage(convertView, kassenzettel)
        setupText(convertView, kassenzettel)
    }

    private fun setupText(
        convertView: View?,
        kassenzettel: Kassenzettel?
    ) {
        val datum = convertView!!.findViewById<TextView>(R.id.kassenzettel_datum)
        datum.text = "Datum: " + kassenzettel!!.getDatumParsing()

        val laden = convertView.findViewById<TextView>(R.id.kassenzettel_laden)
        laden.text = kassenzettel.LadenName

        val betrag = convertView.findViewById<TextView>(R.id.kassenzettel_betrag)
        val betragText = "Betrag: " + (kassenzettel.Betrag/100).toString()+" €"
        betrag.text = betragText
    }

    private fun setupImage(
        convertView: View?,
        kassenzettel: Kassenzettel?
    ) {
        val bild = convertView!!.findViewById<ImageView>(R.id.kassenzettel_bild)

        if (kassenzettel!!.Bild == null){
            bild.setImageResource(R.drawable.black_back)
            return
        }

        val bitmap = BitmapFactory.decodeByteArray(
            kassenzettel?.Bild,
            0,
            kassenzettel?.Bild!!.size
        )


        if (bitmap != null) bild.setImageBitmap(
            Bitmap.createScaledBitmap(
                bitmap,
                bitmap.width,
                bitmap.height,true
            )
        )
    }

}
