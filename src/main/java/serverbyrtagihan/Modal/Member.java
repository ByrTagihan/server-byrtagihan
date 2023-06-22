package serverbyrtagihan.Modal;


import serverbyrtagihan.auditing.DateConfig;

import javax.persistence.*;

@Entity
@Table(name = "member")
public class
Member extends DateConfig {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "unique_id")
    private String uniqueId;

    @Column(name = "name")
    private String name;

    @Column(name = "addres")
    private String addres;

    @Column(name = "hp")
    private String hp;

    @Column(name = "password")
    private String password;

    @Column(name = "type_token")
    private String typeToken;

    public Member() {
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddres() {
        return addres;
    }

    public void setAddres(String addres) {
        this.addres = addres;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTypeToken() {
        return typeToken;
    }

    public void setTypeToken(String typeToken) {
        this.typeToken = typeToken;
    }
}
