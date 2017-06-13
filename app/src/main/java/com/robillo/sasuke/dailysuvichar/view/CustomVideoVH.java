package com.robillo.sasuke.dailysuvichar.view;

import android.app.Activity;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.robillo.sasuke.dailysuvichar.R;
import com.robillo.sasuke.dailysuvichar.utils.ItemClickListener;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.klinker.android.simple_videoview.SimpleVideoView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.login.widget.ProfilePictureView.TAG;

public class CustomVideoVH extends RecyclerView.ViewHolder implements View.OnClickListener{

    private static final String SAMPLE_VIDEO =
            "https://firebasestorage.googleapis.com/v0/b/dailysuvichar-708d4.appspot.com/o/posts%2Fvideos%2F-KkM_JU-RlzvykGl9HUc?alt=media&token=95994318-8a65-402a-a8cb-f8c91c0dba6a.mp4";

    @BindView(R.id.shownhide)
    public ImageView showNHide;
    @BindView(R.id.iv_profile_dp)
    ImageView mPhotoDP;
//    @BindView(R.id.like_button)
//    public LikeButton likeButton;
//    @BindView(R.id.tv_likes_count)
//    TextView tvLikes;
    @BindView(R.id.play_button)
    public TextView play;

    @BindView(R.id.download_button)
    public TextView download;
    private DatabaseReference mDBrefLikes;
    private FirebaseUser mFirebaseUser;

//    TextView downloadButton;

    private ItemClickListener clickListener;

    @BindView(R.id.video_view)
    public SimpleVideoView videoView;
    private String internalStoragePath;

    @BindView(R.id.caption)
    TextView tvCaption;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_post_time)
    TextView mTvPostTime;
    public Uri videoUrl;
    public Uri downloadUrl;


    private Context context;
    private StorageReference storageReference;
    private StorageReference mStorageReferenceDP;
    private DatabaseReference mUsersDatabase;

    private final int TIMEOUT_CONNECTION = 5000;//5sec
    private final int TIMEOUT_SOCKET = 30000;//30sec

    public CustomVideoVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
//        downloadButton = (TextView) itemView.findViewById(R.id.download_button);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDBrefLikes = FirebaseDatabase.getInstance().getReference("allPosts");
        mStorageReferenceDP = FirebaseStorage.getInstance().getReference("profile").child("user").child("dp");
        itemView.setTag(itemView);
        itemView.setOnClickListener(this);
        play.setOnClickListener(this);
        download.setOnClickListener(this);
