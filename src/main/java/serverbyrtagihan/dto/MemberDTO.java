package serverbyrtagihan.dto;


import javax.persistence.Column;
import javax.persistence.Lob;

public class MemberDTO {
    public String uniqueId;

    public String name;

    public String address;

    public String hp;

    public String password;

    public String va_bni;

    public Long last_payment_id_bni;

    public Long trx_id_bni;


    public String last_login;

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

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}