package serverbyrtagihan.dto;

public class DescriptionDTO {
    private String description;
    private Double amount;

    public DescriptionDTO() {
        // Constructor kosong
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
