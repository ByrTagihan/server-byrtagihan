package serverbyrtagihan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ReportBill {

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT")
    private Date periode;
    private int count_bill;
    private double total_bill;
    private double unpaid_bill;
    private double paid_bill;

    public Date getPeriode() {
        return periode;
    }

    public void setPeriode(Date periode) {
        this.periode = periode;
    }

    public int getCount_bill() {
        return count_bill;
    }

    public void setCount_bill(int count_bill) {
        this.count_bill = count_bill;
    }

    public double getTotal_bill() {
        return total_bill;
    }

    public void setTotal_bill(double total_bill) {
        this.total_bill = total_bill;
    }

    public double getUnpaid_bill() {
        return unpaid_bill;
    }

    public void setUnpaid_bill(double unpaid_bill) {
        this.unpaid_bill = unpaid_bill;
    }

    public double getPaid_bill() {
        return paid_bill;
    }

    public void setPaid_bill(double paid_bill) {
        this.paid_bill = paid_bill;
    }
}
