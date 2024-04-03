package com.meet.meet.models;

import java.time.LocalDateTime;

public class DockPOJO {
	
    private Long id;


	private String dockNumber;
    
    private boolean isExpress;
    
    private boolean isActive;

    
    public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	private Long allocationId;

    private String vehicleNumber;

    private LocalDateTime dockTime;

    private LocalDateTime undockTime;
        
    private Long estimatedTime;
    
    private Long waitingTime;
    

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

	public boolean isExpress() {
		return isExpress;
	}

	public void setExpress(boolean isExpress) {
		this.isExpress = isExpress;
	}

	public Long getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(Long allocationId) {
		this.allocationId = allocationId;
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

	public Long getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(Long estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public Long getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(Long waitingTime) {
		this.waitingTime = waitingTime;
	}
}
