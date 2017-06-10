package com.example.sasuke.dailysuvichar.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.CustomVideo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.klinker.android.simple_videoview.SimpleVideoView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectVideoActivity extends BaseActivity {


    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.video_view)
    SimpleVideoView mVideoView;
    @BindView(R.id.diet)
    TextView diet;
    @BindView(R.id.yoga)
    TextView yoga;
    @BindView(R.id.health)
    TextView health;
    @BindView(R.id.religion)
    TextView religion;
    @BindView(R.id.motivation)
    TextView motivation;
    @BindView(R.id.ayurveda)
    TextView ayurveda;
    @BindView(R.id.astrology)
    TextView astrology;
//    @BindView(R.id.next)
//    Button next;
    @BindView(R.id.grid)
    GridLayout grid;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.etCaption)
    EditText etCaption;
    @BindView(R.id.switch_lang)
    Switch switch_lang;
    @BindView(R.id.play_button)
    TextView play;
    @BindView(R.id.shownhide)
    ImageView showNHide;

    private ArrayList<String> interests, data, mSelectedItems;
    private Context context;
    private SimpleVideoView currentlyPlaying;

    private static final String TAG = "PHOTO_POST";
    private static final int PICK_VIDEO_REQUEST = 250;
    private Uri filePath;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReferenceTag, mDatabaseReferenceUser,mDatabaseReferenceAllPosts;
    private StorageReference mStorageReference;
    ProgressDialog progressDialog;
    Bitmap thumb;
    Long size;
    String bucket, encoding, lang;
    Uri downloadUrl;
    private int from = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_video);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_clear_black_24dp);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getName();
        from = getIntent().getIntExtra("from", 1);

        showFileChooser();

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference("posts").child("videos");
//        mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference();

        context = getApplicationContext();
        interests = new ArrayList<>();
//        subInterests = new ArrayList<>();
        mSelectedItems = new ArrayList<>();

    }

    @OnClick(R.id.diet)
    public void diet() {
        setAptBG(getString(R.string.diett), diet);
    }

    @OnClick(R.id.yoga)
    public void yoga() {
        setAptBG(getString(R.string.yogaa), yoga);
    }

    @OnClick(R.id.health)
    public void health() {
        setAptBG(getString(R.string.healthh), health);
    }

    @OnClick(R.id.religion)
    public void religion() {
        setAptBG(getString(R.string.religionn), religion);
    }

    @OnClick(R.id.motivation)
    public void motivation() {
        setAptBG(getString(R.string.motivationn), motivation);
    }

    @OnClick(R.id.ayurveda)
    public void ayurveda() {
        setAptBG(getString(R.string.ayurvedaa), ayurveda);
    }

    @OnClick(R.id.astrology)
    public void astrology() {
        setAptBG(getString(R.string.astrologyy), astrology);
    }

