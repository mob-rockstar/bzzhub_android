package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductDetail {
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

    public class Result {

        @SerializedName("images")
        @Expose
        private List<Image> images = null;
        @SerializedName("product_id")
        @Expose
        private Integer productId;
        @SerializedName("company_id")
        @Expose
        private Integer companyId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("attributes")
        @Expose
        private Attributes attributes;

        public List<Image> getImages() {
            return images;
        }

        public void setImages(List<Image> images) {
            this.images = images;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
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

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Attributes getAttributes() {
            return attributes;
        }

        public void setAttributes(Attributes attributes) {
            this.attributes = attributes;
        }

    }
    public class Size {

        @SerializedName("id")
        @Expose
        private Integer chartId;

        public Integer getChartId() {
            return chartId;
        }

        public void setChartId(Integer chartId) {
            this.chartId = chartId;
        }
        @SerializedName("english")
        @Expose
        private String name;
        @SerializedName("arabic")
        @Expose
        private String arabicName;

        public String getName() {
            return name;
        }

        public String getArabicName() {
            return arabicName;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setArabicName(String arabicName) {
            this.arabicName = arabicName;
        }
    }
    public class Image {

        @SerializedName("image_id")
        @Expose
        private Integer imageId;
        @SerializedName("image")
        @Expose
        private String image;

        public Integer getImageId() {
            return imageId;
        }

        public void setImageId(Integer imageId) {
            this.imageId = imageId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }

    public class Color {

        @SerializedName("id")
        @Expose
        private Integer chartId;

        public Integer getChartId() {
            return chartId;
        }

        public void setChartId(Integer chartId) {
            this.chartId = chartId;
        }
        @SerializedName("english")
        @Expose
        private String name;
        @SerializedName("arabic")
        @Expose
        private String arabicName;

        public String getName() {
            return name;
        }

        public String getArabicName() {
            return arabicName;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setArabicName(String arabicName) {
            this.arabicName = arabicName;
        }
    }

    public class Attributes {

        @SerializedName("colors")
        @Expose
        private List<Color> colors = null;
        @SerializedName("sizes")
        @Expose
        private List<Size> sizes = null;

        public List<Color> getColors() {
            return colors;
        }

        public void setColors(List<Color> colors) {
            this.colors = colors;
        }

        public List<Size> getSizes() {
            return sizes;
        }

        public void setSizes(List<Size> sizes) {
            this.sizes = sizes;
        }

    }
}
