package com.example.sasuke.dailysuvichar.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.example.sasuke.dailysuvichar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends BaseActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private int year, month, day;
    private ImageButton userProfilePic, userCoverPic;
    private Calendar calendar;
    private static final int RESULT_LOAD_IMAGE = 8008, RESULT_LOAD_COVER = 8009;

    @BindView(R.id.bio)
    TextView bio;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.lang)
    TextView language;
    @BindView(R.id.user_type)
    TextView userType;
    @BindView(R.id.dob)
    TextView DOB;
    @BindView(R.id.gender)
    TextView gender;
    @BindView(R.id.age)
    TextView age;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
        }

        userProfilePic = (ImageButton) findViewById(R.id.user_profile_photo);
        userCoverPic = (ImageButton) findViewById(R.id.header_cover_image);

        userCoverPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_COVER);
            }
        });

        userProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Insert Requested", "YES!.");
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Log.e("REQUEST BEFORE", picturePath);
            Glide.with(this)
                    .load(picturePath)
                    .crossFade()
                    .centerCrop()
                    .into(userProfilePic);

            Log.e("REQUEST AFTER", picturePath);
        }
        else if(requestCode == RESULT_LOAD_COVER && resultCode == RESULT_OK && null != data){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Glide.with(this)
                    .load(picturePath)
                    .crossFade()
                    .centerCrop()
                    .into(userCoverPic);
        }
    }

    @OnClick(R.id.name)
    public void setName(){
        new MaterialDialog.Builder(this)
                .title("Set Your Username")
                .content("First Name + Last Name")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("Enter the name...", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        name.setText(input);
                    }
                }).show();
    }

    @OnClick(R.id.bio)
    public void setBio(){
        new MaterialDialog.Builder(this)
                .title("Set Your Bio")
                .content("A Short Descripton About Yourself.")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("Enter bio here...", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        bio.setText("Bio:" + input);
                    }
                }).show();
    }

    @OnClick(R.id.user_name)
    public void setUserName(){
        new MaterialDialog.Builder(this)
                .title("Set Your Username")
                .content("A Short Name YouRepresent.")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("Enter username here...", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        userName.setText(input);
                    }
                }).show();
    }

    @OnClick(R.id.lang)
    public void setLanguage(){
//        new MaterialDialog.Builder(this)
//                .title("Set Your Language Preference")
//                .content("Select Language Type.")
//                .inputType(InputType.TYPE_CLASS_TEXT)
//                .input("English/Hindi", "", new MaterialDialog.InputCallback() {
//                    @Override
//                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
//                        language.setText(input);
//                    }
//                }).show();

        new MaterialDialog.Builder(this)
                .title("Set Your Language Preference")
                .items(new String[]{"English", "Hindi"})
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        switch (which){
                            case 0:{
                                language.setText("ENGLISH");
                                Toast.makeText(ProfileActivity.this, "English Chosen", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            case 1:{
                                language.setText("HINDI");
                                Toast.makeText(ProfileActivity.this, "Hindi Chosen", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        return true;
                    }
                })
                .positiveText("Choose This")
                .show();
    }

    @OnClick(R.id.user_type)
    public void setUserType(){
        new MaterialDialog.Builder(this)
                .title("Set Your Language Preference")
                .items(new String[]{"Standard User", "Spiritual Leader"})
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        switch (which){
                            case 0:{
                                userType.setText("STANDARD");
                                Toast.makeText(ProfileActivity.this, "Standard User chosen", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            case 1:{
                                userType.setText("GURU");
                                Toast.makeText(ProfileActivity.this, "Spiritual Leader chosen", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        return true;
                    }
                })
                .positiveText("Choose This")
                .show();
    }

    @OnClick(R.id.dob)
    public void setDOB(){
        showDialog(999);
    }

    @OnClick(R.id.gender)
    public void setGender(){
        new MaterialDialog.Builder(this)
                .title("Gender:")
                .items(new String[]{"Male", "Female"})
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        switch (which){
                            case 0:{
                                gender.setText("MALE");
                                Toast.makeText(ProfileActivity.this, "Male", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            case 1:{
                                gender.setText("FEMALE");
                                Toast.makeText(ProfileActivity.this, "Female", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        return true;
                    }
                })
                .positiveText("Choose This")
                .show();
    }

    @OnClick(R.id.age)
    public void setAge(){
        new MaterialDialog.Builder(this)
                .title("Set Your Age")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("Enter age here (24, 55, etc.)...", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        age.setText(input);
                    }
                }).show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    year = arg1;
                    month = arg2;
                    day = arg3;
                    //Here Are The Picked Dates
                    Toast.makeText(ProfileActivity.this, day + " " + month + " " + year, Toast.LENGTH_SHORT).show();
                    DOB.setText(day + " " + month + " " + year);
                }
            };
}
