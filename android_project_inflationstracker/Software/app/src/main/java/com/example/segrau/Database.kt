package com.example.segrau


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

private var database_name = "database.db"

class Database (context: Context) :
    SQLiteOpenHelper(context, database_name, null, 1) {

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val qry = "create table test (value integer primary key autoincrement)"
        sqLiteDatabase.execSQL(qry)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, p1: Int, p2: Int) {
        sqLiteDatabase.execSQL("Drop Table If Exists test")
        onCreate(sqLiteDatabase)
    }

    private fun getDatabase(): SQLiteDatabase? {
        return try {
            writableDatabase
        } catch (e: Exception) {
            null
        }
    }

    fun checkIfConnectionSuccess(): Boolean {
        return getDatabase() != null
    }

    fun getSortedProductArray(filter: String,name: String): ArrayList<Produkt>{
        val tempHolder: ArrayList<Produkt> = java.util.ArrayList()
        val query = "SELECT * FROM Position WHERE Name = '$name' ORDER BY $filter"
        val value = getDatabase()!!.rawQuery(query, null)

        if (value.moveToFirst()) {
            do {
                tempHolder.add(Produkt(value.getString(3),value.getInt(0),value.getInt(1),value.getString(4),value.getInt(2)))
            } while (value.moveToNext())
        }
        value.close()

        return tempHolder
    }

    fun getTemporaryProduct(): ArrayList<TempProdukt>{
        val tempHolder: ArrayList<TempProdukt> = java.util.ArrayList()
        val query = "SELECT * FROM Produkt"
        val value = getDatabase()!!.rawQuery(query, null)

        if (value.moveToFirst()) {
            do {
                tempHolder.add(TempProdukt(value.getString(0),
                getRecentPreisFromProdukt(value.getString(0)),
                getRecentDatumFromProdukt(value.getString(0))))
            } while (value.moveToNext())
        }
        value.close()

        return tempHolder
    }

    fun getRecentPreisFromProdukt(name: String): Int{
        val query = "SELECT Preis FROM Position WHERE Name = '$name' ORDER by Datum DESC LIMIT 1"
        val value = getDatabase()!!.rawQuery(query, null)

        if (value.moveToFirst()) {
            return value.getInt(0)
        }
        value.close()
        return 0
    }

    fun getRecentDatumFromProdukt(name: String): Int{
        val query = "SELECT Datum FROM Position WHERE Name = '$name' ORDER by Datum DESC LIMIT 1"
        val value = getDatabase()!!.rawQuery(query, null)

        if (value.moveToFirst()) {
            return value.getInt(0)
        }
        value.close()
        return 0
    }

    fun addKassenzettel(kassenzettel: Kassenzettel){
        val name = kassenzettel.LadenName
        val bild = kassenzettel.Bild
        val datum = kassenzettel.EinkaufsDatum
        val betrag = kassenzettel.Betrag

        var query = "INSERT OR IGNORE INTO Laden VALUES('$name')"
        getDatabase()!!.execSQL(query)
        if (bild == null) {
            query =
                "INSERT OR IGNORE INTO Kassenzettel(EinkaufsDatum,LadenName,Betrag) VALUES($datum,'$name',$betrag)"
            getDatabase()!!.execSQL(query)
        }else{
            val values = ContentValues()
            values.put("EinkaufsDatum",datum)
            values.put("Bild", bild)
            values.put("LadenName",name)
            values.put("Betrag",betrag)
            getDatabase()!!.insert("Kassenzettel",null,values)
        }
        getDatabase()!!.close()
    }

    fun getKassenzettelArrayFromDatabase(): ArrayList<Kassenzettel>{
        val tempHolder: ArrayList<Kassenzettel> = java.util.ArrayList()
        val query = "SELECT * FROM Kassenzettel"
        val value = getDatabase()!!.rawQuery(query, null)

        if (value.moveToFirst()) {
            do {
                tempHolder.add(
                    Kassenzettel(value.getString(0),value.getBlob(1),value.getString(2),value.getInt(3))
                )
            } while (value.moveToNext())
        }
        value.close()

        return tempHolder
    }

    fun getKassenzettelFromDatabase(laden: String,datum: Int): Kassenzettel? {
        val query = "SELECT * FROM Kassenzettel WHERE EinkaufsDatum = $datum AND LadenName = '$laden'"
        val value = getDatabase()!!.rawQuery(query, null)
        if (value.moveToFirst()) {
           return Kassenzettel(value.getString(0),value.getBlob(1),value.getString(2),value.getInt(3))
        }
        value.close()
        return null
    }

    /*private fun parseDatum(datum: String): String {
        val tag = datum[0] + "" + datum[1]
        val monat = datum[2] + "" + datum[3]
        val jahr = datum[4] + "" + datum[5] + "" + datum[6] + "" + datum[7]
        return "$tag-$monat-$jahr"
    }*/

    fun getSortedKassenzettelArrayFromDatabase(filter: String): ArrayList<Kassenzettel> {
        val tempHolder: ArrayList<Kassenzettel> = java.util.ArrayList()
        val query = "SELECT * FROM Kassenzettel ORDER BY $filter"
        val value = getDatabase()!!.rawQuery(query, null)

        if (value.moveToFirst()) {
            do {
                tempHolder.add(
                    Kassenzettel(value.getString(0),value.getBlob(1),value.getString(2),value.getInt(3))
                )
            } while (value.moveToNext())
        }
        value.close()

        return tempHolder
    }

    fun getProduktFromKassenzettel(laden: String,datum: Int,withSumme: Boolean): ArrayList<Produkt>{
        val tempHolder: ArrayList<Produkt> = java.util.ArrayList()
        val query = "SELECT * FROM Position WHERE Datum = $datum AND LadenName = '$laden'"
        val value = getDatabase()!!.rawQuery(query, null)
        if (value.moveToFirst()) {
            do {
                tempHolder.add(Produkt(value.getString(3),value.getInt(0),value.getInt(1),value.getString(4),value.getInt(2)))
            } while (value.moveToNext())
        }
        value.close()
        if (withSumme) {
            tempHolder.add(addGesamtSumme(laden, datum)!!)
        }
        return tempHolder
    }

    fun getProduktFromKassenzettel(name: String): ArrayList<Produkt>{
        val tempHolder: ArrayList<Produkt> = java.util.ArrayList()
        val query = "SELECT * FROM Position WHERE Name = '$name'"
        val value = getDatabase()!!.rawQuery(query, null)
        if (value.moveToFirst()) {
            do {
                tempHolder.add(Produkt(value.getString(3),value.getInt(0),value.getInt(1),value.getString(4),value.getInt(2)))
            } while (value.moveToNext())
        }
        value.close()
        return tempHolder
    }

    private fun addGesamtSumme(laden: String,datum: Int): Produkt? {
        val query = "SELECT SUM(Preis),SUM(Gewichts) FROM Position WHERE Datum = $datum AND LadenName = '$laden'"
        val value = getDatabase()!!.rawQuery(query, null)

        if (value.moveToFirst()) {
            return Produkt("Gesamtsumme",value.getInt(0),value.getInt(1),laden,datum)
        }
        value.close()
        return null
    }

    fun deleteProduct(produkt: Produkt){
        val datum = produkt.thisDatum
        val name = produkt.thisName
        val laden = produkt.thisLadenName

        val query = "DELETE FROM Position WHERE Datum = $datum AND Name = '$name' AND LadenName = '$laden' "
        getDatabase()!!.execSQL(query)
        getDatabase()!!.close()
    }

    fun addProduct(produkt: Produkt){
        var preis = produkt.thisPreis
        var gewicht = produkt.thisGewicht
        val datum = produkt.thisDatum
        val name = produkt.thisName
        val laden = produkt.thisLadenName

        val values = ContentValues()
        values.put("Preis",preis)
        values.put("Gewichts", gewicht)
        values.put("Datum",datum)
        values.put("Name",name)
        values.put("LadenName",laden)
        var query = "INSERT OR IGNORE INTO Produkt VALUES('$name')"
        getDatabase()!!.execSQL(query)
        try {
            getDatabase()!!.insertOrThrow("Position",null,values)
        }catch (exception: SQLiteConstraintException){
            val arguments = "Datum = $datum AND Name = '$name' AND LadenName = '$laden'"
            val query = "SELECT Preis,Gewichts FROM Position WHERE $arguments"
            val value = getDatabase()!!.rawQuery(query, null)

            if (value.moveToFirst()) {
                preis += value.getInt(0)
                gewicht += value.getInt(1)
            }
            val values = ContentValues()
            values.put("Preis",preis)
            values.put("Gewichts", gewicht)
            getDatabase()!!.update("Position",values,arguments,null)
        }
        getDatabase()!!.close()
    }

    fun updateName(neuerName: String,datum: Int){
        var query = "INSERT OR IGNORE INTO Laden VALUES('$neuerName')"
        getDatabase()!!.execSQL(query)
        query = "UPDATE Kassenzettel SET LadenName = '$neuerName' WHERE EinkaufsDatum = $datum"
        getDatabase()!!.execSQL(query)
        query = "UPDATE Position SET LadenName = '$neuerName' WHERE Datum = $datum"
        getDatabase()!!.execSQL(query)
        getDatabase()!!.close()
    }

    fun updateSumme(summe: Int,name: String,datum: Int){
        var query = "UPDATE Kassenzettel SET Betrag = $summe WHERE LadenName = '$name' AND EinkaufsDatum = $datum"
        getDatabase()!!.execSQL(query)
        getDatabase()!!.close()
    }

    fun deleteKassenzettel(laden: String,datum: Int){
        val query = "DELETE FROM Kassenzettel WHERE EinkaufsDatum = $datum AND LadenName = '$laden' "
        getDatabase()!!.execSQL(query)
        getDatabase()!!.close()
    }

    fun checkIfProduktExist(laden: String,datum: Int):Boolean{
        val query = "SELECT * FROM Position WHERE LadenName = '$laden' AND Datum = $datum"
        val value = getDatabase()!!.rawQuery(query, null)

        if (value.moveToFirst()) {
            return true
        }
        value.close()
        return false
    }

    fun checkIfKassenzettelExist(laden: String,datum: Int):Boolean{
        val query = "SELECT * FROM Kassenzettel WHERE LadenName = '$laden' AND EinkaufsDatum = $datum"
        val value = getDatabase()!!.rawQuery(query, null)

        if (value.moveToFirst()) {
            return true
        }
        value.close()
        return false
    }

    fun updateFoto(laden: String,datum: Int,foto: Bitmap){
        if (!foto.isRecycled) {
            val values = ContentValues()
            values.put("Bild", getFotoInByte(foto))
            val arguments = "LadenName = '$laden' AND EinkaufsDatum = $datum"
            getDatabase()!!.update("Kassenzettel", values, arguments, null)
            getDatabase()!!.close()
        }
    }

    private fun getFotoInByte(foto: Bitmap):ByteArray {
        val stream = ByteArrayOutputStream()
        foto.compress(Bitmap.CompressFormat.PNG, 100, stream)
        foto.recycle()
        return stream.toByteArray()
    }

    fun getSearchItemResult(text: String): ArrayList<String>{
        val tempHolder: ArrayList<String> = java.util.ArrayList()
        val query = "SELECT * FROM Produkt WHERE Name LIKE '%$text%'"
        val value = getDatabase()!!.rawQuery(query, null)
        if (value.moveToFirst()) {
            do {
                tempHolder.add(value.getString(0))
            } while (value.moveToNext())
        }
        value.close()
        return tempHolder
    }

}