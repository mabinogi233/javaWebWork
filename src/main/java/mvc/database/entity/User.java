package mvc.database.entity;

public class User {

    private int uid;

    private String username;

    private String password;

    public int getUid() {
        return uid;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
