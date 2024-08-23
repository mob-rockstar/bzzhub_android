package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileMediaResponse {
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

        @SerializedName("images")
        @Expose
        private List<CompanyMediaResponse.Video> images = null;
        @SerializedName("videos")
        @Expose
        private List<CompanyMediaResponse.Video> videos = null;

        public List<CompanyMediaResponse.Video> getImages() {
            return images;
        }

        public void setImages(List<CompanyMediaResponse.Video> images) {
            this.images = images;
        }

        public List<CompanyMediaResponse.Video> getVideos() {
            return videos;
        }

        public void setVideos(List<CompanyMediaResponse.Video> videos) {
            this.videos = videos;
        }

    }

}
