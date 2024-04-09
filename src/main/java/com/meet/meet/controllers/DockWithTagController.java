package com.meet.meet.controllers;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.meet.meet.models.Dock;
import com.meet.meet.models.DockMaster;
import com.meet.meet.models.DockPOJO;
import com.meet.meet.models.DockVehicleAllocation;
import com.meet.meet.repositories.DockMasterRepository;
import com.meet.meet.repositories.DockRepository;
import com.meet.meet.repositories.DockVehicleAllocationRepository;

@Controller
public class DockWithTagController {

	@Autowired
	private DockRepository dockRepository;

	@Autowired
	private DockVehicleAllocationRepository dockVehicleAllocationRepository;

	@Autowired
	private DockMasterRepository dockMasterRepository;

	@GetMapping("/dockTag")
	public String index(Model model) {
		List<Dock> docks = dockRepository.findAll();
		List<DockPOJO> dockPOJOList = new ArrayList();
		for (Dock dock : docks) {

			List<DockVehicleAllocation> activeAllocations = dockVehicleAllocationRepository
					.findByIsActiveTrueAndDock(dock);

			if (activeAllocations != null && activeAllocations.isEmpty() == false) {
				for (DockVehicleAllocation dva : activeAllocations) {
					DockPOJO dockPOJO = new DockPOJO();
					dockPOJO.setId(dock.getId());
					dockPOJO.setActive(dock.isActive());
					dockPOJO.setDockNumber(dock.getDockNumber());
					dockPOJO.setExpress(dock.isExpress());
					dockPOJO.setVehicleNumber(dva.getVehicleNumber());
					dockPOJO.setAllocationId(dva.getId());
					dockPOJO.setEstimatedTime(calculateRemainingTime(dva));
					dockPOJO.setVehicleTag(dva.getVehicleTag());
					dockPOJO.setIs3PDock(dock.isIs3PDock());
					dockPOJO.setDisabled(dock.isDisabled());
					dockPOJOList.add(dockPOJO);
					// dockPOJO.setWaitingTime(null);

				}
			} else {
				DockPOJO dockPOJO = new DockPOJO();
				dockPOJO.setId(dock.getId());
				dockPOJO.setActive(dock.isActive());
				dockPOJO.setDockNumber(dock.getDockNumber());
				dockPOJO.setExpress(dock.isExpress());
				dockPOJO.setIs3PDock(dock.isIs3PDock());
				dockPOJO.setDisabled(dock.isDisabled());
				dockPOJOList.add(dockPOJO);

			}

			// dock.setActiveAllocations(activeAllocations);
		}

		model.addAttribute("docks", dockPOJOList);
		return "dockWithTag";
	}

