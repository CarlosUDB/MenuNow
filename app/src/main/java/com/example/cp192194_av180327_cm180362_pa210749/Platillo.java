package com.example.cp192194_av180327_cm180362_pa210749;

public class Platillo {
    private String id, name, description, price, imageUrl, date;

    // Constructor vacío necesario para usar Firebase
    public Platillo() {
    }

    // Constructor con todos los atributos
    public Platillo(String id,String name, String description, String price, String imageUrl, String date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.date = date;
    }

    // Getters y Setters

    public String getId() {
        return id;
    }
    public String setId() { return id;}
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
