package com.example.sasuke.dailysuvichar.newfragments;


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

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.Status;
import com.example.sasuke.dailysuvichar.view.adapter.StatusItemAdapter;
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
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllStatusFragment extends Fragment {

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

    private StorageReference mStorageReferenceDP;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mUsersDatabase;
    private ArrayList<String> mSelectedGurus;

    private static final String TAG = "ALLSTATUS", STATUS = "status";
    private static final int PICK_IMAGE_REQUEST = 250;
    private Uri filePath;
    Items items;
    private Animation slide_down;
    private Animation slide_up;
    private int CHECK = 1;
    private String intentDBReference = null;
    private HashMap<String, Long> isDone;
    private HashMap<String, Status> statusHashMapStore;
    private HashMap<String, Long> statusHashMap;
    private String from = "HOME";
    private HashMap<String, Boolean> isStatusDone, isStatusDoneGuru;

    @BindView(R.id.recyclerview)
    RecyclerView mRvHome;


    public AllStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all_status, container, false);
        ButterKnife.bind(getActivity());

        Bundle args = getArguments();
        from = args.getString("from");

        mRvHome = (RecyclerView) v.findViewById(R.id.recyclerview);
        mPullToRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        alternateLayout = (LinearLayout) v.findViewById(R.id.alternate_layout);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference("users");
        mStorageReferenceDP = FirebaseStorage.getInstance().getReference("profile").child("user").child("dp");

//        fetchDP();

//        avi = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        mRvHome.setVisibility(View.VISIBLE);

        uid = mFirebaseUser.getUid();

        Log.e(TAG, uid);

        slide_down = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
