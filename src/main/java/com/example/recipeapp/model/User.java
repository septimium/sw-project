package com.example.recipeapp.model;

import jakarta.validation.constraints.NotBlank;

public class User {
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Surname is mandatory")
    private String surname;
    @NotBlank(message = "Skill Level is mandatory")
    private String skillLevel;
    @NotBlank(message = "Preferred Cuisine is mandatory")
    private String preferredCuisine;

    public User() {}

    public User(String name, String surname, String skillLevel, String preferredCuisine) {
        this.name = name;
        this.surname = surname;
        this.skillLevel = skillLevel;
        this.preferredCuisine = preferredCuisine;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public String getSkillLevel() { return skillLevel; }
    public void setSkillLevel(String skillLevel) { this.skillLevel = skillLevel; }
    public String getPreferredCuisine() { return preferredCuisine; }
    public void setPreferredCuisine(String preferredCuisine) { this.preferredCuisine = preferredCuisine; }
}

