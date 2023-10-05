package shopping;

public class MenuItem {
    private String productName;
    private int quantity;

    public MenuItem(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public MenuItem(String productName) {  // 메뉴 이름만을 받는 생성자 추가
        this.productName = productName;
        this.quantity = 0;  // 수량은 기본적으로 0으로 설정
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
}

