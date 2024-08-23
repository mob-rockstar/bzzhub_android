package smartdev.bzzhub.util.networkutil;

import smartdev.bzzhub.BzzApp;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectivityInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            if (!BzzApp.isOnline()) {
                throw new NoConnectivityException();
            } else {
                Request.Builder builder = chain.request().newBuilder();
                return chain.proceed(builder.build());
            }
        } catch (Exception e) {
            throw new NoConnectivityException();
        }

    }
}
