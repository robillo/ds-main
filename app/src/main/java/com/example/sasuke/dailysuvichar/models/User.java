package com.example.sasuke.dailysuvichar.models;

import java.util.HashMap;

/**
 * Created by rishabhshukla on 22/04/17.
 */

public class User {
    private String name;
    private String email;
    private String bio;
    private String preferredLang;
    private String photoUrl;
    private String coverUrl;
    private HashMap<String, Boolean> motivation, religion, astrology, yoga, ayurveda, health, diet;
    private Boolean standardUser = Boolean.TRUE;
    private HashMap<String, HashMap<String, Boolean>> allInterests;
    private String DOB;
    private String gender;
    private String phone;
    private int age;

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setPreferredLang(String preferredLang) {
        this.preferredLang = preferredLang;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setMotivation(HashMap<String, Boolean> motivation) {
        this.motivation = motivation;
    }

    public void setReligion(HashMap<String, Boolean> religion) {
        this.religion = religion;
    }

    public void setAstrology(HashMap<String, Boolean> astrology) {
        this.astrology = astrology;
    }

    public void setYoga(HashMap<String, Boolean> yoga) {
        this.yoga = yoga;
    }

    public void setAyurveda(HashMap<String, Boolean> ayurveda) {
        this.ayurveda = ayurveda;
    }

    public void setHealth(HashMap<String, Boolean> health) {
        this.health = health;
    }

    public void setDiet(HashMap<String, Boolean> diet) {
        this.diet = diet;
    }

    public void setStandardUser(Boolean standardUser) {
        this.standardUser = standardUser;
    }

    public void setAllInterests(HashMap<String, HashMap<String, Boolean>> allInterests) {
        this.allInterests = allInterests;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public User(String name, String email, String bio, String preferredLang, String photoUrl, String coverUrl, HashMap<String, Boolean> motivation, HashMap<String, Boolean> religion, HashMap<String, Boolean> astrology, HashMap<String, Boolean> yoga, HashMap<String, Boolean> ayurveda, HashMap<String, Boolean> health, HashMap<String, Boolean> diet, Boolean standardUser, HashMap<String, HashMap<String, Boolean>> allInterests, String DOB, String gender, String phone, int age) {
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.preferredLang = preferredLang;
        this.photoUrl = photoUrl;
        this.coverUrl = coverUrl;
        this.motivation = motivation;
        this.religion = religion;
        this.astrology = astrology;
        this.yoga = yoga;
        this.ayurveda = ayurveda;
        this.health = health;
        this.diet = diet;
        this.standardUser = standardUser;
        this.allInterests = allInterests;
        this.DOB = DOB;
        this.gender = gender;
        this.phone = phone;
        this.age = age;
    }

    public User(String name, String email, String bio, String preferredLang, String photoUrl, String coverUrl, Boolean standardUser, HashMap<String, HashMap<String, Boolean>> allInterests, String DOB, String gender, String phone, int age) {
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.preferredLang = preferredLang;
        this.photoUrl = photoUrl;
        this.coverUrl = coverUrl;
        this.standardUser = standardUser;
        this.allInterests = allInterests;
        this.DOB = DOB;
        this.gender = gender;
        this.phone = phone;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getBio() {
        return bio;
    }

    public String getPreferredLang() {
        return preferredLang;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public HashMap<String, Boolean> getMotivation() {
        return motivation;
    }

    public HashMap<String, Boolean> getReligion() {
        return religion;
    }

    public HashMap<String, Boolean> getAstrology() {
        return astrology;
    }

    public HashMap<String, Boolean> getYoga() {
        return yoga;
    }

    public HashMap<String, Boolean> getAyurveda() {
        return ayurveda;
    }

    public HashMap<String, Boolean> getHealth() {
        return health;
    }

    public HashMap<String, Boolean> getDiet() {
        return diet;
    }

    public Boolean getStandardUser() {
        return standardUser;
    }

    public HashMap<String, HashMap<String, Boolean>> getAllInterests() {
        return allInterests;
    }

    public String getDOB() {
        return DOB;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public int getAge() {
        return age;
    }
}
