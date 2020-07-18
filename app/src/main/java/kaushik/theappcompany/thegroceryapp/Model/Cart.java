package kaushik.theappcompany.thegroceryapp.Model;

public class Cart {

    public String pid, pname, quantity, discount;

    public Cart() {


    }
    public Cart(String pid, String pname, String quantity, String discount) {
        this.pid = pid;
        this.pname = pname;
        this.quantity = quantity;
        this.discount = discount;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
