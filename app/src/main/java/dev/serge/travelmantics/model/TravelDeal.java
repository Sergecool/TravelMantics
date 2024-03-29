package dev.serge.travelmantics.model;

import java.io.Serializable;

/**
 * A model class that represent a travel deal object
 */

public class TravelDeal implements Serializable {
    private String id;
    private String title;
    private String price;
    private String description;
    private String imageUrl;
    private String imageName;

    public TravelDeal() {}

    public TravelDeal(String title, String price, String description, String imageUrl, String imageName) {
        this.setId(id);
        this.setTitle(title);
        this.setPrice(price);
        this.setDescription(description);
        this.setImageUrl(imageUrl);
        this.setImageName(imageName);
     }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}