package com.icsd19080_icsd19235.service;

import com.icsd19080_icsd19235.model.*;
import com.icsd19080_icsd19235.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConferenceService {

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Conference> getAllConferences() {
        return conferenceRepository.findAll(); 
    }

    public Optional<Conference> getConference(Long id) {
        return conferenceRepository.findById(id);
    }

    public Conference createConference(Conference conference) {
        Set<User> pcChairs = new HashSet<>();
        for (User chair : conference.getPcChair()) {
            User existingChair = userRepository.findById(chair.getUserId())
                    .orElseThrow(() -> new RuntimeException("PC Chair not found with ID: " + chair.getUserId()));
            pcChairs.add(existingChair);
        }
        conference.setPcChair(pcChairs);
    
        Set<User> pcMembers = new HashSet<>();
        for (User member : conference.getPcMembers()) {
            User existingMember = userRepository.findById(member.getUserId())
                    .orElseThrow(() -> new RuntimeException("PC Member not found with ID: " + member.getUserId()));
            pcMembers.add(existingMember);
        }
        conference.setPcMembers(pcMembers);
    
        Set<Paper> papers = new HashSet<>();
        for (Paper paper : conference.getPapers()) {
            Paper existingPaper = paperRepository.findById(paper.getPaperId())
                    .orElseThrow(() -> new RuntimeException("Paper not found with ID: " + paper.getPaperId()));
            existingPaper.setConference(conference);
            papers.add(existingPaper);
        }
        conference.setPapers(papers);
    
        return conferenceRepository.save(conference);
    }
     
    public Conference updateConference(Long conferenceId, String name, String description, Set<User> pcChairs, Set<User> pcMembers) {
        System.out.println("Updating conference with ID: " + conferenceId);
    
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found with ID: " + conferenceId));
    
        boolean isUpdated = false;
    
        if (name != null) {
            conference.setName(name);
            System.out.println("Updated name to: " + name);
            isUpdated = true;
        }
        if (description != null) {
            conference.setDescription(description);
            System.out.println("Updated description to: " + description);
            isUpdated = true;
        }
    
        if (pcChairs != null && !pcChairs.isEmpty()) {
            conference.getPcChair().clear();
            conference.getPcChair().addAll(pcChairs);
            isUpdated = true;
        }
    
        if (pcMembers != null && !pcMembers.isEmpty()) {
            conference.getPcMembers().clear();
            conference.getPcMembers().addAll(pcMembers);
            isUpdated = true;
        }
    
        if (isUpdated) {
            Conference updatedConference = conferenceRepository.save(conference);
            System.out.println("Saved updated conference: " + updatedConference);
            return updatedConference;
        } else {
            System.out.println("No changes detected. Conference update skipped.");
            return conference;
        }
    }

    public Conference addPCChairs(Long conferenceId, Set<User> pcChair) {
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found"));
    
        Set<User> existingPcChair = conference.getPcChair() != null ? conference.getPcChair() : new HashSet<>();
        for (User user : pcChair){
            User existingUser = userRepository.findById(user.getUserId())
                    .orElseThrow(() -> new RuntimeException());
            existingPcChair.add(existingUser);
        }
        conference.setPcChair(existingPcChair);
        
        return conferenceRepository.save(conference);
    }
    
    public Conference addPCMembers(Long conferenceId, Set<User> pcMembers) {
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found"));
    
        Set<User> updatedPcMembers = new HashSet<>(conference.getPcMembers() != null ? conference.getPcMembers() : new HashSet<>());
        
        for (User user : pcMembers) {
            User existingUser = userRepository.findById(user.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            updatedPcMembers.add(existingUser);
        }
        
        conference.setPcMembers(updatedPcMembers); 
        
        return conferenceRepository.save(conference);
    }
    

    public List<Conference> searchConferences(String name, String description) {
        return conferenceRepository.findByNameContainingIgnoreCaseAndDescriptionContainingIgnoreCase(name, description);
    }

    public Conference ConferenceView(Long conferenceId, User user) {
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found with ID: " + conferenceId));
    
        Conference conferenceView = new Conference();
        conferenceView.setConferenceId(conference.getConferenceId());
        conferenceView.setName(conference.getName());
    
        return conferenceView;
    }
    
    public boolean deleteConference(Long conferenceId) {
        Optional<Conference> optionalConference = conferenceRepository.findById(conferenceId);
        if (optionalConference.isPresent()) {
            Conference conference = optionalConference.get();
    
            if (conference.getState() == Conference.ConferenceState.CREATED) {
                conferenceRepository.delete(conference);
                return true;
            }
        }
        return false;
    }
    
    public boolean startSubmission(Long conferenceId) {
        return updateConferenceState(conferenceId, Conference.ConferenceState.CREATED, Conference.ConferenceState.SUBMISSION);
    }

    public boolean startReviewerAssignment(Long conferenceId) {
        return updateConferenceState(conferenceId, Conference.ConferenceState.SUBMISSION, Conference.ConferenceState.ASSIGNMENT);
    }

    public boolean startReview(Long conferenceId) {
        return updateConferenceState(conferenceId, Conference.ConferenceState.ASSIGNMENT, Conference.ConferenceState.REVIEW);
    }

    public boolean startDecisionMaking(Long conferenceId) {
        return updateConferenceState(conferenceId, Conference.ConferenceState.REVIEW, Conference.ConferenceState.DECISION);
    }

    public boolean startFinalSubmission(Long conferenceId) {
        return updateConferenceState(conferenceId, Conference.ConferenceState.DECISION, Conference.ConferenceState.FINAL_SUBMISSION);
    }

    public boolean endConference(Long conferenceId) {
        boolean isUpdated = updateConferenceState(conferenceId, Conference.ConferenceState.FINAL_SUBMISSION, Conference.ConferenceState.FINAL);
        if (isUpdated) {
            Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found"));
            
            for (Paper paper : conference.getPapers()) {
                if (paper.getState() == Paper.PaperState.APPROVED) {
                    paper.setState(Paper.PaperState.ACCEPTED);
                } else {
                    paper.setState(Paper.PaperState.REJECTED);
                }
                paperRepository.save(paper);
            }
        }
        return isUpdated;
    }

    private boolean updateConferenceState(Long conferenceId, Conference.ConferenceState expectedState, Conference.ConferenceState newState) {
        Optional<Conference> optionalConference = conferenceRepository.findById(conferenceId);
        if (optionalConference.isPresent()) {
            Conference conference = optionalConference.get();
            if (conference.getState() == expectedState) {
                conference.setState(newState);
                conferenceRepository.save(conference);
                return true;
            }
        }
        return false;
    }

}
