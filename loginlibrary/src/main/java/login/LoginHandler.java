package login;

import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;

/**
 * Created by Cenxui on 10/16/16.
 */
public interface LoginHandler {
    void onFBSuccess(LoginResult result);
    void onGoogleSuccess(GoogleSignInResult result);
    void onCancel();
    void onFBError(FacebookException e);
    void onGoogleError(ConnectionResult result);
}
