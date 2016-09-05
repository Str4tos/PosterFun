package com.example.stratos.posterfun.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.stratos.posterfun.R;
import com.example.stratos.posterfun.utils.PreCont;

public class FilterSortFragment extends Fragment implements AdapterView.OnItemClickListener {

    public FilterSortFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filters, container, false);
//        if(catNames == null) {
//            DBContent dbContent = new DBContent(rootView.getContext());
//            SQLiteDatabase sqLiteDatabase = dbContent.getWritableDatabase();
//            catNames = new ArrayList<>();
//            catNames.add(ItemListActivity.SELECT_ALL);
//            Cursor cursor = sqLiteDatabase.rawQuery("SELECT catEvent.cat FROM catEvent;", null);
//            if (cursor.moveToFirst())
//                do {
//                    catNames.add(cursor.getString(0));
//                } while (cursor.moveToNext());
//            cursor.close();
//            dbContent.close();
//        }
        ListView lvMain = (ListView) rootView.findViewById(R.id.filtr_listView);
        String[] cont = getActivity().getResources().getStringArray(R.array.fsort);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(rootView.getContext(),
                android.R.layout.simple_list_item_single_choice, cont);
        lvMain.setOnItemClickListener(this);
        lvMain.setAdapter(adapter);
        lvMain.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvMain.setItemChecked(getActivity().getIntent().getIntExtra(PreCont.ARG_SECTION_SORT, 0), true);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Activity activity = getActivity();
        Intent intent = new Intent();
        intent.putExtra(PreCont.ARG_SECTION_SORT, position);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();

    }
}
