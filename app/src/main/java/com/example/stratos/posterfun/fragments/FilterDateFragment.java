package com.example.stratos.posterfun.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.stratos.posterfun.R;

import java.util.Calendar;

public class FilterDateFragment extends Fragment implements View.OnClickListener {

    private DatePicker datePicker;
    public FilterDateFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fdate, container, false);
        rootView.findViewById(R.id.fdate_btnCansel).setOnClickListener(this);
        rootView.findViewById(R.id.fdate_btnDone).setOnClickListener(this);
        rootView.findViewById(R.id.fdate_btnAll).setOnClickListener(this);
        datePicker = (DatePicker) rootView.findViewById(R.id.fdate_picker);
        Calendar calendar = Calendar.getInstance();
        datePicker.updateDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        datePicker.setMaxDate(System.currentTimeMillis() + 31536000000L);
        return rootView;
    }

    @Override
    public void onClick(View v) {
//        ind date = datePicker.getDayOfMonth();
//        ind month = datePicker.getMonth() + 1;
//        ind year = datePicker.getYear();

        Activity activity = getActivity();
        Intent intent = new Intent();
        //intent.putExtra("filter_catName", catNames.get(position));
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

}
