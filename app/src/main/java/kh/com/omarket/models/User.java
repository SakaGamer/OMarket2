package kh.com.omarket.models;

public class User {

    private String uid;
    private String name;
    private String phone;
    private String email;
    private String password;
    private String profile;

    public User(){
        //Default constructor
    }

    public User(String userId, String name, String phone, String email, String password) {
        this.uid = userId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public User(String userId, String name, String phone, String email, String password, String profile) {
        this.uid = userId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.profile = profile;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String userId) {
        this.uid = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
