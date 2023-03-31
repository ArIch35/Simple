package com.example.proto1;

import androidx.annotation.NonNull;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.slider.RangeSlider;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

public class FilterScreen extends AbstractScreen {
    private filterAttributeHolder holder = new filterAttributeHolder();

    static class filterAttributeHolder{
        TextView toolbarName;
        RangeSlider priceRange;
        TextView priceText;
        AutoCompleteTextView timeDropDown;
        ImageView calendarDate;
        TextView availableDate;
        String searchDate;
        TextView minSize;
        RangeSlider minSizeBar;
        AutoCompleteTextView sortType;
        CheckBox ascending;
        CheckBox descending;
        Button filterButton;
        Button clearButton;
        ImageView backButton;
    }

    private filterAttributeHolder initializeHolder(){

        filterAttributeHolder holder = new filterAttributeHolder();

        holder.toolbarName = findViewById(R.id.toolbar_name);
        holder.priceRange = findViewById(R.id.preis_range);
        holder.priceText = findViewById(R.id.preis_text);
        holder.timeDropDown = findViewById(R.id.drop_down_zeit);
        holder.calendarDate = findViewById(R.id.available_date_icon);
        holder.availableDate = findViewById(R.id.available_date);
        holder.searchDate = null;
        holder.minSize = findViewById(R.id.size_text);
        holder.minSizeBar = findViewById(R.id.size_range_bar);
        holder.sortType = findViewById(R.id.sort);
        holder.ascending = findViewById(R.id.checkbox_aufsteigen);
        holder.descending = findViewById(R.id.checkbox_absteigen);
        holder.filterButton = findViewById(R.id.exit_filter_button);
        holder.clearButton = findViewById(R.id.clear_filter_button);
        holder.backButton = findViewById(R.id.back_button);


        return holder;
    }

    @NonNull
    private ArrayList<String> getDurationCategory() {
        return new ArrayList<>(Arrays.asList("< 6 Months","6 - 12 Month","13 - 18 Month","19 - 24 Month","> 24 Months"));
    }

    @NonNull
    private ArrayList<String> getSortCategory() {
        return new ArrayList<>(Arrays.asList("Price","Duration","Date Available","Size"));
    }

