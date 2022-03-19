package com.smart.Entities;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name="USER")
public class User {
//    @Override
//	public String toString() {
//		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
//				+ ", enabled=" + enabled + ", imageurl=" + imageurl + ", about=" + about + ", contacts=" + contacts
//				+ "]";
//	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    @Column(unique=true)
    private String email;
    private String password;
    private String role;
    private boolean enabled;        
    private String imageurl;
    @Column(length=500)
    private String about;
    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY,mappedBy = "user",orphanRemoval=true)
    private List<Contact>contacts = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
    
    
}
