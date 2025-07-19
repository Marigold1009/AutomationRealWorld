package model.profiles;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MProfile {
    @JsonProperty("profile")
    private MProfilesDetail profilesDetail;

    public MProfilesDetail getProfilesDetail() {
        return profilesDetail;
    }

    public void setProfilesDetail(MProfilesDetail profilesDetail) {
        this.profilesDetail = profilesDetail;
    }
}
