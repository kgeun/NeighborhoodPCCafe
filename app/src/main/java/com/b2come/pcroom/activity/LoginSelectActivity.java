package com.b2come.pcroom.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.b2come.pcroom.R;
import com.b2come.pcroom.applicationclass.Util;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.ParseException;

/**
 * Created by kgeun on 2017. 1. 24..
 */

public class LoginSelectActivity extends Activity {

    CallbackManager callbackManager;
    LoginButton loginButton;
    Context mContext;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginselect);

        mContext = this;
        mAuth = FirebaseAuth.getInstance();

        loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        //AccessToken mFacebookAccessToken = loginResult.getAccessToken();
                        //Util.setAppPreferences(LoginSelectActivity.this, "ACCESS_TOKEN", mFacebookAccessToken.getToken());

                        Log.v("result",object.toString());
                        try {
                            MainActivity.userName = object.getString("name");
                            MainActivity.userEmail = object.getString("email");
                            Util.setAppPreferences(LoginSelectActivity.this,"username",object.getString("name"));
                            Util.setAppPreferences(LoginSelectActivity.this,"useremail",object.getString("email"));
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                        finish();
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
                handleFacebookAccessToken(loginResult.getAccessToken());
                Util.setAppPreferences(LoginSelectActivity.this,"accesstoken",loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.e("LoginErr",error.toString());
            }


        });

        ImageView btnLoginExit = (ImageView)findViewById(R.id.btnLoginExit);
        btnLoginExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LinearLayout naverLoginButton = (LinearLayout)findViewById(R.id.naver_login_button);

        naverLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
                    @Override
                    public void run(boolean success) {
                        if (success) {
                            String accessToken = MainActivity.mOAuthLoginModule.getAccessToken(mContext);
                            String refreshToken = MainActivity.mOAuthLoginModule.getRefreshToken(mContext);
                            long expiresAt = MainActivity.mOAuthLoginModule.getExpiresAt(mContext);
                            String tokenType = MainActivity.mOAuthLoginModule.getTokenType(mContext);

                            /*
                            mOauthAT.setText(accessToken);
                            mOauthRT.setText(refreshToken);
                            mOauthExpires.setText(String.valueOf(expiresAt));
                            mOauthTokenType.setText(tokenType);
                            mOAuthState.setText(mOAuthLoginModule.getState(mContext).toString());*/

                            Util.setAppPreferences(LoginSelectActivity.this,"accesstoken",accessToken);

                            String header = "Bearer " + accessToken; // Bearer 다음에 공백 추가

                            AsyncHttpClient client = new AsyncHttpClient();
                            String webURL = "https://openapi.naver.com/v1/nid/me";
                            client.addHeader("Content-Type","application/xml");
                            client.addHeader("Authorization",header);

                            client.get(webURL, null, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    try {
                                        JSONObject object = response.getJSONObject("response");

                                        MainActivity.userName = object.getString("name");
                                        MainActivity.userEmail = object.getString("email");

                                        Util.setAppPreferences(LoginSelectActivity.this,"username",object.getString("name"));
                                        Util.setAppPreferences(LoginSelectActivity.this,"useremail",object.getString("email"));

                                    }
                                    catch(JSONException e){
                                        e.printStackTrace();
                                    }

                                }
                            });

                            finish();

                        } else {
                            String errorCode = MainActivity.mOAuthLoginModule.getLastErrorCode(mContext).getCode();
                            String errorDesc = MainActivity.mOAuthLoginModule.getLastErrorDesc(mContext);
                            Toast.makeText(mContext, "errorCode:" + errorCode
                                    + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                        }
                    };
                };

                MainActivity.mOAuthLoginModule.startOauthLoginActivity(LoginSelectActivity.this, mOAuthLoginHandler);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void handleFacebookAccessToken(AccessToken token) {
        Log.d("result", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("result", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("result", "signInWithCredential", task.getException());
                            Toast.makeText(LoginSelectActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


}

