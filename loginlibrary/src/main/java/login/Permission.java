package login;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cenxui on 10/17/16.
 */
public class Permission {
    List<String> FBp = new ArrayList<>();
    GoogleSignInOptions.Builder builder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN);
    private Permission() {

    }

    //unfinished
    public Permission requestEmail() {
        FBp.add("email");
        builder.requestEmail();
        return this;
    }
}
