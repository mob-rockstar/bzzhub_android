package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyLoginResponse {

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
    private Result result;

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

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("company_id")
        @Expose
        private String companyId;
        @SerializedName("city_id")
        @Expose
        private String cityId;
        @SerializedName("company_name")
        @Expose
        private String companyName;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("image")
        @Expose
        private Object image;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("landline")
        @Expose
        private Object landline;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("sector_id")
        @Expose
        private String sectorId;
        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("cateogry_name")
        @Expose
        private String cateogryName;
        @SerializedName("sector_name")
        @Expose
        private String sectorName;
        @SerializedName("post_code")
        @Expose
        private Object postCode;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("device_type")
        @Expose
        private String deviceType;
        @SerializedName("established_year")
        @Expose
        private Object establishedYear;
        @SerializedName("website")
        @Expose
        private String website;
        @SerializedName("keyword")
        @Expose
        private String keyword;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("is_active")
        @Expose
        private String isActive;
        @SerializedName("facebook_id")
        @Expose
        private String facebookId = "";

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

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Object getImage() {
            return image;
        }

        public void setImage(Object image) {
            this.image = image;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public Object getLandline() {
            return landline;
        }

        public void setLandline(Object landline) {
            this.landline = landline;
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

        public String getSectorId() {
            return sectorId;
        }

        public void setSectorId(String sectorId) {
            this.sectorId = sectorId;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getCateogryName() {
            return cateogryName;
        }

        public void setCateogryName(String cateogryName) {
            this.cateogryName = cateogryName;
        }

        public String getSectorName() {
            return sectorName;
        }

        public void setSectorName(String sectorName) {
            this.sectorName = sectorName;
        }

        public Object getPostCode() {
            return postCode;
        }

        public void setPostCode(Object postCode) {
            this.postCode = postCode;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public Object getEstablishedYear() {
            return establishedYear;
        }

        public void setEstablishedYear(Object establishedYear) {
            this.establishedYear = establishedYear;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIsActive() {
            return isActive;
        }

        public void setIsActive(String isActive) {
            this.isActive = isActive;
        }

        public String getFacebookId() {
            return facebookId;
        }

        public void setFacebookId(String facebookId) {
            this.facebookId = facebookId;
        }
    }
}
