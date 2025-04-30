package com.afaryseller.ui.addshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.afaryseller.R;
import com.afaryseller.ui.addshop.model.CountryModel;
import com.afaryseller.ui.addshop.model.StateModel;

import java.util.ArrayList;

public class CountryAdapter extends BaseAdapter {

    Context context;
    ArrayList<CountryModel.Result> arrayList;
    LayoutInflater inflater ;

    public CountryAdapter(Context context, ArrayList<CountryModel.Result> arrayList) {
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
        //view = inflater.inflate(R.layout.item_country, viewGroup);
        view = inflater.inflate(R.layout.item_country, null);

        TextView names =  view.findViewById(R.id.tvName11);
        names.setText(arrayList.get(i).getName());
        return view;
    }
}
