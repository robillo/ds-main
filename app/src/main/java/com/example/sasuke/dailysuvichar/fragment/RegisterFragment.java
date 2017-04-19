package com.example.sasuke.dailysuvichar.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.UserAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by rishabhshukla on 19/04/17.
 */

public class RegisterFragment extends BaseFragment {


    private static final String TAG = "REGISTER";
    @NotEmpty
    @Email
    @BindView(R.id.et_email)
    EditText mEtEmail;
    @NotEmpty
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @NotEmpty
    @BindView(R.id.et_password_again)
    EditText mEtPasswordAgain;

    private DatabaseReference databaseReference;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_register;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();

    }
    @OnClick(R.id.btn_login)
    public void login(){
        getFragmentManager().popBackStackImmediate();
    }
    @OnClick(R.id.btn_register)
    public void register(){
        signUp();
    }


    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEtEmail.getText().toString())) {
            mEtEmail.setError("Required");
            result = false;
        } else {
            mEtEmail.setError(null);
        }

        if (TextUtils.isEmpty(mEtPassword.getText().toString())) {
            mEtPassword.setError("Required");
            result = false;
        } else {
            mEtPassword.setError(null);
        }
        if (TextUtils.isEmpty(mEtPasswordAgain.getText().toString())) {
            mEtPasswordAgain.setError("Required");
            result = false;
        } else {
            mEtPasswordAgain.setError(null);
        }

        return result;
    }

    private void signUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }

        String email = mEtEmail.getText().toString();
        String password = mEtPassword.getText().toString();
        String passwordAgain = mEtPasswordAgain.getText().toString();

        if(!isEmailValid(email)){
            mEtEmail.setError(getResources().getString(R.string.invalid_email));
            return;
        }
        if(!isPasswordValid(password)){
            mEtPassword.setError(getResources().getString(R.string.invalid_password));
            return;
        }
        if(!password.toString().equals(passwordAgain.toString())){
            mEtPasswordAgain.setError(getResources().getString(R.string.invalid_password_again));
            return;
        }
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(getContext(),getResources().getString(R.string.signup_failed),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());
        Toast.makeText(getContext(), "Sign Up Successful!", Toast.LENGTH_SHORT).show();
        login();

//        Intent i = new Intent(LoginActivity.this, AddressActivity.class);
//        i.putExtra("email",user.getEmail());
//        startActivity(i);

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
    }

}
