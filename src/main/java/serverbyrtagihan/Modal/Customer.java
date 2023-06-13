package serverbyrtagihan.Modal;


import serverbyrtagihan.auditing.DateConfig;

import javax.persistence.*;

@Entity
@Table(name = "customer")
public class Customer extends DateConfig  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column (name = "password")
    private String password;

    @Column(name = "nama")
    private String name;

    @Column(name = "no_hp")
    private String hp;

    @Lob
    @Column(name = "address")
    private String address;

    @Lob
    @Column(name = "picture")
    private String picture;

    @Column(name = "active")
    private boolean active;


    public Customer() {
    }


    public Customer(String email, String password, String name, String hp, String address, boolean active) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.hp = hp;
        this.address = address;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
