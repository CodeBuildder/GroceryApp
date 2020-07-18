package kaushik.theappcompany.thegroceryapp.Model;



public class AdminOrders{

    private String email,name,time,quantity,date,phone;

    public AdminOrders() {

    }

    public AdminOrders(String email, String name, String time, String quantity, String date, String phone) {
        this.email = email;
        this.name = name;
        this.time = time;
        this.quantity = quantity;
        this.date = date;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}


