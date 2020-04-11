package kh.com.omarket.activities;

import kh.com.omarket.models.Product;
import kh.com.omarket.models.User;


public class AppSingleTon {

    private static AppSingleTon instance;
    private AppSingleTon(){}

    private User user;
    private Product product;

    public static AppSingleTon getInstance() {
        if (instance == null) {
            instance = new AppSingleTon();
        }
        return instance;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
