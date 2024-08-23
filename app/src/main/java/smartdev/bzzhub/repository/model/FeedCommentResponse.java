package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FeedCommentResponse {

    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("Code")
    @Expose
    private Integer code;
    @SerializedName("Errors")
    @Expose
    private Object errors;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Result")
    @Expose
    private ArrayList<Result> result = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Result> getResult() {
        return result;
    }

    public void setResult(ArrayList<Result> result) {
        this.result = result;
    }
    public class Result {

        @SerializedName("comment_id")
        @Expose
        private Integer commentId;
        @SerializedName("feed_id")
        @Expose
        private Integer feedId;
        @SerializedName("user_or_company")
        @Expose
        private Integer userOrCompany;
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("flag")
        @Expose
        private Integer flag;
        @SerializedName("is_visible")
        @Expose
        private Integer isVisible;
        @SerializedName("created_date")
        @Expose
        private String createdDate;

        public Integer getCommentId() {
            return commentId;
        }

        public void setCommentId(Integer commentId) {
            this.commentId = commentId;
        }

        public Integer getFeedId() {
            return feedId;
        }

        public void setFeedId(Integer feedId) {
            this.feedId = feedId;
        }

        public Integer getUserOrCompany() {
            return userOrCompany;
        }

        public void setUserOrCompany(Integer userOrCompany) {
            this.userOrCompany = userOrCompany;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Integer getFlag() {
            return flag;
        }

        public void setFlag(Integer flag) {
            this.flag = flag;
        }

        public Integer getIsVisible() {
            return isVisible;
        }

        public void setIsVisible(Integer isVisible) {
            this.isVisible = isVisible;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

    }

}
