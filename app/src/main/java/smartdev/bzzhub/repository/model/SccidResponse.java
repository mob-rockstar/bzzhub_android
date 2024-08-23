package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SccidResponse {
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

        @SerializedName("countries")
        @Expose
        private List<CountryResponse.Result> countries = null;
        @SerializedName("cities")
        @Expose
        private List<CityResponse.Result> cities = null;
        @SerializedName("sectors")
        @Expose
        private List<SelectorResponse.Result> sectors = null;

        public List<CountryResponse.Result> getCountries() {
            return countries;
        }

        public void setCountries(List<CountryResponse.Result> countries) {
            this.countries = countries;
        }

        public List<CityResponse.Result> getCities() {
            return cities;
        }

        public void setCities(List<CityResponse.Result> cities) {
            this.cities = cities;
        }

        public List<SelectorResponse.Result> getSectors() {
            return sectors;
        }

        public void setSectors(List<SelectorResponse.Result> sectors) {
            this.sectors = sectors;
        }
    }
}
