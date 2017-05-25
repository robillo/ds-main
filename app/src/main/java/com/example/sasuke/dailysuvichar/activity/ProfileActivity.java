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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.Guru;
import com.example.sasuke.dailysuvichar.models.User;
import com.example.sasuke.dailysuvichar.utils.SharedPrefs;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends BaseActivity {

    private static final String TAG = "PROFILE";
    private DatabaseReference mUsersDatabase, mGurusDatabase, mTempDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private StorageReference mStorageReferenceDP,mStorageReferenceCover, mStorageReferenceGovID, mStorageReferenceSpecID;

    private int year, month, day, code;
    private ImageButton userProfilePic, userCoverPic;
    private Calendar calendar;
    private static final int RESULT_LOAD_IMAGE = 8008, RESULT_LOAD_COVER = 8009, RESULT_LOAD_GOV_ID = 8010,
    RESULT_LOAD_SPEC_ID = 8011;
    Uri dpPath = null, coverPath = null, govPath = null, specPath = null;
    private int ageInt = 0;
    private String special = null;
    private String userTypeInput = null;
    private enum std{NAME, USERNAME, DP, COVER, BIO, LANG, USERTYPE, DOB, GENDER, AGE};
    private enum guru{NAME, USERNAME, DP, COVER, BIO, LANG, USERTYPE, DOB, GENDER, AGE, GOVID, SPECID, SPECIALIZATION};

    @BindView(R.id.specialization)
    TextView specialization;
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
    @BindView(R.id.invisible)
    LinearLayout mLinearLayout;
    @BindView(R.id.govID)
    ImageView govID;
    @BindView(R.id.specID)
    ImageView specID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        code = getIntent().getIntExtra("fromLogin", 0);

        Log.e("ENUM", " " + guru.NAME);
        Log.e("ENUM", " " + guru.USERNAME);
        Log.e("ENUM", " " + guru.DP);
        Log.e("ENUM", " " + guru.COVER);
        Log.e("ENUM", " " + guru.BIO);
        Log.e("ENUM", " " + guru.LANG);
        Log.e("ENUM", " " + guru.USERTYPE);
        Log.e("ENUM", " " + guru.DOB);
        Log.e("ENUM", " " + guru.GENDER);
        Log.e("ENUM", " " + guru.AGE);
        Log.e("ENUM", " " + guru.GOVID);
        Log.e("ENUM", " " + guru.SPECID);
        Log.e("ENUM", " " + guru.SPECIALIZATION);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference("users");
        mTempDatabase = FirebaseDatabase.getInstance().getReference();
        mTempDatabase.child("gurus").child(mFirebaseUser.getUid()).push();
        mGurusDatabase = FirebaseDatabase.getInstance().getReference("gurus").child("pending");

        mStorageReferenceDP = FirebaseStorage.getInstance().getReference("profile").child("user").child("dp");
        mStorageReferenceCover = FirebaseStorage.getInstance().getReference("profile").child("user").child("cover");
        mStorageReferenceGovID = FirebaseStorage.getInstance().getReference("gurus").child("pending").child("govid");
        mStorageReferenceSpecID = FirebaseStorage.getInstance().getReference("gurus").child("pending").child("specid");

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
        }

        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 6);
        }

        userProfilePic = (ImageButton) findViewById(R.id.user_profile_photo);
        userCoverPic = (ImageButton) findViewById(R.id.header_cover_image);

//        loadDP(SharedPrefs.getFacebookToken());
//        loadCOVER(SharedPrefs.getFacebookToken());

        if(code==0) {
            fetchData();
        }

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
    }
