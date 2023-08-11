package serverbyrtagihan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import java.util.Date;

public class EcollectionDTO {
    private String client_id;
    private String trx_amount;
    private String customer_name;
    private String customer_email;
    private String customer_phone;
    private String virtual_account;
    private String trx_id;
    @Column(name = "va_expired_date")
    private String datetime_expired;
    private String description;
    private String type;

    private String billing_type;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getTrx_amount() {
        return trx_amount;
    }

    public void setTrx_amount(String trx_amount) {
        this.trx_amount = trx_amount;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getVirtual_account() {
        return virtual_account;
    }

    public void setVirtual_account(String virtual_account) {
        this.virtual_account = virtual_account;
    }

    public String getTrx_id() {
        return trx_id;
    }

    public void setTrx_id(String trx_id) {
        this.trx_id = trx_id;
    }

    public String getDatetime_expired() {
        return datetime_expired;
    }

    public void setDatetime_expired(String datetime_expired) {
        this.datetime_expired = datetime_expired;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBilling_type() {
        return billing_type;
    }

    public void setBilling_type(String billing_type) {
        this.billing_type = billing_type;
    }
}
