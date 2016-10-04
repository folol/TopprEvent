package com.topprevents.android;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devansh on 9/24/16.
 */
public class Website {


//    Event events ;
//
//    public Event getEvents() {
//        return events;
//    }
//
//    public void setEvents(Event events) {
//        this.events = events;
//    }

    int id;
    String name , image , category , description , experience;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
