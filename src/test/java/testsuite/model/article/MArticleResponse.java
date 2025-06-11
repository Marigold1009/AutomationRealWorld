package testsuite.model.article;

import java.util.List;

public class MArticleResponse {
    private List<MArticle> articles;
    private int articlesCount;

    public List<MArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<MArticle> articles) {
        this.articles = articles;
    }

    public int getArticlesCount() {
        return articlesCount;
    }

    public void setArticlesCount(int articlesCount) {
        this.articlesCount = articlesCount;
    }
}
