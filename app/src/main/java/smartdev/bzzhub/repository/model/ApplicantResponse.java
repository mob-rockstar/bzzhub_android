package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApplicantResponse {

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
        private Integer countryId;
        @SerializedName("category_id")
        @Expose
        private Integer categoryId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("banner")
        @Expose
        private String banner;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("company_id")
        @Expose
        private Integer companyId;
        @SerializedName("country")
        @Expose
        private String country;

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

        public Integer getCountryId() {
            return countryId;
        }

        public void setCountryId(Integer countryId) {
            this.countryId = countryId;
        }

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
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

        public Integer getCompanyId() {
            return companyId;
        }

        public void setCompanyId(Integer companyId) {
            this.companyId = companyId;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }


    }

}
