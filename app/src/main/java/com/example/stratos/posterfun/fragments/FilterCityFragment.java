package com.example.stratos.posterfun.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.stratos.posterfun.R;
import com.example.stratos.posterfun.utils.DBContent;
import com.example.stratos.posterfun.utils.PreCont;

import java.util.ArrayList;

public class FilterCityFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ArrayList<String> cityNames;

    public FilterCityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_filters, container, false);

        if(cityNames == null) {
            DBContent dbContent = new DBContent(rootView.getContext());
            SQLiteDatabase sqLiteDatabase = dbContent.getWritableDatabase();
            cityNames = new ArrayList<>();
            cityNames.add(PreCont.SELECT_ALL);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT city.city FROM city;", null);
            if (cursor.moveToFirst())
                do {
                    cityNames.add(cursor.getString(0));
                } while (cursor.moveToNext());
            cursor.close();
            dbContent.close();
        }
        ListView lvMain = (ListView) rootView.findViewById(R.id.filtr_listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(rootView.getContext(),
                android.R.layout.simple_list_item_single_choice, cityNames);
        lvMain.setOnItemClickListener(this);
        lvMain.setAdapter(adapter);
        lvMain.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvMain.setItemChecked(cityNames.indexOf(getActivity().getIntent().getStringExtra(PreCont.ARG_SECTION_CITY)), true);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Activity activity = getActivity();
        Intent intent = new Intent();
        intent.putExtra(PreCont.ARG_SECTION_CITY, cityNames.get(position));
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();

    }
}
