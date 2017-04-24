package com.example.sasuke.dailysuvichar.models;

/**
 * Created by rishabhshukla on 24/04/17.
 */

public class Product {
    String name;
    String price;
    String productUrl;
    String imageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Product(String name, String price, String productUrl, String imageUrl) {
        this.name = name;
        this.price = price;
        this.productUrl = productUrl;
        this.imageUrl = imageUrl;
    }
}
