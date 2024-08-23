package smartdev.bzzhub.repository.facebook;


import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.google.gson.GsonBuilder;

import io.reactivex.Single;

import static com.facebook.GraphRequest.newMeRequest;

public class FacebookAPI {
    public static Single<FacebookUser> getUser() {
        Single<FacebookUser> single = Single.create(e -> {
            GraphRequest request = newMeRequest(AccessToken.getCurrentAccessToken(),
                    (object, response) -> e.onSuccess(new GsonBuilder().create().fromJson(object.toString(), FacebookUser.class)));
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,email,first_name,last_name,gender");
            request.setParameters(parameters);
            request.executeAndWait();
        });
        return single;
    }
}