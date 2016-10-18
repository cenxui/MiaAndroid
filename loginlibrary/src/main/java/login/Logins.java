package login;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Cenxui on 10/16/16.
 */
public class Logins {
    private static AWSLogin awsLogin;

    private static GoogleLogin googleLogin;

    public static Login newAWSCognitoLogin(AppCompatActivity activity,
                                           String identityPoolID,
                                           String regions) {
       if (awsLogin == null)  {
           Logins.awsLogin = new AWSLogin(activity, identityPoolID, regions);
       }
        return Logins.awsLogin;
    }

    public static Login newGoogleFirbaseLogin() {
        return null;
    }

}
