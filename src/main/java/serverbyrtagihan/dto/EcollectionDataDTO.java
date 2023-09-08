package serverbyrtagihan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import serverbyrtagihan.modal.Transaction;

import java.util.Date;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
    public class EcollectionDataDTO {
        private Long id;
        private String description;
        private Long member_id;
        private Long organization_id;
        private String organization_name;
        private Date periode;
        private Double amount;
        private Long channel_id;
        private String channel_name;
        private Long bill_id;
        private Long payment_id;
        private String va_number;
        private String va_expired_date;
        private Double fee_admin;
        private String created_date;
        private String updated_date;

        private List<DescriptionDTO> descriptions;


    public EcollectionDataDTO() {
        // Constructor kosong
    }


    // Getter dan setter untuk semua properti
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public Date getPeriode() {
        return periode;
    }

    public void setPeriode(Date periode) {
        this.periode = periode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public Long getMember_id() {
        return member_id;
    }

    public void setMember_id(Long member_id) {
        this.member_id = member_id;
    }

    public Long getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(Long payment_id) {
        this.payment_id = payment_id;
    }

    public Long getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(Long organization_id) {
        this.organization_id = organization_id;
    }


    public Long getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(Long channel_id) {
        this.channel_id = channel_id;
    }

    public Long getBill_id() {
        return bill_id;
    }

    public void setBill_id(Long bill_id) {
        this.bill_id = bill_id;
    }

    public String getVa_number() {
        return va_number;
    }

    public void setVa_number(String va_number) {
        this.va_number = va_number;
    }

    public String getVa_expired_date() {
        return va_expired_date;
    }

    public void setVa_expired_date(String va_expired_date) {
        this.va_expired_date = va_expired_date;
    }

    public Double getFee_admin() {
        return fee_admin;
    }

    public void setFee_admin(Double fee_admin) {
        this.fee_admin = fee_admin;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }
}
