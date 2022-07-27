package mvc.database.entity;

public class Book {

    private int bid;

    private String bname;

    private String bauthor;

    private int bstock;

    public int getBid() {
        return bid;
    }

    public int getBstock() {
        return bstock;
    }

    public String getBauthor() {
        return bauthor;
    }

    public String getBname() {
        return bname;
    }

    public void setBauthor(String bauthor) {
        this.bauthor = bauthor;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public void setBstock(int bstock) {
        this.bstock = bstock;
    }
}
