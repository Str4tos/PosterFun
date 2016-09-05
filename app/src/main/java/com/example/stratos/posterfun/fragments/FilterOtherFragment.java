package com.example.stratos.posterfun.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.stratos.posterfun.R;
import com.example.stratos.posterfun.utils.PreCont;

public class FilterOtherFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Button btnDone;
    private EditText etCost;
    private EditText etDist;
    private CheckBox cbFreeOnly;
    private CheckBox cbCost;
    private CheckBox cbDistance;
    public FilterOtherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fother, container, false);
        cbFreeOnly = (CheckBox) rootView.findViewById(R.id.fother_freeonly);
        cbFreeOnly.setOnCheckedChangeListener(this);
        cbCost = (CheckBox) rootView.findViewById(R.id.fother_cost);
        cbCost.setOnCheckedChangeListener(this);
        cbDistance =(CheckBox) rootView.findViewById(R.id.fother_distance);
        cbDistance.setOnCheckedChangeListener(this);
        etCost = (EditText) rootView.findViewById(R.id.fother_costval);
        etDist = (EditText) rootView.findViewById(R.id.fother_distval);

        btnDone = (Button) rootView.findViewById(R.id.fother_done);
        btnDone.setOnClickListener(this);
        if(PreCont.filter_costVal == 0)
            cbFreeOnly.setChecked(true);
        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.fother_done:
                if(cbFreeOnly.isChecked())
                    PreCont.filter_costVal = 0;
                else if(cbCost.isChecked() && etCost.getText() != null)
                    PreCont.filter_costVal = Integer.parseInt(etCost.getText().toString());
                else PreCont.filter_costVal = -1;
                if(cbDistance.isChecked() && etDist.getText() != null){
                    PreCont.filter_distVal = Integer.parseInt(etDist.getText().toString());
                }else PreCont.filter_distVal = -1;
                Activity activity = getActivity();
                Intent intent = new Intent();
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.fother_freeonly:
                if(isChecked)
                    cbCost.setChecked(false);
                cbCost.setEnabled(!isChecked);
                btnDone.setEnabled(isChecked);
                break;
            case R.id.fother_cost:
                etCost.setEnabled(isChecked);
                btnDone.setEnabled(isChecked);
                break;
            case R.id.fother_distance:
                etDist.setEnabled(isChecked);
                btnDone.setEnabled(isChecked);
                break;

        }
    }
}
