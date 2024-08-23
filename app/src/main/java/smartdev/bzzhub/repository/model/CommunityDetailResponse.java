package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommunityDetailResponse {
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

        @SerializedName("community_id")
        @Expose
        private Integer communityId;
        @SerializedName("user_or_company_id")
        @Expose
        private Integer userOrCompanyId;
        @SerializedName("sector_id")
        @Expose
        private Integer sectorId;

        @SerializedName("country_id")
        @Expose
        private Integer countryId;
        @SerializedName("city_id")
        @Expose
        private Integer cityId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("flag")
        @Expose
        private Integer flag;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("members")
        @Expose
        private Integer members;

        @SerializedName("category_id")
        @Expose
        private Integer categoryId;
        @SerializedName("isMember")
        @Expose
        private Integer isMember = 0;

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

        public Integer getSectorId() {
            return sectorId;
        }

        public void setSectorId(Integer sectorId) {
            this.sectorId = sectorId;
        }

        public Integer getCityId() {
            return cityId;
        }

        public void setCityId(Integer cityId) {
            this.cityId = cityId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getMembers() {
            return members;
        }

        public void setMembers(Integer members) {
            this.members = members;
        }

        public Integer getIsMember() {
            return isMember;
        }

        public void setIsMember(Integer isMember) {
            this.isMember = isMember;
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
    }

}
