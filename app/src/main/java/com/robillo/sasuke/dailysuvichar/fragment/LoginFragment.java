package com.robillo.sasuke.dailysuvichar.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.internal.FetchedAppSettings;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.robillo.sasuke.dailysuvichar.R;
import com.robillo.sasuke.dailysuvichar.activity.ProfileActivity;
import com.robillo.sasuke.dailysuvichar.utils.SharedPrefs;
import com.robillo.sasuke.dailysuvichar.utils.ValidationListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment implements GoogleApiClient.OnConnectionFailedListener {


    private String currentPackage;
    private String expectedChallenge;

    @NotEmpty
    @Email
//    @BindView(R.id.et_email)
//    EditText mEtEmail;
//    @NotEmpty
//    @BindView(R.id.et_password)
//    EditText mEtPassword;
//    @BindView(R.id.google_sign_in)
//    SignInButton googleSignIn;
    @BindView(R.id.fb)
    LoginButton facebookSignIn;

    private Validator validator;
//    private static final int RC_GSIGN_IN = 9001;
//    private GoogleApiClient mGoogleApiClient;
    private static final int RC_FB_SIGN_IN = 9002;
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

//        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mFirebaseAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        // Add code to print out the key hash
        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(
                    "com.robillo.sasuke.dailysuvichar",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
//        if(SharedPrefs.getIsLoggedIn()!=null){
//            if(SharedPrefs.getIsLoggedIn().equals("TRUE")){
//                startActivity(new Intent(getActivity(), ProfileActivity.class));
//            }
//        }

//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail().build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .enableAutoManage(getActivity(), this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
        setupDialog();
        validator = new Validator(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        googleSignIn.setSize(SignInButton.SIZE_WIDE);
//        googleSignIn.setColorScheme(SignInButton.COLOR_AUTO);

        validator.setValidationListener(new ValidationListener() {

            @Override
            public Context getContext() {
                return getActivity();
            }

            @Override
            public void onValidationSucceeded() {
//                FirebaseSignIn();
            }
        });

    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

//    @OnClick(R.id.btn_register)
//    public void register() {
//        RegisterFragment registerFragment = new RegisterFragment();
//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container, registerFragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }

//    @OnClick(R.id.btn_login)
//    public void login() {
//        validator.validate();
//    }
//
//    private void FirebaseSignIn() {
//        showDialog("", getResources().getString(R.string.please_wait));
//        String email = getStringFromEditText(mEtEmail);
//        String password = getStringFromEditText(mEtPassword);
//        mFirebaseAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            onAuthSuccess(task.getResult().getUser());
//                            SharedPrefs.setLoginToken(task.getResult().getUser().getToken(true).toString());
//                        } else {
//                            dismissDialog();
//                            Toast.makeText(getContext(), getResources().getString(R.string.sign_in_failed), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

//    private void onAuthSuccess(FirebaseUser user) {
//        String username = usernameFromEmail(user.getEmail());
//        // Write new user
//        writeNewUser(user.getUid(), username, user.getEmail());
//        Intent i = new Intent(getContext(), ProfileActivity.class);
//        i.putExtra("fromLogin", 1);
//        startActivity(i);
////        startActivity(ChooseInterestActivity.newIntent(getContext()));
//        dismissDialog();
//    }

//    private String usernameFromEmail(String email) {
//        if (email.contains("@")) {
//            return email.split("@")[0];
//        } else {
//            return email;
//        }
//    }

//    private void writeNewUser(String userId, String name, String email) {
//        UserAuth user = new UserAuth(email, name);
//        databaseReference.child("users").child(userId).setValue(user);
//    }

//    @OnClick(R.id.google_sign_in)
//    public void signIn() {
//        Intent i = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(i, RC_GSIGN_IN);
//    }

    @OnClick(R.id.fb)
    public void signInFB() {
        facebookSignIn.setReadPermissions("public_profile", "email", "user_friends");
        facebookSignIn.setFragment(this);
        facebookSignIn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String w = loginResult.getAccessToken().getToken();
                Log.e("FID", w);
                Log.d("LOG", "facebook:onSuccess:" + loginResult + w);
                SharedPrefs.setFacebookToken(w);
                showDialog("", getResources().getString(R.string.please_wait));
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity().getApplicationContext(), R.string.cancelled, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.exception, Toast.LENGTH_SHORT).show();
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

//        if (requestCode == RC_GSIGN_IN) {
//            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            if (googleSignInResult.isSuccess()) {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = googleSignInResult.getSignInAccount();
//                firebaseAuthWithGoogle(account);
//            }else{
//                Log.d(TAG, "onActivityResult: failed "+googleSignInResult.getStatus());
//            }
//        } else {
//            Log.d(TAG, "onActivityResult: ");
//            // Pass the activity result back to the Facebook SDK
//            callbackManager.onActivityResult(requestCode, resultCode, data);
//        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

//    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        mFirebaseAuth.signInWithCredential(credential)
//                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.d(TAG, "onComplete: failed");
//                            Toast.makeText(getActivity(), getResources().getString(R.string.authentication_failed),
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            Log.d(TAG, "onComplete: passed");
//                            SharedPrefs.setLoginToken(acct.getIdToken());
//                            Intent i = new Intent(getContext(), ProfileActivity.class);
//                            i.putExtra("fromLogin", 1);
//                            startActivity(i);
////                            startActivity(ChooseInterestActivity.newIntent(getContext()));
//                            getActivity().finish();
//                        }
//                    }
//                });
//    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("HANDLING", "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("SUCCESS?   ", "NO");
                            Log.w("EXCEPTION", "signInWithCredential:failure", task.getException());
                            dismissDialog();
                            Toast.makeText(getActivity().getApplicationContext(), R.string.authfailed, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("SUCCESS?   ", "YES");
                            Intent i = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
                            i.putExtra("fromLogin", 0);
                            SharedPrefs.setIsLoggedIn("TRUE");
                            startActivity(i);
                            dismissDialog();
                            Toast.makeText(getActivity().getApplicationContext(), R.string.authsuccess, Toast.LENGTH_SHORT).show();
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
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser!=null){
            Intent i = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
            i.putExtra("fromLogin", 0);
            startActivity(i);
        }
    }
}
