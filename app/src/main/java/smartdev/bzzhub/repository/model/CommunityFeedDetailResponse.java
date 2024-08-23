package smartdev.bzzhub.repository.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommunityFeedDetailResponse {

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
    private Result result;

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

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("new_feeds_id")
        @Expose
        private Integer newFeedsId;
        @SerializedName("community_id")
        @Expose
        private Integer communityId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("user_or_company_id")
        @Expose
        private Integer userOrCompanyId;
        @SerializedName("flag")
        @Expose
        private Integer flag;
        @SerializedName("created_date")
        @Expose
        private String createdDate;

        public Integer getNewFeedsId() {
            return newFeedsId;
        }

        public void setNewFeedsId(Integer newFeedsId) {
            this.newFeedsId = newFeedsId;
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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

    }

}
