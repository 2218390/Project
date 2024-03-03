package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//Let's create a simple User class
@Entity
@Table(name = "Users")
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;


	@NotBlank
	String name;
	
	@NotBlank
	@Column(unique=true)
	String email;
	
	@NotBlank
	String password;


	String mission;

	@OneToMany(mappedBy="user", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Event> experience=new ArrayList<>();

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] profilePicture;

	@ManyToMany
	@JoinTable(
			name = "user_favorite_events",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "event_id")
	)
	private List<Event> favoriteEvents = new ArrayList<>();
	@OneToMany(mappedBy="user", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Event> events=new ArrayList<>();

	@OneToMany(mappedBy="user", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Application> applications=new ArrayList<>();
	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdAt;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedAt;
	
	
	 public User() {
			super();
			// TODO Auto-generated constructor stub
	}
		
	 
	 public User(String name, String email, String password, String mission, byte[] profilePicture, List<Event> events, List<Application> applications, List<Event> experience, List<Event> favoriteEvents) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.mission = mission;
		this.profilePicture = profilePicture;
		this.events=events;
		this.applications=applications;
		this.experience=experience;
		this.favoriteEvents=favoriteEvents;
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}



	public String getMission(){return mission;}
	public void setMission(String mission){this.mission=mission;}

	public byte[] getProfilePicture(){return profilePicture;}
	public void setProfilePicture(byte[] profilePicture){this.profilePicture=profilePicture;}

	public List<Event> getEvents(){return events;}
	public void setEvents(List<Event> events){this.events=events;}

	public List<Application> getApplications(){return applications;}
	public void setApplications(List<Application> applications){
		 this.applications=applications;
	}
	public List<Event> getExperience(){return experience;}
	public void setExperience(List<Event> experience){this.experience=experience;}
	public List<Event> getFavoriteEvents(){return favoriteEvents;}
	public void setFavoriteEvents(List<Event> favoriteEvents){this.favoriteEvents=favoriteEvents;}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", mission=" + mission + "]";
	}
	

}
