package serverbyrtagihan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ReportTranscation {
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT")
    private Date periode;
    private int count_bill;
    private double total_bill;

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

}
