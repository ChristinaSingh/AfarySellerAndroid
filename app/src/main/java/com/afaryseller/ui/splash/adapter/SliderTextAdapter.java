package com.afaryseller.ui.splash.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afaryseller.R;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.ui.splash.model.TitleDesModel;
import com.afaryseller.utility.SessionManager;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderTextAdapter extends
        SliderViewAdapter<SliderTextAdapter.SliderAdapterVH> {

    private Context context;
    private List<TitleDesModel.Result> mSliderItems ;

    public SliderTextAdapter(Context context, ArrayList<TitleDesModel.Result> get_result1) {
        this.context = context;
        this.mSliderItems = get_result1;
    }




    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_text, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            if(SessionManager.readString(context, Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("en"))  {
                viewHolder.tvDescription.setText(Html.fromHtml(mSliderItems.get(position).getDescription(), Html.FROM_HTML_MODE_COMPACT));
                viewHolder.tvTitle.setText(Html.fromHtml(mSliderItems.get(position).getHeading(), Html.FROM_HTML_MODE_COMPACT));
            }
            else if(SessionManager.readString(context, Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("fr")){
                viewHolder.tvDescription.setText(Html.fromHtml(mSliderItems.get(position).getDescriptionFr(), Html.FROM_HTML_MODE_LEGACY));
                viewHolder.tvTitle.setText(Html.fromHtml(mSliderItems.get(position).getHeadingFr(), Html.FROM_HTML_MODE_LEGACY));
            }
            else {
                viewHolder.tvDescription.setText(Html.fromHtml(mSliderItems.get(position).getDescription(), Html.FROM_HTML_MODE_COMPACT));
                viewHolder.tvTitle.setText(Html.fromHtml(mSliderItems.get(position).getHeading(), Html.FROM_HTML_MODE_COMPACT));

            }


        } else {
            if(SessionManager.readString(context, Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("en"))    {
                viewHolder.tvDescription.setText(Html.fromHtml(mSliderItems.get(position).getDescription()));
                viewHolder.tvTitle.setText(mSliderItems.get(position).getHeading());
            }
            else if(SessionManager.readString(context, Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("fr"))  {
                viewHolder.tvDescription.setText(Html.fromHtml(mSliderItems.get(position).getDescriptionFr()));
                viewHolder.tvTitle.setText(mSliderItems.get(position).getHeadingFr());
            }
            else  {
                viewHolder.tvDescription.setText(Html.fromHtml(mSliderItems.get(position).getDescription()));
                viewHolder.tvTitle.setText(mSliderItems.get(position).getHeading());
            }



        }



    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends ViewHolder {

        View itemView;

        TextView textViewDescription;
        TextView tvTitle,tvDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
        //    imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
         //   imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);


            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);

            this.itemView = itemView;
        }
    }

}