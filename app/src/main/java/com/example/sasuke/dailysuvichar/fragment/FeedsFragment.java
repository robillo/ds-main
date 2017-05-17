package com.example.sasuke.dailysuvichar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.example.sasuke.dailysuvichar.R;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import butterknife.BindView;

public class FeedsFragment extends BaseFragment {

    @NotEmpty
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.edittext)
    EditText editText;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_gurus;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static FeedsFragment newInstance() {
        return new FeedsFragment();
    }
}