//    @OnClick(R.id.next)
//    public void nextIsSubinterests() {
//        if (interests.size() < 1) {
//            Toast.makeText(context, getString(R.string.pluese), Toast.LENGTH_SHORT).show();
//        } else {
//            if (interests.contains(getString(R.string.diett))) {
//                String[] temp = getResources().getStringArray(R.array.diet_array);
//                addToSubinterests(temp);
//            }
//            if (interests.contains(getString(R.string.yogaa))) {
//                String[] temp = getResources().getStringArray(R.array.yoga_array);
//                addToSubinterests(temp);
//            }
//            if (interests.contains(getString(R.string.healthh))) {
//                String[] temp = getResources().getStringArray(R.array.health_array);
//                addToSubinterests(temp);
//            }
//            if (interests.contains(getString(R.string.religionn))) {
//                String[] temp = getResources().getStringArray(R.array.religion_array);
//                addToSubinterests(temp);
//            }
//            if (interests.contains(getString(R.string.motivationn))) {
//                String[] temp = getResources().getStringArray(R.array.motivation_array);
//                addToSubinterests(temp);
//            }
//            if (interests.contains(getString(R.string.ayurvedaa))) {
//                String[] temp = getResources().getStringArray(R.array.ayurveda_array);
//                addToSubinterests(temp);
//            }
//            if (interests.contains(getString(R.string.astrologyy))) {
//                String[] temp = getResources().getStringArray(R.array.astrology_array);
//                addToSubinterests(temp);
//            }
//            grid.setVisibility(View.GONE);
//            recyclerView.setVisibility(View.VISIBLE);
//            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
////            data = fillWithData();
//            recyclerView.setAdapter(new RVTags(context, subInterests, mSelectedItems));
////            submit.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private void addToSubinterests(String[] temp) {
//        for (String s : temp) {
//            subInterests.add(s);
//        }
//    }

    private void setAptBG(String temp, TextView view) {
        if (interests.contains(temp)) {
            interests.remove(temp);
            view.setBackgroundColor(getResources().getColor(R.color.white));
            view.setTextColor(getResources().getColor(R.color.black));
        } else {
            interests.add(temp);
            view.setBackgroundColor(getResources().getColor(R.color.black));
            view.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @OnClick(R.id.btnPostImage)
    public void postImage() {
        uploadToFirebase();
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.selecto)), PICK_VIDEO_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            mVideoView.setShowSpinner(false);

            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("video/mpeg")
                    .build();
            if (filePath == null) {
                Log.e("selected video path", "null");
                finish();
            } else {
                thumb = ThumbnailUtils.createVideoThumbnail(getPath(filePath), MediaStore.Images.Thumbnails.MINI_KIND);
//                String tmb = getThumbnailPath(th);
                Log.d(TAG, "onActivityResult: TMB "+thumb);
//                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//                mmr.setDataSource(data.getDataString());
//                thumb = mmr.getFrameAtTime();
//                mmr.release();


                mVideoView.setVisibility(View.VISIBLE);
                Log.e("selectedVideoPath", filePath.getPath());
//                mVideoView.setVideoURI(filePath);
                mVideoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mVideoView.isPlaying()) {
                            mVideoView.pause();
                        } else {
                            if (mVideoView != currentlyPlaying) {
                                releaseVideo();
                                mVideoView.start(Uri.parse(String.valueOf(filePath)));
                                currentlyPlaying = mVideoView;
                            }else {
                                mVideoView.setVisibility(View.VISIBLE);
//                    if(holder.videoUrl!=null) {
//                        holder.videoView.start(holder.videoUrl.toString() + ".mp4");
//                    }
                                mVideoView.play();

//                            } else {
//                                Toast.makeText(context, "Sorry. This Video Cannot Be Played", Toast.LENGTH_SHORT).show();
//                            }
                            }
                        }
                    }
                });
                if (filePath != null) {
//                    StorageReference videoGalleryRef = storageRefVideo.child(selectedVideoUri + "_video");
//                    sendFileFirebaseVideo(videoGalleryRef, selectedVideoUri, metadata);
                }
            }
        }
        else {
            Toast.makeText(context, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    private void uploadToFirebase() {
        if (interests.size() < 1) {
            Toast.makeText(context, R.string.please_atleast, Toast.LENGTH_SHORT).show();
        } else {
            if (filePath != null) {
                if (etCaption.length() >= 1) {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle(getString(R.string.uploading));
                    progressDialog.show();



                    mDatabaseReferenceTag = FirebaseDatabase.getInstance().getReference("posts");
                    mDatabaseReferenceAllPosts = FirebaseDatabase.getInstance().getReference("allPosts");

                    final String postID = mDatabaseReferenceTag.push().getKey();
                    Log.d(TAG, "uploadToFirebase: "+thumb);
                    if(thumb!=null) {
                        StorageReference storageReferenceImage = FirebaseStorage.getInstance().getReference("thumbnails").child(postID);
                        storageReferenceImage.putFile(getImageUri(this, thumb)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            }
                        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                    getName();
                    StorageReference riversRef = mStorageReference.child(postID);
                    riversRef.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    Log.d(TAG, "onSuccess: " + taskSnapshot.getMetadata().getSizeBytes());
                                    Log.d(TAG, "onSuccess: " + taskSnapshot.getDownloadUrl());

                                    size = taskSnapshot.getMetadata().getSizeBytes();
                                    lang = taskSnapshot.getMetadata().getContentLanguage();
                                    encoding = taskSnapshot.getMetadata().getContentEncoding();
                                    bucket = taskSnapshot.getMetadata().getBucket();
                                    downloadUrl = taskSnapshot.getDownloadUrl();

//                            progressDialog.dismiss();
                                    Toast.makeText(SelectVideoActivity.this, R.string.uploaded, Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
//                                progressDialog.dismiss();
                                    Toast.makeText(SelectVideoActivity.this, R.string.failure, Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onFailure: " + exception.getMessage());

                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                progressDialog.setMessage(getString(R.string.uploaded) + " " + ((int) progress) + "%...");
                                }
                            });

                    CustomVideo video = null;

                    if (name.getText() != "") {
                        if(!switch_lang.isChecked()) {
                            video = new CustomVideo("video", name.getText().toString(), mFirebaseUser.getEmail(),
                                    -System.currentTimeMillis(), 0, 0, null, etCaption.getText().toString(),
                                    mFirebaseUser.getUid(), interests,"english");
                        }else{
                            video = new CustomVideo("video", name.getText().toString(), mFirebaseUser.getEmail(),
                                    -System.currentTimeMillis(), 0, 0, null, etCaption.getText().toString(),
                                    mFirebaseUser.getUid(), interests,"hindi");
                        }
                    }else{
                        if(!switch_lang.isChecked()) {
                            video = new CustomVideo("video", getString(R.string.unknown), mFirebaseUser.getEmail(),
                                    -System.currentTimeMillis(), 0, 0, null, etCaption.getText().toString(),
                                    mFirebaseUser.getUid(), interests,"english");
                        }else{
                            video = new CustomVideo("video", getString(R.string.unknown), mFirebaseUser.getEmail(),
                                    -System.currentTimeMillis(), 0, 0, null, etCaption.getText().toString(),
                                    mFirebaseUser.getUid(), interests,"hindi");
                        }
                    }

                    for (String interest : interests) {
                        mDatabaseReferenceTag.child(interest.toLowerCase()).child(postID).setValue(video);
                    }

                    mDatabaseReferenceAllPosts.child(postID).setValue(video);

                    mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference();
                    mDatabaseReferenceUser.child("users").child(mFirebaseUser.getUid()).child("userPosts").child(postID).setValue(video);

                    Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();
//                    if(from == 1){
//                        startActivity(new Intent(this, NewExploreyActivity.class));
//                    }
//                    else if(from == 2){
//                        startActivity(new Intent(this, NewHomeyActivity.class));
//                    }
                    finish();
                }else{
                    Toast.makeText(this, R.string.caption, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.no_file, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "uploadToFirebase: No file chosen!");
            }
        }
    }

    public void releaseVideo() {
        if (currentlyPlaying != null) {
            currentlyPlaying.release();
            currentlyPlaying.setVisibility(View.GONE);
        }
    }

    public void getName() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(mFirebaseUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").getValue() != null) {
                    Log.d(TAG, "onDataChange: NAMEE" + (dataSnapshot.child("name").getValue()));
                    name.setText(String.valueOf(dataSnapshot.child("name").getValue()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
//        return null;
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        mVideoView.release();
        super.onDestroy();
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = this.managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.play_button)
    public void setPlay(){
        play.setVisibility(View.INVISIBLE);
        mVideoView.setVisibility(View.VISIBLE);
        showNHide.setVisibility(View.INVISIBLE);

        if (mVideoView.isPlaying()) {
            mVideoView.pause();
        } else {
            if (mVideoView != currentlyPlaying) {
                releaseVideo();
                mVideoView.start(Uri.parse(String.valueOf(filePath)));
                currentlyPlaying = mVideoView;
            }else {
                mVideoView.setVisibility(View.VISIBLE);
//                    if(holder.videoUrl!=null) {
//                        holder.videoView.start(holder.videoUrl.toString() + ".mp4");
//                    }
                mVideoView.play();
            }
        }
//        if(play.getText().equals(getString(R.string.pause))){
//            play.setText(getString(R.string.play));
//            showNHide.setVisibility(View.VISIBLE);
//
//            if(mVideoView == currentlyPlaying){
//                mVideoView.pause();
//            }
//        }
//
//        else if(play.getText().equals(getString(R.string.play))){
//            play.setText(getString(R.string.pause));
//            showNHide.setVisibility(View.INVISIBLE);
//
//            if (mVideoView != currentlyPlaying) {
//                mVideoView.play();
//                }
//                else {
//                    Toast.makeText(context, "Sorry. This Video Cannot Be Played", Toast.LENGTH_SHORT).show();
//                }
//            }
//            else {
//                mVideoView.play();
//            }
        }
}
