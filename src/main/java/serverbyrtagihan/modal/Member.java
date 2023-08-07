package serverbyrtagihan.modal;


import com.fasterxml.jackson.annotation.JsonFormat;
import serverbyrtagihan.auditing.DateConfig;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "member")
public class
Member extends DateConfig{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "organization_id")
    private Long organization_id;

    @Column(name = "organization_name")
    private String organization_name;

    @Column(name = "unique_id")
    private String unique_id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "hp", unique = true)
    private String hp;

    @Column(name = "password")
    private String password;

    @Column(name = "token")
    private String token;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "last_login")
    private Date last_login;

    @Lob
    @Column(name = "picture")
    private String picture;

    @Column(name = "organization_id")
    private Long organizationId;

    @Column(name = "active")
    private boolean active;

    @Column(name = "va_bni")
    private String vaBni;

    @Column(name = "last_payment_id_bni")
    private Long lastPaymentIdBni;

    @Column(name = "trx_id_bni")
    private Long trxIdBni;

    @Column(name = "last_login")
    private String lastLogin;

    @Column(name = "username")
    private String username;


    @Column(name = "va_bni")
    private String va_bni;

    @Column(name = "last_payment_id_bni")
    private Long last_payment_id_bni;

    @Column(name = "trx_id_bni")
    private Long trx_id_bni;

    public Member() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(Long organization_id) {
        this.organization_id = organization_id;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVa_bni() {
        return va_bni;
    }

    public void setVa_bni(String va_bni) {
        this.va_bni = va_bni;
    }

    public Long getLast_payment_id_bni() {
        return last_payment_id_bni;
    }

    public void setLast_payment_id_bni(Long last_payment_id_bni) {
        this.last_payment_id_bni = last_payment_id_bni;
    }

    public Long getTrx_id_bni() {
        return trx_id_bni;
    }

    public void setTrx_id_bni(Long trx_id_bni) {
        this.trx_id_bni = trx_id_bni;
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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
