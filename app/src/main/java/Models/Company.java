package Models;

import com.google.gson.annotations.SerializedName;

public class Company {

    private String name;
    private String logo;
    @SerializedName("custom_color")
    private String customColor;
    @SerializedName("password_changing")
    private Password passwordChanging;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }


    public String getCustomColor() {
        return customColor;
    }

    public void setCustomColor(String customColor) {
        this.customColor = customColor;
    }

    public Password getPasswordChanging() {
        return passwordChanging;
    }

    public void setPasswordChanging(Password passwordChanging) {
        this.passwordChanging = passwordChanging;
    }
}
