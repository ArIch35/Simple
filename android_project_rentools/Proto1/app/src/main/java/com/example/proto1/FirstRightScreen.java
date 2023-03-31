package com.example.proto1;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class FirstRightScreen extends Fragment {
    private View view;
    private Database thisDatabase;
    private SharedPreferenceMaker spMaker;

    private SearchView getSearchBar(){
        return view.findViewById(R.id.search_bar_first_screen);
    }

    private ListView getSearchList(){return  view.findViewById(R.id.list_view_result_first_screen);}

    private FirstScreen getFirstScreen(){
        return ((FirstScreen) getActivity());
    }

    private void setDatabase() {
        thisDatabase = new Database(getActivity());
    }

    private void setView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_first_right_screen, container, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setupScreen(inflater, container);
        searchBarControl();
        return view;
    }

    private void setupScreen(LayoutInflater inflater, ViewGroup container) {
        setView(inflater, container);
        setDatabase();
        setupSearch();
    }

    private void setupSearch() {
        spMaker = new SharedPreferenceMaker(getActivity());
        getSearchBar().setIconified(false);
        if (spMaker.getSearchedCity() != null){
            getSearchBar().setQuery(spMaker.getSearchedCity(),false);
        }
    }

    private void searchBarControl() {
        getSearchBar().setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                controlQueryChange(newText);
                return false;
            }
        });


    }

    private void controlQueryChange(String newText){
        ArrayList<String> filtered_city = new ArrayList<>();

        if(!newText.equals("")) {
            filtered_city = (ArrayList<String>) thisDatabase.getCityResultFromDatabase(newText);
        }
        else {
            setCityFilter("");
            setupSideListView(new ArrayList<>());
            setupMainList(thisDatabase.getSearchResultFromDatabase(null));
        }

        if (filtered_city.isEmpty()) {
            setupSideListView(new ArrayList<>());
            return;
        }

        setupSideListView(filtered_city);
        clickedCityControl(newText);


        if (textEqualCityList(newText, filtered_city)){
            setupSideListView(new ArrayList<>());
            updateMainList(filtered_city.get(0));
        }
    }

    private void setupSideListView(ArrayList<String> filtered_item) {
        ArrayAdapter<String> filter_string = new ArrayAdapter<>(getFirstScreen(), android.R.layout.simple_list_item_1, filtered_item);
        getSearchList().setClickable(true);
        getSearchList().setAdapter(filter_string);
    }

    private void clickedCityControl(String text){
        getSearchList().setOnItemClickListener((adapterView, view, i, l) -> {
            getSearchBar().setQuery(getCityFromDatabase(text, i),false);
            getSearchBar().clearFocus();
        });
    }

    private String getCityFromDatabase(String text, int i) {
        return thisDatabase.getCityResultFromDatabase(text).get(i);
    }

    private boolean textEqualCityList(String newText, ArrayList<String> filtered_city) {
        return filtered_city.get(0).equals(newText);
    }

    private void updateMainList(String query) {
        setupMainList(thisDatabase.getSearchResultFromDatabase(query));
        setCityFilter(query);
    }

    private void setupMainList(ArrayList<House> list) {
        getFirstScreen().setHouseList(list);
        getFirstScreen().setupMainListView();
    }

    private void setCityFilter(String city){
        SharedPreferences.Editor sp_editor = spMaker.getSPEditor();
        sp_editor.putString("cityNow",city);
        sp_editor.apply();
    }
}