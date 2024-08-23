package smartdev.bzzhub.repository.facebook;


import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FacebookIO {
    public static Single<FacebookUser> getUser() {
        return FacebookAPI.getUser().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
