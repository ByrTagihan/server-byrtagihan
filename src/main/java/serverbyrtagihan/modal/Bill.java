package serverbyrtagihan.modal;


import com.fasterxml.jackson.annotation.JsonFormat;
import serverbyrtagihan.auditing.DateConfig;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bill")
public class Bill extends DateConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "member_id")
    private Long member_id = 0L;

    @Column(name = "organization_id")
    private Long organization_id = 0L;

    @Column(name = "organization_name")
    private String organization_name = "";

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "periode")
    private Date periode;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "member_name")
    private String member_name = "";

    @Column(name = "paid_id")
    private Long paid_id = 0L;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "paid_date")
    private Date paid_date;

    @Column(name = "payment_id")
    private Long payment_id = 0L;

    @Column(name = "paid_amount")
    private Double paid_amount = 0.0;

    public Bill() {
    }
    public Bill(Long id, String description, Long member_id, Long organization_id, String organization_name, Date periode, Double amount, String member_name, Long paid_id, Date paid_date, Long payment_id, Double paid_amount) {
        this.id = id;
        this.description = description;
        this.member_id = member_id;
        this.organization_id = organization_id;
        this.organization_name = organization_name;
        this.periode = periode;
        this.amount = amount;
        this.member_name = member_name;
        this.paid_id = paid_id;
        this.paid_date = paid_date;
        this.payment_id = payment_id;
        this.paid_amount = paid_amount;
    }

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

    public Long getMember_id() {
        return member_id;
    }

    public void setMember_id(Long member_id) {
        this.member_id = member_id;
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

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public Long getPaid_id() {
        return paid_id;
    }

    public void setPaid_id(Long paid_id) {
        this.paid_id = paid_id;
    }

    public Date getPaid_date() {
        return paid_date;
    }

    public void setPaid_date(Date paid_date) {
        this.paid_date = paid_date;
    }

    public Long getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(Long payment_id) {
        this.payment_id = payment_id;
    }

    public Double getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(Double paid_amount) {
        this.paid_amount = paid_amount;
    }
}
