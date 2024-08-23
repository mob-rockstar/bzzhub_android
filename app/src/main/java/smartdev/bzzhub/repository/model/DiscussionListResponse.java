package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DiscussionListResponse {
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

        @SerializedName("discussion_id")
        @Expose
        private Integer discussionId;
        @SerializedName("community_id")
        @Expose
        private Integer communityId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("user_or_company")
        @Expose
        private Integer userOrCompany;
        @SerializedName("flag")
        @Expose
        private Integer flag;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("community")
        @Expose
        private String community;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("image")
        @Expose
        private String image;

        public Integer getDiscussionId() {
            return discussionId;
        }

        public void setDiscussionId(Integer discussionId) {
            this.discussionId = discussionId;
        }

        public Integer getCommunityId() {
            return communityId;
        }

        public void setCommunityId(Integer communityId) {
            this.communityId = communityId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getUserOrCompany() {
            return userOrCompany;
        }

        public void setUserOrCompany(Integer userOrCompany) {
            this.userOrCompany = userOrCompany;
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

        public String getCommunity() {
            return community;
        }

        public void setCommunity(String community) {
            this.community = community;
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
