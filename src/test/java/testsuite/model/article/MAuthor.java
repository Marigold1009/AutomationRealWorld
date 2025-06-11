package testsuite.model.article;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MAuthor {
    @JsonProperty("username")
    private String username;
    @JsonProperty("bio")
    private String bio;
    @JsonProperty("image")
    private String image;
    @JsonProperty("following")
    private boolean following;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
