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

public class FilterCategoryFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ArrayList<String> catNames;

    public FilterCategoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filters, container, false);
        if(catNames == null) {
            DBContent dbContent = new DBContent(rootView.getContext());
            SQLiteDatabase sqLiteDatabase = dbContent.getWritableDatabase();
            catNames = new ArrayList<>();
            catNames.add(PreCont.SELECT_ALL);
            //Cursor cursor = sqLiteDatabase.rawQuery("SELECT catevent.cat FROM catevent;", null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT catevent.cat, Count(eventincat.idcat) AS countid" +
                    " FROM catevent INNER JOIN eventincat ON catevent.id = eventincat.idcat" +
                    " GROUP BY catevent.cat" +
                    " ORDER BY countid DESC;", null);
            if (cursor.moveToFirst())
                do {
                    catNames.add(cursor.getString(0));
                } while (cursor.moveToNext());
            cursor.close();
            dbContent.close();
        }
        ListView lvMain = (ListView) rootView.findViewById(R.id.filtr_listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(rootView.getContext(),
                android.R.layout.simple_list_item_single_choice, catNames);
        lvMain.setOnItemClickListener(this);
        lvMain.setAdapter(adapter);
        lvMain.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvMain.setItemChecked(catNames.indexOf(getActivity().getIntent().getStringExtra(PreCont.ARG_SECTION_CAT)), true);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Activity activity = getActivity();
        Intent intent = new Intent();
        intent.putExtra(PreCont.ARG_SECTION_CAT, catNames.get(position));
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();

    }
}
