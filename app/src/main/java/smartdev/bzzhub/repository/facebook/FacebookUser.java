package smartdev.bzzhub.repository.facebook;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FacebookUser {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String last_name;
    @SerializedName("gender")
    @Expose
    private String gender;
    private String mobile;

    public FacebookUser(String id, String email, String firstName, String last_name, String gender) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.last_name = last_name;
        this.gender = gender;
    }

    public FacebookUser() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "FacebookUser{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", last_name='" + last_name + '\'' +
                ", gender='" + gender + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