//
//    private void loadDP(String token){
//        String url = "https://graph.facebook.com/me/picture?type=large&method=GET&access_token=" + token;
//        Log.e("URL", url);
//        Glide.with(this)
//                .load(url)
//                .fitCenter()
//                .into(userProfilePic);
//    }
//
//    private void loadCOVER(String token){
//        String url = "https://graph.facebook.com/me/picture?fields=cover&access_token=" + token;
//
//        final String[] coverPhoto = {null};
//        new GraphRequest(
//                AccessToken.getCurrentAccessToken(),
//                "/+me+?fields=cover",
//                null,
//                HttpMethod.GET,
//                new GraphRequest.Callback()
//                {
//                    public void onCompleted(GraphResponse response)
//                    {
//                        try
//                        {
//                            JSONObject jsonObject = response.getJSONObject();
//                            if(jsonObject==null)
//                                return;
//                            JSONObject JOSource = jsonObject.getJSONObject("cover");
//                            coverPhoto[0] = JOSource.getString("source");
//
//                            Log.e("COVER", " " + coverPhoto[0]);
//
//                            if(coverPhoto[0]!=null){
//                                Glide.with(getApplicationContext())
//                                        .load(coverPhoto[0])
//                                        .fitCenter()
//                                        .into(userCoverPic);
//                            }
//
//                        }
//                        catch (JSONException e)
//                        {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//        ).executeAsync();
//    }

    private void fetchData() {

        mUsersDatabase.child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
                if(dataSnapshot.child("name").getValue()!=null) {
                    name.setText(dataSnapshot.child("name").getValue().toString());
                }
                if(dataSnapshot.child("userName").getValue()!=null) {
                    userName.setText(dataSnapshot.child("userName").getValue().toString());
                }
                if(dataSnapshot.child("bio").getValue()!=null) {
                    bio.setText(dataSnapshot.child("bio").getValue().toString());
                }
                if(dataSnapshot.child("dob").getValue()!=null) {
                    DOB.setText(dataSnapshot.child("dob").getValue().toString());
                }
                if(dataSnapshot.child("gender").getValue()!=null) {
                    gender.setText(dataSnapshot.child("gender").getValue().toString());
                }
                if(dataSnapshot.child("preferredLang").getValue()!=null) {
                    language.setText(dataSnapshot.child("preferredLang").getValue().toString());
                }
                if(dataSnapshot.child("photoUrl").getValue()!=null) {
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(Uri.parse(dataSnapshot.child("photoUrl").getValue().toString()),
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Glide.with(ProfileActivity.this)
                            .load(picturePath)
                            .crossFade()
                            .centerCrop()
                            .into(userProfilePic);
                }
                if(dataSnapshot.child("coverUrl").getValue()!=null) {
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(Uri.parse(dataSnapshot.child("coverUrl").getValue().toString()),
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Glide.with(ProfileActivity.this)
                            .load(picturePath)
                            .crossFade()
                            .centerCrop()
                            .into(userCoverPic);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            dpPath = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(dpPath,
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

            String dpPathDB = null;
            if(dpPath!=null){
                dpPathDB = String.valueOf(dpPath);
                StorageReference riversRef = mStorageReferenceDP.child(mFirebaseUser.getUid());
                riversRef.putFile(dpPath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(ProfileActivity.this, "File Uploaded! ", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(ProfileActivity.this, "Upload Failed. Please Try Again!", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            }
                        });

            }
            if(dpPathDB!=null) {
                mUsersDatabase.child(mFirebaseUser.getUid()).child("photoUrl").setValue(dpPathDB);
            }

        }
        else if(requestCode == RESULT_LOAD_COVER && resultCode == RESULT_OK && null != data){
            coverPath = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(coverPath,
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

            String coverPathDB=null;
            if(coverPath!=null){
                coverPathDB = String.valueOf(coverPath);

                StorageReference riversRef = mStorageReferenceCover.child(mFirebaseUser.getUid());
                riversRef.putFile(coverPath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(ProfileActivity.this, "File Uploaded! ", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(ProfileActivity.this, "Upload Failed. Please Try Again!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            }
                        });
            }
            if(coverPathDB!=null){
                mUsersDatabase.child(mFirebaseUser.getUid()).child("coverUrl").setValue(coverPathDB);
            }

        }
        else if(requestCode == RESULT_LOAD_GOV_ID && resultCode == RESULT_OK && null != data){
            govPath = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(govPath,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Glide.with(this)
                    .load(picturePath)
                    .crossFade()
                    .centerCrop()
                    .into(govID);


            if(govPath!=null){

                StorageReference riversRef = mStorageReferenceGovID.child(mFirebaseUser.getUid());
                riversRef.putFile(govPath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(ProfileActivity.this, "File Uploaded! ", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(ProfileActivity.this, "Upload Failed. Please Try Again!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            }
                        });
                mGurusDatabase.child(mFirebaseUser.getUid()).child("govID").setValue(govPath.toString());

            }
        }
        else if(requestCode == RESULT_LOAD_SPEC_ID && resultCode == RESULT_OK && null != data){
            specPath = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(specPath,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Glide.with(this)
                    .load(picturePath)
                    .crossFade()
                    .centerCrop()
                    .into(specID);

            if(specPath!=null){

                StorageReference riversRef = mStorageReferenceSpecID.child(mFirebaseUser.getUid());
                riversRef.putFile(specPath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(ProfileActivity.this, "File Uploaded! ", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(ProfileActivity.this, "Upload Failed. Please Try Again!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            }
                        });
                mGurusDatabase.child(mFirebaseUser.getUid()).child("specID").setValue(specPath.toString());

            }
        }
    }

    @OnClick(R.id.name)
    public void setName(){
        final Boolean[] b = {false};
        new MaterialDialog.Builder(this)
                .title("Set Your Name")
                .content("First Name + Last Name")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("Enter your name...", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        name.setText(input);
                        b[0] =true;
                    }
                }).show();
        if(name.getText()!=null&&b[0]){
            mUsersDatabase.child(mFirebaseUser.getUid()).child("name").setValue(name.getText());
        }
    }

    @OnClick(R.id.bio)
    public void setBio(){
        final Boolean[] b = {false};
        new MaterialDialog.Builder(this)
                .title("Set Your Bio")
                .content("A Short Descripton About Yourself.")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("Enter bio here...", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        bio.setText("Bio: " + input);
                        b[0] =true;
                    }
                }).show();
        if(bio.getText()!=null&&b[0]){
            mUsersDatabase.child(mFirebaseUser.getUid()).child("bio").setValue(bio.getText());
        }
    }

    @OnClick(R.id.user_name)
    public void setUserName(){
        final Boolean[] b = {false};
        new MaterialDialog.Builder(this)
                .title("Set Your Username")
                .content("A Short Name YouRepresent.")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("Enter username here...", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        userName.setText(input);
                        b[0] =true;
                    }
                }).show();
        if(userName.getText()!=null &&b[0]){
            mUsersDatabase.child(mFirebaseUser.getUid()).child("userName").setValue(userName.getText());
        }
    }

    @OnClick(R.id.lang)
    public void setLanguage(){

        new MaterialDialog.Builder(this)
                .title("Set Your Language Preference")
                .items(new String[]{"English", "Hindi"})
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which){
                            case 0:{
                                language.setText("ENGLISH");
                                break;
                            }
                            case 1:{
                                language.setText("HINDI");
                                break;
                            }
                        }
                        return true;
                    }
                })
                .positiveText("Choose This")
                .show();
        if(language.getText()!=null){
            mUsersDatabase.child(mFirebaseUser.getUid()).child("preferredLang").setValue(language.getText());
        }
    }

    @OnClick(R.id.user_type)
    public void setUserType(){
        new MaterialDialog.Builder(this)
                .title("Set Your Language Preference")
                .items(new String[]{"Standard User", "Spiritual Leader"})
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which){
                            case 0:{
                                userType.setText("STANDARD");
                                if(mLinearLayout.getVisibility()== View.VISIBLE){
                                    mLinearLayout.setVisibility(View.GONE);
                                    specialization.setVisibility(View.GONE);
                                    userTypeInput = "STANDARD";
                                }
                                break;
                            }
                            case 1:{
                                userType.setText("GURU");
                                if(mLinearLayout.getVisibility()== View.GONE){
                                    mLinearLayout.setVisibility(View.VISIBLE);
                                    specialization.setVisibility(View.VISIBLE);
                                    userTypeInput = "GURU";
                                }
                                break;
                            }
                        }
                        return true;
                    }
                })
                .positiveText("Choose This")
                .show();
        if(userType.getText()!=null){
            mUsersDatabase.child(mFirebaseUser.getUid()).child("preferredLang").setValue(language.getText());
        }
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
                        switch (which){
                            case 0:{
                                gender.setText("MALE");
                                break;
                            }
                            case 1:{
                                gender.setText("FEMALE");
                                break;
                            }
                        }
                        return true;
                    }
                })
                .positiveText("Choose This")
                .show();
        if(gender.getText()!=null){
            mUsersDatabase.child(mFirebaseUser.getUid()).child("gender").setValue(gender.getText());
        }
    }

    public int setAge(int day2, int month2, int year2){
        Calendar now = Calendar.getInstance();

        int year1 = now.get(Calendar.YEAR);
        int age = year1 - year2;
        int month1 = now.get(Calendar.MONTH);
        if (month2 > month1) {
            age--;
        } else if (month1 == month2) {
            int day1 = now.get(Calendar.DAY_OF_MONTH);
            if (day2 > day1) {
                age--;
            }
        }
        return age;
    }

    @OnClick(R.id.govID)
    public void setGovID(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_GOV_ID);
    }

    @OnClick(R.id.specID)
    public void setSpecID(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_SPEC_ID);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    @OnClick(R.id.specialization)
    public void setSpecialization(){
        final String[] temp = new String[1];
        new MaterialDialog.Builder(this)
                .title("Set Your Specialization:")
                .content("Spiritual Skill You Majorly Specialize In.")
                .items(new String[]{"Astrology Guru", "Yoga Guru", "Pandit", "Motivation Guru", "Ayurveda Guru"})
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        switch (which){
                            case 0:{
                                temp[0] = "Astrology Guru";
                                specialization.setText(temp[0]);
                                special = temp[0];
                                break;
                            }
                            case 1:{
                                temp[0] = "Yoga Guru";
                                specialization.setText(temp[0]);
                                special = temp[0];
                                break;
                            }
                            case 2:{
                                temp[0] = "Pandit";
                                specialization.setText(temp[0]);
                                special = temp[0];
                                break;
                            }
                            case 3:{
                                temp[0] = "Motivation Guru";
                                specialization.setText(temp[0]);
                                special = temp[0];
                                break;
                            }
                            case 4:{
                                temp[0] = "Ayurveda Guru";
                                specialization.setText(temp[0]);
                                special = temp[0];
                                break;
                            }
                        }
                        return true;
                    }
                })
                .positiveText("Choose This")
                .show();
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    year = arg1;
                    month = arg2+1;
                    day = arg3;
                    ageInt = setAge(day,month,year);
                    DOB.setText(day + "/" + month + "/" + year);
                    age.setText(" " + ageInt);
                    mUsersDatabase.child(mFirebaseUser.getUid()).child("dob").setValue(DOB.getText());
                    mUsersDatabase.child(mFirebaseUser.getUid()).child("age").setValue(ageInt);

                }
            };

    @OnClick(R.id.age)
    public void setAge(){
        if(ageInt==0){
            Toast.makeText(getApplicationContext(), "Please Enter Correct Date Of Birth", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btnSave)
    public void save(){
        if(userType.getText().equals("STANDARD")){
            //PUSH STANDARD USER DATA
            if(checkValidate("STANDARD")){
//                writetoFirebaseAsStandard();
                writetoFirebase();
                startActivity(new Intent(this, ChooseInterestActivity.class));
            }
            else {
                Toast.makeText(getApplicationContext(), "PLEASE ENTER FULL DETAILS", Toast.LENGTH_SHORT).show();
            }
        }
        else if(userType.getText().equals("GURU")){
            //PUSH GURU USER DATA FIRST AS USER, THEN AS GURU
            if(!checkValidate("GURU")){
//                writetoFirebaseAsGuru();
                writetoFirebase();
                startActivity(new Intent(this, ChooseInterestActivity.class));
            }
            else {
                Toast.makeText(getApplicationContext(), "PLEASE ENTER FULL DETAILS", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "PLEASE SELECT USER TYPE", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkValidate(String type){
        boolean flag = true;
        boolean[] check;
        check = new boolean[]{false, false, false, false, false, false, false, false, false, false};
        if(type.equals("STANDARD")){
            //VALIDATE FOR STANDARD USER
            if(!name.getText().equals("(Full Name: Not Selected)")){
                check[0] = true;
                Log.e("LOG", name.getText().toString());
            }
            if(!userName.getText().equals("Username: Not Selected")){
                check[1] = true;
                Log.e("LOG", userName.getText().toString());
            }
            if(dpPath!=null){
                check[2] = true;
                Log.e("LOG", dpPath.toString());
            }
            if(coverPath!=null){
                check[3] = true;
                Log.e("LOG", coverPath.toString());
            }
            if(!bio.getText().equals("Short Description/Bio. : Not Selected")){
                check[4] = true;
                Log.e("LOG", bio.getText().toString());
            }
            if(!language.getText().equals("Language: Not Selelcted")){
                check[5] = true;
                Log.e("LOG", language.getText().toString());
            }
            if(!userType.getText().equals("User Type: Not Selected")){
                check[6] = true;
                Log.e("LOG", userType.getText().toString());
            }
            if(!DOB.getText().equals("Date Of Birth: Not Selected")){
                check[7] = true;
                Log.e("LOG", DOB.getText().toString());
            }
            if(!gender.getText().equals("Gender: Not Selected")){
                check[8] = true;
                Log.e("LOG", gender.getText().toString());
            }
            if(!age.getText().equals("Age: Select DOB to evaluate")){
                check[9] = true;
                Log.e("LOG", age.getText().toString());
            }
            for(int i=0; i<10; i++){
                if(!check[i]){
                    flag = false;
                }
            }
        }
        else if(type.equals("GURU")){
            //VALIDATE FOR GURU
            if(name.getText()!="(Full Name: Not Selected)"){
                check[0] = true;
                Log.e("LOG", name.getText().toString());
            }
            if(userName.getText()!="Username: Not Selected"){
                check[1] = true;
                Log.e("LOG", userName.getText().toString());
            }
            if(dpPath!=null){
                check[2] = true;
                Log.e("LOG", dpPath.toString());
            }
            if(coverPath!=null){
                check[3] = true;
                Log.e("LOG", coverPath.toString());
            }
            if(bio.getText()!="Short Description/Bio. : Not Selected"){
                check[4] = true;
                Log.e("LOG", bio.getText().toString());
            }
            if(language.getText()!="Language: Not Selelcted"){
                check[5] = true;
                Log.e("LOG", language.getText().toString());
            }
            if(userType.getText()!="User Type: Not Selected"){
                check[6] = true;
                Log.e("LOG", userType.getText().toString());
            }
            if(DOB.getText()!="Date Of Birth: Not Selected"){
                check[7] = true;
                Log.e("LOG", DOB.getText().toString());
            }
            if(gender.getText()!="Gender: Not Selected"){
                check[8] = true;
                Log.e("LOG", gender.getText().toString());
            }
            if(age.getText()!="Age: Select DOB to evaluate"){
                check[9] = true;
                Log.e("LOG", age.getText().toString());
            }
            if(govPath!=null){
                check[10] = true;
                Log.e("LOG", govPath.toString());
            }
            if(specPath!=null){
                check[11] = true;
                Log.e("LOG", specPath.toString());
            }
            if(special!=null){
                check[12] = true;
                Log.e("LOG", special);
            }
            for(int i=0; i<13; i++){
                if(!check[i]){
                    flag = false;
                }
            }
        }
        else {
            flag = false;
        }

        Log.e("FLAG IS", String.valueOf(flag));
        return flag;
    }

//    private void writetoFirebaseAsStandard(){
//        if(name.getText()!="(Full Name: Not Selected)"){
//
//        }
//        if(userName.getText()!="Username: Not Selected"){
//
//        }
//        if(dpPath!=null){
//
//        }
//        if(coverPath!=null){
//
//        }
//        if(bio.getText()!="Short Description/Bio. : Not Selected"){
//
//        }
//        if(language.getText()!="Language: Not Selelcted"){
//
//        }
//        if(userType.getText()!="User Type: Not Selected"){
//
//        }
//        if(DOB.getText()!="Date Of Birth: Not Selected"){
//
//        }
//        if(gender.getText()!="Gender: Not Selected"){
//
//        }
//        if(age.getText()!="Age: Select DOB to evaluate"){
//
//        }
//    }
//
//    private void writetoFirebaseAsGuru(){
//        if(name.getText()!="(Full Name: Not Selected)"){
//
//        }
//        if(userName.getText()!="Username: Not Selected"){
//
//        }
//        if(dpPath!=null){
//
//        }
//        if(coverPath!=null){
//
//        }
//        if(bio.getText()!="Short Description/Bio. : Not Selected"){
//
//        }
//        if(language.getText()!="Language: Not Selelcted"){
//
//        }
//        if(userType.getText()!="User Type: Not Selected"){
//
//        }
//        if(DOB.getText()!="Date Of Birth: Not Selected"){
//
//        }
//        if(gender.getText()!="Gender: Not Selected"){
//
//        }
//        if(age.getText()!="Age: Select DOB to evaluate"){
//
//        }
//        if(govPath!=null){
//
//        }
//        if(specPath!=null){
//
//        }
//        if(special!=null){
//
//        }
//    }

    private void writetoFirebase() {

        String nameDB = null, langDB= null, usertypeDB= null, dobDB= null, genderDB= null, userNameDB= null, bioDB= null;
        String dpPathDB = null, coverPathDB = null, govDB = null, specDB = null;
        int ageDB = -1;
        if(name!=null && name.getText().length()>0){
            nameDB = name.getText().toString();
        }
        if(ageInt!=0){
            ageDB = ageInt;
        }
        if(language!=null && language.getText().length()>0){
            langDB = language.getText().toString();
        }
        if(DOB!=null && DOB.getText().length()>0){
            dobDB = DOB.getText().toString();
        }
        if(userName!=null && userName.getText().length()>0){
            userNameDB = userName.getText().toString();
        }
        if(gender!=null && gender.getText().length()>0){
            genderDB = gender.getText().toString();
        }

        if(bio!=null && bio.getText().length()>0){
            bioDB = bio.getText().toString();
        }

        Log.d(TAG, "writetoFirebase: "+mFirebaseUser.getUid());
        Log.d(TAG, "writetoFirebase: "+userType);
        Log.d(TAG, "writetoFirebase: "+userType.getText());
        if(userType!=null && (userType.getText().equals("STANDARD")||userType.getText().equals("User Type: Standard"))){
            Log.d(TAG, "writetoFirebase: "+mFirebaseUser.getUid());
            User user = new User(nameDB,bioDB,langDB,dobDB,dpPathDB,coverPathDB,genderDB,userNameDB,ageDB);
            mUsersDatabase.child(mFirebaseUser.getUid()).child("email").setValue(mFirebaseUser.getEmail());
        }
        else {
            if(govPath==null || specPath==null){
                Toast.makeText(this, "Please upload documents to get verified as a Guru", Toast.LENGTH_SHORT).show();
            }else {
                Guru user = new Guru(nameDB, mFirebaseUser.getEmail(), bioDB, new ArrayList<String>(), langDB, dpPathDB, coverPathDB, dobDB, genderDB, ageDB, govDB, specDB, special);
                mGurusDatabase.child(mFirebaseUser.getUid()).child("name").setValue(nameDB);
                mGurusDatabase.child(mFirebaseUser.getUid()).child("email").setValue(mFirebaseUser.getEmail());
                mGurusDatabase.child(mFirebaseUser.getUid()).child("dob").setValue(dobDB);
                mGurusDatabase.child(mFirebaseUser.getUid()).child("age").setValue(ageDB);
                mGurusDatabase.child(mFirebaseUser.getUid()).child("uid").setValue(mFirebaseUser.getUid());
                if(special!=null){
                    mGurusDatabase.child(mFirebaseUser.getUid()).child("specialization").setValue(special);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
