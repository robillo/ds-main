package com.example.sasuke.dailysuvichar.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.newactivities.NewMainActivity;
import com.example.sasuke.dailysuvichar.utils.SharedPrefs;
import com.firebase.ui.storage.images.FirebaseImageLoader;
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

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.btnSave)
    Button saveButton;

    private List<String> notSelected;

    public static Intent newIntent(Context context) {
        return new Intent(context, ProfileActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        notSelected = new ArrayList<>();

        code = getIntent().getIntExtra("fromHome", 0);

        Log.d(TAG, "onCreate: CODE "+code);

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

        if(code==1) {
            Log.d(TAG, "onCreate: yfydfkuggjcgkjjkgc");
            fetchData();
        }
        else if(code == 0){
            if(SharedPrefs.getIsProfileSet()!=null){
                if(SharedPrefs.getIsProfileSet().equals("TRUE")){
                    startActivity(new Intent(this, ChooseInterestActivity.class));
                }
            }
        }

        userCoverPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
                    }
                    if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 6);
                    }
                }
                else{
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_COVER);
                }
            }
        });

        userProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
                    }
                    if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 6);
                    }
                }
                else{
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
            }
        });

        if(code == 1){
//            saveButton.setVisibility(View.INVISIBLE);
        }
    }

    private void fetchData() {
        mUsersDatabase.child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
                if(dataSnapshot.child("name").getValue()!=null) {
                    name.setText(dataSnapshot.child("name").getValue().toString());
                }
                    age.setText(dataSnapshot.child("age").getValue().toString());
                if(dataSnapshot.child("userName").getValue()!=null) {
                    userName.setText(dataSnapshot.child("userName").getValue().toString());
                }
                if(dataSnapshot.child("userType").getValue()!=null) {
                    userType.setText(dataSnapshot.child("userType").getValue().toString());
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

                    Glide.with(ProfileActivity.this).
                            using(new FirebaseImageLoader())
                            .load(mStorageReferenceDP.child(dataSnapshot.getKey()))
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(userProfilePic);
                }
                if(dataSnapshot.child("coverUrl").getValue()!=null) {

                    Glide.with(ProfileActivity.this).
                            using(new FirebaseImageLoader())
                            .load(mStorageReferenceCover.child(dataSnapshot.getKey()))
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
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
//                Toast.makeText(getApplicationContext(), R.string.shortly, Toast.LENGTH_SHORT).show();
                dpPathDB = String.valueOf(dpPath);
                StorageReference riversRef = mStorageReferenceDP.child(mFirebaseUser.getUid());
                riversRef.putFile(dpPath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(ProfileActivity.this, getString(R.string.uploaded), Toast.LENGTH_SHORT).show();
//                                fetchData();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(ProfileActivity.this, getString(R.string.upload_failed), Toast.LENGTH_SHORT).show();

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
//                Toast.makeText(getApplicationContext(), R.string.shortly, Toast.LENGTH_SHORT).show();
                coverPathDB = String.valueOf(coverPath);

                StorageReference riversRef = mStorageReferenceCover.child(mFirebaseUser.getUid());
                riversRef.putFile(coverPath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(ProfileActivity.this, getString(R.string.uploaded), Toast.LENGTH_SHORT).show();
//                                fetchData();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(ProfileActivity.this, getString(R.string.failure), Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(getApplicationContext(), R.string.shortly, Toast.LENGTH_SHORT).show();
                StorageReference riversRef = mStorageReferenceGovID.child(mFirebaseUser.getUid());
                riversRef.putFile(govPath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(ProfileActivity.this, R.string.file_uploaded, Toast.LENGTH_SHORT).show();
//                                fetchData();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(ProfileActivity.this, R.string.upload_failed, Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(getApplicationContext(), R.string.shortly, Toast.LENGTH_SHORT).show();
                StorageReference riversRef = mStorageReferenceSpecID.child(mFirebaseUser.getUid());
                riversRef.putFile(specPath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(ProfileActivity.this, R.string.file_uploaded, Toast.LENGTH_SHORT).show();
//                                fetchData();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(ProfileActivity.this, R.string.upload_failed, Toast.LENGTH_SHORT).show();
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
                .title(R.string.set_name)
                .content(R.string.full_name)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(getString(R.string.enter_name), "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        name.setText(input);
                        b[0] =true;
                    }
                }).show();
//        if(name.getText()!=null){
//            mUsersDatabase.child(mFirebaseUser.getUid()).child("name").setValue(name.getText());
//        }
    }

    @OnClick(R.id.bio)
    public void setBio(){
        final Boolean[] b = {false};
        new MaterialDialog.Builder(this)
                .title(R.string.set_bio)
                .content(R.string.short_desc)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(getString(R.string.enter_bio_here), "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        bio.setText(getString(R.string.bio) + input);
                        b[0] =true;
                    }
                }).show();
//        if(bio.getText()!=null){
//            mUsersDatabase.child(mFirebaseUser.getUid()).child("bio").setValue(bio.getText());
//        }
    }

    @OnClick(R.id.user_name)
    public void setUserName(){
        final Boolean[] b = {false};
        new MaterialDialog.Builder(this)
                .title(R.string.set_username)
                .content(R.string.represent)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(getString(R.string.enter_username), "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        userName.setText(input);
                        b[0] =true;
                    }
                }).show();
