package com.meet.meet.controllers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.meet.meet.models.Dock;
import com.meet.meet.models.DockVehicleAllocation;
import com.meet.meet.repositories.DockRepository;
import com.meet.meet.repositories.DockVehicleAllocationRepository;

@Controller
public class DockController {

	@Autowired
	private DockRepository dockRepository;

	@Autowired
	private DockVehicleAllocationRepository dockVehicleAllocationRepository;

	@GetMapping("/docks")
	public String index(Model model) {
		List<Dock> docks = dockRepository.findAll();

		for (Dock dock : docks) {
			List<DockVehicleAllocation> activeAllocations = dockVehicleAllocationRepository
					.findByIsActiveTrueAndDock(dock);
			dock.setActiveAllocations(activeAllocations);
		}
		model.addAttribute("docks", docks);
		return "index";
	}

	@PostMapping("/allocateDock")
	public ResponseEntity<String> allocateDock(@RequestParam String vehicleNumber,@RequestParam Long estimatedTimeForCompletion) {
		
		List<Dock> availableDocks = dockRepository.findByIsActiveTrue();
		

		if (!availableDocks.isEmpty()) {
			Dock dock = availableDocks.get(0);
			dock.setActive(false);

			DockVehicleAllocation allocation = new DockVehicleAllocation();
			allocation.setDock(dock);
			allocation.setVehicleNumber(vehicleNumber);
			allocation.setDockTime(LocalDateTime.now());
			allocation.setActive(true);
			allocation.setEstimatedTime(estimatedTimeForCompletion);
			dockVehicleAllocationRepository.save(allocation);

		} else {

			List<Dock> expressDocks = dockRepository.findByIsExpressTrue();
			
			for(Dock expressDock: expressDocks) {
				
				if(expressDock.getActiveAllocations()!=null && expressDock.getActiveAllocations().size()>0) {
					
					long count=expressDock.getActiveAllocations().stream()
	                .filter(DockVehicleAllocation::isActive)
	                .count();

						if(count<2) {
							
							expressDock.setActive(false);

							DockVehicleAllocation allocation = new DockVehicleAllocation();
							allocation.setDock(expressDock);
							allocation.setVehicleNumber(vehicleNumber);
							allocation.setDockTime(LocalDateTime.now());
							allocation.setActive(true);
							allocation.setEstimatedTime(estimatedTimeForCompletion);
							dockVehicleAllocationRepository.save(allocation);
							return ResponseEntity.ok().body("Successfully Allocated");

						}
				}
			}
			// if (!docksAvailable) {
			return ResponseEntity.badRequest().body("No Empty dock available");
			// }
			// Handle case when no available docks are found
			// You can add your logic here, like redirecting to an error page
		}
		return ResponseEntity.ok().body("Successfully Allocated");
	}

	@PostMapping("/express/{dockId}/{value}")
	public ResponseEntity<String> allocateDock(@PathVariable Long dockId, @PathVariable String value) {
		Dock dock = dockRepository.findById(dockId).get();
		if (value.equalsIgnoreCase("true")) {
			dock.setExpress(true);

		} else {
			dock.setExpress(false);

		}
		dockRepository.save(dock);
		return ResponseEntity.ok().body("Successfull");

	}

	@PostMapping("/undock/{id}")
	public String undock(@PathVariable Long id) {
		DockVehicleAllocation allocation = dockVehicleAllocationRepository.findById(id).orElse(null);
		if (allocation != null) {
			allocation.setUndockTime(LocalDateTime.now());
			allocation.setActive(false);

			dockVehicleAllocationRepository.save(allocation);

			// Check if there are any active allocations remaining for the dock
			Dock dock = allocation.getDock();
			boolean hasActiveAllocations = dockVehicleAllocationRepository.existsByDockAndUndockTimeIsNull(dock);

			// Update the dock's isActive flag accordingly
			dock.setActive(!hasActiveAllocations);
			dockRepository.save(dock);
		}
		return "redirect:/";
	}

	@PostMapping("/allocateDockWithWaiting")
	public ResponseEntity<String> allocateDockWithWaiting(
			@RequestParam String vehicleNumber) {
		// Find an available dock

		// Check if the dock is already allocated
		// List<DockVehicleAllocation> currentAllocations =
		// dockVehicleAllocationRepository.findByDockAndUndockTimeIsNull(dock);
		List<Dock> availableDocks = dockRepository.findByIsActiveTrue();

		if (!availableDocks.isEmpty()) {
			Dock dock = availableDocks.get(0);
			dock.setActive(false);

			DockVehicleAllocation allocation = new DockVehicleAllocation();
			allocation.setDock(dock);
			allocation.setVehicleNumber(vehicleNumber);
			allocation.setDockTime(LocalDateTime.now());
			allocation.setActive(true);
			dockVehicleAllocationRepository.save(allocation);

		} else {
			// Calculate the undock time based on the waiting time
			
			
			 List<Dock> docks = dockRepository.findAll();
		        Optional<Dock> shortestWaitingDock = docks.stream()
		                .min(Comparator.comparingLong(dock -> {
		                    List<DockVehicleAllocation> activeAllocations = dockVehicleAllocationRepository.findByIsActiveTrueAndDock(dock);
		                  //  if (!activeAllocations.isEmpty()) {
		                    long time=calculateRemainingTime(activeAllocations.get(0));
		                    dock.setWaitTime(time);
		                        return calculateRemainingTime(activeAllocations.get(0)); // Use the first active allocation
		                  //  }
		                }));
		        
		        DockVehicleAllocation allocation = new DockVehicleAllocation();
				allocation.setDock(shortestWaitingDock.get());
				allocation.setVehicleNumber(vehicleNumber);
				allocation.setDockTime(LocalDateTime.now());
				allocation.setActive(true);
				allocation.setEstimatedTime(shortestWaitingDock.get().getWaitTime());
				dockVehicleAllocationRepository.save(allocation);

//			List<Dock> docks = dockRepository.findAll();
//
//			for (Dock dock : docks) {
//				List<DockVehicleAllocation> activeAllocations = dockVehicleAllocationRepository
//						.findByIsActiveTrueAndDock(dock);
//				 Optional<DockVehicleAllocation> optionalDock = activeAllocations.stream()
//		                    .min(Comparator.comparingLong(allocation -> {
//		                        LocalDateTime currentTime = LocalDateTime.now();
//		                        LocalDateTime dockTime = allocation.getDockTime() != null ? allocation.getDockTime() : currentTime;
//		                        Duration timeSinceDocking = Duration.between(dockTime, currentTime);
//		                        Long estimatedTime=timeSinceDocking.toMinutes();
//		                        
//		                      //  long remainingTime = estimatedTimeForCompletion - timeSinceDocking.toMinutes();
//		                        return Math.max(0, remainingTime); // Ensure remaining time is non-negative
//		                    }));
//			
//			}
			
		
			
		//	allocateDock(vehicleNumber,waitingTime,true);
	
		}

//    else {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No available dock found");
//        }

	}
	
	private long calculateRemainingTime(DockVehicleAllocation allocation) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime dockTime = allocation.getDockTime();
        Duration timeSinceDocking = Duration.between(dockTime, currentTime);
        return Math.max(0, allocation.getEstimatedTime() - timeSinceDocking.toMinutes()); // Ensure remaining time is non-negative
    }

}