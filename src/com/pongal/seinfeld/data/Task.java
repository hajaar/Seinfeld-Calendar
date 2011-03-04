package com.pongal.seinfeld.data;

import java.util.HashSet;
import java.util.Set;

import android.util.Log;

public final class Task {
	private int id;
	private String text;
	private Set<Date> accomplishedDates = new HashSet<Date>();

	public Task(int id, String text) {
		this.id = id;
		this.text = text;
	}

	public int getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

//	public Set<Date> getAccomplishedDates() {
//		return accomplishedDates;
//	}
	
	public int getAccomplishedDatesCount() {
	    return accomplishedDates.size();
	}
	
	public boolean isAccomplishedDate(Date date) {
	    return accomplishedDates.contains(date);
	}
	
	public void addAccomplishedDates(Date date) {
		Log.d(null, "Adding: " + date + " : "
				+ accomplishedDates.contains(date));
		this.accomplishedDates.add(date);
	}

}