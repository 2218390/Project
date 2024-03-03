package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.List;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Application {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="event_id")
    @JsonIgnoreProperties("applications")
    private Event event;
    private String applicantName;
    private String eventName;
    private String applicationStatus;
    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;
    public Application() {
        super();
        // TODO Auto-generated constructor stub
    }
    public Application(String applicantName, String eventName, String applicationStatus, Event event, User user) {
        super();
        this.event=event;
        this.applicantName=applicantName;
        this.eventName=eventName;
        this.applicationStatus=applicationStatus;
        this.user=user;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }
    public Event getEvent(){
        return event;
    }
    public void setEvent(Event event){
        this.event=event;
    }
    public String getApplicantName(){
        return applicantName;
    }
    public void setApplicantName(String applicantName){
        this.applicantName=applicantName;
    }
    public String getEventName(){
        return eventName;
    }
    public void setEventName(String eventName){
        this.eventName=eventName;
    }
    public String getApplicationStatus(){
        return applicationStatus;
    }
    public void setApplicationStatus(String applicationStatus){
        this.applicationStatus=applicationStatus;
    }
    public User getUser(){
        return user;
    }
    public void setUser(User user){
        this.user=user;
    }
}
