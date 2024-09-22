package com.icsd19080_icsd19235.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.icsd19080_icsd19235.model.Conference;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {
    List<Conference> findByNameContainingIgnoreCaseAndDescriptionContainingIgnoreCase(String name, String description);
}


