package com.google.healthme;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.healthme.Common.Common;
import com.google.healthme.Model.AdminApproval;
import com.google.healthme.Model.MemberDoctorModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

public class DoctorRegister extends AppCompatActivity {
    private EditText name_doc, dob_doc, address, street, city, state, zip_code, email_text, mobile_num, country, profile_image, qualification_document;
    private Spinner gender_doc;
    private Spinner category;
    private Button submit_pending;
    private StorageReference storageReference;
    private Set<String> set;
    MemberDoctorModel model;
    private DatabaseReference mDatabase;
    private String mob;
    private SharedPreferences preferences;
    private final int SELECT_PROFILE_IMAGE = 1;
    private final int SELECT_QUALIFICATION_IMAGE = 2;
    private Uri imageUri,imageUri2;
    private ProgressDialog pDialog;
    String status = "-5";
    private String doctor_status = "Doctor";
    private int imageId;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private ArrayList<Uri> arrayList = new ArrayList<Uri>();
    String orderDescription;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    AdminApproval adminApproval;
    String type = "D";
    String full_name_str, dob_str, gender_str, address_str, city_str, email_str, mobile_str, state_st, pincode_str, country_str, profile_str, qualification_str, category_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_register);
        name_doc = findViewById(R.id.fullNameEdit);
        dob_doc = findViewById(R.id.dob);
        gender_doc = findViewById(R.id.gender);
        street = findViewById(R.id.street);
        city = findViewById(R.id.cityEdit);
        state = findViewById(R.id.stateEdit);
        zip_code = findViewById(R.id.zipEdit);
        country = findViewById(R.id.countryEdit);
        profile_image = findViewById(R.id.profileEdit);
        qualification_document = findViewById(R.id.qualificationEdit);
        preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        mob = preferences.getString("mobile", "");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        verifyStoragePermissions(this);
        category = findViewById(R.id.categoryEdit);
        email_text = findViewById(R.id.emailText);
        mobile_num = findViewById(R.id.mobilenum);
        storageReference = FirebaseStorage.getInstance().getReference().child(Common.DATABASE_DOCTOR_PATH);
        ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_doc.setAdapter(adapter_gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.doctor_categories, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        category.setAdapter(adapter);

        submit_pending = findViewById(R.id.submitButton);

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PROFILE_IMAGE);
            }
        });

        qualification_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photopickerIntent = new Intent(Intent.ACTION_PICK);
                photopickerIntent.setType("image/*");
                startActivityForResult(photopickerIntent, SELECT_QUALIFICATION_IMAGE);

            }
        });
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category_str = category.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        gender_doc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender_str = gender_doc.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEditText();
            }
        };

        dob_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(DoctorRegister.this, listener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
//                        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();


            }
        });
        submit_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                full_name_str = name_doc.getText().toString();
                dob_str = dob_doc.getText().toString();
                address_str = street.getText().toString();
                city_str = city.getText().toString();
                state_st = state.getText().toString();
                pincode_str = zip_code.getText().toString();
                country_str = country.getText().toString();
                profile_str = profile_image.getText().toString();
                qualification_str = qualification_document.getText().toString();
                email_str = email_text.getText().toString();
                mobile_str = mobile_num.getText().toString();

                if (full_name_str.isEmpty() || dob_str.isEmpty() || address_str.isEmpty() ||
                        city_str.isEmpty() || state_st.isEmpty() || pincode_str.isEmpty() ||
                        country_str.isEmpty() || category_str.isEmpty() || gender_str.isEmpty() ||
                        profile_str.isEmpty() || qualification_str.isEmpty() || mobile_str.length() < 10 || email_str.isEmpty()) {
                    Toast.makeText(DoctorRegister.this, "All fields are important!!", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

    }

    private void updateEditText() {
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dob_doc.setText(simpleDateFormat.format(calendar.getTime()));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PROFILE_IMAGE:
                if (resultCode == Activity.RESULT_OK) {

                    imageUri = imageReturnedIntent.getData();
                    String path = getRealPathFromURI(imageUri);
                    String filename = path.substring(path.lastIndexOf("/") + 1);
                    profile_image.setText(filename);
                    break;

                }
            case SELECT_QUALIFICATION_IMAGE:

                if (resultCode == Activity.RESULT_OK) {

                    imageUri2 = imageReturnedIntent.getData();
                    String path = getRealPathFromURI(imageUri2);
                    String filename = path.substring(path.lastIndexOf("/") + 1);
                    qualification_document.setText(filename);
                    break;

                }
        }
    }



    public String getRealPathFromURI(Uri contentUri) {
// can post image
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri,
                proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        //checking if file is available
        if (imageUri != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //getting the storage reference
            StorageReference sRef = storageReference.child(Common.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(imageUri));

            //adding the file to reference
            sRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            Uri url = uri.getResult();
                            //displaying success toast
                            Toast.makeText(DoctorRegister.this, "Data Saved Successfully!", Toast.LENGTH_SHORT).show();

                            model = new MemberDoctorModel(full_name_str, dob_str, gender_str, address_str, city_str, state_st, pincode_str
                                    , country_str, category_str, type);
//                            adminApproval = new AdminApproval(false);
//                            mDatabase.child(mobile_str).setValue(model);
//                            if (!set.isEmpty()) {
////                                for (int i = 0; i < set.size(); i++) {
//
//                                for (String numbers : set) {
//                                    FirebaseDatabase.getInstance()
//                                            .getReference(Common.DATABASE_DOCTOR_PATH)
//                                            .child(numbers).child("voteList").child(mobile_str).setValue(adminApproval);
//                                }
//                                FirebaseDatabase.getInstance()
//                                        .getReference(Common.DATABASE_DOCTOR_PATH)
//                                        .child(mob).child("voteList").child(mobile_str).setValue(adminApproval);
//                            } else {
//                                Toast.makeText(DoctorRegister.this, "Set is empty", Toast.LENGTH_SHORT).show();
//                            }


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(DoctorRegister.this, "Image Upload Failed " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }

}
