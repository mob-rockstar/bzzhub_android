package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CommunityImageResponse {
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

        @SerializedName("media_id")
        @Expose
        private Integer mediaId;
        @SerializedName("community_id")
        @Expose
        private Integer communityId;
        @SerializedName("user_or_company_id")
        @Expose
        private Integer userOrCompanyId;
        @SerializedName("path")
        @Expose
        private String path;
        @SerializedName("is_img_or_video")
        @Expose
        private Integer isImgOrVideo;
        @SerializedName("flag")
        @Expose
        private Integer flag;
        @SerializedName("created_date")
        @Expose
        private String createdDate;
        @SerializedName("thumbnail")
        @Expose
        private String thumbnail;
        @SerializedName("is_private")
        @Expose
        private Integer isPrivate;
        @SerializedName("search_keywords")
        @Expose
        private String searchKeywords;

        public Integer getMediaId() {
            return mediaId;
        }

        public void setMediaId(Integer mediaId) {
            this.mediaId = mediaId;
        }

        public Integer getCommunityId() {
            return communityId;
        }

        public void setCommunityId(Integer communityId) {
            this.communityId = communityId;
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

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public Integer getIsPrivate() {
            return isPrivate;
        }

        public void setIsPrivate(Integer isPrivate) {
            this.isPrivate = isPrivate;
        }

        public String getSearchKeywords() {
            return searchKeywords;
        }

        public void setSearchKeywords(String searchKeywords) {
            this.searchKeywords = searchKeywords;
        }

    }


}
