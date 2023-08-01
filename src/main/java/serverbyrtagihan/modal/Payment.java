package serverbyrtagihan.modal;

import com.fasterxml.jackson.annotation.JsonFormat;
import serverbyrtagihan.auditing.DateConfig;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payment")
public class Payment extends DateConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_id")
    private Long organization_id;

    @Column(name = "member_id")
    private Long member_id;

    @Column(name = "channel_id")
    private Long channel_id;

    @Column(name = "organization_name")
    private String organization_name;

    @Column(name = "channel_name")
    private String channel_name;

    @Column(name = "bill_ids")
    private String bill_ids;

    @Column(name = "va_number")
    private String va_number;

    @Column(name = "va_expired_date", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date va_expired_date;

    @Column(name = "status")
    private int status;

    @Column(name = "description")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date periode;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "fee_admin")
    private Double fee_admin = 0.0;

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

    public String getVa_number() {
        return va_number;
    }

    public void setVa_number(String va_number) {
        this.va_number = va_number;
    }

    public Date getVa_expired_date() {
        return va_expired_date;
    }

    public void setVa_expired_date(Date va_expired_date) {
        this.va_expired_date = va_expired_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(Long organization_id) {
        this.organization_id = organization_id;
    }

    public Long getMember_id() {
        return member_id;
    }

    public void setMember_id(Long member_id) {
        this.member_id = member_id;
    }

    public Long getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(Long channel_id) {
        this.channel_id = channel_id;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getBill_ids() {
        return bill_ids;
    }

    public void setBill_ids(String bill_ids) {
        this.bill_ids = bill_ids;
    }

    public Double getFee_admin() {
        return fee_admin;
    }

    public void setFee_admin(Double fee_admin) {
        this.fee_admin = fee_admin;
    }
}
