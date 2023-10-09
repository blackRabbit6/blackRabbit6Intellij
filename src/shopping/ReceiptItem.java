package shopping;

import java.sql.Date;
import java.time.LocalDate;

public class ReceiptItem {
    private String name;
    private String productName;
    private int quantity;
    private Date buyday;
    private int receiptNum;


    public ReceiptItem(String name, String productName, int quantity, Date buyday, int receiptNum) {
        this.name = name;
        this.productName = productName;
        this.quantity = quantity;
        this.buyday = buyday;
        this.receiptNum = receiptNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getBuyday() {
        return buyday;
    }

    public void setBuyday(Date buyday) {
        this.buyday = buyday;
    }

    public int getReceiptNum() {
        return receiptNum;
    }

    public void setReceiptNum(int receiptNum) {
        this.receiptNum = receiptNum;
    }
}
