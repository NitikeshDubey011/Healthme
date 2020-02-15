package com.google.healthme.FragmentsUI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.healthme.Common.Common;
import com.google.healthme.Interface.ItemClickListener;
import com.google.healthme.Model.CategoryModel;
import com.google.healthme.Model.DoctorModel;
import com.google.healthme.R;
import com.google.healthme.ViewHolder.CategoryViewHolder;
import com.google.healthme.ViewHolder.DoctorViewHolder;


public class MyHealth extends Fragment {
    private RecyclerView recyclerView;

    //Firebase Database
    private FirebaseDatabase database;
    private DatabaseReference reference;

    //FirebaseUi adapter
    private FirebaseRecyclerOptions<DoctorModel> options;
    FirebaseRecyclerAdapter<DoctorModel, DoctorViewHolder> adapter;

    private static MyHealth INSTANCE = null;

    public static MyHealth getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new MyHealth();

        }
        return INSTANCE;
    }

    public MyHealth() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference(Common.ALL_DOCTOR);
        options = new FirebaseRecyclerOptions.Builder<DoctorModel>()
                .setQuery(reference, DoctorModel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<DoctorModel, DoctorViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final DoctorViewHolder doctorViewHolder, int i, @NonNull final DoctorModel doctorModel) {
                Glide.with(MyHealth.this)
                        .load(doctorModel.getDoctorProfileImage())
                        .error(R.drawable.doctornotfound)
                        .into(doctorViewHolder.doctorProfileImage);


//                Picasso.get().load(categoryModel.getImageUrl()).networkPolicy(NetworkPolicy.OFFLINE)
//                        .into(categoryViewHolder.categoryImage, new Callback() {
//                            @Override
//                            public void onSuccess() {
//
//                            }
//
//                            @Override
//                            public void onError(Exception e) {
//                                Picasso.get().load(categoryModel.getImageUrl()).error(R.drawable.doctornotfound)
//                                        .into(categoryViewHolder.categoryImage, new Callback() {
//                                            @Override
//                                            public void onSuccess() {
//
//                                            }
//
//                                            @Override
//                                            public void onError(Exception e) {
//                                                Log.e("healthme_error","Could not fetch image");
//                                            }
//                                        });
//                            }
//                        });

                doctorViewHolder.doctorName.setText(doctorModel.getDoctorName());
                doctorViewHolder.doctorQualification.setText(doctorModel.getDoctorQualification());

                doctorViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                    }
                });
            }

            @NonNull
            @Override
            public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_doctor, parent, false);

                return new DoctorViewHolder(view);
            }
        };
    }


    private void setDoctors() {
        adapter.startListening();
        recyclerView.requestLayout();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        if (adapter != null) {
            adapter.stopListening();
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.startListening();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_health, container, false);
        recyclerView = view.findViewById(R.id.rcViewDoct);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        setDoctors();
        return view;
    }

}
