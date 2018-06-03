package com.example.cookit;

public class Ingredient {
    private String quantity;
    private String description;

    public Ingredient(String q, String d) {
        quantity = q;
        description = d;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