	@PostMapping("/allocateDockTagWithWaiting")
	public ResponseEntity<String> allocateDockWithWaiting(@RequestParam String vehicleNumber,
			@RequestParam Long estimatedTime, @RequestParam String vehicleTag, @RequestParam boolean is3PDock) {
		// Find an available dock

		// Check if the dock is already allocated
		// List<DockVehicleAllocation> currentAllocations =
		// dockVehicleAllocationRepository.findByDockAndUndockTimeIsNull(dock);
		if (is3PDock) {
			List<Dock> availableDocks = dockRepository.findByIsActiveTrueAndIsDisabledFalseAndIs3PDockTrue();
			if (!availableDocks.isEmpty()) {
				Dock dock = availableDocks.get(0);
				dock.setActive(false);

				DockVehicleAllocation allocation = new DockVehicleAllocation();
				allocation.setDock(dock);
				allocation.setVehicleNumber(vehicleNumber);
				allocation.setDockTime(LocalDateTime.now());
				allocation.setActive(true);
				allocation.setEstimatedTime(estimatedTime);
				allocation.setVehicleTag(vehicleTag);
				dockVehicleAllocationRepository.save(allocation);
				return ResponseEntity.ok().body("Successfully Allocated");
			} else {
				List<Dock> docks = dockRepository.findByIsDisabledFalseAndIs3PDockTrue();
				Optional<Dock> shortestWaitingDock = docks.stream().min(Comparator.comparingLong(dock -> {
					List<DockVehicleAllocation> activeAllocations = dockVehicleAllocationRepository
							.findByIsActiveTrueAndDock(dock);
					// if (!activeAllocations.isEmpty()) {
					long time = calculateRemainingTime(activeAllocations.get(0));
					dock.setWaitTime(time);
					return calculateRemainingTime(activeAllocations.get(0)); // Use the first active allocation
					// }
				}));

				return allocateWaitingTime(vehicleNumber, estimatedTime, shortestWaitingDock, vehicleTag);

			}
		} else {

			List<Dock> availableDocks = dockRepository.findByIsActiveTrueAndIsDisabledFalseAndIs3PDockFalse();

			if (vehicleTag.toUpperCase().contentEquals("PH")) {
				List<Dock> expressDocks = dockRepository.findByIsExpressTrueAndIsDisabledFalse();

				for (Dock expressDock : expressDocks) {

					if (expressDock.getActiveAllocations() != null && expressDock.getActiveAllocations().size() > 0) {

						long count = expressDock.getActiveAllocations().stream().filter(DockVehicleAllocation::isActive)
								.count();

						if (count < 2) {

							expressDock.setActive(false);

							DockVehicleAllocation allocation = new DockVehicleAllocation();
							allocation.setDock(expressDock);
							allocation.setVehicleNumber(vehicleNumber);
							allocation.setDockTime(LocalDateTime.now());
							allocation.setActive(true);
							allocation.setEstimatedTime(estimatedTime);
							allocation.setVehicleTag(vehicleTag);
							dockVehicleAllocationRepository.save(allocation);
							return ResponseEntity.ok().body("Successfully Allocated");

						}
					}
				}
			}

			if (!availableDocks.isEmpty()) {
				Dock dock = availableDocks.get(0);
				dock.setActive(false);

				DockVehicleAllocation allocation = new DockVehicleAllocation();
				allocation.setDock(dock);
				allocation.setVehicleNumber(vehicleNumber);
				allocation.setDockTime(LocalDateTime.now());
				allocation.setActive(true);
				allocation.setEstimatedTime(estimatedTime);
				allocation.setVehicleTag(vehicleTag);
				dockVehicleAllocationRepository.save(allocation);
				return ResponseEntity.ok().body("Successfully Allocated");

			} else {

				// Calculate the undock time based on the waiting time

				// if (!docksAvailable) {
				// return ResponseEntity.badRequest().body("No Empty dock available");

				List<Dock> docks = dockRepository.findByIsDisabledFalseAndIs3PDockFalse();
				Optional<Dock> shortestWaitingDock = docks.stream().min(Comparator.comparingLong(dock -> {
					List<DockVehicleAllocation> activeAllocations = dockVehicleAllocationRepository
							.findByIsActiveTrueAndDock(dock);
					// if (!activeAllocations.isEmpty()) {
					long time = calculateRemainingTime(activeAllocations.get(0));
					dock.setWaitTime(time);
					return calculateRemainingTime(activeAllocations.get(0)); // Use the first active allocation
					// }
				}));

				// Check if is ETA within the promise time
				DockMaster dockMaster = dockMasterRepository.findByTag(vehicleTag); // Assuming "SMH" is the provided
																					// tag
				if (dockMaster == null) {
					return allocateWaitingTime(vehicleNumber, estimatedTime, shortestWaitingDock, vehicleTag);
				}
				LocalTime promiseTime = dockMaster.getPromiseToFinishTime();
				LocalTime dockInTime = dockMaster.getDockInTime();
				LocalDateTime currentTime = LocalDateTime.now();
				LocalDateTime combinedDockInTime = LocalDateTime.of(LocalDate.now(), dockInTime);
				LocalDateTime combinedPromiseTime = LocalDateTime.of(LocalDate.now(), promiseTime);

				LocalDateTime estimatedFinishTimeWithWaitingTime = LocalDateTime.now()
						.plusMinutes(estimatedTime + shortestWaitingDock.get().getWaitTime());

				if (currentTime.isBefore(combinedDockInTime)) {
					return allocateWaitingTime(vehicleNumber, estimatedTime, shortestWaitingDock, vehicleTag);
				}
				if (estimatedFinishTimeWithWaitingTime.isBefore(combinedPromiseTime)) {
					return allocateWaitingTime(vehicleNumber, estimatedTime, shortestWaitingDock, vehicleTag);

				} else {
					// All Allocation with Wait Time
					if (vehicleTag.toUpperCase().contentEquals("PH") || vehicleTag.toUpperCase().contentEquals("FRK")
							|| vehicleTag.toUpperCase().contentEquals("GGN")) {

						List<DockVehicleAllocation> activeAllocations = dockVehicleAllocationRepository
								.findByIsActiveTrueAndUndockTimeIsNullAndWaitingTimeIsNotNull();
						// Logic to check which vehicle can be reallocated,without missing the promise
						// time
						for (DockVehicleAllocation allocation : activeAllocations) {
							DockMaster dockMasterAllocation = dockMasterRepository
									.findByTag(allocation.getVehicleTag());
							if (dockMasterAllocation != null) {
								LocalTime promiseTimeAllocation = dockMaster.getPromiseToFinishTime();
								LocalTime dockInTimeAllocation = dockMaster.getDockInTime();
								LocalDateTime estimatedFinishTimeWithWaitingTimeAgain = LocalDateTime.now().plusMinutes(
										allocation.getEstimatedTime() + shortestWaitingDock.get().getWaitTime());
								LocalDateTime combinedDockInTimeAllocation = LocalDateTime.of(LocalDate.now(),
										dockInTimeAllocation);
								LocalDateTime combinedPromiseTimeAllocation = LocalDateTime.of(LocalDate.now(),
										promiseTimeAllocation);

								if (estimatedFinishTimeWithWaitingTimeAgain.isBefore(combinedPromiseTimeAllocation)) {
									return changeAllocation(vehicleNumber, estimatedTime, vehicleTag,
											shortestWaitingDock, currentTime, allocation);
								}

							} else {
								return changeAllocation(vehicleNumber, estimatedTime, vehicleTag, shortestWaitingDock,
										currentTime, allocation);
							}
						}
					}

				}

//	            if (shortestWaitingDock.get().isAfter(promiseTime)) {
//	            }

				// If not check which Allotted vehicle can be rescheduled so that Priority can
				// be given.

				return allocateWaitingTime(vehicleNumber, estimatedTime, shortestWaitingDock, vehicleTag);

			}
		}
	}

