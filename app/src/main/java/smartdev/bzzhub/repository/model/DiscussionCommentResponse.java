package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DiscussionCommentResponse {
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

        @SerializedName("reply_id")
        @Expose
        private Integer replyId;
        @SerializedName("discussion_id")
        @Expose
        private Integer discussionId;
        @SerializedName("post")
        @Expose
        private String post;
        @SerializedName("user_or_company_id")
        @Expose
        private Integer userOrCompanyId;
        @SerializedName("flag")
        @Expose
        private Integer flag;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("image")
        @Expose
        private String image;

        public Integer getReplyId() {
            return replyId;
        }

        public void setReplyId(Integer replyId) {
            this.replyId = replyId;
        }

        public Integer getDiscussionId() {
            return discussionId;
        }

        public void setDiscussionId(Integer discussionId) {
            this.discussionId = discussionId;
        }

        public String getPost() {
            return post;
        }

        public void setPost(String post) {
            this.post = post;
        }

        public Integer getUserOrCompanyId() {
            return userOrCompanyId;
        }

        public void setUserOrCompanyId(Integer userOrCompanyId) {
            this.userOrCompanyId = userOrCompanyId;
        }

        public Integer getFlag() {
            return flag;
        }

        public void setFlag(Integer flag) {
            this.flag = flag;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }


}
