package testsuite.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MUserLoginError {
    @JsonProperty("errors")
    private List<MUserLoginErrorDetail> errors;

    public List<MUserLoginErrorDetail> getUserLoginErrors() {
        return errors;
    }

    public void setUserLoginErrors(List<MUserLoginErrorDetail> userLoginErrors) {
        this.errors = userLoginErrors;
    }
}