    private ArrayList<Integer> getCalendarDates(){
        final Calendar calendar = Calendar.getInstance();
        return new ArrayList<>(Arrays.asList(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)));
    }

    @NonNull
    private DatePickerDialog getCalendar() {
        return new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String month = decideMonth(monthOfYear);
                    String day = decideDay(dayOfMonth);

                    String date = day + "-" + month + "-" + year1;
                    holder.availableDate.setText(date);
                    holder.searchDate = year1 + "-" + month + "-" + day;
                },
                getCalendarDates().get(0), getCalendarDates().get(1), getCalendarDates().get(2));
    }

    @NonNull
    private String decideMonth(int monthOfYear) {
        if (monthOfYear + 1 < 10){
           return "0" + (monthOfYear + 1);
        }
        return String.valueOf(monthOfYear + 1);
    }

    private String decideDay(int day) {
        if (day < 10){
            return "0" + day;
        }
        return String.valueOf(day);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_filter_screen);
        filterWindowSetup();
        filterWindowControl();
    }

    private void filterWindowSetup() {
        holder = initializeHolder();
        holder.toolbarName.setText(getStringValueFromRes(R.string.filter));
        setPreviousFilter();
    }

    private void setPreviousFilter() {
        setupPreisBar();
        setupDatePicker();
        setupSize();
        setDropDownDate(getIntent().getStringExtra("houseDuration"));
        setDropDownSort(getIntent().getStringExtra("houseSort"));
        setOrder(getIntent().getStringExtra("order"));
    }

    private void setRangeBarValue(int from, int to){
        holder.priceRange.setValueFrom(0);
        holder.priceRange.setValueTo(thisDatabase.getMaxHousePrice());
        holder.priceRange.setValues((float) from,(float)to);
        updatePrice();
    }

    private void setDate(String date) {
        holder.availableDate.setText(date);

        if (date == null || date.equals("")){
            date = null;
        }

        holder.searchDate = date;
    }

    private void setSize(int size) {
        holder.minSizeBar.setValues((float) size);
        updateMinSizeBar(size);
    }

    private void setDropDownDate(String selection) {
        holder.timeDropDown.setText(selection);
    }

    private void setDropDownSort(String selection) {
        holder.sortType.setText(selection);
    }

    private void setOrder(String order) {
        if (order != null)
            switch (order){
                case "ASC":
                    holder.ascending.setChecked(true);
                    holder.descending.setChecked(false);
                    break;
                case "DESC":
                    holder.descending.setChecked(true);
                    holder.ascending.setChecked(false);
                    break;
                default:
                    holder.descending.setChecked(false);
                    holder.ascending.setChecked(false);
                    break;
            }
    }

    private void setupPreisBar() {
        int maximum = getIntent().getIntExtra("priceMax",0);
        if (maximum == 0){
            maximum = thisDatabase.getMaxHousePrice();
        }
        int minimum = getIntent().getIntExtra("priceMin",0);
        setRangeBarValue(minimum,maximum);
    }

    private void setupSize() {
        int size = getIntent().getIntExtra("houseSize",0);
        holder.minSizeBar.setValueTo(thisDatabase.getMaxHouseSize());
        setSize(size);
    }

    private void setupDatePicker() {
        String date = getIntent().getStringExtra("houseDate");
        if (date != null) {
            setDate(date);
        }
    }

    private void filterWindowControl() {
        priceBarControl();
        availableDateControl();
        timeDropDownControl();
        houseSizeControl();
        sortTypeControl();
        filterButtonControl();
        clearButtonControl();
        exitButtonControl();
    }

    private void sortTypeControl() {
        holder.sortType.setOnFocusChangeListener((v, hasFocus) -> {
            ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), R.layout.drop_down_material,getSortCategory());
            holder.sortType.setAdapter(adapter);
        });
    }

    private void timeDropDownControl() {
        holder.timeDropDown.setOnFocusChangeListener((v, hasFocus) -> {
            ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), R.layout.drop_down_material,getDurationCategory());
            holder.timeDropDown.setAdapter(adapter);
        });
    }

    private void houseSizeControl() {
        holder.minSizeBar.setLabelFormatter(value -> {
            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(0);
            return format.format(Double.valueOf(value));
        });
        holder.minSizeBar.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
                updateMinSizeBar(holder.minSizeBar.getValues().get(0).intValue());
            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
               updateMinSizeBar(holder.minSizeBar.getValues().get(0).intValue());
            }
        });
    }

    private void updateMinSizeBar(int value) {
        String text = getStringValueFromRes(R.string.min_size)+": " + value + " m²";
        holder.minSize.setText(text);
    }

    private void availableDateControl() {
        holder.calendarDate.setOnClickListener(v -> getCalendar().show());
    }

    private void priceBarControl(){
        holder.priceRange.setLabelFormatter(value -> {
            NumberFormat format = NumberFormat.getCurrencyInstance();
            format.setMaximumFractionDigits(0);
            format.setCurrency(Currency.getInstance("EUR"));
            return format.format(Double.valueOf(value));
        });
        holder.priceRange.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
                updatePrice();
            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                updatePrice();
            }
        });
    }

    private void updatePrice() {
        Pair<Integer,Integer> prices = getFilteredPrices();
        String text = getStringValueFromRes(R.string.price_filter)+": " + prices.first + "€ - " + prices.second + "€";
        holder.priceText.setText(text);
    }

    private Pair<Integer,Integer> getFilteredPrices(){
        List<Float> prices = holder.priceRange.getValues();
        int maximum = prices.get(1).intValue();
        int minimum = prices.get(0).intValue();
        return new Pair<>(minimum,maximum);
    }

    private void filterButtonControl(){
        holder.filterButton.setOnClickListener(view -> {
            Intent intent = intentMaker.getFirstScreenIntent();
            putValueToIntent(intent);
            startActivity(intent);
        });
    }

    private void clearButtonControl(){
        holder.clearButton.setOnClickListener(view -> deleteFilters());
    }

    private void putValueToIntent(Intent intent) {
        putPrice(intent);
        putDuration(intent);
        putDate(intent);
        putSize(intent);
        putSort(intent);
        putOrder(intent);
    }

    private void putPrice(Intent intent) {
        Pair<Integer,Integer> price_filter = getFilteredPrices();
        boolean price_is_not_filtered = (price_filter.first == 0 && price_filter.second.equals(thisDatabase.getMaxHousePrice()));
        if (!price_is_not_filtered) {
            intent.putExtra("priceMin", price_filter.first);
            intent.putExtra("priceMax", price_filter.second);
            intent.putExtra("filtered",true);
        }
    }

    private void putDuration(Intent intent) {
        intent.putExtra("houseDuration",holder.timeDropDown.getText().toString());
        if (!holder.timeDropDown.getText().toString().equals(""))
            intent.putExtra("filtered",true);
    }

    private void putDate(Intent intent) {
        intent.putExtra("houseDate",holder.searchDate);
        if (holder.searchDate != null)
            intent.putExtra("filtered",true);
    }

    private void putSize(Intent intent){
        intent.putExtra("houseSize",holder.minSizeBar.getValues().get(0).intValue());
        if (holder.minSizeBar.getValues().get(0) != 0)
            intent.putExtra("filtered",true);
    }

    private void putSort(Intent intent){
        if (!holder.sortType.getText().toString().equals("")) {
            intent.putExtra("houseSort", holder.sortType.getText().toString());
        }
    }

    private void putOrder(Intent intent) {
        boolean ascending = holder.ascending.isChecked();
        boolean descending = holder.descending.isChecked();

        if (ascending || !descending){
            intent.putExtra("order","ASC");
        } else{
            intent.putExtra("order","DESC");
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        savePriceOnRotate(outState);
        outState.putString("houseDate",holder.searchDate);
        outState.putInt("houseSize",holder.minSizeBar.getValues().get(0).intValue());
        outState.putString("houseSort", holder.sortType.getText().toString());
    }

    private void savePriceOnRotate(@NonNull Bundle outState) {
        Pair<Integer,Integer> price_filter = getFilteredPrices();
        outState.putInt("priceMin", price_filter.first);
        outState.putInt("priceMax", price_filter.second);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        setRangeBarValue(savedInstanceState.getInt("priceMin"),savedInstanceState.getInt("priceMax"));
        setDate(savedInstanceState.getString("houseDate"));
        setSize(savedInstanceState.getInt("houseSize"));
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void deleteFilters(){
        setRangeBarValue(0,thisDatabase.getMaxHousePrice());
        setDate("");
        setSize(0);
        setDropDownDate("");
        setDropDownSort("");
        setOrder("");
    }

    private void exitButtonControl() {
        holder.backButton.setOnClickListener(v -> startActivity(intentMaker.getFirstScreenIntent()));
    }
}