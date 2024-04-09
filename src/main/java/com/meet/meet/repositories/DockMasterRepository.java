package com.meet.meet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meet.meet.models.Dock;
import com.meet.meet.models.DockMaster;

public interface DockMasterRepository extends JpaRepository<DockMaster, Long> {

	DockMaster findByTag(String tag);


}