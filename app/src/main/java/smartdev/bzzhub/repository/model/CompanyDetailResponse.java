package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CompanyDetailResponse {

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
    private CompanyDetail result;

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

    public CompanyDetail getResult() {
        return result;
    }

    public void setResult(CompanyDetail result) {
        this.result = result;
    }


    public class CompanyDetail {

        @SerializedName("company_id")
        @Expose
        private Integer companyId;
        @SerializedName("city_id")
        @Expose
        private Integer cityId;
        @SerializedName("company_name")
        @Expose
        private String companyName;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("banner")
        @Expose
        private String banner;
        @SerializedName("business_id")
        @Expose
        private Integer businessId;
        @SerializedName("established_year")
        @Expose
        private Integer establishedYear;
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("device_type")
        @Expose
        private Integer deviceType;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("website")
        @Expose
        private String website;
        @SerializedName("post_code")
        @Expose
        private String postCode;
        @SerializedName("landline")
        @Expose
        private String landline;
        @SerializedName("keyword")
        @Expose
        private Object keyword;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("notification")
        @Expose
        private Integer notification;
        @SerializedName("is_active")
        @Expose
        private Integer isActive;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("country_id")
        @Expose
        private Integer countryId;
        @SerializedName("sector_name")
        @Expose
        private String sectorName;
        @SerializedName("sector_id")
        @Expose
        private Integer sectorId;
        @SerializedName("category_id")
        @Expose
        private Integer categoryId;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("trader")
        @Expose
        private Integer trader;
        @SerializedName("serviceCompany")
        @Expose
        private Integer serviceCompany;
        @SerializedName("manufacturer")
        @Expose
        private Integer manufacturer;

        public void setTrader(Integer trader) {
            this.trader = trader;
        }

        public void setServiceCompany(Integer serviceCompany) {
            this.serviceCompany = serviceCompany;
        }

        public void setManufacturer(Integer manufacturer) {
            this.manufacturer = manufacturer;
        }

        public Integer getTrader() {
            return trader;
        }

        public Integer getServiceCompany() {
            return serviceCompany;
        }

        public Integer getManufacturer() {
            return manufacturer;
        }

        public Integer getCompanyId() {
            return companyId;
        }

        public void setCompanyId(Integer companyId) {
            this.companyId = companyId;
        }

        public Integer getCityId() {
            return cityId;
        }

        public void setCityId(Integer cityId) {
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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
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

        public Integer getBusinessId() {
            return businessId;
        }

        public void setBusinessId(Integer businessId) {
            this.businessId = businessId;
        }

        public Integer getEstablishedYear() {
            return establishedYear;
        }

        public void setEstablishedYear(Integer establishedYear) {
            this.establishedYear = establishedYear;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Integer getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(Integer deviceType) {
            this.deviceType = deviceType;
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

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getPostCode() {
            return postCode;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }

        public String getLandline() {
            return landline;
        }

        public void setLandline(String landline) {
            this.landline = landline;
        }

        public Object getKeyword() {
            return keyword;
        }

        public void setKeyword(Object keyword) {
            this.keyword = keyword;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getNotification() {
            return notification;
        }

        public void setNotification(Integer notification) {
            this.notification = notification;
        }

        public Integer getIsActive() {
            return isActive;
        }

        public void setIsActive(Integer isActive) {
            this.isActive = isActive;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Integer getCountryId() {
            return countryId;
        }

        public void setCountryId(Integer countryId) {
            this.countryId = countryId;
        }

        public String getSectorName() {
            return sectorName;
        }

        public void setSectorName(String sectorName) {
            this.sectorName = sectorName;
        }

        public Integer getSectorId() {
            return sectorId;
        }

        public void setSectorId(Integer sectorId) {
            this.sectorId = sectorId;
        }

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
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

        @SerializedName("intersts")
        @Expose
        private List<Interst> intersts = new ArrayList<>();

        @SerializedName("products")
        @Expose
        private List<Product> products = null;


        public List<Interst> getIntersts() {
            return intersts;
        }

        public void setIntersts(List<Interst> intersts) {
            this.intersts = intersts;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }
    }

    public static class Interst {

        public Interst(Integer id,Integer subID, String name) {
            this.id = id;
            this.name = name;
            this.subID = subID;
        }

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("sub_id")
        @Expose
        private Integer subID;
        @SerializedName("name")
        @Expose
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getSubID() {
            return subID;
        }

        public void setSubID(Integer subID) {
            this.subID = subID;
        }
    }

    public class Product{
        @SerializedName("product_id")
        @Expose
        private Integer productId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("image")
        @Expose
        private Object image;

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public Object getImage() {
            return image;
        }

        public void setImage(Object image) {
            this.image = image;
        }


    }
}
