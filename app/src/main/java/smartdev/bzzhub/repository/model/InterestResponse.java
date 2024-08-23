package smartdev.bzzhub.repository.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InterestResponse {@SerializedName("Status")
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
    private List<Result> result = null;

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

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }
    public static class Result {

        public Result(Integer interestId, String interest) {
            this.interestId = interestId;
            this.interest = interest;
        }

        @SerializedName("interest_id")
        @Expose
        private Integer interestId;
        @SerializedName("interest")
        @Expose
        private String interest;
        private boolean isSelected;

        public Integer getInterestId() {
            return interestId;
        }

        public void setInterestId(Integer interestId) {
            this.interestId = interestId;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        @NonNull
        @Override
        public String toString() {
            return interest;
        }
    }
}