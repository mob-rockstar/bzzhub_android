package smartdev.bzzhub.util.networkutil;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtils {
    private static final int MAX_TRY_COUNT = 2;

    private static Interceptor retryInterceptor = chain -> {
        Request request = chain.request();
        int tryCount = 0;
        Response response = null;
        do {
            try {
                response = chain.proceed(request);
            } catch (IOException e) {
                try {
                    int pow = Double.valueOf(Math.pow(5, tryCount)).intValue();
                    Thread.sleep(pow * 1000);
                } catch (InterruptedException ex) {
                    //NOP
                }
                tryCount++;
            }
        } while (response == null && tryCount < MAX_TRY_COUNT);
        try {
            //  if (response == null)
            //       response = new Response.Builder().code(500).message("Server is not responding").build();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return response;
    };

    public static Retrofit getRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClientBuilder())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .build();
    }

    private static OkHttpClient okHttpClientBuilder() {
        final int CONNECT_TIMEOUT_SECOND = 60;
        final int READ_TIMEOUT_SECONDS = 60;
        final OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT_SECOND, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .addInterceptor(new StethoInterceptor())
                .retryOnConnectionFailure(true);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(loggingInterceptor);
        client.addInterceptor(new StethoInterceptor());
        client.addInterceptor(new ConnectivityInterceptor());
        client.addInterceptor(retryInterceptor);
        return client.build();
    }
}
