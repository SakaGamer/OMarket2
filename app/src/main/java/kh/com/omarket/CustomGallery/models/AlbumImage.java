package kh.com.omarket.CustomGallery.models;

public class AlbumImage {

    private String name;
    private String cover;

    public AlbumImage(String name, String cover) {
        this.name = name;
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
