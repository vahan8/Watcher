package gevorgyan.vahan.newsfeed.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import gevorgyan.vahan.newsfeed.domain.model.Article;
import gevorgyan.vahan.newsfeed.domain.model.SearchArticle;

public class SearchQuery {

    @Expose
    @SerializedName("status")
    private String status;

    @Expose
    @SerializedName("total")
    private int total;

    @Expose
    @SerializedName("pageSize")
    private int pageSize;

    @Expose
    @SerializedName("currentPage")
    private int currentPage;

    @Expose
    @SerializedName("pages")
    private int pages;

    @Expose
    @SerializedName("orderBy")
    private String orderBy;

    @Expose
    @SerializedName("results")
    private List<SearchArticle> searchArticles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public List<SearchArticle> getSearchArticles() {
        return searchArticles;
    }

    public void setSearchArticles(List<SearchArticle> searchArticles) {
        this.searchArticles = searchArticles;
    }

    public List<Article> getArticles(){
        List<Article> result = new ArrayList<>();
        for(SearchArticle searchArticle:searchArticles){
            String thumbnailUrl =  searchArticle.getFields() ==null ? "" : searchArticle.getFields().getThumbnailUrl();
            Article article = new Article(searchArticle.getId(), searchArticle.getSectionId(), searchArticle.getSectionName(),
                    searchArticle.getWebPublicationDate(), searchArticle.getWebTitle(),searchArticle.getWebUrl(), searchArticle.getApiUrl(),
                    thumbnailUrl);
            result.add(article);
        }
        return result;
    }
}
