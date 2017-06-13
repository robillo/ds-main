package com.robillo.sasuke.dailysuvichar.newfragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.robillo.sasuke.dailysuvichar.R;
import com.robillo.sasuke.dailysuvichar.models.Photo;
import com.robillo.sasuke.dailysuvichar.view.adapter.PhotoItemAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllPhotosFragment extends Fragment {

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mPullToRefresh;
    @BindView(R.id.alternate_layout)
    LinearLayout alternateLayout;

    private MultiTypeAdapter mAdapter;

    private LinearLayoutManager mLayoutManager;
    private static FirebaseUser mFirebaseUser;
    private String uid;
    private AVLoadingIndicatorView avi;
    private HashMap<String, String> userStatus;
    private static DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReferencePosts, mDatabaseReferenceGuru;
    private StorageReference mStorageReference;
    private ArrayList<String> mSelectedSubInterests;
    private ArrayList<String> mSelectedGurus;

    private StorageReference mStorageReferenceDP;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mUsersDatabase;

    private static final String TAG = "ALLSTATUS", STATUS = "status";
    private static final int PICK_IMAGE_REQUEST = 250;
    private Uri filePath;
    Items items;
    private Animation slide_down;
    private Animation slide_up;
    private int CHECK = 1;
    private String intentDBReference = null;
    private HashMap<String,Boolean> isPhotoDoneYour,isPhotoDoneGuru;
    private HashMap<String, Long> isDone;
    private HashMap<String, Photo> photoHashMapStore;
    private HashMap<String, Long> photoHashMap;
    private String from = "HOME";

    @BindView(R.id.recyclerview)
    RecyclerView mRvHome;

    public AllPhotosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_all_photos, container, false);

        mRvHome= (RecyclerView) v.findViewById(R.id.recyclerview);
        mPullToRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        alternateLayout = (LinearLayout) v.findViewById(R.id.alternate_layout);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference("users");
        mStorageReferenceDP = FirebaseStorage.getInstance().getReference("profile").child("user").child("dp");

        mRvHome.setVisibility(View.VISIBLE);

        uid = mFirebaseUser.getUid();

        Log.e(TAG, uid);

        slide_down = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setItemPrefetchEnabled(true);
        mLayoutManager.setInitialPrefetchItemCount(10);
        mRvHome.setLayoutManager(mLayoutManager);
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(Photo.class, new PhotoItemAdapter());
        mRvHome.setAdapter(mAdapter);

        items = new Items();

        //WONT CAUSE NPE DONT WORRY
        if(from.equals("YOUR")){
            //SHOW YOUR FEEDS, COPY CODE FROM YOUR FEEDS FRAGMENT
            Log.e("FROM", "YOUR TO PHOTOS");

            isPhotoDoneYour = new HashMap<>();

            mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
            Log.e(TAG, uid);
            Log.e(TAG, mDatabaseReference.toString());

            if(isOnline()) {
                refresh();

                fetchPhotosFromFirebaseYour();

            }else{
                Toast.makeText(getActivity(), getString(R.string.no_inter), Toast.LENGTH_SHORT).show();
            }

        }
        else if(from.equals("EXPLORE")){
            //SHOW FEEDS ON YOUR INTERESTS
            Log.e("FROM", "EXPLORE TO PHOTOS");


            mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
            Log.e(TAG, uid);
            Log.e(TAG, mDatabaseReference.toString());

            mSelectedSubInterests = new ArrayList<>();

            isDone= new HashMap<>();
            photoHashMap = new HashMap<>();
            photoHashMapStore = new HashMap<>();

            if(isOnline()) {
                refresh();

                mDatabaseReference.child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("mSelectedSubInterests").getValue() != null) {
                            mSelectedSubInterests.addAll((Collection<? extends String>) dataSnapshot.child("mSelectedSubInterests").getValue());
                        }
                        fetchPhotosFromFirebaseHome();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                fetchPhotosFromFirebaseHome();

            }else{
                Toast.makeText(getActivity(), getString(R.string.no_inter), Toast.LENGTH_SHORT).show();
            }

        }
        else if(from.equals("HOME")){
            //SHOW FEEDS FROM WHO YOU FOLLOW + DS PEOPLE
            Log.e("FROM", "HOME TO PHOTOS");

            mSelectedGurus = new ArrayList<>();
            isPhotoDoneGuru = new HashMap<>();

            mDatabaseReferenceGuru = FirebaseDatabase.getInstance().getReference("users");

            if(isOnline()) {
                refresh();

                mDatabaseReferenceGuru.child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("following").getValue() != null) {
                            mSelectedGurus.addAll((Collection<? extends String>) dataSnapshot.child("following").getValue());
                        }
                        fetchPhotosFromFirebaseGuru();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                fetchPhotosFromFirebaseGuru();

            }else{
                Toast.makeText(getActivity(), getString(R.string.no_inter), Toast.LENGTH_SHORT).show();
            }
        }


        return v;
    }

    private void refresh(){
        mPullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                Handler handler1 = new Handler();
                handler1.post(new Runnable() {
                    @Override
                    public void run() {
                        //CALL DATA HERE
                        if(isOnline()) {
                            if(from.equals("YOUR")){
                                //SHOW YOUR FEEDS, COPY CODE FROM YOUR FEEDS FRAGMENT
                                Log.e("FROM", "YOUR TO PHOTOS");
                                fetchPhotosFromFirebaseYour();

                            }
                            else if(from.equals("EXPLORE")){
                                //SHOW FEEDS ON YOUR INTERESTS
                                Log.e("FROM", "EXPLORE TO PHOTOS");
                                    fetchPhotosFromFirebaseHome();
                            }
                            else if(from.equals("HOME")){
                                //SHOW FEEDS FROM WHO YOU FOLLOW + DS PEOPLE
                                Log.e("FROM", "HOME TO PHOTOS");
                                fetchPhotosFromFirebaseGuru();
                            }

                        }else{
                            Toast.makeText(getActivity(), getString(R.string.no_inter), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefresh.setRefreshing(false);
                    }
                },1500);
            }
        });
    }

    private void fetchPhotosFromFirebaseGuru() {

//        mLayoutManager.setStackFromEnd(true);

        if(mSelectedGurus!=null && mSelectedGurus.size()>0) {

            for(String guru: mSelectedGurus) {
                int i=0;
                final int finalI = i;

                mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("users").child(guru).child("posts");
                mStorageReference = FirebaseStorage.getInstance().getReference("posts").child("images");


                mDatabaseReferencePosts.child("photo").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int k=0;

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Photo photoSnap = postSnapshot.getValue(Photo.class);
                            photoSnap.setStorageReference(mStorageReference.child(postSnapshot.getKey()));

                            if (!isPhotoDoneGuru.containsKey(postSnapshot.getKey())) {

                                items.add(photoSnap);
                                isPhotoDoneGuru.put(postSnapshot.getKey(), true);
                            }
                            if(finalI ==mSelectedGurus.size()-1 && k==dataSnapshot.getChildrenCount()-1){
                                Collections.reverse(items);
                            }
                            k++;
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                    }
                });
                i++;
            }

            Log.d(TAG, "fetchStatusFromFirebase: " + items.size());
            if(items.size()>0){
                alternateLayout.setVisibility(View.INVISIBLE);
            }
            mAdapter.setItems(items);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void fetchPhotosFromFirebaseYour() {

//        mLayoutManager.setStackFromEnd(true);

        mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("users").child(mFirebaseUser.getUid()).child("posts");
        mStorageReference = FirebaseStorage.getInstance().getReference("posts").child("images");

        mDatabaseReferencePosts.child("photo").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int k=0;

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                    Photo photoSnap = postSnapshot.getValue(Photo.class);
                    photoSnap.setStorageReference(mStorageReference.child(postSnapshot.getKey()));

                    Log.d(TAG, "onDataChange:uuuuuu "+mStorageReference.child(postSnapshot.getKey()));

                    if (!isPhotoDoneYour.containsKey(postSnapshot.getKey())) {

                        items.add(photoSnap);
                        isPhotoDoneYour.put(postSnapshot.getKey(), true);
                    }
                    if(k==dataSnapshot.getChildrenCount()-1){
                        Collections.reverse(items);
                    }
                    k++;
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        Log.d(TAG, "fetchStatusFromFirebase: " + items.size());
        mAdapter.setItems(items);
        if(items.size()>0){
            alternateLayout.setVisibility(View.INVISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void fetchPhotosFromFirebaseHome() {


        if(mSelectedSubInterests!=null) {

            if (mSelectedSubInterests.size() > 0) {
                mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("tags");
                mStorageReference = FirebaseStorage.getInstance().getReference("posts").child("images");

                mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("tags");
                for (int i = 0; i < mSelectedSubInterests.size(); i++) {

                    String subInt = mSelectedSubInterests.get(i);
//                mDatabaseReferencePosts.child(subInt).child("status");

                    final int finalI = i;
                    mDatabaseReferencePosts.child(subInt).child("photo").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

//                        Log.i(TAG, "onDataChange2222: " + dataSnapshot.getValue());
//                        Log.i(TAG, "onDataChange2222: " + dataSnapshot.getChildrenCount());

//                        Log.i(TAG, "onDataChange: "+mUserList.size()+" ");

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Photo photoSnap = postSnapshot.getValue(Photo.class);
                                photoSnap.setStorageReference(mStorageReference.child(postSnapshot.getKey()));

                                if (!isDone.containsKey(postSnapshot.getKey())) {
//                                items.add(photoSnap);
                                    isDone.put(postSnapshot.getKey(), photoSnap.getTimestamp());
                                    photoHashMapStore.put(postSnapshot.getKey(), photoSnap);
                                }
//                            Log.d(TAG, "fetchPhotosFromFirebaseHome: ISDONE "+isDone.size());

                                mAdapter.notifyDataSetChanged();
//                            if (mAdapter.getItemCount() > 0) {
//                                avi.hide();
//                            }
                            }
                            Log.d(TAG, "fetchPhotosFromFirebaseHome: ISDONE " + isDone.size());
                            if (finalI == mSelectedSubInterests.size() - 1 && isDone.size() > 0) {
                                photoHashMap = sortByComparator(isDone, false);
                                Log.d(TAG, "fetchPhotosFromFirebaseHome: photo" + photoHashMap);
                                Log.d(TAG, "fetchPhotosFromFirebaseHome: photo" + photoHashMapStore);
                                for (int i = 0; i < photoHashMap.size(); i++) {
                                    if (!items.contains(photoHashMapStore.get(photoHashMap.keySet().toArray()[i]))) {
                                        items.add(photoHashMapStore.get(photoHashMap.keySet().toArray()[i]));
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                        }
                    });
//                Log.d(TAG, "fetchPhotosFromFirebaseHome: ISDONE "+isDone.size());

                }
//            Log.d(TAG, "fetchPhotosFromFirebaseHome: ISDONE "+isDone.size());

            }
            Log.d(TAG, "fetchPhotosFromFirebaseHome: " + items.size());
            mAdapter.setItems(items);
            if(items.size()>0){
                alternateLayout.setVisibility(View.INVISIBLE);
            }
            mAdapter.notifyDataSetChanged();
//        if(mAdapter.getItemCount()>0){
//            avi.hide();
//        }
        }
    }

    private static HashMap<String, Long> sortByComparator(HashMap<String, Long> unsortMap, final boolean order)
    {

        List<HashMap.Entry<String, Long>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<HashMap.Entry<String, Long>>()
        {
            public int compare(HashMap.Entry<String, Long> o1,
                               HashMap.Entry<String, Long> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        HashMap<String, Long> sortedMap = new LinkedHashMap<>();
        for (HashMap.Entry<String, Long> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