//        videoView.setOnClickListener(this);
    }

    public void setClickListener(ItemClickListener itemClickListener){
        this.clickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        clickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setName(String name) {
        if (name != null && name.length() > 0) {
            mTvUserName.setText(name);
        }
    }

    public void setPostTime(String postTime) {
        if (mTvPostTime != null && mTvPostTime.length() > 0) {
            mTvPostTime.setText(postTime);
        }
    }

    public void setCaption(String caption) {
        if (caption != null && caption.length() > 0) {
            tvCaption.setText(caption);
        }
    }

    public void setVideo(StorageReference storageReference) {
//        final Uri videoUrl;

        Log.d(TAG, "setVideo: STORAGEREF " + storageReference);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                videoUrl = uri;
                Log.d(TAG, "onSuccess: VIDEOURL" + videoUrl);
            }
        });

        Log.d(TAG, "onSuccess: VIDEOURL" + videoUrl);

        videoView.setErrorTracker(new SimpleVideoView.VideoPlaybackErrorTracker() {
            @Override
            public void onPlaybackError(Exception e) {
                e.printStackTrace();
//                Snackbar.make(videoView, "Uh oh, error playing!", Snackbar.LENGTH_INDEFINITE).show();
            }
        });
        if (videoUrl != null) {
            videoView.start(videoUrl.toString() + ".mp4");
//            videoView.start(SAMPLE_VIDEO);
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (videoView.isPlaying())
                        videoView.pause();
                    else
                        videoView.play();
                }
            });
        }
    }

    public void setImageView() {
//        imageView.setImageResource(R.drawable.astrology);
    }

    public void setStatusDP(final String UID) {
        mUsersDatabase.child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("photoUrl").getValue() != null) {
                    Activity a = (Activity) context;
                    if (context != null && !a.isDestroyed()) {
                        Glide.with(context).
                                using(new FirebaseImageLoader())
                                .load(mStorageReferenceDP.child(UID))
                                .centerCrop()
                                .placeholder(R.mipmap.ic_launcher)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(mPhotoDP);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("GLIDINGGGGGG", "CANCELLED");
            }
        });
    }

    public void setDownload(StorageReference ref, final Context context) {
        Toast.makeText(context, "Downloading video..", Toast.LENGTH_SHORT).show();
        File rootPath = new File(Environment.getExternalStorageDirectory(), "dailysuvichar");
        if (!rootPath.exists()) {
            rootPath.mkdirs();
        }


        final File file = new File(rootPath, generateRandomName() + ".mp4");

        ref.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebase ", ";local tem file created  created " + file.toString());
                scanGallery(getApplicationContext(), file.toString());

                //  updateDb(timestamp,localFile.toString(),position);
//                getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                Toast.makeText(context, "Video Download Successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ", ";local tem file not created  created " + exception.toString());

                Toast.makeText(context, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }
//
//    @OnClick(R.id.download_button)
    public void downloadVideo(StorageReference storageReference) {

        Toast.makeText(context, "Downloading video...", Toast.LENGTH_SHORT).show();
        if(storageReference!=null) {
            File rootPath = new File(Environment.getExternalStorageDirectory(), "dailysuvichar");
            if (!rootPath.exists()) {
                rootPath.mkdirs();
            }

            final File file = new File(rootPath, generateRandomName() + ".mp4");
            URL url = null;
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    downloadUrl = uri;
                }
            });
            if(downloadUrl!=null) {
                try {
                    url = new URL(downloadUrl.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                long startTime = System.currentTimeMillis();
                Log.i(TAG, "image download beginning: " + downloadUrl);

                //Open a connection to that URL.
                URLConnection ucon = null;
                if(url!=null) {
                    try {
                        ucon = url.openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //this timeout affects how long it takes for the app to realize there's a connection problem
                    ucon.setReadTimeout(TIMEOUT_CONNECTION);
                    ucon.setConnectTimeout(TIMEOUT_SOCKET);


                    //Define InputStreams to read from the URLConnection.
                    // uses 3KB download buffer
                    InputStream is = null;
                    try {
                        is = ucon.getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);
                    FileOutputStream outStream = null;
                    try {
                        outStream = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    byte[] buff = new byte[5 * 1024];

                    //Read bytes (and store them) until there is nothing more to read(-1)
                    int len;
                    try {
                        while ((len = inStream.read(buff)) != -1) {
                            outStream.write(buff, 0, len);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //clean up
                    try {
                        outStream.flush();
                        outStream.close();
                        inStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "Video Downloaded!", Toast.LENGTH_SHORT).show();


                    Log.i(TAG, "download completed in "
                            + ((System.currentTimeMillis() - startTime) / 1000)
                            + " sec");
                }

            }else{
                Log.d(TAG, "downloadVideo: NULLURL");
            }
        }
    }

    private String generateRandomName(){
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    private void scanGallery(final Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[] { path },null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    //unimplemeted method
                    Log.e("PATH IS", path);
                    internalStoragePath = path;
                    Toast.makeText(getApplicationContext(), "PATH IS" + internalStoragePath, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void setLikedUser(String authorUid, String uid, final boolean liked, ArrayList<String> likedUsers) {
//
//        Log.d(GifHeaderParser.TAG, "setLikedUser: ");
//
//
////        final ArrayList<String> finalLikedUsers1 = likedUsers;
////        mDBrefLikes.child(uid).child("likes").runTransaction(new Transaction.Handler() {
////            @Override
////            public Transaction.Result doTransaction(MutableData mutableData) {
////                if (mutableData != null) {
////                    int likes = mutableData.getValue(Integer.class);
////
////                    Log.d(TAG, "doTransaction: " + finalLikedUsers1);
////                    if (liked) {
////                        if (!finalLikedUsers1.contains(mFirebaseUser.getUid())) {
////                            likes++;
//////                            likedUsers.add(mFirebaseUser.getUid());
////                        }
////                    }else{
////                        Log.d(TAG, "doTransaction: 2 " + finalLikedUsers1);
////
////                        if (finalLikedUsers1.contains(mFirebaseUser.getUid())) {
////                            likes--;
//////                            likedUsers.remove(mFirebaseUser.getUid());
////                        }
////                    }
////                    Log.d(TAG, "doTransaction: " + finalLikedUsers1);
////                mutableData.setValue(finalLikedUsers1);
////            }
////                return Transaction.success(mutableData);
////
////            }
////
////
////            @Override
////            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
////
////                if (liked) {
////                    likeButton.setLiked(true);
////                } else {
////                    likeButton.setLiked(false);
////                }
////                Log.d(TAG, "onComplete: " + b);
////            }
////        });
//
////        mUsersDatabase.child(mFirebaseUser.getUid()).child("userPosts").child(uid).child("likes").runTransaction(new Transaction.Handler() {
////            @Override
////            public Transaction.Result doTransaction(MutableData mutableData) {
////                if (mutableData != null) {
////                    int likes = mutableData.getValue(Integer.class);
////                    if (liked) {
//////                        if(finalLikedUsers.contains(mFirebaseUser.getUid())) {
//////                            return Transaction.success(mutableData);
//////                        }else {
////                            likes++;
//////                        }
////                    } else if (!liked && likes > 0) {
////                        likes--;
////                    }
////                    mutableData.setValue(likes);
////                }
////                return Transaction.success(mutableData);
////            }
////
////            @Override
////            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
////
//////                if(liked){
//////                    likeButton.setLiked(true);
//////                }else{
//////                    likeButton.setLiked(false);
//////                }
////                Log.d(TAG, "onComplete: " + b);
////            }
////        });
//
//        if(liked) {
//            if(likedUsers==null){
//                likedUsers=new ArrayList<>();
//            }
//            if(!likedUsers.contains(mFirebaseUser.getUid())) {
//                likedUsers.add(mFirebaseUser.getUid());
//                mDBrefLikes.child(uid).child("likedUsers").setValue(likedUsers);
//                mUsersDatabase.child(authorUid).child("userPosts").child(uid).child("likedUsers").setValue(likedUsers);
//            }
//        }else if(!liked){
//
//            Log.d(GifHeaderParser.TAG, "setLikedUser: likedusers "+likedUsers);
//            if(likedUsers==null){
////                likedUsers.remove(mFirebaseUser.getUid());
//                return;
//            }else {
//                if(likedUsers.contains(mFirebaseUser.getUid())) {
//                    likedUsers.remove(mFirebaseUser.getUid());
//                    mDBrefLikes.child(uid).child("likedUsers").setValue(likedUsers);
//                    mUsersDatabase.child(authorUid).child("userPosts").child(uid).child("likedUsers").setValue(likedUsers);
//                }
//            }
//        }
//
//        if (liked) {
//            likeButton.setLiked(true);
//        } else {
//            likeButton.setLiked(false);
//        }
//    }

//    public Boolean containsLikedUser(ArrayList<String> likedUsers) {
//        if (likedUsers != null) {
//            return likedUsers.contains(mFirebaseUser.getUid());
//        }
//        return false;
//    }

//    public void setLikes(int likes) {
//        tvLikes.setText(String.valueOf(likes)+" like this");
//    }
}
