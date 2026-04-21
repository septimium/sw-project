package com.example.recipeapp.model;

import jakarta.validation.constraints.NotBlank;

public class Recipe {
    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "Cuisine 1 is mandatory")
    private String cuisine1;
    @NotBlank(message = "Cuisine 2 is mandatory")
    private String cuisine2;
    @NotBlank(message = "Difficulty is mandatory")
    private String difficulty;

    public Recipe() {}

    public Recipe(String title, String cuisine1, String cuisine2, String difficulty) {
        this.title = title;
        this.cuisine1 = cuisine1;
        this.cuisine2 = cuisine2;
        this.difficulty = difficulty;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCuisine1() { return cuisine1; }
    public void setCuisine1(String cuisine1) { this.cuisine1 = cuisine1; }
    public String getCuisine2() { return cuisine2; }
    public void setCuisine2(String cuisine2) { this.cuisine2 = cuisine2; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
}

