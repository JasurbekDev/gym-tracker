package com.idyllic.activitytracker.data.models;

public class User {
    private String name;
    private double height;
    private String heightUnit;
    private int age;
    private double weight;
    private String weightUnit;
    private String email;
    private String password;

    public User() {
    }

    public User(String name, double height, String heightUnit, int age, double weight, String weightUnit) {
        this.name = name;
        this.height = height;
        this.heightUnit = heightUnit;
        this.age = age;
        this.weight = weight;
        this.weightUnit = weightUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getHeightUnit() {
        return heightUnit;
    }

    public void setHeightUnit(String heightUnit) {
        this.heightUnit = heightUnit;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
