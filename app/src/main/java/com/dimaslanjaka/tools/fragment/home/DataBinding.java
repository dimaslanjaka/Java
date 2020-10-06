package com.dimaslanjaka.tools.fragment.home;

public class DataBinding {

	public Class<?> classname = null;
	public String thumbnail;
	public String title;
	public String description;

	public DataBinding(String thumbnail, String title, String description, Class<?> className) {
		this.thumbnail = thumbnail;
		this.title = title;
		this.description = description;
		this.classname = className;
	}

	public DataBinding(String thumbnail, String title, String description) {
		this.thumbnail = thumbnail;
		this.title = title;
		this.description = description;
	}
}
