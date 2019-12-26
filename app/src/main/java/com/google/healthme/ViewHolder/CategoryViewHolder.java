package com.google.healthme.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.healthme.Interface.ItemClickListener;
import com.google.healthme.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView categoryImage;
    public TextView  categoryName;
    ItemClickListener itemClickListener;


    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }
    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        categoryImage=itemView.findViewById(R.id.doctorImages);
        categoryName=itemView.findViewById(R.id.doctorCategoryName);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition());
    }
}
