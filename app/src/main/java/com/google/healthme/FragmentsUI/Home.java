package com.google.healthme.FragmentsUI;


import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.healthme.Common.Common;
import com.google.healthme.DoctorCategory;
import com.google.healthme.Interface.ItemClickListener;
import com.google.healthme.Model.CategoryModel;
import com.google.healthme.R;
import com.google.healthme.ViewHolder.CategoryViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {
    private RecyclerView recyclerView;

    //Firebase Database
    private FirebaseDatabase database;
    private DatabaseReference reference,databaseReference,referenceTemp;

    //FirebaseUi adapter
    private FirebaseRecyclerOptions<CategoryModel> options;
    FirebaseRecyclerAdapter<CategoryModel, CategoryViewHolder> adapter;


    private static Home INSTANCE = null;

    public static Home getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Home();

        }
        return INSTANCE;
    }

    public Home() {
        database = FirebaseDatabase.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference();
        referenceTemp=databaseReference.child("User");
        reference = database.getReference(Common.STR_CATEGORY_BACKGROUND);
        options = new FirebaseRecyclerOptions.Builder<CategoryModel>()
                .setQuery(reference, CategoryModel.class)
                .build();
        referenceTemp.child("value").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
//                demoValue.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        adapter = new FirebaseRecyclerAdapter<CategoryModel, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CategoryViewHolder categoryViewHolder, int i, @NonNull final CategoryModel categoryModel) {
                Glide.with(Home.this)
                        .load(categoryModel.getImageLink())
                        .error(R.drawable.doctornotfound)
                        .into(categoryViewHolder.categoryImage);


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

                categoryViewHolder.categoryName.setText(categoryModel.getName());

                categoryViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                    }
                });
            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout, parent, false);

                return new CategoryViewHolder(view);
            }
        };
    }


    private void setDoctorCategory() {
        adapter.startListening();
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
        View view = inflater.inflate(R.layout.fragment_home_ui, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        setDoctorCategory();
        return view;
    }

}
