package gevorgyan.vahan.newsfeed.remote;

import java.io.IOException;

import gevorgyan.vahan.newsfeed.util.Constants;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class NewsfeedInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();

        HttpUrl url = originalHttpUrl.newBuilder()
                .addQueryParameter("api-key", Constants.API_KEY2)
                .addQueryParameter("format", Constants.RESPONSE_FORMAT)
                .build();

        // Request customization: add request headers
        Request.Builder requestBuilder = original.newBuilder().url(url);

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
