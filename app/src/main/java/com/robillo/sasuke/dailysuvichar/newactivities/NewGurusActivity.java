package com.robillo.sasuke.dailysuvichar.newactivities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.robillo.sasuke.dailysuvichar.R;
import com.robillo.sasuke.dailysuvichar.models.Guru;
import com.robillo.sasuke.dailysuvichar.view.adapter.RVGuruAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class NewGurusActivity extends AppCompatActivity {

    private RVGuruAdapter mRvGuruAdapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<Guru> guruList;
    private String MOTIVATION_GURU = "Motivation Guru";
    private String AYURVEDA_GURU = "Ayurveda Guru";
    private String YOGA_GURU = "Yoga Guru";
    private String PANDIT = "Pandit";
    private String ASTROLOGY_GURU = "Astrology Guru";
    private DatabaseReference mDatabaseReference, mDatabaseReferenceUsers;
    private StorageReference mStorageReference;
    private static FirebaseUser mFirebaseUser;
    private LinearLayoutManager mLayoutManager;
    Items items;
    HashMap<String, Integer> guruMap;
    private MultiTypeAdapter mAdapter;
    private static ArrayList<String> following;
    private static HashMap<String, Boolean> followingMap;
    //    private static ArrayList<String> guruFollowers;
    private static Integer getFollowerCount;
    @BindView(R.id.recyclerview)
    public RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_gurus);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference("profile").child("user").child("dp");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("gurus").child("official");
        mDatabaseReferenceUsers = FirebaseDatabase.getInstance().getReference("users").child(mFirebaseUser.getUid());

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager = new GridLayoutManager(this, 3);
        rv.setLayoutManager(gridLayoutManager);

        following = new ArrayList<>();
