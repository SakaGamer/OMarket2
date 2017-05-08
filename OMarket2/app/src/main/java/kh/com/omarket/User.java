package kh.com.omarket;

/**
 * Created by daly on 3/27/17.
 */

public class User {
    private String name = "";
    private String password = "";
    private String phone = "";
    private String email = "";
    private String profile = "";
    private String userId;

    public User(){
        //Default constructor
    }

    public User(String userID, String Name, String Phone, String Email, String Password){
        userId = userID;
        name = Name;
        phone = Phone;
        email = Email;
        password = Password;
    }

    public User(String userID, String Name, String Phone, String Email, String Password, String Profile){
        userId = userID;
        name = Name;
        phone = Phone;
        email = Email;
        password = Password;
        profile = Profile;
    }

    public String getUsername() {
        return name;
    }

    public void setUsername(String username) {
        name = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String Password) {
        password = Password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String Phone) {
        phone = Phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String Email) {
        email = Email;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String Profile) {
        profile = Profile;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserId(){
        return userId;
    }

}
