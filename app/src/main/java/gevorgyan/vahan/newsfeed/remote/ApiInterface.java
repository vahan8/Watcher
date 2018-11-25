package gevorgyan.vahan.newsfeed.remote;

import java.util.List;

import gevorgyan.vahan.newsfeed.domain.model.SearchQueryResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    @GET("search")
    Call<SearchQueryResponse> getArticles(@Query("page") int page, @Query("show-fields") List<String> showFields);

    @GET
    Call<SearchQueryResponse> getArticle(@Url String url);
}


