package testsuite.model.article;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class MArticles {
    @JsonProperty("articles")
    private List<MArticle> articles = new ArrayList<>();
    @JsonProperty("articlesCount")
    private int articlesCount;

    public int getArticlesCount() {
        return articlesCount;
    }

    public void setArticlesCount(int articlesCount) {
        this.articlesCount = articlesCount;
    }

    public List<MArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<MArticle> articles) {
        this.articles = articles;
    }
}