//        guruFollowers= new ArrayList<>();
        guruList = new ArrayList<>();
        guruMap = new HashMap<>();
        followingMap = new HashMap<>();
        mRvGuruAdapter = new RVGuruAdapter(this, guruList);

        if (isOnline()) {

            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        Guru guru = postSnapshot.getValue(Guru.class);
                        guru.setGuruUid(postSnapshot.getKey());
                        guru.setStorageReference(mStorageReference.child(guru.getUid()));
                        if (guruMap.containsKey(postSnapshot.getKey())) {
                            guruList.set(guruMap.get(postSnapshot.getKey()), guru);
                        } else {
                            guruList.add(guru);
                            guruMap.put(postSnapshot.getKey(), guruList.indexOf(guru));
                        }
                        Log.d(TAG, "onDataChange: count " + guru.getFollowersCount());
//                    if(postSnapshot.child("followers").getValue()!=null){
//                        guruFollowers.addAll((Collection<? extends String>) postSnapshot.child("followers").getValue());
//                    }
//                    videoSnap.setStorageReference(mStorageReferenceVideo.child(postSnapshot.getKey()));
                        mRvGuruAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            mDatabaseReferenceUsers.child("following").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            if (!followingMap.containsKey(String.valueOf(postSnapshot.getValue()))) {
                                following.add(String.valueOf(postSnapshot.getValue()));
                                followingMap.put(String.valueOf(postSnapshot.getValue()), true);
                            }
                            mRvGuruAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else {
            Toast.makeText(this, R.string.no_inter, Toast.LENGTH_SHORT).show();
        }

        rv.setAdapter(mRvGuruAdapter);
    }

    public static void setFollowing(ArrayList<String> guruFollowers, String uid, boolean isFollowing, String guruUid) {
        DatabaseReference mDatabaseReferenceGuru = FirebaseDatabase.getInstance().getReference("gurus").child("official");
        DatabaseReference mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference("users").child(mFirebaseUser.getUid());

        if (guruFollowers == null) {
            guruFollowers = new ArrayList<>();
        }

        if (isFollowing) {
            if (!following.contains(uid)) {

                following.add(uid);

                Set<String> set = new HashSet<>();
                set.addAll(following);
                following.clear();
                following.addAll(set);

                mDatabaseReferenceUser.child("following").setValue(following);
            }

            guruFollowers.add(mFirebaseUser.getUid());
            mDatabaseReferenceGuru.child(guruUid).child("followersCount").setValue(guruFollowers.size());
            mDatabaseReferenceGuru.child(guruUid).child("followers").setValue(guruFollowers);
        } else {
            following.remove(uid);
            guruFollowers.remove(mFirebaseUser.getUid());
            if (!guruFollowers.contains(mFirebaseUser.getUid()) && following.contains(uid)) {
                following.remove(uid);
            }
            mDatabaseReferenceGuru.child(guruUid).child("followersCount").setValue(guruFollowers.size());
            mDatabaseReferenceUser.child("following").setValue(following);
            mDatabaseReferenceGuru.child(guruUid).child("followers").setValue(guruFollowers);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_sort: {
                showPopup();
                break;
            }
            case R.id.about: {

                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

//        @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        switch (id) {
//            case R.id.action_search: {
//
//                break;
//            }
//            case R.id.action_sort: {
//                showPopup();
//                break;
//            }
//            case R.id.about: {
//
//                break;
//            }
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void showPopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Sorting Option:")
                .setItems(new CharSequence[]{getString(R.string.followers), getString(R.string.astrologyy), getString(R.string.yogaa), getString(R.string.pandit_),
                        getString(R.string.motivationn), getString(R.string.ayurvedaa), getString(R.string.show_all)}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which) {
                            case 0: {
                                //Filter By Followers
                                sortGuru("Following", false,false);
                                rv.setAdapter(mRvGuruAdapter);
                                mRvGuruAdapter.notifyDataSetChanged();
                                break;
                            }
                            case 1: {
                                //Astrology Gurus1
//                                mRvGuruAdapter = new RVGuruAdapter(getActivity(), guruList, 1);
                                sortGuru("Astrology Guru", true,false);
                                mRvGuruAdapter.notifyDataSetChanged();
                                rv.setAdapter(mRvGuruAdapter);
                                break;
                            }
                            case 2: {
                                //Yoga Gurus2
//                                mRvGuruAdapter = new RVGuruAdapter(getActivity(), guruList, 2);
                                sortGuru("Yoga Guru", true,false);
                                mRvGuruAdapter.notifyDataSetChanged();
                                rv.setAdapter(mRvGuruAdapter);
                                break;
                            }
                            case 3: {
                                //Pandits Gurus3
//                                mRvGuruAdapter = new RVGuruAdapter(getActivity(), guruList, 3);
                                sortGuru("Pandit Guru", true,false);
                                mRvGuruAdapter.notifyDataSetChanged();
                                rv.setAdapter(mRvGuruAdapter);
                                break;
                            }
                            case 4: {
                                //Motivation Gurus4
//                                mRvGuruAdapter = new RVGuruAdapter(getActivity(), guruList, 4);
                                sortGuru("Motivation Guru", true,false);
                                rv.setAdapter(mRvGuruAdapter);
                                mRvGuruAdapter.notifyDataSetChanged();
                                break;
                            }
                            case 5: {
                                //Ayurveda Gurus5
//                                mRvGuruAdapter = new RVGuruAdapter(getActivity(), guruList, 5);
                                sortGuru("Ayurveda Guru", true, false);
                                mRvGuruAdapter.notifyDataSetChanged();
                                rv.setAdapter(mRvGuruAdapter);
                                break;
                            }
                            case 6: {
                                //SHOW ALL
//                                mRvGuruAdapter = new RVGuruAdapter(NewGurusActivity.this, guruList, 100);
                                sortGuru("All", true, true);
                                mRvGuruAdapter.notifyDataSetChanged();
                                rv.setAdapter(mRvGuruAdapter);                            }
                        }
                    }
                });
        builder.show();
    }

    public void refreshDB() {

    }

    public void sortGuru(String option, boolean b, boolean all) {

        if (isOnline()) {
            if(!all) {
                if (!b) {

                    guruList.clear();
                    guruMap.clear();

                    mDatabaseReference.orderByChild("followersCount").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int k = 0;

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                Guru guru = postSnapshot.getValue(Guru.class);
                                guru.setGuruUid(postSnapshot.getKey());
                                guru.setStorageReference(mStorageReference.child(guru.getUid()));
                                if (guruMap.containsKey(postSnapshot.getKey())) {
                                    guruList.set(guruMap.get(postSnapshot.getKey()), guru);
                                } else {
                                    guruList.add(guru);
                                    guruMap.put(postSnapshot.getKey(), guruList.indexOf(guru));
                                }
                                Log.d(TAG, "onDataChange: count " + guru.getFollowersCount());
//                    if(postSnapshot.child("followers").getValue()!=null){
//                        guruFollowers.addAll((Collection<? extends String>) postSnapshot.child("followers").getValue());
//                    }
//                    videoSnap.setStorageReference(mStorageReferenceVideo.child(postSnapshot.getKey()));
                                if (k == dataSnapshot.getChildrenCount() - 1) {
                                    Collections.reverse(guruList);
                                }
                                k++;
                                mRvGuruAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                } else {
                    guruList.clear();
                    guruMap.clear();

                    mDatabaseReference.orderByChild("specialization").equalTo(option).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                Guru guru = postSnapshot.getValue(Guru.class);
                                guru.setGuruUid(postSnapshot.getKey());
                                guru.setStorageReference(mStorageReference.child(guru.getUid()));
                                if (guruMap.containsKey(postSnapshot.getKey())) {
                                    guruList.set(guruMap.get(postSnapshot.getKey()), guru);
                                } else {
                                    guruList.add(guru);
                                    guruMap.put(postSnapshot.getKey(), guruList.indexOf(guru));
                                }
                                Log.d(TAG, "onDataChange: count " + guru.getFollowersCount());
//                    if(postSnapshot.child("followers").getValue()!=null){
//                        guruFollowers.addAll((Collection<? extends String>) postSnapshot.child("followers").getValue());
//                    }
//                    videoSnap.setStorageReference(mStorageReferenceVideo.child(postSnapshot.getKey()));
                                mRvGuruAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }else{
                guruList.clear();
                guruMap.clear();

                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int k = 0;

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            Guru guru = postSnapshot.getValue(Guru.class);
                            guru.setGuruUid(postSnapshot.getKey());
                            guru.setStorageReference(mStorageReference.child(guru.getUid()));
                            if (guruMap.containsKey(postSnapshot.getKey())) {
                                guruList.set(guruMap.get(postSnapshot.getKey()), guru);
                            } else {
                                guruList.add(guru);
                                guruMap.put(postSnapshot.getKey(), guruList.indexOf(guru));
                            }
                            Log.d(TAG, "onDataChange: count " + guru.getFollowersCount());
//                    if(postSnapshot.child("followers").getValue()!=null){
//                        guruFollowers.addAll((Collection<? extends String>) postSnapshot.child("followers").getValue());
//                    }
//                    videoSnap.setStorageReference(mStorageReferenceVideo.child(postSnapshot.getKey()));
                            if (k == dataSnapshot.getChildrenCount() - 1) {
                                Collections.reverse(guruList);
                            }
                            k++;
                            mRvGuruAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        } else {
            Toast.makeText(this, R.string.no_inter, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, NewMainActivity.class);
        i.putExtra("pageNumber", 1);
        startActivity(i);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
