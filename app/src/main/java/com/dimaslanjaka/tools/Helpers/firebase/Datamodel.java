package com.dimaslanjaka.tools.Helpers.firebase;

public class Datamodel {
	public String strings;
	public int integers;
	public long longs;
	public boolean booleans;

	public Datamodel(boolean data) {
		booleans = data;
	}

	public Datamodel(String data) {
		strings = data;
	}

	public Datamodel(int data) {
		integers = data;
	}

	public Datamodel(long data) {
		longs = data;
	}
}
