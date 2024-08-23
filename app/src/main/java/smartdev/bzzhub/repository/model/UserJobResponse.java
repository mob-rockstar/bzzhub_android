package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserJobResponse {
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

    public class Result {

        @SerializedName("job_id")
        @Expose
        private Integer jobId;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("city_id")
        @Expose
        private Integer cityId;
        @SerializedName("sector_id")
        @Expose
        private Integer sectorId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("country_id")
        @Expose
        private Integer countryID;
        @SerializedName("category_id")
        @Expose
        private Integer categoryID;

        public Integer getJobId() {
            return jobId;
        }

        public void setJobId(Integer jobId) {
            this.jobId = jobId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getCityId() {
            return cityId;
        }

        public void setCityId(Integer cityId) {
            this.cityId = cityId;
        }

        public Integer getSectorId() {
            return sectorId;
        }

        public void setSectorId(Integer sectorId) {
            this.sectorId = sectorId;
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

        public Integer getCountryID() {
            return countryID;
        }

        public Integer getCategoryID() {
            return categoryID;
        }

        public void setCountryID(Integer countryID) {
            this.countryID = countryID;
        }

        public void setCategoryID(Integer categoryID) {
            this.categoryID = categoryID;
        }
    }
}
