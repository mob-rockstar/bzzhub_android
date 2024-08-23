package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserProfileResponse {

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

    public class Friend {

        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("name")
        @Expose
        private String fullName;
        @SerializedName("image")
        @Expose
        private String image;

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }

    public class Group {

        @SerializedName("groups_id")
        @Expose
        private String groupsId;
        @SerializedName("community_id")
        @Expose
        private String communityId;
        @SerializedName("title")
        @Expose
        private String title;

        public String getGroupsId() {
            return groupsId;
        }

        public void setGroupsId(String groupsId) {
            this.groupsId = groupsId;
        }

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }
    public static class Interst {

        public Interst(String interest, Integer id) {
            this.interest = interest;
            this.id = id;
        }

        @SerializedName("interest")
        @Expose
        private String interest;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("interest_id")
        @Expose
        private Integer interestID;

        private boolean isSelected;

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public Integer getId() {
            return id;
        }

        public Integer getInterestID() {
            return interestID;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
    public class Result {

        @SerializedName("friends")
        @Expose
        private List<Friend> friends = null;
        @SerializedName("user_id")
        @Expose
        private Integer userId;

        @SerializedName("full_name")
        @Expose
        private String fullName;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("banner")
        @Expose
        private String banner;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("landline")
        @Expose
        private Object landline;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("post_code")
        @Expose
        private Object postCode;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude = "";
        @SerializedName("token")
        @Expose
        private String token = "";
        @SerializedName("device_type")
        @Expose
        private String deviceType = "";
        @SerializedName("has_business")
        @Expose
        private String hasBusiness = "";
        @SerializedName("facebook_id")
        @Expose
        private String facebookId = "";

        @SerializedName("email_is_visible")
        @Expose
        private Integer emailIsVisible = 0;
        @SerializedName("phone_is_visible")
        @Expose
        private Integer phoneIsVisible = 0;
        @SerializedName("school_is_visible")
        @Expose
        private Integer schoolIsVisible = 0;
        @SerializedName("universty_is_visible")
        @Expose
        private Integer universtyIsVisible = 0;
        @SerializedName("work_is_visible")
        @Expose
        private Integer workIsVisible = 0;
        @SerializedName("country_id")
        @Expose
        private Integer countryId = 0;
        @SerializedName("city_id")
        @Expose
        private Integer cityId = 0;
        @SerializedName("membership")
        @Expose
        private String membership;
        @SerializedName("notification")
        @Expose
        private String notification;
        @SerializedName("work")
        @Expose
        private String work;
        @SerializedName("university")
        @Expose
        private String university;

        @SerializedName("languages")
        @Expose
        private List<String> languages = null;
        @SerializedName("school")
        @Expose
        private String school;
        @SerializedName("user_languages")
        @Expose
        private String userLanguage;
        @SerializedName("intersts")
        @Expose
        private List<Interst> interst = null;
        @SerializedName("groups")
        @Expose
        private List<Group> groups = null;

        public List<Friend> getFriends() {
            return friends;
        }

        public void setFriends(List<Friend> friends) {
            this.friends = friends;
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


        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getBanner() {
            return banner;
        }

        public void setBanner(String banner) {
            this.banner = banner;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
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

        public String getHasBusiness() {
            return hasBusiness;
        }

        public void setHasBusiness(String hasBusiness) {
            this.hasBusiness = hasBusiness;
        }

        public Integer getEmailIsVisible() {
            return emailIsVisible;
        }

        public void setEmailIsVisible(Integer emailIsVisible) {
            this.emailIsVisible = emailIsVisible;
        }

        public Integer getPhoneIsVisible() {
            return phoneIsVisible;
        }

        public void setPhoneIsVisible(Integer phoneIsVisible) {
            this.phoneIsVisible = phoneIsVisible;
        }

        public String getFacebookId() {
            return facebookId;
        }

        public void setFacebookId(String facebookId) {
            this.facebookId = facebookId;
        }

        public Integer getSchoolIsVisible() {
            return schoolIsVisible;
        }

        public void setSchoolIsVisible(Integer schoolIsVisible) {
            this.schoolIsVisible = schoolIsVisible;
        }

        public Integer getUniverstyIsVisible() {
            return universtyIsVisible;
        }

        public void setUniverstyIsVisible(Integer universtyIsVisible) {
            this.universtyIsVisible = universtyIsVisible;
        }

        public Integer getWorkIsVisible() {
            return workIsVisible;
        }

        public void setWorkIsVisible(Integer workIsVisible) {
            this.workIsVisible = workIsVisible;
        }

        public String getMembership() {
            return membership;
        }

        public void setMembership(String membership) {
            this.membership = membership;
        }

        public String getNotification() {
            return notification;
        }

        public void setNotification(String notification) {
            this.notification = notification;
        }

        public String getWork() {
            return work;
        }

        public void setWork(String work) {
            this.work = work;
        }

        public String getUniversity() {
            return university;
        }

        public void setUniversity(String university) {
            this.university = university;
        }

        public Integer getCountryId() {
            return countryId;
        }

        public void setCountryId(Integer countryId) {
            this.countryId = countryId;
        }

        public void setCityId(Integer cityId) {
            this.cityId = cityId;
        }

        public List<String> getLanguages() {
            return languages;
        }

        public void setLanguages(List<String> languages) {
            this.languages = languages;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public List<Interst> getInterst() {
            return interst;
        }

        public void setInterst(List<Interst> interst) {
            this.interst = interst;
        }

        public List<Group> getGroups() {
            return groups;
        }

        public void setGroups(List<Group> groups) {
            this.groups = groups;
        }

        public String getUserLanguage() {
            if (userLanguage == null) return "";
            else return userLanguage;
        }

        public void setUserLanguage(String userLanguage) {
            this.userLanguage = userLanguage;
        }
    }




}