//        customVideoAdapter = new CustomVideoAdapter();

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setItemPrefetchEnabled(true);
//        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setInitialPrefetchItemCount(10);
        mRvHome.setLayoutManager(mLayoutManager);
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(Status.class, new StatusItemAdapter());
        mRvHome.setAdapter(mAdapter);

        items = new Items();

        //WONT CAUSE NPE DONT WORRY
        if (isOnline()) {
            if (from.equals("YOUR")) {
                //SHOW YOUR FEEDS, COPY CODE FROM YOUR FEEDS FRAGMENT
                Log.e("FROM", "YOUR TO STATUS");
                isStatusDone = new HashMap<>();
                fetchStatusFromFirebaseYour();
                refresh();

            } else if (from.equals("EXPLORE")) {
                //SHOW FEEDS ON YOUR INTERESTS

                mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
                Log.e(TAG, uid);
                Log.e(TAG, mDatabaseReference.toString());

                mSelectedSubInterests = new ArrayList<>();
                isDone = new HashMap<>();

                statusHashMap = new HashMap<>();
                statusHashMapStore = new HashMap<>();

                refresh();

                mDatabaseReference.child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d(TAG, "onDataChange: SUBINTS " + dataSnapshot.child("selectedSubInterests"));
//                Log.d(TAG, "onDataChange: SUBINTS " + dataSnapshot.getChildrenCount());
//                User user = dataSnapshot.getValue(User.class);
//                Log.d(TAG, "onDataChange: INTTTT "+dataSnapshot.child("selectedSubInterests").getValue());
                        if (dataSnapshot.child("mSelectedSubInterests").getValue() != null) {
                            mSelectedSubInterests.addAll((Collection<? extends String>) dataSnapshot.child("mSelectedSubInterests").getValue());
                        }
                        fetchStatusFromFirebaseExplore();
                        alternateLayout.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                fetchStatusFromFirebaseExplore();


                Log.e("FROM", "EXPLORE TO STATUS");

            } else if (from.equals("HOME")) {
                //SHOW FEEDS FROM WHO YOU FOLLOW + DS PEOPLE
                Log.e("FROM", "HOME TO STATUS");

                mSelectedGurus = new ArrayList<>();
                isStatusDoneGuru = new HashMap<>();

                mDatabaseReferenceGuru = FirebaseDatabase.getInstance().getReference("users");

                    refresh();

                    mDatabaseReferenceGuru.child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("following").getValue() != null) {
                                mSelectedGurus.addAll((Collection<? extends String>) dataSnapshot.child("following").getValue());
                            }
                            fetchStatusFromFirebaseGuru();
                            alternateLayout.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                fetchStatusFromFirebaseGuru();

            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_inter), Toast.LENGTH_SHORT).show();
        }


        return v;
    }


    private void fetchStatusFromFirebaseGuru() {

        mLayoutManager.setStackFromEnd(true);

        if(mSelectedGurus!=null && mSelectedGurus.size()>0) {

            for(String guru: mSelectedGurus) {

                mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("users").child(guru).child("posts");

                mDatabaseReferencePosts.child("status").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Status statusSnap = postSnapshot.getValue(Status.class);

                            if (!isStatusDoneGuru.containsKey(postSnapshot.getKey())) {

                                items.add(statusSnap);
                                isStatusDoneGuru.put(postSnapshot.getKey(), true);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                    }
                });
            }

            Log.d(TAG, "fetchStatusFromFirebase: " + items.size());
            mAdapter.setItems(items);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void fetchStatusFromFirebaseYour() {

        mLayoutManager.setStackFromEnd(true);

        mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("users").child(mFirebaseUser.getUid()).child("posts");

        Log.d(TAG, "fetchStatusFromFirebase: URLL " + mDatabaseReferencePosts);
        mDatabaseReferencePosts.child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                        Log.i(TAG, "onDataChange: "+mUserList.size()+" ");

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Status status = postSnapshot.getValue(Status.class);

                    if (!isStatusDone.containsKey(postSnapshot.getKey())) {
                        items.add(status);
                        isStatusDone.put(postSnapshot.getKey(), true);
                    }
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
        mAdapter.notifyDataSetChanged();
    }

    private void refresh() {
        mPullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                Handler handler1 = new Handler();
                handler1.post(new Runnable() {
                    @Override
                    public void run() {
                        //CALL DATA HERE
                        if (isOnline()) {
                            if (from.equals("YOUR")) {
                                //SHOW YOUR FEEDS, COPY CODE FROM YOUR FEEDS FRAGMENT
                                Log.e("FROM", "YOUR TO PHOTOS");
                                fetchStatusFromFirebaseYour();

                            } else if (from.equals("EXPLORE")) {
                                //SHOW FEEDS ON YOUR INTERESTS
                                Log.e("FROM", "EXPLORE TO PHOTOS");
                                fetchStatusFromFirebaseExplore();
                            } else if (from.equals("HOME")) {
                                //SHOW FEEDS FROM WHO YOU FOLLOW + DS PEOPLE
                                Log.e("FROM", "HOME TO PHOTOS");
                                fetchStatusFromFirebaseGuru();
                            }

                        } else {
                            Toast.makeText(getActivity(), getString(R.string.no_inter), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefresh.setRefreshing(false);
                    }
                }, 1500);
            }
        });
    }

    private void fetchStatusFromFirebaseExplore() {


        if (mSelectedSubInterests.size() > 0) {
            Log.d(TAG, "fetchStatusFromFirebaseExplore: SIZE " + mSelectedSubInterests.size());
            Log.d(TAG, "fetchStatusFromFirebaseExplore: " + mSelectedSubInterests);

            mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("tags");
            for (int i = 0; i < mSelectedSubInterests.size(); i++) {

                String subInt = mSelectedSubInterests.get(i);
//                mDatabaseReferencePosts.child(subInt).child("status");

                final int finalI = i;
                mDatabaseReferencePosts.child(subInt).child("status").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

//                        Log.i(TAG, "onDataChange2222: " + dataSnapshot.getValue());
//                        Log.i(TAG, "onDataChange2222: " + dataSnapshot.getChildrenCount());

//                        Log.i(TAG, "onDataChange: "+mUserList.size()+" ");

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Status status = postSnapshot.getValue(Status.class);

                            if (!isDone.containsKey(postSnapshot.getKey())) {
//                                items.add(status);
                                isDone.put(postSnapshot.getKey(), status.getTimestamp());
                                statusHashMapStore.put(postSnapshot.getKey(), status);
                            }
//                            Log.d(TAG, "fetchStatusFromFirebaseExplore: ISDONE "+isDone.size());

                            mAdapter.notifyDataSetChanged();
//                            if (mAdapter.getItemCount() > 0) {
//                                avi.hide();
//                            }
                        }
                        Log.d(TAG, "fetchStatusFromFirebaseExplore: ISDONE " + isDone.size());
                        if (finalI == mSelectedSubInterests.size() - 1 && isDone.size() > 0) {
                            statusHashMap = sortByComparator(isDone, false);
                            Log.d(TAG, "fetchStatusFromFirebaseExplore: STATUS" + statusHashMap);
                            Log.d(TAG, "fetchStatusFromFirebaseExplore: STATUS" + statusHashMapStore);
                            for (int i = 0; i < statusHashMap.size(); i++) {
                                if (!items.contains(statusHashMapStore.get(statusHashMap.keySet().toArray()[i]))) {
                                    items.add(statusHashMapStore.get(statusHashMap.keySet().toArray()[i]));
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                    }
                });
//                Log.d(TAG, "fetchStatusFromFirebaseExplore: ISDONE "+isDone.size());

            }
//            Log.d(TAG, "fetchStatusFromFirebaseExplore: ISDONE "+isDone.size());

        }
        Log.d(TAG, "fetchStatusFromFirebaseExplore: " + items.size());
        mAdapter.setItems(items);
        mAdapter.notifyDataSetChanged();
//        if(mAdapter.getItemCount()>0){
//            avi.hide();
//        }
    }

    private static HashMap<String, Long> sortByComparator(HashMap<String, Long> unsortMap, final boolean order) {

        List<HashMap.Entry<String, Long>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<HashMap.Entry<String, Long>>() {
            public int compare(HashMap.Entry<String, Long> o1,
                               HashMap.Entry<String, Long> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        HashMap<String, Long> sortedMap = new LinkedHashMap<>();
        for (HashMap.Entry<String, Long> entry : list) {
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
