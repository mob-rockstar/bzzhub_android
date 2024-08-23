package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserProfileFeedResponse {
    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("Records")
    @Expose
    private Integer records;
    @SerializedName("CurrentPage")
    @Expose
    private Integer currentPage;
    @SerializedName("LimitPerPage")
    @Expose
    private String limitPerPage;
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
    private List<Result> result = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getRecords() {
        return records;
    }

    public void setRecords(Integer records) {
        this.records = records;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public String getLimitPerPage() {
        return limitPerPage;
    }

    public void setLimitPerPage(String limitPerPage) {
        this.limitPerPage = limitPerPage;
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

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("feed_id")
        @Expose
        private Integer feedId;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("flag")
        @Expose
        private Integer flag;
        @SerializedName("is_visible")
        @Expose
        private Integer isVisible;
        @SerializedName("created_date")
        @Expose
        private String createdDate;

        public Integer getFeedId() {
            return feedId;
        }

        public void setFeedId(Integer feedId) {
            this.feedId = feedId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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
