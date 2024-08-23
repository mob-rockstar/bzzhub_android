package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RFQResponse {
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
        @SerializedName("rfq_id")
        @Expose
        private Integer rfqId;
        @SerializedName("company_id")
        @Expose
        private Integer companyId;
        @SerializedName("sub_id")
        @Expose
        private Integer subId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("details")
        @Expose
        private String details;
        @SerializedName("unit")
        @Expose
        private String unit;
        @SerializedName("qty")
        @Expose
        private String qty;
        @SerializedName("file_path")
        @Expose
        private String filePath;
        @SerializedName("sector_name")
        @Expose
        private String sectorName;
        @SerializedName("sector_id")
        @Expose
        private Integer sectorId;
        @SerializedName("category_id")
        @Expose
        private Integer categoryId;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("image")
        @Expose
        private String image;

        public Integer getRfqId() {
            return rfqId;
        }

        public void setRfqId(Integer rfqId) {
            this.rfqId = rfqId;
        }

        public Integer getCompanyId() {
            return companyId;
        }

        public void setCompanyId(Integer companyId) {
            this.companyId = companyId;
        }

        public Integer getSubId() {
            return subId;
        }

        public void setSubId(Integer subId) {
            this.subId = subId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
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

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }


    }

}
