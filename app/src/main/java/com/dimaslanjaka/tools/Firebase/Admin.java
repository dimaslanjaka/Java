package com.dimaslanjaka.tools.Firebase;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Admin {

	public String username;
	public String email;

	public Admin() {
		// Default constructor required for calls to DataSnapshot.getValue(User.class)
	}

	public Admin(String username, String email) {
		this.username = username;
		this.email = email;
	}

}