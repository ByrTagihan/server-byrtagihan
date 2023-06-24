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
    private Long memberId = 0L;

    @Column(name = "organization_id")
    private Long organizationId = 0L;

    @Column(name = "organization_name")
    private String organizationName = "";

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "periode")
    private Date periode;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "member_name")
    private String memberName = "";

    @Column(name = "paid_id")
    private Long paidId = 0L;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "paid_date")
    private Date paidDate;

    @Column(name = "payment_id")
    private Long paymentId = 0L;

    @Column(name = "paid_amount")
    private Double paidAmount = 0.0;

    public Bill() {
    }

    public Bill(Long id, String description, Long memberId, Long organizationId, String organizationName, Date periode, Double amount, String memberName, Long paidId, Date paidDate, Long paymentId, Double paidAmount) {
        this.id = id;
        this.description = description;
        this.memberId = memberId;
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.periode = periode;
        this.amount = amount;
        this.memberName = memberName;
        this.paidId = paidId;
        this.paidDate = paidDate;
        this.paymentId = paymentId;
        this.paidAmount = paidAmount;
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

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Long getPaidId() {
        return paidId;
    }

    public void setPaidId(Long paidId) {
        this.paidId = paidId;
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }
}
