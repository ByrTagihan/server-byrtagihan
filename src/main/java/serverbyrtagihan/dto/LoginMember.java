package serverbyrtagihan.dto;

import javax.persistence.Column;

public class LoginMember {
    @Column(name = "unique_id")
    private String unique_id;

    @Column(name = "password")
    private String password;

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
