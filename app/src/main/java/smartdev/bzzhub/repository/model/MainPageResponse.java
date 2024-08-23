package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainPageResponse {

    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("Records")
    @Expose
    private Integer records;
    @SerializedName("CurrentPage")
    @Expose
    private Integer currentPage;
    @SerializedName("LimitPerPage")
    @Expose
    private String limitPerPage;
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
    private List<Company> result = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getRecords() {
        return records;
    }

    public void setRecords(Integer records) {
        this.records = records;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public String getLimitPerPage() {
        return limitPerPage;
    }

    public void setLimitPerPage(String limitPerPage) {
        this.limitPerPage = limitPerPage;
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

    public List<Company> getResult() {
        return result;
    }

    public void setResult(List<Company> result) {
        this.result = result;
    }

}

