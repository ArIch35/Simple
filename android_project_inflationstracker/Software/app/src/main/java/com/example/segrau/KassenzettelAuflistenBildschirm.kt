package com.example.segrau

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView


class KassenzettelAuflistenBildschirm : AppCompatActivity() {
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
        setContentView(R.layout.activity_kassenzettel_auflisten)

        setupList(thisDatabase.getKassenzettelArrayFromDatabase())
        toolbarControl()
        controlList()
        addButtonControl()
        sideMenuControl()
    }

    private fun sideMenuControl() {
        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.nav_auflistung_produkt -> switchScreenToProduktAuflisten()
                R.id.nav_auflistung_kassenzettel -> false
            }
            true
        })
    }

    private fun switchScreenToProduktAuflisten() {
        val toProduktAuflisten = Intent(this, ProduktAuflistung::class.java)
        startActivity(toProduktAuflisten)
    }

    private fun addButtonControl(){
        val addButton = findViewById<FloatingActionButton>(R.id.add_auflisten)
        addButton.setOnClickListener{
            val newIntent = Intent(this,KassenzettelEditieren::class.java)
            startActivity(newIntent)
        }

    }

    private fun controlList(){
        val mainList = findViewById<ListView>(R.id.list_view_main)
        mainList.onItemClickListener =
            AdapterView.OnItemClickListener { _: AdapterView<*>?, _: View?, i: Int, _: Long ->
                switchScreenToKassenEinzel(i, thisDatabase.getKassenzettelArrayFromDatabase())
            }
    }

    private fun switchScreenToKassenEinzel(position: Int, list: List<Kassenzettel> ){
        val toKassenEinzel = Intent(this, KassenzettelEinzel::class.java)
        putValuesToIntent(position,toKassenEinzel,list)
        startActivity(toKassenEinzel)
    }

    private fun putValuesToIntent(position: Int,intent: Intent, list: List<Kassenzettel>){
        val tempHolder = list[position]

        intent.putExtra("ladenName",tempHolder.LadenName)
        intent.putExtra("einkaufsDatum",tempHolder.EinkaufsDatum)
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

    private fun initializeHolder(popupView: View): FilterAttributeHolder {
        val holder = FilterAttributeHolder()
        holder.popupWindow = setupPopupWindow(popupView)
        holder.dropDown = popupView.findViewById(R.id.drop_down_category)
        holder.aufsteigen = popupView.findViewById(R.id.checkbox_aufsteigen)
        holder.absteigen = popupView.findViewById(R.id.checkbox_absteigen)
        holder.apply = popupView.findViewById(R.id.apply_filter)
        return holder
    }

    private fun setupList(listZettel: ArrayList<Kassenzettel>) {
        val mainList = findViewById<ListView>(R.id.list_view_main)
        val adapter = KassenzettelAdapter(this, R.layout.activity_kassenzettel_adapter, listZettel)
        mainList.isClickable = true
        mainList.adapter = adapter
    }

    private fun toolbarControl() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val thisToolbar = findViewById<Toolbar>(R.id.toolbar)
        listIconControl(thisToolbar, drawerLayout)
        filterIconControl(thisToolbar)
    }

    private fun filterIconControl(this_toolbar: Toolbar) {
        val filterToggle =
            this_toolbar.findViewById<ImageView>(R.id.list_toggle_filter)
        filterToggle.setOnClickListener {
            popupFilterControl()
        }
    }

    private fun popupFilterControl() {
        val popupView: View = inflateView()!!
        val holder = initializeHolder(popupView)

        holder.popupWindow!!.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        setupDropDownCategory(holder,popupView)

        dropDownControl(holder)

        applyButtonControl(holder)
    }

    private fun applyButtonControl(holder: FilterAttributeHolder?){
        var queryDecider = ""
        holder!!.apply!!.setOnClickListener {
            when(holder.dropDown!!.selectedItem.toString()){
                in "Laden" -> queryDecider = "LadenName "
                in "Datum" -> queryDecider = "EinkaufsDatum "
            }
            val ascending = holder.aufsteigen!!.isChecked
            val descending = holder.absteigen!!.isChecked

            queryDecider += if (ascending || !descending){
                "ASC"
            } else{
                "DESC"
            }

            setupList(thisDatabase.getSortedKassenzettelArrayFromDatabase(queryDecider))
            holder.popupWindow!!.dismiss()
        }
    }

    private fun dropDownControl(holder: FilterAttributeHolder?) {
        holder!!.dropDown!!.onItemSelectedListener = object : OnItemSelectedListener {
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

    private fun setupDropDownCategory(holder: FilterAttributeHolder?, popupView: View) {
        val sortCategory = arrayOf("Datum", "Laden")
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(popupView.context, android.R.layout.simple_spinner_dropdown_item, sortCategory)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder!!.dropDown!!.adapter = adapter

    }

    private fun listIconControl(
        this_toolbar: Toolbar,
        drawer_layout: DrawerLayout
    ) {
        val listToggleFirstScreen =
            this_toolbar.findViewById<ImageView>(R.id.list_toggle)

        //lambda function of  setOnClickListener
        listToggleFirstScreen.setOnClickListener {
            drawer_layout.openDrawer(
                GravityCompat.START
            )
        }
    }

    private fun generateToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}