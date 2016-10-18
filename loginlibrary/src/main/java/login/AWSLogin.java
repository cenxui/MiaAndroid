package login;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.regions.Regions;
import com.facebook.AccessToken;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Cenxui on 10/16/16.
 */
class AWSLogin extends Login {
    private final CognitoCachingCredentialsProvider provider;
    private final LoginHandler handler;

    private CognitoSyncManager syncClient;

    private final String identityPoolID;

    private final Regions regions;

    AWSLogin(final AppCompatActivity activity , String identityPoolID, String regions) {
        super(activity);

        this.identityPoolID = identityPoolID;

        this.regions = Regions.fromName(regions);

        this.provider = new CognitoCachingCredentialsProvider(
                this.activity,    /* get the context for the application */
                this.identityPoolID,    /* Identity Pool ID */
                this.regions      /* Region for your identity pool--US_EAST_1 or EU_WEST_1*/
        );


        this.handler = new LoginHandler() {
            @Override
            public void onFBSuccess(LoginResult result) {
                Log.d("login", "Token  =  " + AccessToken.getCurrentAccessToken().getToken());

                Map<String, String> logins = new HashMap<>();
                logins.put("graph.facebook.com", AccessToken.getCurrentAccessToken().getToken());
                provider.setLogins(logins);
            }

            @Override
            public void onGoogleSuccess(GoogleSignInResult result) {

                Map<String, String> logins = new HashMap<String, String>();
                logins.put("accounts.google.com", result.getSignInAccount().getIdToken());
                provider.setLogins(logins);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onFBError(FacebookException e) {

            }

            @Override
            public void onGoogleError(ConnectionResult result) {

            }
        };

    }

    @Override
    protected LoginHandler getLoginHandler() {
        return this.handler;
    }

    @Override
    void storeUserData() {
         this.syncClient = new CognitoSyncManager (
                this.activity,
                this.regions, // Region
                this.provider);
        Log.d("login", "store user data");

        Dataset dataset = syncClient.openOrCreateDataset("myDataset");
        dataset.put("myKey", "myValue");
        dataset.synchronize(new DefaultSyncCallback() {
            @Override
            public void onSuccess(Dataset dataset, List newRecords) {
                Log.d("login", "success user data");
            }
        });
    }
}
