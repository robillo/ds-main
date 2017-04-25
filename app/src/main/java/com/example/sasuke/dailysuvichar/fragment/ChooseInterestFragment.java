package com.example.sasuke.dailysuvichar.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.activity.SubInterestActivity;
import com.example.sasuke.dailysuvichar.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Sasuke on 4/19/2017.
 */

public class ChooseInterestFragment extends BaseFragment implements BubblePickerListener {

    @BindView(R.id.picker)
    BubblePicker mPicker;

    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private static final int BUBBLE_COLOR = 0xffc11313;
    private static final int BUBBLE_SIZE = 15;
    private static final int TEXT_SIZE = 30;
    private static final int TEXT_COLOR = 0xffffffff;
    private static final float OVERLAY_ALPHA = 0.2f;
    private static final boolean ICON_ON_TOP = true;
    private static final BubbleGradient BUBBLE_GRADIENT = null; //ADD GRADIENT IF NEEDED IN FUTURE
    private HashMap<String,Boolean> allInterests, diet, yoga, health, religion, motivation, ayurveda, astrology;

    private ArrayList<PickerItem> mSelectedItems = new ArrayList<>();
    private ArrayList<String> mSelectedInterests;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_choose_interest;
    }

    public static ChooseInterestFragment newInstance() {
        return new ChooseInterestFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mSelectedInterests = new ArrayList<>();

        mPicker.setItems(getItems());
        mPicker.setBubbleSize(BUBBLE_SIZE);
        mPicker.setCenterImmediately(true);
        mPicker.setListener(this);

//        allInterests = getResources().getStringArray(R.array.allInterests);
//        diet = getResources().getStringArray(R.array.diet_array);
//        yoga = getResources().getStringArray(R.array.yoga_array);
//        health = getResources().getStringArray(R.array.health_array);
//        religion = getResources().getStringArray(R.array.religion_array);
//        motivation = getResources().getStringArray(R.array.motivation_array);
//        ayurveda = getResources().getStringArray(R.array.ayurveda_array);
//        astrology = getResources().getStringArray(R.array.astrology_array);
    }


    @Override
    public void onResume() {
        super.onResume();
        mPicker.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPicker.onPause();
    }

    @Override
    public void onBubbleSelected(@NotNull PickerItem pickerItem) {
        mSelectedItems.add(pickerItem);
        mSelectedInterests.add(pickerItem.getTitle().toLowerCase());
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBubbleDeselected(@NotNull PickerItem pickerItem) {
        mSelectedItems.remove(pickerItem);
        mSelectedInterests.remove(pickerItem.getTitle().toLowerCase());
    }

    @OnClick(R.id.tv_next)
    public void openSubInterestActivity() {

        User user = new User("Rishabh", "abcd@gmail.com",
                "Bio....", "EN",mSelectedInterests,"DP.JPG", "COVER.PNG",
                "02.01.95", "MALE", "9999999999", 22);
        mDatabase.child(mFirebaseUser.getUid()).setValue(user);
        startActivity(SubInterestActivity.newIntent(getActivity()));
    }

    public ArrayList<PickerItem> getItems() {
        ArrayList<PickerItem> itemList = new ArrayList<>();
        itemList.add(new PickerItem(getResources().getString(R.string.astrology), getResources().getDrawable(R.drawable.ic_astrology),
                ICON_ON_TOP, BUBBLE_COLOR, BUBBLE_GRADIENT, OVERLAY_ALPHA, Typeface.DEFAULT_BOLD, TEXT_COLOR,
                TEXT_SIZE, getResources().getDrawable(R.drawable.background)));

        itemList.add(new PickerItem(getResources().getString(R.string.diet), getResources().getDrawable(R.drawable.ic_diet),
                ICON_ON_TOP, BUBBLE_COLOR, BUBBLE_GRADIENT, OVERLAY_ALPHA, Typeface.DEFAULT_BOLD, TEXT_COLOR, TEXT_SIZE,
                getResources().getDrawable(R.drawable.background)));

        itemList.add(new PickerItem(getResources().getString(R.string.religion), getResources().getDrawable(R.drawable.ic_god),
                ICON_ON_TOP, BUBBLE_COLOR, BUBBLE_GRADIENT, OVERLAY_ALPHA, Typeface.DEFAULT_BOLD, TEXT_COLOR, TEXT_SIZE,
                getResources().getDrawable(R.drawable.background)));

        itemList.add(new PickerItem(getResources().getString(R.string.yoga), getResources().getDrawable(R.drawable.ic_yoga),
                ICON_ON_TOP, BUBBLE_COLOR, BUBBLE_GRADIENT, OVERLAY_ALPHA, Typeface.DEFAULT_BOLD, TEXT_COLOR,
                TEXT_SIZE, getResources().getDrawable(R.drawable.background)));

        itemList.add(new PickerItem(getResources().getString(R.string.motivation), getResources().getDrawable(R.drawable.ic_motivation),
                ICON_ON_TOP, BUBBLE_COLOR, BUBBLE_GRADIENT, OVERLAY_ALPHA, Typeface.DEFAULT_BOLD, TEXT_COLOR,
                TEXT_SIZE, getResources().getDrawable(R.drawable.background)));

        itemList.add(new PickerItem(getResources().getString(R.string.health), getResources().getDrawable(R.drawable.ic_health),
                ICON_ON_TOP, BUBBLE_COLOR, BUBBLE_GRADIENT, OVERLAY_ALPHA, Typeface.DEFAULT_BOLD, TEXT_COLOR,
                TEXT_SIZE, getResources().getDrawable(R.drawable.background)));

        itemList.add(new PickerItem(getResources().getString(R.string.ayurveda), getResources().getDrawable(R.drawable.ic_health),
                ICON_ON_TOP, BUBBLE_COLOR, BUBBLE_GRADIENT, OVERLAY_ALPHA, Typeface.DEFAULT_BOLD, TEXT_COLOR,
                TEXT_SIZE, getResources().getDrawable(R.drawable.background)));

        return itemList;
    }

    @OnClick(R.id.action_bar_home_icon)
    public void returnToPreviousActivity() {
        getActivity().finish();
    }
}
