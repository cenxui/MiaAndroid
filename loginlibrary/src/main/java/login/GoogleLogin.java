package login;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Cenxui on 10/16/16.
 */
class GoogleLogin extends Login{

    GoogleLogin(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    protected LoginHandler getLoginHandler() {
        return null;
    }

    @Override
    void storeUserData() {

    }
}
