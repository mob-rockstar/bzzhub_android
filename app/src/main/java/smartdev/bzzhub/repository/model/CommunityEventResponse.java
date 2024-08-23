package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CommunityEventResponse {
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

        @SerializedName("event_id")
        @Expose
        private Integer eventId;
        @SerializedName("community_id")
        @Expose
        private Integer communityId;
        @SerializedName("city_id")
        @Expose
        private Integer cityId;

        @SerializedName("country_id")
        @Expose
        private Integer countryId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("event_date")
        @Expose
        private String eventDate;
        @SerializedName("user_or_company")
        @Expose
        private Integer userOrCompany;
        @SerializedName("flag")
        @Expose
        private Integer flag;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("country")
        @Expose
        private String country;

        public Integer getEventId() {
            return eventId;
        }

        public void setEventId(Integer eventId) {
            this.eventId = eventId;
        }

        public Integer getCommunityId() {
            return communityId;
        }

        public void setCommunityId(Integer communityId) {
            this.communityId = communityId;
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

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getEventDate() {
            return eventDate;
        }

        public void setEventDate(String eventDate) {
            this.eventDate = eventDate;
        }

        public Integer getUserOrCompany() {
            return userOrCompany;
        }

        public void setUserOrCompany(Integer userOrCompany) {
            this.userOrCompany = userOrCompany;
        }

        public Integer getFlag() {
            return flag;
        }

        public void setFlag(Integer flag) {
            this.flag = flag;
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

        public Integer getCountryId() {
            return countryId;
        }
    }
}
