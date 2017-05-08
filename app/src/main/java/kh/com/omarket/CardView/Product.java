package kh.com.omarket.CardView;

public class Product {

    private String userId;
    private String category;
    private String subCategory;
    private String name;
    private String description;
    private String location;
    private String price;
    private String image;
    private String key;

    private String[] images;

    public Product() {
        //  Default constructor
    }

    public Product(String userId, String category, String subCategory, String name, String description,
                   String location, String price, String[] images, String key) {
        this.userId = userId;
        this.category = category;
        this.subCategory = subCategory;
        this.name = name;
        this.description = description;
        this.location = location;
        this.price = price;
        this.images = images;
        this.key = key;
    }

    public Product(String userId, String category, String subCategory, String name, String description,
                   String location, String price, String image, String key) {
        this.userId = userId;
        this.category = category;
        this.subCategory = subCategory;
        this.name = name;
        this.description = description;
        this.location = location;
        this.price = price;
        this.image = image;
        this.key = key;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }
}