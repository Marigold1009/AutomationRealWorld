package testsuite.model.article;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MErrorResponse {
    @JsonProperty("errors")
    private List<MErrorDetails> errors;

    public List<MErrorDetails> getErrors() {
        return errors;
    }

    public void setErrors(List<MErrorDetails> errors) {
        this.errors = errors;
    }
}