//        if(userName.getText()!=null){
//            mUsersDatabase.child(mFirebaseUser.getUid()).child("userName").setValue(userName.getText());
//        }
    }

    @OnClick(R.id.lang)
    public void setLanguage(){

        new MaterialDialog.Builder(this)
                .title(R.string.preference)
                .items(new String[]{getString(R.string.english_lang), getString(R.string.hindi_lang)})
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which){
                            case 0:{
                                language.setText(R.string.english_lang);
                                break;
                            }
                            case 1:{
                                language.setText(R.string.hindi_lang);
                                break;
                            }
                        }
                        return true;
                    }
                })
                .positiveText(R.string.choose)
                .show();
        if(language.getText()!=null){
            mUsersDatabase.child(mFirebaseUser.getUid()).child("preferredLang").setValue(language.getText());
        }
    }

    @OnClick(R.id.user_type)
    public void setUserType(){
        new MaterialDialog.Builder(this)
                .title(R.string.usertype)
                .items(new String[]{getString(R.string.standard), getString(R.string.spiritual)})
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which){
                            case 0:{
                                userType.setText(R.string.standard_caps);
                                if(mLinearLayout.getVisibility()== View.VISIBLE){
                                    mLinearLayout.setVisibility(View.GONE);
                                    specialization.setVisibility(View.GONE);
                                    userTypeInput = getString(R.string.standard_caps);
                                    if(notSelected.contains(getString(R.string.ns_specid))){
                                        notSelected.remove(getString(R.string.ns_specid));
                                    }
                                    if(notSelected.contains(getString(R.string.ns_govid))){
                                        notSelected.remove(getString(R.string.ns_govid));
                                    }
                                    if(notSelected.contains(getString(R.string.ns_special))){
                                        notSelected.remove(getString(R.string.ns_special));
                                    }
                                }
                                break;
                            }
                            case 1:{
                                userType.setText(R.string.guru_caps);
                                if(mLinearLayout.getVisibility()== View.GONE){
                                    mLinearLayout.setVisibility(View.VISIBLE);
                                    specialization.setVisibility(View.VISIBLE);
                                    userTypeInput = getString(R.string.guru_caps);
                                }
                                break;
                            }
                        }
                        return true;
                    }
                })
                .positiveText(R.string.choose)
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
                .title(R.string.gender)
                .items(new String[]{getString(R.string.male), getString(R.string.female)})
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which){
                            case 0:{
                                gender.setText(R.string.male_caps);
                                break;
                            }
                            case 1:{
                                gender.setText(R.string.female_caps);
                                break;
                            }
                        }
                        return true;
                    }
                })
                .positiveText(R.string.choose)
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
                .title(R.string.set_spec)
                .content(R.string.descc)
                .items(new String[]{getString(R.string.astrology_), getString(R.string.yoga_), getString(R.string.pandit_), getString(R.string.motivation_), getString(R.string.ayurveda_)})
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        switch (which){
                            case 0:{
                                temp[0] = getString(R.string.astrology_);
                                specialization.setText(temp[0]);
                                special = temp[0];
                                break;
                            }
                            case 1:{
                                temp[0] = getString(R.string.yoga_);
                                specialization.setText(temp[0]);
                                special = temp[0];
                                break;
                            }
                            case 2:{
                                temp[0] = getString(R.string.pandit_);
                                specialization.setText(temp[0]);
                                special = temp[0];
                                break;
                            }
                            case 3:{
                                temp[0] = getString(R.string.motivation_);
                                specialization.setText(temp[0]);
                                special = temp[0];
                                break;
                            }
                            case 4:{
                                temp[0] = getString(R.string.ayurveda_);
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
            Toast.makeText(getApplicationContext(), R.string.please_correct, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btnSave)
    public void save(){
        if(userType.getText().equals(getString(R.string.standard_caps))){
            if(code==1){
                if(checkValidate(getString(R.string.standard_caps))){
                    writetoFirebase();
                    finish();
                    Toast.makeText(getApplicationContext(), R.string.successfully, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.make_changes, Toast.LENGTH_SHORT).show();
                }
            }
            else {
                if(checkValidate(getString(R.string.standard_caps))){
                    writetoFirebase();
                    SharedPrefs.setIsProfileSet("TRUE");
                    startActivity(new Intent(this, ChooseInterestActivity.class));
                }
                else {
                    String temp = getString(R.string.please) + "\n";
                    for(String s: notSelected){
                        temp = temp.concat(s + "\n");
                    }
                    Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if(userType.getText().equals(getString(R.string.guru_caps))){
            if(code==1){
                if(checkValidate(getString(R.string.guru_caps))){
                    writetoFirebase();
                    finish();
                    Toast.makeText(getApplicationContext(), R.string.successfully, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.make_changes, Toast.LENGTH_SHORT).show();
                }
            }
            else {
                if(checkValidate(getString(R.string.guru_caps))){
                    writetoFirebase();
                    SharedPrefs.setIsProfileSet("TRUE");
                    startActivity(new Intent(this, ChooseInterestActivity.class));
                }
                else {
                    String temp = getString(R.string.please) + "\n";
                    for(String s: notSelected){
                        temp = temp.concat(s + "\n");
                    }
                    Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
                }
            }
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.please_user, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkValidate(String type){
        boolean flagFromLogin = true;
        boolean flagFromHome = false;
        boolean[] check;
        if(type.equals(getString(R.string.standard_caps))){
            //VALIDATE FOR STANDARD USER
            check = new boolean[]{false, false, false, false, false, false, false, false, false, false};
            if(!name.getText().equals(getString(R.string.ns_fullname))){
                check[0] = true;
                Log.e("LOG", name.getText().toString());
                if(notSelected.contains(getString(R.string.ns_fullname))){
                    notSelected.remove(getString(R.string.ns_fullname));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_fullname))){
                    notSelected.add(getString(R.string.ns_fullname));
                }
            }
            if(!userName.getText().equals(getString(R.string.ns_username))){
                check[1] = true;
                Log.e("LOG", userName.getText().toString());
                if(notSelected.contains(getString(R.string.ns_username))){
                    notSelected.remove(getString(R.string.ns_username));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_username))){
                    notSelected.add(getString(R.string.ns_username));
                }
            }
            if(dpPath!=null){
                check[2] = true;
                Log.e("LOG", dpPath.toString());
                if(notSelected.contains(getString(R.string.ns_dp))){
                    notSelected.remove(getString(R.string.ns_dp));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_dp))){
                    notSelected.add(getString(R.string.ns_dp));
                }
            }
            if(coverPath!=null){
                check[3] = true;
                Log.e("LOG", coverPath.toString());
                if(notSelected.contains(getString(R.string.ns_cover))){
                    notSelected.remove(getString(R.string.ns_cover));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_cover))){
                    notSelected.add(getString(R.string.ns_cover));
                }
            }
            if(!bio.getText().equals(getString(R.string.ns_shortdesc))){
                check[4] = true;
                Log.e("LOG", bio.getText().toString());
                if(notSelected.contains(getString(R.string.ns_shortdesc))){
                    notSelected.remove(getString(R.string.ns_shortdesc));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_shortdesc))){
                    notSelected.add(getString(R.string.ns_shortdesc));
                }
            }
            if(!language.getText().equals(getString(R.string.ns_lang))){
                check[5] = true;
                Log.e("LOG", language.getText().toString());
                if(notSelected.contains(getString(R.string.ns_lang))){
                    notSelected.remove(getString(R.string.ns_lang));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_lang))){
                    notSelected.add(getString(R.string.ns_lang));
                }
            }
            if(!userType.getText().equals(getString(R.string.ns_usertype))){
                check[6] = true;
                Log.e("LOG", userType.getText().toString());
                if(notSelected.contains(getString(R.string.ns_usertype))){
                    notSelected.remove(getString(R.string.ns_usertype));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_usertype))){
                    notSelected.add(getString(R.string.ns_usertype));
                }
            }
            if(!DOB.getText().equals(getString(R.string.ns_dob))){
                check[7] = true;
                Log.e("LOG", DOB.getText().toString());
                if(notSelected.contains(getString(R.string.ns_dob))){
                    notSelected.remove(getString(R.string.ns_dob));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_dob))){
                    notSelected.add(getString(R.string.ns_dob));
                }
            }
            if(!gender.getText().equals(getString(R.string.ns_gender))){
                check[8] = true;
                Log.e("LOG", gender.getText().toString());
                if(notSelected.contains(getString(R.string.ns_gender))){
                    notSelected.remove(getString(R.string.ns_gender));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_gender))){
                    notSelected.add(getString(R.string.ns_gender));
                }
            }
            if(!age.getText().equals(getString(R.string.ns_age))){
                check[9] = true;
                Log.e("LOG", age.getText().toString());
                if(notSelected.contains(getString(R.string.ns_age))){
                    notSelected.remove(getString(R.string.ns_age));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_age))){
                    notSelected.add(getString(R.string.ns_age));
                }
            }
            for(int i=0; i<10; i++){
                if(!check[i]){
                    flagFromLogin = false;
                }
            }
            //Check flagFromHome
            for(int i=0; i<10; i++){
                if(check[i]){
                    flagFromHome = true;
                    break;
                }
            }
        }
        else if(type.equals("GURU")){
            //VALIDATE FOR GURU
            check = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false};
            if(!name.getText().equals(getString(R.string.ns_fullname))){
                check[0] = true;
                Log.e("LOG", name.getText().toString());
                if(notSelected.contains(getString(R.string.ns_fullname))){
                    notSelected.remove(getString(R.string.ns_fullname));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_fullname))){
                    notSelected.add(getString(R.string.ns_fullname));
                }
            }
            if(!userName.getText().equals(getString(R.string.ns_username))){
                check[1] = true;
                Log.e("LOG", userName.getText().toString());
                if(notSelected.contains(getString(R.string.ns_username))){
                    notSelected.remove(getString(R.string.ns_username));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_username))){
                    notSelected.add(getString(R.string.ns_username));
                }
            }
            if(dpPath!=null){
                check[2] = true;
                Log.e("LOG", dpPath.toString());
                if(notSelected.contains(getString(R.string.ns_dp))){
                    notSelected.remove(getString(R.string.ns_dp));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_dp))){
                    notSelected.add(getString(R.string.ns_dp));
                }
            }
            if(coverPath!=null){
                check[3] = true;
                Log.e("LOG", coverPath.toString());
                if(notSelected.contains(getString(R.string.ns_cover))){
                    notSelected.remove(getString(R.string.ns_cover));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_cover))){
                    notSelected.add(getString(R.string.ns_cover));
                }
            }
            if(!bio.getText().equals(getString(R.string.ns_shortdesc))){
                check[4] = true;
                Log.e("LOG", bio.getText().toString());
                if(notSelected.contains(getString(R.string.ns_shortdesc))){
                    notSelected.remove(getString(R.string.ns_shortdesc));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_shortdesc))){
                    notSelected.add(getString(R.string.ns_shortdesc));
                }
            }
            if(!language.getText().equals(getString(R.string.ns_lang))){
                check[5] = true;
                Log.e("LOG", language.getText().toString());
                if(notSelected.contains(getString(R.string.ns_lang))){
                    notSelected.remove(getString(R.string.ns_lang));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_lang))){
                    notSelected.add(getString(R.string.ns_lang));
                }
            }
            if(!userType.getText().equals(getString(R.string.ns_usertype))){
                check[6] = true;
                Log.e("LOG", userType.getText().toString());
                if(notSelected.contains(getString(R.string.ns_usertype))){
                    notSelected.remove(getString(R.string.ns_usertype));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_usertype))){
                    notSelected.add(getString(R.string.ns_usertype));
                }
            }
            if(!DOB.getText().equals(getString(R.string.ns_dob))){
                check[7] = true;
                Log.e("LOG", DOB.getText().toString());
                if(notSelected.contains(getString(R.string.ns_dob))){
                    notSelected.remove(getString(R.string.ns_dob));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_dob))){
                    notSelected.add(getString(R.string.ns_dob));
                }
            }
            if(!gender.getText().equals(getString(R.string.ns_gender))){
                check[8] = true;
                Log.e("LOG", gender.getText().toString());
                if(notSelected.contains(getString(R.string.ns_gender))){
                    notSelected.remove(getString(R.string.ns_gender));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_gender))){
                    notSelected.add(getString(R.string.ns_gender));
                }
            }
            if(!age.getText().equals(getString(R.string.ns_age))){
                check[9] = true;
                Log.e("LOG", age.getText().toString());
                if(notSelected.contains(getString(R.string.ns_age))){
                    notSelected.remove(getString(R.string.ns_age));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_age))){
                    notSelected.add(getString(R.string.ns_age));
                }
            }
            if(govPath!=null){
                check[10] = true;
                Log.e("LOG", govPath.toString());
                if(notSelected.contains(getString(R.string.ns_govid))){
                    notSelected.remove(getString(R.string.ns_govid));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_govid))){
                    notSelected.add(getString(R.string.ns_govid));
                }
            }
            if(specPath!=null){
                check[11] = true;
                Log.e("LOG", specPath.toString());
                if(notSelected.contains(getString(R.string.ns_specid))){
                    notSelected.remove(getString(R.string.ns_specid));
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_specid))){
                    notSelected.add(getString(R.string.ns_specid));
                }
            }
            if(special!=null){
                if(!special.equals(getString(R.string.ns_special))){
                    check[12] = true;
                    Log.e("LOG", special);
                    if(notSelected.contains(getString(R.string.ns_special))){
                        notSelected.remove(getString(R.string.ns_special));
                    }
                }
                else {
                    if(!notSelected.contains(getString(R.string.ns_special))){
                        notSelected.add(getString(R.string.ns_special));
                    }
                }
            }
            else {
                if(!notSelected.contains(getString(R.string.ns_special))){
                    notSelected.add(getString(R.string.ns_special));
                }
            }
            //check flagFromLogin
            for(int i=0; i<13; i++){
                if(!check[i]){
                    flagFromLogin = false;
                }
            }
            //Check flagFromHome
            for(int i=0; i<13; i++){
                if(check[i]){
                    flagFromHome = true;
                    break;
                }
            }
        }
        else {
            flagFromLogin = false;
            flagFromHome = false;
        }
        Log.e("FLAG IS", String.valueOf(flagFromLogin));
        if(code == 1){
            return flagFromHome;
        }
        else {
            return flagFromLogin;
        }
    }

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
        if(userType!=null&&userType.getText().length()>0){
            usertypeDB=userType.getText().toString();
        }

        Log.d(TAG, "writetoFirebase: "+mFirebaseUser.getUid());
        Log.d(TAG, "writetoFirebase: "+userType);
        Log.d(TAG, "writetoFirebase: "+userType.getText());
        if(userType!=null && (userType.getText().equals("STANDARD")||userType.getText().equals("User Type: Standard"))){
            Log.d(TAG, "writetoFirebase: "+mFirebaseUser.getUid());
            mUsersDatabase.child(mFirebaseUser.getUid()).child("userName").setValue(userNameDB);
            mUsersDatabase.child(mFirebaseUser.getUid()).child("gender").setValue(genderDB);
            mUsersDatabase.child(mFirebaseUser.getUid()).child("name").setValue(nameDB);
            mUsersDatabase.child(mFirebaseUser.getUid()).child("bio").setValue(bioDB);
            mUsersDatabase.child(mFirebaseUser.getUid()).child("userType").setValue(usertypeDB);
            mUsersDatabase.child(mFirebaseUser.getUid()).child("email").setValue(mFirebaseUser.getEmail());
        }
        else {
            if(govPath==null || specPath==null){
                Toast.makeText(this, R.string.please_docs, Toast.LENGTH_SHORT).show();
            }else {
//                Guru user = new Guru(nameDB, mFirebaseUser.getEmail(), bioDB, new ArrayList<String>(), langDB, dpPathDB, coverPathDB, dobDB, genderDB, ageDB, govDB, specDB, special);
                mGurusDatabase.child(mFirebaseUser.getUid()).child("name").setValue(nameDB);
                mGurusDatabase.child(mFirebaseUser.getUid()).child("email").setValue(mFirebaseUser.getEmail());
                mGurusDatabase.child(mFirebaseUser.getUid()).child("dob").setValue(dobDB);
                mUsersDatabase.child(mFirebaseUser.getUid()).child("userType").setValue(usertypeDB);
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
        if(code==1){
            startActivity(new Intent(this, NewMainActivity.class));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
