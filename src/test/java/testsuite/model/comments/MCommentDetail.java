package testsuite.model.comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import testsuite.model.article.MAuthor;

public class MCommentDetail {
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("updatedAt")
    private String updatedAt;
    @JsonProperty("id")
    private int id;
    @JsonProperty("body")
    private String body;
    @JsonProperty("author")
    private MAuthor author;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public MAuthor getAuthor() {
        return author;
    }

    public void setAuthor(MAuthor author) {
        this.author = author;
    }
}
