package com.example.segrau

open class Produkt (name : String, preis : Int, gewicht : Int, ladenName : String?, datum : Int) {
    val thisName = name
    val thisPreis = preis
    val thisGewicht = gewicht
    var thisLadenName = ladenName
    val thisDatum = datum

    private fun getTag():String{
        return thisDatum.toString()[6] + "" + thisDatum.toString()[7]
    }

    private fun getMonat():String{
        return thisDatum.toString()[4] + "" + thisDatum.toString()[5]
    }

    fun getJahr():String{
        return thisDatum.toString()[0] + "" + thisDatum.toString()[1] + "" + thisDatum.toString()[2] + "" + thisDatum.toString()[3]
    }

    fun getDate():String{
        return "${getJahr()}-${getMonat()}-${getTag()}"
    }
}

class TempProdukt (name : String, preis : Int,
                   datum: Int): Produkt(name,preis,-1,"",datum) {

}