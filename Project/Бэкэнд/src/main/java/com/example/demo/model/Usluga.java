package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Usluga {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;

    private String location;
    private String coordinates;
    private Date date;
    private String userName;



    public Usluga() {
        super();
        // TODO Auto-generated constructor stub
    }


    public Usluga(String name, String description, User user, String location, String coordinates, Date date, String userName) {
        super();
        this.name = name;
        this.description=description;
        this.user=user;
        this.location=location;
        this.coordinates=coordinates;
        this.date=date;
        this.userName=userName;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public String getLocation(){return location;}
    public void setLocation(String location){this.location=location;}
    public String getCoordinates(){return coordinates;}
    public void setCoordinates(String coordinates){this.coordinates=coordinates;}
    public Date getDate(){return date;}
    public void setDate(Date date){this.date=date;}
    public String getUserName(){return userName;}
    public void setUserName(String userName){this.userName=userName;}
}

