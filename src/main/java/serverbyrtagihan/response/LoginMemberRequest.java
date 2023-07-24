package serverbyrtagihan.response;

import javax.persistence.Column;

public class LoginMemberRequest {

    @Column(name = "UniqueId")
    private String uniqueId;

    @Column(name = "password")
    private String password;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
