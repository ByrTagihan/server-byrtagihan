package serverbyrtagihan.dto;


import javax.persistence.Column;
import javax.persistence.Lob;

public class MemberDTO {
    public String uniqueId;

    public String name;

    public String address;

    public String hp;

    public String password;

    public String vaBni;

    public Long lastPaymentIdBni;

    public Long trxIdBni;


    public String lastLogin;

    @Lob
    public String picture;


    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}