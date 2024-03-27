package com.example.demo.model;

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
	@Column(unique=true)
	String telephone_number;
	
	@NotBlank
	String password;


	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] profilePicture;

	@ManyToMany
	@JoinTable(
			name = "user_favorite_Uslugas",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "Usluga_id")
	)
	private List<Usluga> favoriteUslugas = new ArrayList<>();


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
		
	 
	 public User(String name, String email, String telephone_number, String password, byte[] profilePicture, List<Usluga> favoriteUslugas) {
		super();
		this.name = name;
		this.email = email;
		this.telephone_number = telephone_number;
		this.password = password;
		this.profilePicture = profilePicture;
		this.favoriteUslugas=favoriteUslugas;
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

	public String getTelephone_number() {
		return telephone_number;
	}

	public void setTelephone_number(String telephone_number) {
		this.telephone_number = telephone_number;
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

	public byte[] getProfilePicture(){return profilePicture;}
	public void setProfilePicture(byte[] profilePicture){this.profilePicture=profilePicture;}



	public List<Usluga> getFavoriteUslugas(){return favoriteUslugas;}
	public void setFavoriteUslugas(List<Usluga> favoriteUslugas){this.favoriteUslugas=favoriteUslugas;}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + "]";
	}
	

}
