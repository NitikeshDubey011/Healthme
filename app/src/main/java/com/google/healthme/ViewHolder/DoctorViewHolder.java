package com.google.healthme.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.healthme.Interface.ItemClickListener;
import com.google.healthme.R;

public class DoctorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView doctorProfileImage;
    public TextView doctorName;
    public TextView doctorQualification;
    ItemClickListener itemClickListener;


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public DoctorViewHolder(@NonNull View itemView) {
        super(itemView);

        doctorProfileImage = itemView.findViewById(R.id.doctImage);
        doctorName = itemView.findViewById(R.id.doctNameTv);
        doctorQualification=itemView.findViewById(R.id.qualificationTv);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition());
    }
}
