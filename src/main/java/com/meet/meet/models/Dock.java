package com.meet.meet.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

@Entity
public class Dock {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String dockNumber;

	private boolean isActive;

	private boolean isExpress;
	
	private boolean is3PDock;

	private boolean isDisabled;


	@Transient
	private long waitTime;

	public long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

	public boolean isExpress() {
		return isExpress;
	}

	public void setExpress(boolean isExpress) {
		this.isExpress = isExpress;
	}

	@OneToMany(mappedBy = "dock", cascade = CascadeType.ALL)
	private List<DockVehicleAllocation> activeAllocations = new ArrayList<>();

//	@ManyToOne
//	private DockMaster dockMaster;
//
//	public DockMaster getDockMaster() {
//		return dockMaster;
//	}
//
//	public void setDockMaster(DockMaster dockMaster) {
//		this.dockMaster = dockMaster;
//	}

	public List<DockVehicleAllocation> getActiveAllocations() {
		return activeAllocations;
	}

	public void setActiveAllocations(List<DockVehicleAllocation> activeAllocations) {
		this.activeAllocations = activeAllocations;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDockNumber() {
		return dockNumber;
	}

	public void setDockNumber(String dockNumber) {
		this.dockNumber = dockNumber;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isIs3PDock() {
		return is3PDock;
	}

	public void setIs3PDock(boolean is3pDock) {
		is3PDock = is3pDock;
	}

	public boolean isDisabled() {
		return isDisabled;
	}

	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	// Getters and setters
}