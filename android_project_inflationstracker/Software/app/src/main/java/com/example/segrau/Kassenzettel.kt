package com.example.segrau

class Kassenzettel(datum: String, bild: ByteArray?, laden: String,betrag: Int) {
    val EinkaufsDatum = datum
    val Bild = bild
    val LadenName = laden
    val Betrag = betrag

    fun getDatumParsing():String{
            return "${getTag()}-${getMonat()}-${getJahr()}"
    }

    private fun getTag():String{
        return EinkaufsDatum[6] + "" + EinkaufsDatum[7]
    }

    private fun getMonat():String{
        return EinkaufsDatum[4] + "" + EinkaufsDatum[5]
    }

    private fun getJahr():String{
        return EinkaufsDatum[0] + "" + EinkaufsDatum[1] + "" + EinkaufsDatum[2] + "" + EinkaufsDatum[3]
    }
}