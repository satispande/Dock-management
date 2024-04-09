package com.meet.meet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meet.meet.models.Dock;
import com.meet.meet.models.DockVehicleAllocation;

public interface DockVehicleAllocationRepository extends JpaRepository<DockVehicleAllocation, Long> {

	List<DockVehicleAllocation> findByDockAndUndockTimeIsNull(Dock dock);

	boolean existsByDockAndUndockTimeIsNull(Dock dock);
	
    List<DockVehicleAllocation> findByIsActiveTrueAndDock(Dock dock);
    
	List<DockVehicleAllocation> findByIsActiveTrueAndUndockTimeIsNullAndWaitingTimeIsNotNull();


}