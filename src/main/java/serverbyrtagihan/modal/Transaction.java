package serverbyrtagihan.modal;

import serverbyrtagihan.auditing.DateConfig;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transaction")
public class Transaction extends DateConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(name = "organization_id")
    private Long organization_Id;

    @Column(name = "member_id")
    private Long member_id;

    @Column(name = "description")
    private String description;

    @Column(name = "priode")
    private Date priode;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "organization_name")
    private String organization_name;

    public Transaction() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganization_Id() {
        return organization_Id;
    }

    public void setOrganization_Id(Long organization_Id) {
        this.organization_Id = organization_Id;
    }

    public Long getMember_id() {
        return member_id;
    }

    public void setMember_id(Long member_id) {
        this.member_id = member_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPriode() {
        return priode;
    }

    public void setPriode(Date priode) {
        this.priode = priode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }
}
