package com.example.sasuke.dailysuvichar.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Guru {
    private String name;
    private String email;
    private String bio;
    private ArrayList<String> followers;
    private String preferredLang;
    private String photoUrl;
    private String coverUrl;
    private HashMap<String, Boolean> motivation, religion, astrology, yoga, ayurveda, health, diet;
    private HashMap<String, ArrayList<String>> allInterests;
    private String DOB;
    private String gender;
    private ArrayList<String> selectedSubInterests;
    private String phone;
    private int age;

    public Guru() {
    }

    public Guru(String name, String email, String bio, ArrayList<String> followers, String preferredLang, String photoUrl, String coverUrl, String DOB, String gender, String phone, int age, ArrayList<Post> posts, ArrayList<Blog> blogs, ArrayList<Product> shop, ArrayList<Video> videos, HashMap<String, ArrayList<String>> allInterests, ArrayList<String> selectedSubInterests) {
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.followers = followers;
        this.allInterests = allInterests;
        this.preferredLang = preferredLang;
        this.photoUrl = photoUrl;
        this.coverUrl = coverUrl;
        this.DOB = DOB;
        this.gender = gender;
        this.phone = phone;
        this.age = age;
        this.selectedSubInterests = selectedSubInterests;
    }

    public HashMap<String, ArrayList<String>> getAllInterests() {
        return allInterests;
    }

    public void setAllInterests(HashMap<String, ArrayList<String>> allInterests) {
        this.allInterests = allInterests;
    }

    public ArrayList<String> getSelectedSubInterests() {
        return selectedSubInterests;
    }

    public void setSelectedSubInterests(ArrayList<String> selectedSubInterests) {
        this.selectedSubInterests = selectedSubInterests;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPreferredLang() {
        return preferredLang;
    }

    public void setPreferredLang(String preferredLang) {
        this.preferredLang = preferredLang;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public HashMap<String, Boolean> getMotivation() {
        return motivation;
    }

    public void setMotivation(HashMap<String, Boolean> motivation) {
        this.motivation = motivation;
    }

    public HashMap<String, Boolean> getReligion() {
        return religion;
    }

    public void setReligion(HashMap<String, Boolean> religion) {
        this.religion = religion;
    }

    public HashMap<String, Boolean> getAstrology() {
        return astrology;
    }

    public void setAstrology(HashMap<String, Boolean> astrology) {
        this.astrology = astrology;
    }

    public HashMap<String, Boolean> getYoga() {
        return yoga;
    }

    public void setYoga(HashMap<String, Boolean> yoga) {
        this.yoga = yoga;
    }

    public HashMap<String, Boolean> getAyurveda() {
        return ayurveda;
    }

    public void setAyurveda(HashMap<String, Boolean> ayurveda) {
        this.ayurveda = ayurveda;
    }

    public HashMap<String, Boolean> getHealth() {
        return health;
    }

    public void setHealth(HashMap<String, Boolean> health) {
        this.health = health;
    }

    public HashMap<String, Boolean> getDiet() {
        return diet;
    }

    public void setDiet(HashMap<String, Boolean> diet) {
        this.diet = diet;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


}
