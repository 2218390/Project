package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Application {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="usluga_id")
    @JsonIgnoreProperties("applications")
    private Usluga usluga;
    private String applicantName;
    private String eventName;
    private String applicationStatus;
    private String applicantEmail;
    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;
    public Application() {
        super();
        // TODO Auto-generated constructor stub
    }
    public Application(String applicantName, String eventName, String applicationStatus, String applicantEmail, Usluga usluga, User user) {
        super();
        this.usluga=usluga;
        this.applicantName=applicantName;
        this.eventName=eventName;
        this.applicationStatus=applicationStatus;
        this.applicantEmail=applicantEmail;
        this.user=user;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }
    public Usluga getUsluga(){
        return usluga;
    }
    public void setUsluga(Usluga event){
        this.usluga=usluga;
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
    public String getApplicantEmail(){return applicantEmail;}
    public void setApplicantEmail(String applicantEmail){this.applicantEmail=applicantEmail;}
    public User getUser(){
        return user;
    }
    public void setUser(User user){
        this.user=user;
    }
}
