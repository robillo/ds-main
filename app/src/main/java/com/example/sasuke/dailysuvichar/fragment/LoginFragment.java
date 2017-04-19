package com.example.sasuke.dailysuvichar.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.activity.HomeActivity;
import com.example.sasuke.dailysuvichar.utils.ValidationListener;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Sasuke on 4/17/2017.
 */

public class LoginFragment extends BaseFragment {

    @NotEmpty
    @Email
    @BindView(R.id.et_email)
    EditText mEtEmail;
    @NotEmpty
    @BindView(R.id.et_password)
    EditText mEtPassword;

    private Validator validator;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_login;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        validator = new Validator(this);
        validator.setValidationListener(new ValidationListener() {

            @Override
            public Context getContext() {
                return getActivity();
            }

            @Override
            public void onValidationSucceeded() {
                String email = getStringFromEditText(mEtEmail);
                String password = getStringFromEditText(mEtPassword);
                Toast.makeText(getContext(), getResources().getString(R.string.validation_successful), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @OnClick(R.id.btn_login)
    public void login() {
        startActivity(HomeActivity.newIntent(getContext()));
    }
}
