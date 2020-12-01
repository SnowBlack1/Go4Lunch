package com.aureliev.go4lunch.model;

public class User {
    private String name;
    private String email;
    private String profilePicture;
    //private Restaurant chosenRestaurant;
    //private List<Restaurant> restaurantListFavorites;
    //private boolean chooseRestaurant; si user a choisi ce restaurant ci ou pas

    //CONSTRUCTORS
    /** Empty constructor for Firebase*/
    public User(){}

    /**Constructor to create an User in Firebase*/
    public User(String name,String email,String profilePicture){
        this.name = name;
        this.email = email;
        this.profilePicture = profilePicture;

    }

    //GETTERS & SETTERS
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }






}
