package com.example.segrau

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class KassenzettelEditieren : AppCompatActivity(),ClickListener{
    private var thisDatabase= Database(this)
    private var allItemList: ArrayList<Produkt> = ArrayList()
    private var deletedItemList: MutableList<Produkt> = ArrayList()
    private var addedItemList: MutableList<Produkt> = ArrayList()
    private var laden:String = ""
    private var datum:String = ""
    private var nameChanged = false
    private lateinit var neuerFoto:Bitmap
    private val REQUEST_IMAGE_CAPTURE = 1
    private var imageUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kassenzettel_editieren)
        setupScreen()
        saveButtonControl()
        backButtonControl()
        ladenNameControl()
        deleteButtonControl()
        cameraControl()
        editControl()
    }

    private fun editControl(){
        val editKnopf = findViewById<FloatingActionButton>(R.id.editKnopf)
        editKnopf.setOnClickListener{
            val view = inflateView(R.layout.popup_layout)!!
            val popUpWindow = setupPopupWindow(view)
            popUpWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
            controlProduktAddierung(view,popUpWindow)
        }
    }

    private fun controlProduktAddierung(view: View, popUpWindow: PopupWindow) {
        abbrechenControl(view, popUpWindow)
        akzeptierenControl(view, popUpWindow)
        arrowControl(view)
    }

    private fun arrowControl(viewMain: View) {
        viewMain.findViewById<ImageView>(R.id.down_arrow).setOnClickListener {
            val view = inflateView(R.layout.popup_search)!!
            val popUpWindow = setupPopupWindow(view)
            popUpWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
            controlSearch(view,popUpWindow,viewMain)
        }
    }

    private fun getAusgewaehlt(view: View): TextView{
        return view.findViewById(R.id.produkt_auswahl)!!
    }

    private fun controlSearch(view: View,popUpWindow: PopupWindow,viewMain: View){
        view.findViewById<Button>(R.id.accept_search).setOnClickListener {
            popUpWindow.dismiss()
            getAusgewaehlt(viewMain).text = getSearchBar(view).query
        }
        view.findViewById<Button>(R.id.cancel_search).setOnClickListener {
            popUpWindow.dismiss()
        }
        getSearchBar(view).setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                var filtered_city = ArrayList<String>()

                if (newText != "" ) {
                    filtered_city = thisDatabase.getSearchItemResult(newText)
                } else
                    setupSideListView(ArrayList(),view)

                if (filtered_city.isEmpty()){
                    setupSideListView(ArrayList(),view)
                    return false
                };

                setupSideListView(filtered_city,view)
                clickedCityControl(newText,view)

                if (textEqualList(newText, filtered_city)) {
                    setupSideListView(ArrayList(),view)
                }
                return false
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })
    }

    private fun textEqualList(newText: String, filtered_city: ArrayList<String>): Boolean {
        return filtered_city[0] == newText
    }

    private fun setupSideListView(filtered_item: ArrayList<String>,view: View) {
        val filter_string =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, filtered_item)
        getSearchList(view).adapter = filter_string
    }

    private fun getSearchList(view: View): ListView{
        return view.findViewById(R.id.produkt_name)
    }

    private fun getSearchBar(view: View): SearchView{
        return view.findViewById(R.id.search_bar)
    }

    private fun clickedCityControl(text: String,view: View) {
        getSearchList(view).setOnItemClickListener(OnItemClickListener { adapterView: AdapterView<*>?, dview: View?, i: Int, l: Long ->
            getSearchBar(view).setQuery(getProdukt(text, i), false)
            getSearchBar(view).clearFocus()
        })
    }

    private fun getProdukt(text: String, i: Int): String {
        return thisDatabase.getSearchItemResult(text).get(i)
    }

    private fun generateToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun akzeptierenControl(view: View, popUpWindow: PopupWindow) {
        view.findViewById<Button>(R.id.accept).setOnClickListener {
            val produktName = nameDecider(getAusgewaehlt(view).text.toString())
            if (produktName == null){
            generateToast("Bitte Geben Sie die Name!")
            return@setOnClickListener
            }

            var produktPreis = intDecider(getPreisView(view).text.toString())
            if (produktPreis == null){
                generateToast("Bitte Geben Sie der Preis!")
                return@setOnClickListener
            }
            produktPreis*=100

            val produktGewicht = intDecider(getGewichtView(view).text.toString())
            if (produktGewicht == null){
                generateToast("Bitte Geben Sie der Gewicht!")
                return@setOnClickListener
            }

            val produktDatum = datumDecider()!!
            addedItemList.add(Produkt(produktName,produktPreis,produktGewicht,null,produktDatum))
            allItemList.add(Produkt(produktName,produktPreis,produktGewicht,null,produktDatum))
            setupList()
            popUpWindow.dismiss()
        }
    }

    private fun datumDecider(): Int?{
        if (thisDatabase.checkIfKassenzettelExist(laden,datum.toInt()))
            return thisDatabase.getKassenzettelFromDatabase(laden,datum.toInt())!!.EinkaufsDatum.toInt()
        return getDateNow().toInt()
    }

    private fun getDateNow():String{
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)

        var month_string = month.toString()
        var date_string = day.toString()

        if (month < 10){
            month_string = "0$month_string";
        }
        if (day < 10){
            date_string = "0$date_string"
        }


        return year.toString()+month_string+date_string
    }

    private fun nameDecider(name: String):String?{
        if (name == "Ausgewählt"){
            return null;
        }
        return name;
    }

    private fun intDecider(name: String):Int?{
        if (name == ""){
            return null;
        }
        try {
            name.toInt()
        }catch (e: Exception){
            generateToast("ungültige Eingabe!")
            return null
        }
        return name.toInt();
    }

    private fun getPreisView(view: View): EditText{
        return view.findViewById(R.id.preis_view)
    }

    private fun getGewichtView(view: View): EditText{
        return view.findViewById(R.id.gewicht_view)
    }

    private fun abbrechenControl(view: View, popUpWindow: PopupWindow) {
        view.findViewById<Button>(R.id.cancel).setOnClickListener {
            popUpWindow.dismiss()
        }
    }


    private fun cameraControl(){
        val cameraButton = findViewById<Button>(R.id.button_photo)
        cameraButton.setOnClickListener{
            cameraSetup()
        }
    }

    private fun cameraSetup(){
        val dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile("temp",".jpg",dir)
        imageUri = imageFile.absolutePath

        val tempUri = FileProvider.getUriForFile(this
            ,"com.example.segrau.fileprovider",imageFile)

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,tempUri)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {

        }
    }

    private fun textErkennungSetup(){
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image: InputImage
        try {
            image = InputImage.fromBitmap(neuerFoto,0)
            val result = recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val resultText = visionText.textBlocks
                    var text = ""
                    for (block in resultText){
                        for (line in block.lines){

                        }
                    }
                }
                .addOnFailureListener { e ->
                }

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            neuerFoto = BitmapFactory.decodeFile(imageUri)
            textErkennungSetup()
        }
    }


    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    private fun setupScreen() {
        getIntentFromPrevious()
        setupList()
    }

    private fun setupBitmap(zettel: Kassenzettel){
        if (zettel.Bild == null){
            neuerFoto = BitmapFactory.decodeResource(this.resources,
                R.drawable.black_back);
            return
        }

        neuerFoto = BitmapFactory.decodeByteArray(
            zettel?.Bild,
            0,
            zettel?.Bild!!.size
        )
    }

    private fun getIntentFromPrevious(){
        val prevIntent = intent
        laden = prevIntent.getStringExtra("ladenName").toString()
        datum = prevIntent.getStringExtra("einkaufsDatum").toString()
        if (!laden.equals("null") && !datum.equals("null")) {
            allItemList = thisDatabase.getProduktFromKassenzettel(laden, datum.toInt(),false)
            setupLadenName(laden)
            setupBitmap(thisDatabase.getKassenzettelFromDatabase(laden,datum.toInt())!!)
        }
        else{
            val myIcon = resources.getDrawable(R.drawable.filter_icon)
            val bitmap = (myIcon as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val bitmapdata: ByteArray = stream.toByteArray()
            datum = getDateNow()
            setupBitmap(Kassenzettel(datum,bitmapdata,"null",0))
        }
    }

    private fun setupList(){
        val listView = findViewById<ListView>(R.id.listViewProdukt)
        val adapter = EditierenAdapter(this,R.layout.editieren_adapter,allItemList,this)
        listView.adapter = adapter
        listView.isClickable = true
    }

    private fun setupLadenName(ladenName:String){
            getLadenTitle().setText(ladenName)
    }

    private fun ladenNameControl(){
        getLadenTitle().setOnClickListener{
            val view = inflateView(R.layout.popup_edit_laden_name)!!
            val popUp = setupPopupWindow(view)
            popUpEditNameSetup(view,popUp)
            popUpEditNameControl(view,popUp)
        }
    }

    private fun popUpEditNameSetup(view: View,popupMenu: PopupWindow){
        popupMenu.showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    private fun popUpEditNameControl(view: View,popupMenu: PopupWindow){
        saveButtonPopupControl(view,popupMenu)
    }

    private fun saveButtonPopupControl(view: View,popupMenu: PopupWindow){
        val neuerName = view.findViewById<EditText>(R.id.text_name_popup)
        val saveButton = view.findViewById<Button>(R.id.speichern_name_popup)
        saveButton.setOnClickListener{
            if(!getLadenTitle().text.equals(neuerName.text.toString())){
                nameChanged = true
                setupLadenName(neuerName.text.toString())
            }
            popupMenu.dismiss()
        }
    }

    private fun saveButtonControl(){
        val saveButton = findViewById<Button>(R.id.button_save)
        saveButton.setOnClickListener{
            saveToDatabase()
            allItemList = thisDatabase.getProduktFromKassenzettel(laden, datum.toInt(),false)
            setupList()
        }
    }

    private fun saveToDatabase() {
        if (!hasTitle()) {
            generateToast("Bitte Laden Name Eingeben!")
            return
        }
        if (!thisDatabase.checkIfKassenzettelExist(laden, datum.toInt())) {
            val laden = getLadenTitle().text.toString();
            val datum = getDateNow()
            val betrag = getTotalValue()
            val foto = decideFoto(neuerFoto)
            thisDatabase.addKassenzettel(Kassenzettel(datum,foto,laden,betrag))
        } else {
            thisDatabase.updateSumme(getTotalValue(), laden, datum.toInt())
            updateLadenNameInDatabase()
            updateFoto()
        }
        updateEntry()
        addedItemList.clear()
        deletedItemList.clear()
    }

    private fun decideFoto(bitmap: Bitmap): ByteArray?{
        if (bitmap == null || bitmap.isRecycled)
            return null
        return getFotoInByte(bitmap)
    }

    private fun getFotoInByte(foto: Bitmap):ByteArray {
        val stream = ByteArrayOutputStream()
        foto.compress(Bitmap.CompressFormat.PNG, 100, stream)
        foto.recycle()
        return stream.toByteArray()
    }

    private fun getTotalValue():Int{
        var total = 0
        for (produkt in allItemList){
            total+=produkt.thisPreis
        }
        return total
    }

    private fun hasTitle():Boolean{
        if (getLadenTitle().text.toString() == "LADEN" || getLadenTitle().text.toString() == "")
            return false
        return true
    }

    private fun updateFoto(){
        thisDatabase.updateFoto(laden,datum.toInt(),neuerFoto)
    }

    private fun updateEntry(){
        deleteEntry()
        addEntry()
    }

    private fun addEntry() {
        for (produkt in addedItemList){
            if(produkt.thisLadenName == null)
                produkt.thisLadenName = getLadenTitle().text.toString()
            thisDatabase.addProduct(produkt)
        }

    }

    private fun deleteEntry() {
        for (produkt in deletedItemList)
            thisDatabase.deleteProduct(produkt)
    }

    private fun backButtonControl(){
        val backButton = findViewById<ImageView>(R.id.back_button_edit)
        backButton.setOnClickListener{

            if(addedItemList.isNotEmpty()||deletedItemList.isNotEmpty()){
                val view = inflateView(R.layout.popup_ask_save_action)!!
                popUpBackSetup(view)
                popUpBackControl(view)
            }
            else if (!thisDatabase.checkIfProduktExist(laden,datum.toInt())){
                switchScreenToAuflisten()
            }
            else {
                switchScreenToEinzel()
            }
        }
    }

    private fun popUpBackSetup(view: View){
        setupPopupWindow(view).showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    private fun popUpBackControl(view: View){
        popUpBackButtonNo(view)
        popUpBackButtonYes(view)
    }

    private fun popUpBackButtonNo(view: View){
        val buttonNo = view.findViewById<Button>(R.id.popup_no)
        buttonNo.setOnClickListener{
            switchScreenToEinzel()
        }
    }

    private fun popUpBackButtonYes(view: View){
        val buttonYes = view.findViewById<Button>(R.id.popup_yes)
        buttonYes.setOnClickListener{
            saveToDatabase()
            if (!thisDatabase.checkIfProduktExist(laden,datum.toInt())){
                switchScreenToAuflisten()
            }
            else {
                switchScreenToEinzel()
            }
        }
    }

    private fun updateLadenNameInDatabase() {
        thisDatabase.updateName(getLadenTitle().text.toString(), datum.toInt())
        nameChanged = false
    }

    private fun getLadenTitle(): TextView {
        return findViewById(R.id.laden_edit)!!
    }

    private fun switchScreenToAuflisten(){
        val toAuflisten = Intent(this, KassenzettelAuflistenBildschirm::class.java)
        thisDatabase.deleteKassenzettel(laden,datum.toInt())
        startActivity(toAuflisten)
    }

    private fun inflateView(id: Int): View? {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return inflater.inflate(id, null)
    }

    private fun setupPopupWindow(view: View): PopupWindow {
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT
        val focusable = true
        return PopupWindow(view,width,height,focusable)
    }

    private fun switchScreenToEinzel(){
        val toKassenEdit = Intent(this, KassenzettelEinzel::class.java)
        putValuesToIntent(toKassenEdit)
        startActivity(toKassenEdit)
    }

    private fun putValuesToIntent(toKassenEdit: Intent){
        toKassenEdit.putExtra("ladenName",laden)
        toKassenEdit.putExtra("einkaufsDatum",datum)
    }

    override fun minusButtonClicked(produkt: Produkt) {
        allItemList.remove(produkt)
        deletedItemList.add(produkt)
        setupList()
    }

    private fun deleteButtonControl(){
        val deleteAll = findViewById<Button>(R.id.button_del)
        deleteAll.setOnClickListener{
            for (produkt in allItemList){
                deletedItemList.add(produkt)
            }
            allItemList.clear()
            setupList()
        }

    }

}