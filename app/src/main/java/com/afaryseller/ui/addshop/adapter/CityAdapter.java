package com.afaryseller.ui.addshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.afaryseller.R;
import com.afaryseller.ui.addshop.model.StateModel;
import com.afaryseller.ui.signup.CityModel;

import java.util.ArrayList;

public class CityAdapter extends BaseAdapter {

    Context context;
    ArrayList<CityModel.Result> arrayList;
    LayoutInflater inflater ;

    public CityAdapter(Context context, ArrayList<CityModel.Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        inflater = (LayoutInflater.from(context));

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_country, null);
        TextView names =  view.findViewById(R.id.tvName11);
        names.setText(arrayList.get(i).getName());
        return view;
    }
}