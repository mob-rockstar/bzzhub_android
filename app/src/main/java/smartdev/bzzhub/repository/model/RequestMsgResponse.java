package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestMsgResponse {
    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("Errors")
    @Expose
    private Object errors;
    @SerializedName("Code")
    @Expose
    private Integer code;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Result")
    @Expose
    private Result result;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("pending")
        @Expose
        private List<UserProfileResponse.Friend> pending = null;
        @SerializedName("sent")
        @Expose
        private List<UserProfileResponse.Friend> sent = null;
        @SerializedName("suggestion")
        @Expose
        private List<UserProfileResponse.Friend> suggestion = null;

        public List<UserProfileResponse.Friend> getPending() {
            return pending;
        }

        public void setPending(List<UserProfileResponse.Friend> pending) {
            this.pending = pending;
        }

        public List<UserProfileResponse.Friend> getSent() {
            return sent;
        }

        public void setSent(List<UserProfileResponse.Friend> sent) {
            this.sent = sent;
        }

        public List<UserProfileResponse.Friend> getSuggestion() {
            return suggestion;
        }

        public void setSuggestion(List<UserProfileResponse.Friend> suggestion) {
            this.suggestion = suggestion;
        }

    }


}
