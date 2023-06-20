package serverbyrtagihan.dto;

import java.util.Date;

public class BillPaidDTO {

    private Date paid_date;

    private Double paid_amount;

    public Date getPaid_date() {
        return paid_date;
    }

    public void setPaid_date(Date paid_date) {
        this.paid_date = paid_date;
    }

    public Double getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(Double paid_amount) {
        this.paid_amount = paid_amount;
    }
}
