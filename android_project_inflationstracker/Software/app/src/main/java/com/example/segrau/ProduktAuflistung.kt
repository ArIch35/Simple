package com.example.segrau

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class ProduktAuflistung : AppCompatActivity() {
    private var thisDatabase= Database(this)
    private lateinit var listProdukte: ArrayList<TempProdukt>
    private var modeNow = Mode.PRODUKT_EINZEl

    internal class FilterAttributeHolder {
        var popupWindow: PopupWindow? = null
        var dropDown: Spinner? = null
        var aufsteigen: CheckBox? = null
        var absteigen: CheckBox? = null
        var apply: Button? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produkt_auflisten)
        listProdukte = thisDatabase.getTemporaryProduct()

        setupList()
        toolbarControl()
        controlChangeList()
        controlList()
        addButtonControl()
    }

    private fun addButtonControl(){
        val addButton = findViewById<FloatingActionButton>(R.id.add_auflisten_produkt)
        addButton.setOnClickListener{
            val newIntent = Intent(this,KassenzettelEditieren::class.java)
            startActivity(newIntent)
        }
    }

    private fun controlChangeList() {
        val changeButton = findViewById<ImageView>(R.id.switch_mode_button)
        changeButton.setOnClickListener {
            if (changeButton.tag.equals("einzel")){
                changeButton.tag = "grafik"
                changeButton.setImageResource(R.drawable.ic_round_square_24)
                modeNow = Mode.PRODUKT_GRAFIK
                setupList()
            }
            else{
                changeButton.tag = "einzel"
                changeButton.setImageResource(R.drawable.ic_baseline_view_agenda_24)
                modeNow = Mode.PRODUKT_EINZEl
                setupList()
            }
        }
    }

    private fun controlList(){
        val mainList = findViewById<ListView>(R.id.produktAuflisten_List)
        mainList.onItemClickListener =
            AdapterView.OnItemClickListener { _: AdapterView<*>?, _: View?, i: Int, _: Long ->
                switchScreenToProduktEinzel(i, thisDatabase.getTemporaryProduct())
            }
    }

    private fun switchScreenToProduktEinzel(position: Int, list: List<Produkt> ){
        val toProduktEinzel = Intent(this, ProduktEinzeln::class.java)
        toProduktEinzel.putExtra("produkt_name",list[position].thisName)
        toProduktEinzel.putExtra("prev_screen","produkt_listung")
        startActivity(toProduktEinzel)
    }

    private fun inflateView(): View? {
        val inflater = getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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

    private fun navigationMenuControl() {
        val navView = findViewById<NavigationView>(R.id.produkt_list_nav_view)
        navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.nav_auflistung_produkt -> false
                R.id.nav_auflistung_kassenzettel -> switchScreenToKassenzettelAuflisten()
            }
            true
        })

    }

    private fun switchScreenToKassenzettelAuflisten() {
        val toKassenzettelAuflisten = Intent(this, KassenzettelAuflistenBildschirm::class.java)
        startActivity(toKassenzettelAuflisten)
    }

    private fun setupList() {
        val mainList = findViewById<ListView>(R.id.produktAuflisten_List)
        val adapter = ProduktAuflistungAdapter(this, R.layout.produktauflisten_adapter_einzel,
            listProdukte, modeNow, thisDatabase)
        mainList.isClickable = true
        mainList.adapter = adapter
    }

    private fun toolbarControl() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.produkt_list_drawer_layout)
        val thisToolbar = findViewById<Toolbar>(R.id.produkt_list_toolbar)
        listIconControl(thisToolbar, drawerLayout)
        filterIconControl()
        navigationMenuControl()
    }

    private fun filterIconControl() {
        val filterToggle = findViewById<ImageView>(R.id.produkt_list_toggle_filter)
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
        holder!!.apply!!.setOnClickListener {
            val ascending = holder.aufsteigen!!.isChecked
            val descending = holder.absteigen!!.isChecked

            if (ascending || !descending){
                when(holder.dropDown!!.selectedItem.toString()){
                    in "Name" -> listProdukte.sortBy { it.thisName }
                    in "Preis" -> listProdukte.sortBy { it.thisPreis }
                    in "Zuletzt Gekauft" -> listProdukte.sortBy { it.thisDatum }
                }
            } else{
                when(holder.dropDown!!.selectedItem.toString()){
                    in "Name" -> listProdukte.sortByDescending { it.thisName }
                    in "Preis" -> listProdukte.sortByDescending { it.thisPreis }
                    in "Zuletzt Gekauft" -> listProdukte.sortByDescending { it.thisDatum }
                }
            }
            setupList()
            holder.popupWindow!!.dismiss()
        }
    }

    private fun dropDownControl(holder: FilterAttributeHolder?) {
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

    private fun setupDropDownCategory(holder: FilterAttributeHolder?, popupView: View) {
        val sortCategory = arrayOf("Name","Preis","Zuletzt Gekauft")
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
            this_toolbar.findViewById<ImageView>(R.id.produkt_burger_menu)

        //lambda function of  setOnClickListener
        listToggleFirstScreen.setOnClickListener {
            drawer_layout.openDrawer(
                GravityCompat.START
            )
        }
    }
}
