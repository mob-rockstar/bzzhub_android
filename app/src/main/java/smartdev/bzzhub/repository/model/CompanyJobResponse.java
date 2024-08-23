package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompanyJobResponse {
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
        @SerializedName("company_id")
        @Expose
        private Integer companyId;
        @SerializedName("city_id")
        @Expose
        private Integer cityId;
        @SerializedName("sector_id")
        @Expose
        private Integer sectorId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("salary")
        @Expose
        private String salary;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("age")
        @Expose
        private Integer age;
        @SerializedName("no_vacancies")
        @Expose
        private Integer noVacancies;
        @SerializedName("start_date")
        @Expose
        private String startDate;
        @SerializedName("end_date")
        @Expose
        private String endDate;
        @SerializedName("created_date")
        @Expose
        private String createdDate;
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

        public String getSalary() {
            return salary;
        }

        public void setSalary(String salary) {
            this.salary = salary;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public Integer getNoVacancies() {
            return noVacancies;
        }

        public void setNoVacancies(Integer noVacancies) {
            this.noVacancies = noVacancies;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
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
