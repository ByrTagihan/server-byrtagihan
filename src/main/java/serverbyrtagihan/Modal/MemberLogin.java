package serverbyrtagihan.Modal;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.UpdateTimestamp;
import serverbyrtagihan.Auditing.DateConfig;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "table_member_login")
public class MemberLogin extends DateConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "unique_id")
    private String unique_id;

    @Column(name = "name")
    private String name;

    @Column(name = "token")
    private String token;

    @Column(name = "no_hp")
    private String hp;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "last_login")
    private Date last_login;

    @Lob
    @Column(name = "picture")
    private String picture;

    @Column(name = "address")
    private String address;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type_token")
    private MemberTypeToken typeToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public Date getLast_login() {
        return last_login;
    }

    public void setLast_login(Date last_login) {
        this.last_login = last_login;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MemberTypeToken getTypeToken() {
        return typeToken;
    }

    public void setTypeToken(MemberTypeToken typeToken) {
        this.typeToken = typeToken;
    }
}
