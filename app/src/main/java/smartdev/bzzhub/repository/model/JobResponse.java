package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class JobResponse {
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
    public class Result implements Serializable {

        @SerializedName("job_id")
        @Expose
        private Integer jobId;
        @SerializedName("company_id")
        @Expose
        private String companyId;
        @SerializedName("user_id")
        @Expose
        private String userId = null;
        @SerializedName("city_id")
        @Expose
        private String cityId;
        @SerializedName("sector_id")
        @Expose
        private String sectorId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("email")
        @Expose
        private String email = null;
        @SerializedName("banner")
        @Expose
        private String banner;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("country")
        @Expose
        private String country;

        public Integer getJobId() {
            return jobId;
        }

        public void setJobId(Integer jobId) {
            this.jobId = jobId;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getSectorId() {
            return sectorId;
        }

        public void setSectorId(String sectorId) {
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getBanner() {
            return banner;
        }

        public void setBanner(String banner) {
            this.banner = banner;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

}
