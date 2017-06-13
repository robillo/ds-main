package com.robillo.sasuke.dailysuvichar.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.robillo.sasuke.dailysuvichar.R;
import com.robillo.sasuke.dailysuvichar.models.UserAuth;
import com.robillo.sasuke.dailysuvichar.utils.ValidationListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by rishabhshukla on 19/04/17.
 */

public class RegisterFragment extends BaseFragment {

    @NotEmpty
    @Email
    @BindView(R.id.et_email)
    EditText mEtEmail;
    @NotEmpty
    @Password
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @NotEmpty
    @ConfirmPassword
    @BindView(R.id.et_password_again)
    EditText mEtPasswordAgain;

    private DatabaseReference databaseReference;
    private FirebaseAuth mFirebaseAuth;
    private Validator validator;
    private MaterialDialog mDialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_register;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        validator = new Validator(this);
        setupDialog();
        validator.setValidationListener(new ValidationListener() {

            @Override
            public Context getContext() {
                return getActivity();
            }

            @Override
            public void onValidationSucceeded() {
                signUp();
            }
        });
    }

    @OnClick(R.id.btn_login)
    public void login() {
        getFragmentManager().popBackStackImmediate();
    }

    @OnClick(R.id.btn_register)
    public void register() {
        validator.validate();
    }


    private void signUp() {
        showDialog("", getResources().getString(R.string.please_wait));
        String email = getStringFromEditText(mEtEmail);
        String password = getStringFromEditText(mEtPassword);
        String passwordAgain = getStringFromEditText(mEtPasswordAgain);
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            dismissDialog();
                            Toast.makeText(getContext(), getResources().getString(R.string.sign_up_failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());
        Toast.makeText(getContext(), getResources().getString(R.string.sign_up_successful), Toast.LENGTH_SHORT).show();
        login();

    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void writeNewUser(String userId, String name, String email) {
        UserAuth user = new UserAuth(email, name);
        databaseReference.child("users").child(userId).setValue(user);
        dismissDialog();
    }


    public void setupDialog() {
        mDialog = new MaterialDialog.Builder(getContext())
                .progress(true, 0)
                .build();
    }

    public void showDialog(String title, String content) {
        mDialog.setTitle(title);
        mDialog.setContent(content);
        mDialog.show();
    }

    public void dismissDialog() {
        if (mDialog.isShowing())
            mDialog.dismiss();
    }

}
