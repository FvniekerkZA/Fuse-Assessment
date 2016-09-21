package Models;

public class Password {
    private boolean enabled;
    private String secure_field;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSecure_field() {
        return secure_field;
    }

    public void setSecure_field(String secure_field) {
        this.secure_field = secure_field;
    }
}
