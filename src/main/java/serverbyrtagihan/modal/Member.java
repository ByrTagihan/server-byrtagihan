package serverbyrtagihan.modal;


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

    @Column(name = "address")
    private String address;

    @Column(name = "hp")
    private String hp;

    @Column(name = "password")
    private String password;
    @Lob
    @Column(name = "picture")
    private String picture;

    @Column(name = "active")
    private boolean active;

    @Column(name = "token")
    private String token;

    @Column(name = "va_bni")
    private String vaBni;

    @Column(name = "last_payment_id_bni")
    private Long lastPaymentIdBni;

    @Column(name = "trx_id_bni")
    private Long trxIdBni;

    @Column(name = "last_login")
    private String lastLogin;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVaBni() {
        return vaBni;
    }

    public void setVaBni(String vaBni) {
        this.vaBni = vaBni;
    }

    public Long getLastPaymentIdBni() {
        return lastPaymentIdBni;
    }

    public void setLastPaymentIdBni(Long lastPaymentIdBni) {
        this.lastPaymentIdBni = lastPaymentIdBni;
    }

    public Long getTrxIdBni() {
        return trxIdBni;
    }

    public void setTrxIdBni(Long trxIdBni) {
        this.trxIdBni = trxIdBni;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }
}
