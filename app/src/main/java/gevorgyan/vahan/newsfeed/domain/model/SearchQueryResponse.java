package gevorgyan.vahan.newsfeed.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchQueryResponse {

    @Expose
    @SerializedName("response")
    private SearchQuery response;

    public SearchQuery getResponse() {
        return response;
    }

    public void setResponse(SearchQuery response) {
        this.response = response;
    }
}
