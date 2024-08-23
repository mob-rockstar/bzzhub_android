package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompanyMediaResponse {
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
    private List<Video> result;

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

    public List<Video> getResult() {
        return result;
    }

    public void setResult(List<Video> result) {
        this.result = result;
    }

    public class Video {

        @SerializedName("media_id")
        @Expose
        private Integer mediaId;
        @SerializedName("user_or_company_id")
        @Expose
        private Integer userOrCompanyId;
        @SerializedName("path")
        @Expose
        private String path;
        @SerializedName("thumbnail")
        @Expose
        private String thumbnail;
        @SerializedName("search_keywords")
        @Expose
        private String searchKeywords;
        @SerializedName("is_img_or_video")
        @Expose
        private Integer isImgOrVideo;
        @SerializedName("flag")
        @Expose
        private Integer flag;
        @SerializedName("is_private")
        @Expose
        private Integer isPrivate;
        @SerializedName("created_date")
        @Expose
        private String createdDate;

        public Integer getMediaId() {
            return mediaId;
        }

        public void setMediaId(Integer mediaId) {
            this.mediaId = mediaId;
        }

        public Integer getUserOrCompanyId() {
            return userOrCompanyId;
        }

        public void setUserOrCompanyId(Integer userOrCompanyId) {
            this.userOrCompanyId = userOrCompanyId;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getSearchKeywords() {
            return searchKeywords;
        }

        public void setSearchKeywords(String searchKeywords) {
            this.searchKeywords = searchKeywords;
        }

        public Integer getIsImgOrVideo() {
            return isImgOrVideo;
        }

        public void setIsImgOrVideo(Integer isImgOrVideo) {
            this.isImgOrVideo = isImgOrVideo;
        }

        public Integer getFlag() {
            return flag;
        }

        public void setFlag(Integer flag) {
            this.flag = flag;
        }

        public Integer getIsPrivate() {
            return isPrivate;
        }

        public void setIsPrivate(Integer isPrivate) {
            this.isPrivate = isPrivate;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getThumbnail() {
            return thumbnail;
        }
    }
}
