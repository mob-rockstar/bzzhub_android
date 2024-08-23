package smartdev.bzzhub.repository.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Size {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("english")
    @Expose
    private String name;
    @SerializedName("arabic")
    @Expose
    private String arabicName;

    private boolean selected;

    public Size(String name) {
        this.name = name;
    }

    public Size(String name, boolean selected) {
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Integer getId() {
        return id;
    }

    public String getArabicName() {
        return arabicName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setArabicName(String arabicName) {
        this.arabicName = arabicName;
    }
}
