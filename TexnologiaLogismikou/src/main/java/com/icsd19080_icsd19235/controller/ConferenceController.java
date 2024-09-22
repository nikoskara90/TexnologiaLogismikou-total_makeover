package com.icsd19080_icsd19235.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import com.icsd19080_icsd19235.service.ConferenceService;
import com.icsd19080_icsd19235.model.Conference;
import com.icsd19080_icsd19235.model.User;

import java.util.*;

@RestController
@RequestMapping("/api/conferences")
public class ConferenceController {

    @Autowired
    private ConferenceService conferenceService;

    @GetMapping
    public ResponseEntity<List<Conference>> getAllConferences() {
        List<Conference> conferences = conferenceService.getAllConferences();
        return ResponseEntity.ok(conferences);
        
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conference> getConference(@PathVariable Long id) {
        Optional<Conference> conference = conferenceService.getConference(id);
        return conference.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
   
    @PutMapping("/{id}")
    public ResponseEntity<Conference> updateConference(
            @PathVariable("id") Long conferenceId,
            @RequestBody Conference updateRequest) { 
    
        Set<User> pcChair = updateRequest.getPcChair();
        Set<User> pcMembers = updateRequest.getPcMembers();
    
        Conference updatedConference = conferenceService.updateConference(
            conferenceId,
            updateRequest.getName(),
            updateRequest.getDescription(),
            pcChair,  
            pcMembers
        );
    
        return ResponseEntity.ok(updatedConference);
    }
    
    @PostMapping
    public ResponseEntity<Conference> createConference(@RequestBody Conference conference) {
        Conference createdConference = conferenceService.createConference(conference);
        return ResponseEntity.ok(createdConference);
    }

    @PostMapping("/{id}/add-pc-chairs")
    public Conference addPCChairs(@PathVariable("id") Long conferenceId, @RequestBody Set<User> pcChair) {
        return conferenceService.addPCChairs(conferenceId, pcChair);
    }

    @PostMapping("/{id}/add-pc-members")
    public Conference addPCMembers(@PathVariable("id") Long conferenceId, @RequestBody Set<User> pcMembers) {
        return conferenceService.addPCMembers(conferenceId, pcMembers);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Conference>> searchConferences(@RequestBody Conference searchCriteria) {
        String name = searchCriteria.getName() != null ? searchCriteria.getName() : "";
        String description = searchCriteria.getDescription() != null ? searchCriteria.getDescription() : "";
        
        List<Conference> conferences = conferenceService.searchConferences(name, description);
        return ResponseEntity.ok(conferences);
    }

    @GetMapping("/conferences/{id}/view")
    public ResponseEntity<Conference> ConferenceView(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal(); 
    
        Conference conferenceView = conferenceService.ConferenceView(id, user);
        
        return ResponseEntity.ok(conferenceView);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConference(@PathVariable("id") Long conferenceId) {
        boolean isDeleted = conferenceService.deleteConference(conferenceId);
        if (isDeleted) {
            return ResponseEntity.ok("Conference deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body("You are not authorized to delete this conference or the conference is not in a deletable state.");
        }
    }
    
    @PutMapping("/{id}/start-submission")
    public ResponseEntity<String> startSubmission(@PathVariable("id") Long conferenceId) {
        boolean isStarted = conferenceService.startSubmission(conferenceId);
        return isStarted ? ResponseEntity.ok("Submission started successfully.") :
                        ResponseEntity.status(HttpStatus.FORBIDDEN).body("Conference is not in the correct state.");
    }

    @PutMapping("/{id}/start-reviewer-assignment")
    public ResponseEntity<String> startReviewerAssignment(@PathVariable("id") Long conferenceId) {
        boolean isStarted = conferenceService.startReviewerAssignment(conferenceId);
        return isStarted ? ResponseEntity.ok("Reviewer assignment started successfully.") :
                        ResponseEntity.status(HttpStatus.FORBIDDEN).body("Conference is not in the correct state.");
    }

    @PutMapping("/{id}/start-review")
    public ResponseEntity<String> startReview(@PathVariable("id") Long conferenceId) {
        boolean isStarted = conferenceService.startReview(conferenceId);
        return isStarted ? ResponseEntity.ok("Review started successfully.") :
                        ResponseEntity.status(HttpStatus.FORBIDDEN).body("Conference is not in the correct state.");
    }

    @PutMapping("/{id}/start-decision-making")
    public ResponseEntity<String> startDecisionMaking(@PathVariable("id") Long conferenceId) {
        boolean isStarted = conferenceService.startDecisionMaking(conferenceId);
        return isStarted ? ResponseEntity.ok("Decision making started successfully.") :
                        ResponseEntity.status(HttpStatus.FORBIDDEN).body("Conference is not in the correct state.");
    }

    @PutMapping("/{id}/start-final-submission")
    public ResponseEntity<String> startFinalSubmission(@PathVariable("id") Long conferenceId) {
        boolean isStarted = conferenceService.startFinalSubmission(conferenceId);
        return isStarted ? ResponseEntity.ok("Final submission started successfully.") :
                        ResponseEntity.status(HttpStatus.FORBIDDEN).body("Conference is not in the correct state.");
    }

    @PutMapping("/{id}/end-conference")
    public ResponseEntity<String> endConference(@PathVariable("id") Long conferenceId) {
        boolean isEnded = conferenceService.endConference(conferenceId);
        return isEnded ? ResponseEntity.ok("Conference ended successfully.") :
                        ResponseEntity.status(HttpStatus.FORBIDDEN).body("Conference is not in the correct state.");
    }

}
