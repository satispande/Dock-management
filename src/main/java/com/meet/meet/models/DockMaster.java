package com.meet.meet.models;

import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DockMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tag;

    private LocalTime dockInTime;
    
	private LocalTime promiseToFinishTime;
	
    private Long priority;


    public Long getPriority() {
		return priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public LocalTime getDockInTime() {
		return dockInTime;
	}

	public void setDockInTime(LocalTime dockInTime) {
		this.dockInTime = dockInTime;
	}

	public LocalTime getPromiseToFinishTime() {
		return promiseToFinishTime;
	}

	public void setPromiseToFinishTime(LocalTime promiseToFinishTime) {
		this.promiseToFinishTime = promiseToFinishTime;
	}


    // Getters and setters
}