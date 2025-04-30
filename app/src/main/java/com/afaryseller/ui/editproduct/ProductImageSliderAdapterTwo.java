package com.afaryseller.ui.editproduct;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afaryseller.R;
import com.afaryseller.ui.addproduct.adapter.ProductImageSliderAdapterCopy;
import com.afaryseller.ui.addproduct.listener.ImageChangeListener;
import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProductImageSliderAdapterTwo extends
        SliderViewAdapter<ProductImageSliderAdapterTwo.SliderAdapterVH> {

    private Context context;
    private List<BitmapUrlModel> mSliderItems;
    ImageChangeListener listener;
    String chkImg;


    public ProductImageSliderAdapterTwo(Context context, ArrayList<BitmapUrlModel> mSliderItems,ImageChangeListener listener) {
        this.context = context;
        this.mSliderItems = mSliderItems;
        this.listener = listener;
    }

   /* public void renewItems(List<String> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(String sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }*/

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item3, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

   if(mSliderItems.get(position).getChkImg().equalsIgnoreCase("Url")) {

       Glide.with(viewHolder.itemView)
               .load(mSliderItems.get(position).getUrl())
               //.fitCenter()
               .into(viewHolder.imageViewBackground);
   }
   else {
       Glide.with(viewHolder.itemView)
               .load(mSliderItems.get(position).getImg())
               .fitCenter()
               .into(viewHolder.imageViewBackground);
   }


        viewHolder.tvUpdate.setOnClickListener(v -> listener.imageChange(position,"change"));
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        TextView textViewDescription,tvUpdate;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            tvUpdate = itemView.findViewById(R.id.tvUpdate);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }


}
