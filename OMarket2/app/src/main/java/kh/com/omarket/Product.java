package kh.com.omarket;

public class Product {
    //////////////Product Information/////////////////
//    private int pId;
//    private int userId;
//    private int pListId;
//    private int pDetailId;
//    private int pFromId;
//    private int pExpireDay;
//    private double pRecentlyAdd;
//
//    //////////////Product Show At Home/////////////
//    //private String pTitle;
//    //private String pPrice;
//    //private String pCategory;
//    //private String pImageItem;
//
//    public int getpId() {
//        return pId;
//    }
//
//    public void setpId(int pId) {
//        this.pId = pId;
//    }
//
//    public int getUserId() {
//        return userId;
//    }
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
//
//    public int getpListId() {
//        return pListId;
//    }
//
//    public void setpListId(int pListId) {
//        this.pListId = pListId;
//    }
//
//    public int getpDetailId() {
//        return pDetailId;
//    }
//
//    public void setpDetailId(int pDetailId) {
//        this.pDetailId = pDetailId;
//    }
//
//    public int getpFromId() {
//        return pFromId;
//    }
//
//    public void setpFromId(int pFromId) {
//        this.pFromId = pFromId;
//    }
//
//    public int getpExpireDay() {
//        return pExpireDay;
//    }
//
//    public void setpExpireDay(int pExpireDay) {
//        this.pExpireDay = pExpireDay;
//    }
//
//    public double getpRecentlyAdd() {
//        return pRecentlyAdd;
//    }
//
//    public void setpRecentlyAdd(double pRecentlyAdd) {
//        this.pRecentlyAdd = pRecentlyAdd;
//    }
//
//    public String getpTitle() {
//        return pTitle;
//    }
//
//    public void setpTitle(String pTitle) {
//        this.pTitle = pTitle;
//    }
//
//    public String getpPrice() {
//        return pPrice;
//    }
//
//    public void setpPrice(String pPrice) {
//        this.pPrice = pPrice;
//    }
//
//    public String getpCategory() {
//        return pCategory;
//    }
//
//    public void setpCategory(String pCategory) {
//        this.pCategory = pCategory;
//    }
//
//    public String getpImageItem() {
//        return pImageItem;
//    }
//
//    public void setpImageItem(String pImageItem) {
//        this.pImageItem = pImageItem;
//    }
//    //////////////////////////////////////////////////////////////////////////////////
//    public Product(){}
//    public Product(String pTitle, String ptCategory,String pPrice , String pImageItem) {
//        this.pTitle = pTitle;
//        this.pPrice = pPrice;
//        this.pCategory = ptCategory;
//        this.pImageItem = pImageItem;
//    }

    String name;
    String description;
    String category;
    String subCategory;
    String location;
    String price;
    String image;

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public String getImage(){
        return image;
    }

}