package gevorgyan.vahan.newsfeed.remote;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import gevorgyan.vahan.newsfeed.domain.model.SearchQuery;
import gevorgyan.vahan.newsfeed.domain.model.SearchQueryResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Api {

    private static Call<SearchQueryResponse> getArticlesCall(int page) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String thumbnail = "thumbnail";
        List<String> showFields = new ArrayList<>();
        showFields.add(thumbnail);
        return apiInterface.getArticles(page, showFields);
    }

    public static void downloadArticles(int page, final RequestCallbacks callbacks) {
        Call<SearchQueryResponse> call = getArticlesCall(page);
        call.enqueue(new Callback<SearchQueryResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchQueryResponse> call, @NonNull Response<SearchQueryResponse> response) {
                SearchQueryResponse searchQueryResponse = response.body();
                if (callbacks != null) {
                    if (response.isSuccessful()) {
                        callbacks.onSuccess(searchQueryResponse);
                    } else {
                        callbacks.onFailure();
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchQueryResponse> call, Throwable t) {
                if (callbacks != null) {
                    callbacks.onFailure();
                }
            }
        });
    }

}
