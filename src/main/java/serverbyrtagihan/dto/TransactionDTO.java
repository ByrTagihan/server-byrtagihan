package serverbyrtagihan.dto;

import java.util.Date;

public class TransactionDTO {
    private Long organization_id;
    private Long member_id;
    private String description;
    private Date priode;
    private Float amount;

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

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
