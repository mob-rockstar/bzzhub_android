package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryResponse {
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
    public static class Result {

        public Result(Integer countryId, String country) {
            this.countryId = countryId;
            this.country = country;
        }

        @SerializedName("country_id")
        @Expose
        private Integer countryId = 0;
        @SerializedName("country")
        @Expose
        private String country;

        public Integer getCountryId() {
            return countryId;
        }

        public void setCountryId(Integer countryId) {
            this.countryId = countryId;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        @Override
        public String toString(){
            return country;
        }
    }

}
