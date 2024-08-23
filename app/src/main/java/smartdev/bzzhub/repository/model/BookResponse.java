package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookResponse {

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
    private List<PDFResult> result = null;

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

    public List<PDFResult> getResult() {
        return result;
    }

    public void setResult(List<PDFResult> result) {
        this.result = result;
    }

    public class PDFResult {

        @SerializedName("tender_id")
        @Expose
        private Integer tenderId;
        @SerializedName("file_path")
        @Expose
        private String filePath;
        @SerializedName("company_id")
        @Expose
        private Integer companyId;
        @SerializedName("name")
        @Expose
        private String title;

        public Integer getTenderId() {
            return tenderId;
        }

        public void setTenderId(Integer tenderId) {
            this.tenderId = tenderId;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public Integer getCompanyId() {
            return companyId;
        }

        public void setCompanyId(Integer companyId) {
            this.companyId = companyId;
        }

        public String getTitle() {
            return title;
        }
    }


}
