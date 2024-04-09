package com.meet.meet.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class DockVehicleAllocation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "dock_id")
	private Dock dock;

	private String vehicleNumber;
	
	private String vehicleTag;

	public String getVehicleTag() {
		return vehicleTag;
	}

	public void setVehicleTag(String vehicleTag) {
		this.vehicleTag = vehicleTag;
	}

	private LocalDateTime dockTime;

	private LocalDateTime undockTime;

	private boolean isActive;

	private Long estimatedTime;

	private Long waitingTime;

	public Long getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(Long waitingTime) {
		this.waitingTime = waitingTime;
	}

	public Long getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(Long estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Dock getDock() {
		return dock;
	}

	public void setDock(Dock dock) {
		this.dock = dock;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public LocalDateTime getDockTime() {
		return dockTime;
	}

	public void setDockTime(LocalDateTime dockTime) {
		this.dockTime = dockTime;
	}

	public LocalDateTime getUndockTime() {
		return undockTime;
	}

	public void setUndockTime(LocalDateTime undockTime) {
		this.undockTime = undockTime;
	}

	// Getters and setters
}