	private ResponseEntity<String> changeAllocation(String vehicleNumber, Long estimatedTime, String vehicleTag,
			Optional<Dock> shortestWaitingDock, LocalDateTime currentTime, DockVehicleAllocation allocation) {
		// Allot allocation ->Shortest waitingtime dock
		allocateWaitingTime(allocation.getVehicleNumber(), estimatedTime, shortestWaitingDock,
				allocation.getVehicleTag());
		// Allot priority vehicle -> current Allocation
		allocation.setVehicleNumber(vehicleNumber);
		allocation.setVehicleTag(vehicleTag);
		allocation.setDockTime(currentTime);
		dockVehicleAllocationRepository.save(allocation);
		return ResponseEntity.ok().body("Successfully Allocated");
	}

	private ResponseEntity<String> allocateWaitingTime(String vehicleNumber, Long estimatedTime,
			Optional<Dock> shortestWaitingDock, String vehicleTag) {
		DockVehicleAllocation allocation = new DockVehicleAllocation();
		allocation.setDock(shortestWaitingDock.get());
		allocation.setVehicleNumber(vehicleNumber);
		allocation.setDockTime(LocalDateTime.now());
		allocation.setVehicleTag(vehicleTag);
		allocation.setActive(true);
		allocation.setEstimatedTime(estimatedTime + shortestWaitingDock.get().getWaitTime());
		allocation.setWaitingTime(estimatedTime + shortestWaitingDock.get().getWaitTime());
		dockVehicleAllocationRepository.save(allocation);
		return ResponseEntity.ok().body("Successfully Allocated");
	}

	private long calculateRemainingTime(DockVehicleAllocation allocation) {
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime dockTime = allocation.getDockTime();
		Duration timeSinceDocking = Duration.between(dockTime, currentTime);
		return Math.max(0, allocation.getEstimatedTime() - timeSinceDocking.toMinutes()); // Ensure remaining time is
																							// non-negative
	}

}