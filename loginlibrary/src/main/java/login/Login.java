package login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Cenxui on 10/16/16.
 */
abstract public class Login {
    private static final String name = "Login";

    protected final AppCompatActivity activity;

    private final int RC_SIGN_IN = 2331856;

    private CallbackManager callback;

    private Permission permission;

    private GoogleSignInOptions gso;

    private GoogleApiClient googleApiClient;

    private LoginHandler handler;

    protected Login (@NonNull AppCompatActivity activity) {
        this.activity = activity;
    }

    public void onCreate(final LoginHandler loginHandler, Permission permission) {
        FacebookSdk.sdkInitialize(this.activity);

        this.callback = CallbackManager.Factory.create();

        this.permission = permission;

        this.handler = loginHandler;

        LoginManager.getInstance().registerCallback(this.callback,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(Login.name, "success");
                        getLoginHandler().onFBSuccess(loginResult);

                        if (handler != null) {
                            handler.onFBSuccess(loginResult);
                        }
                    }

                    @Override
                    public void onCancel() {
                        Log.d(Login.name, "cancel");
                        getLoginHandler().onCancel();
                        if (handler != null) {
                            handler.onCancel();
                        }
                    }

                    @Override
                    public void onError(FacebookException e) {
                        Log.d(Login.name, "error");
                        getLoginHandler().onFBError(e);
                        if (handler != null) {
                            handler.onFBError(e);
                        }
                    }
                });

        this.gso = permission.builder
                .requestIdToken(getString(R.string.server_client_id))
                .build();
        this.googleApiClient = new GoogleApiClient.Builder(this.activity)
                .enableAutoManage(this.activity, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        getLoginHandler().onGoogleError(connectionResult);
                        if (handler != null) {
                            handler.onGoogleError(connectionResult);
                        }
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
    }



    abstract protected LoginHandler getLoginHandler();

    abstract void storeUserData();

    public void logOutFB() {
        LoginManager.getInstance().logOut();
    }

    public void loginFB() {
        LoginManager.getInstance().logInWithReadPermissions(this.activity, permission.FBp);
    }

    public void loginGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(this.googleApiClient);
        this.activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    // google log out
    public void logOutGoogle() {

    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        onFaceBookActivityResult(requestCode, resultCode, data);
        onGoogleActivityResult(requestCode, data);
    }

    private void onFaceBookActivityResult(int requestCode, int resultCode, Intent data) {
        this.callback.onActivityResult(requestCode, resultCode, data);
    }

    private void onGoogleActivityResult(int requestCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                getLoginHandler().onGoogleSuccess(result);
                if (handler != null) {
                    handler.onGoogleSuccess(result);
                }

            } else {
                getLoginHandler().onCancel();
                if (handler != null) {
                    handler.onCancel();
                }
            }
        }
    }

    enum Pool {
        AWS, Google;
    }
}
