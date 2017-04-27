package com.example.sasuke.dailysuvichar.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.activity.ChooseInterestActivity;
import com.example.sasuke.dailysuvichar.models.UserAuth;
import com.example.sasuke.dailysuvichar.utils.ValidationListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Sasuke on 4/17/2017.
 */

public class LoginFragment extends BaseFragment implements GoogleApiClient.OnConnectionFailedListener {

    @NotEmpty
    @Email
    @BindView(R.id.et_email)
    EditText mEtEmail;
    @NotEmpty
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.google_sign_in)
    SignInButton googleSignIn;
    @BindView(R.id.fb)
    LoginButton facebookSignIn;

    private Validator validator;
    private static final int RC_GSIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference databaseReference;
    private FirebaseAuth mFirebaseAuth;
    private CallbackManager callbackManager;
    private MaterialDialog mDialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_login;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        setupDialog();
        validator = new Validator(this);
        validator.setValidationListener(new ValidationListener() {

            @Override
            public Context getContext() {
                return getActivity();
            }

            @Override
            public void onValidationSucceeded() {
                FirebaseSignIn();
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

    @OnClick(R.id.btn_register)
    public void register() {
        RegisterFragment registerFragment = new RegisterFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, registerFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.btn_login)
    public void login() {
        validator.validate();
    }

    private void FirebaseSignIn() {
        showDialog("", getResources().getString(R.string.please_wait));
        String email = getStringFromEditText(mEtEmail);
        String password = getStringFromEditText(mEtPassword);
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            dismissDialog();
                            Toast.makeText(getContext(), getResources().getString(R.string.sign_in_failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());
        startActivity(ChooseInterestActivity.newIntent(getContext()));
        dismissDialog();

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

    @OnClick(R.id.google_sign_in)
    public void signIn() {
        Intent i = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(i, RC_GSIGN_IN);
    }

    @OnClick(R.id.fb)
    public void signInFB() {
        facebookSignIn.setFragment(this);
        facebookSignIn.setReadPermissions("public_profile", "email", "user_friends");
        facebookSignIn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), getResources().getString(R.string.play_services_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GSIGN_IN) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (googleSignInResult.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = googleSignInResult.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        } else {
            // Pass the activity result back to the Facebook SDK
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.authentication_failed),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(ChooseInterestActivity.newIntent(getContext()));
                            getActivity().finish();
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.authentication_failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void setupDialog() {
        mDialog = new MaterialDialog.Builder(getContext())
                .cancelable(false)
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

    @Override
    public void onPause() {
        super.onPause();
        if (mDialog != null)
            dismissDialog();
    }
}
