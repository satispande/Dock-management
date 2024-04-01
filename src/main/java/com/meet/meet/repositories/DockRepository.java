package com.meet.meet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.meet.meet.models.Dock;

public interface DockRepository extends JpaRepository<Dock, Long> {

	List<Dock> findByIsActiveTrue();
	List<Dock> findByIsActiveTrueAndIsExpressTrue();


	Dock findFirstByIsActiveTrue();
	List<Dock> findByIsExpressTrue();
	
//    @Query("SELECT d FROM Dock d LEFT JOIN FETCH d.activeAllocations a WHERE a.isActive = true")
//    List<Dock> findAllActiveDocksAndAllocations();

}