package com.example.sasuke.dailysuvichar.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.activity.HomeActivity;
import com.example.sasuke.dailysuvichar.activity.MainActivity;
import com.example.sasuke.dailysuvichar.utils.ValidationListener;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import butterknife.BindView;
import butterknife.OnClick;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Sasuke on 4/17/2017.
 */

public class LoginFragment extends BaseFragment implements GoogleApiClient.OnConnectionFailedListener{

    @NotEmpty
    @Email
    @BindView(R.id.et_email)
    EditText mEtEmail;
    @NotEmpty
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @NotEmpty
    @BindView(R.id.google_sign_in)
    SignInButton googleSignIn;
//    @NotEmpty
//    @BindView(R.id.facebook_sign_in)
//    LoginButton facebooSignIn;

    private Validator validator;
    private static final int RC_GSIGN_IN = 9001;
    private static final String TAG = "STATUS";
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_login;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

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

    @OnClick(R.id.google_sign_in)
    public void signIn(){
        Intent i = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(i, RC_GSIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), "PLAY SERVICES ERROR.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_GSIGN_IN){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(googleSignInResult.isSuccess()){
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = googleSignInResult.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
        else {
            // Pass the activity result back to the Facebook SDK
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.e("NEWS", "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("NEWS", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("NEWS", "signInWithCredential", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(getActivity(), HomeActivity.class));
                            getActivity().finish();
                        }
                    }
                });
    }
}
