package serverbyrtagihan.dto;

import java.util.Date;

public class PutTransactionDTO {
    private String description;
    private Date priode;
    private Float amount;

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
