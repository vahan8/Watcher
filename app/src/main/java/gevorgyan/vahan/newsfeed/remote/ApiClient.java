package gevorgyan.vahan.newsfeed.remote;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static Retrofit getClient() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        Interceptor newsfeedInterceptor = new NewsfeedInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(new StethoInterceptor()).
                addInterceptor(loggingInterceptor).addInterceptor(newsfeedInterceptor).build();

        HttpUrl baseUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("content.guardianapis.com")
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .callFactory(client)
                .client(client)
                .build();
        return retrofit;
    }
}
