package kaushik.theappcompany.thegroceryapp.Model;

public class Products {

    private String Pname, Description, Quantity, Image, Category, Pid, Date, Time;


    public Products(){


    }

    public Products(String pname, String description, String quantity, String image, String category, String pid, String date, String time) {
        Pname = pname;
        Description = description;
        Quantity = quantity;
        Image = image;
        Category = category;
        Pid = pid;
        Date = date;
        Time = time;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
