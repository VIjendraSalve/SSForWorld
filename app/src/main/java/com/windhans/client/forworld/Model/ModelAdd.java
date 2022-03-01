package com.windhans.client.forworld.Model;

public class ModelAdd {
    String startDate;
    String endDate;
    String total;
    String remaining;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ModelAdd(String startDate, String endDate, String total, String remaining, String status) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.total = total;
        this.remaining = remaining;
        this.status=status;
    }



    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }


}
