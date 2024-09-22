package com.icsd19080_icsd19235.service;

import com.icsd19080_icsd19235.model.*;
import com.icsd19080_icsd19235.model.Conference.ConferenceState;
import com.icsd19080_icsd19235.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
@Service
public class PaperService {

    //DEBUG
    String red = "\033[31m";
    String reset = "\033[0m";

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;


    public List<Paper> getAllPapers() {
        return paperRepository.findAll();
    }

    public Optional<Paper> getPaperById(Long id) {
        return paperRepository.findById(id);
    }

    public Paper createPaper(Long conferenceId, Paper paper) {
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found with ID: " + conferenceId));

        paper.setConference(conference);

        return paperRepository.save(paper);
    }
      
    public void deletePaper(Long id) {
        paperRepository.deleteById(id);
    }

    public Paper updatePaper(Long paperId, String title, String abstractContent, String authors, String content) {
        // Fetch the existing paper
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new RuntimeException("Paper not found with ID: " + paperId));
    
        // Get existing authors and convert to a list
        List<String> existingAuthors = new ArrayList<>(Arrays.asList(paper.getAuthorNames().split(",")));
    
        // Split the incoming authors string and add new authors to the list
        List<String> newAuthors = Arrays.asList(authors.split(","));
        for (String author : newAuthors) {
            if (!existingAuthors.contains(author)) {
                existingAuthors.add(author);
            }
        }
    
        // Convert the list back to a comma-separated string
        String updatedAuthors = String.join(",", existingAuthors);
    
        // Update only the fields that are provided
        if (title != null) {
            paper.setTitle(title);
        }
        if (abstractContent != null) {
            paper.setAbstractText(abstractContent);
        }
        if (authors != null) {
            paper.setAuthorNames(updatedAuthors);
        }
        if (content != null) {
            paper.setContent(content);
        }
    
        // Save and return the updated paper
        return paperRepository.save(paper);
    }
    
    public Paper addCoAuthors(Long paperId, Set<User> coAuthors) {
        // Fetch the paper by its ID
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new RuntimeException("Paper not found with ID: " + paperId));

        // Handle co-authors
        Set<User> existingCoAuthors = paper.getCoAuthors() != null ? paper.getCoAuthors() : new HashSet<>();
        for (User user : coAuthors) {
            // Fetch each user by their ID and add to co-authors
            User existingUser = userRepository.findById(user.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + user.getUserId()));
            existingCoAuthors.add(existingUser);
        }
        paper.setCoAuthors(existingCoAuthors);

        // Save the updated paper
        return paperRepository.save(paper);
    }

    public Paper submitPaper(Long paperId) {
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new RuntimeException("Paper not found with ID: " + paperId));
        

        Conference conference = paper.getConference();
        if (conference == null) {
            throw new RuntimeException("The paper is not associated with any conference.");
        }
    
        if (conference.getState() != Conference.ConferenceState.SUBMISSION) {
            throw new RuntimeException("Paper submission is not allowed as the conference is not in the SUBMISSION state.");
        }   

        if (paper.getContent() == null || paper.getContent().trim().isEmpty()) {
            throw new RuntimeException("Paper content cannot be empty.");
        }
            paper.setSubmitted(true);
    
        Set<Paper> papers = conference.getPapers();
        if (papers == null) {
            papers = new HashSet<>();
            conference.setPapers(papers);
        }
        
        papers.add(paper); 
        paperRepository.save(paper); 
        conferenceRepository.save(conference);
        return paper;
    }
    
    public Paper assignReviewers(Long paperId, Long reviewerId1, Long reviewerId2) {
        // Fetch the paper by ID
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new RuntimeException("Paper not found with ID: " + paperId));
    
        // Fetch the conference associated with the paper
        Conference conference = paper.getConference();
    
        // Check if the conference is in the ASSIGNMENT state
        if (conference.getState() != Conference.ConferenceState.ASSIGNMENT) {
            throw new RuntimeException("Reviewer assignment is not allowed as the conference is not in the ASSIGNMENT state.");
        }
    
        // Fetch reviewers by ID
        User reviewer1 = userRepository.findById(reviewerId1)
                .orElseThrow(() -> new RuntimeException("Reviewer not found with ID: " + reviewerId1));
        User reviewer2 = userRepository.findById(reviewerId2)
                .orElseThrow(() -> new RuntimeException("Reviewer not found with ID: " + reviewerId2));
    
        // Check if the reviewers are members of the PC for the conference
        boolean isReviewer1PcMember = conference.getPcMembers().stream()
                .anyMatch(user -> user.getUserId().equals(reviewerId1));
        boolean isReviewer2PcMember = conference.getPcMembers().stream()
                .anyMatch(user -> user.getUserId().equals(reviewerId2));
    
        if (!isReviewer1PcMember || !isReviewer2PcMember) {
            throw new RuntimeException("At least one of the reviewers is not a member of the Programme Committee for the conference.");
        }
    
        // Check the number of existing reviewers
        int existingReviewers = 0;
        if (paper.getReviewer1() != null) existingReviewers++;
        if (paper.getReviewer2() != null) existingReviewers++;
    
        if (existingReviewers >= 2) {
            throw new RuntimeException("The maximum number of reviewers has already been reached.");
        }
    
        // Assign the reviewers
        if (paper.getReviewer1() == null) {
            paper.setReviewer1(reviewer1);
        }
        if (paper.getReviewer2() == null && !paper.getReviewer1().equals(reviewer2)) {
            paper.setReviewer2(reviewer2);
        } else if (paper.getReviewer2() != null && paper.getReviewer2().equals(reviewer2)) {
            throw new RuntimeException("The second reviewer is the same as the first one.");
        }
    
        // Save the updated paper
        return paperRepository.save(paper);
    }
       
    public Paper submitReview(Long paperId, Integer reviewer1Score, String reviewer1Comments, Integer reviewer2Score, String reviewer2Comments) {
        // Fetch the paper by ID
        Paper paper = paperRepository.findById(paperId)
        .orElseThrow(() -> new RuntimeException("Paper not found with ID: " + paperId));

        // Fetch the conference associated with the paper
        Conference conference = paper.getConference();

       // Check if the conference is in the REVIEW state
        if (conference.getState() != Conference.ConferenceState.REVIEW) {
        throw new RuntimeException("Review is not allowed as the conference is not in the REVIEW state.");
        }

        // Update the paper with the review
        if (paper.getReviewer1() != null) {
        // Check if reviewer1's score and comments should be updated
        if (reviewer1Score != null) {
            paper.setReviewer1Score(reviewer1Score);
        }
        if (reviewer1Comments != null) {
             paper.setReviewer1Comments(reviewer1Comments);
        }
        }

        if (paper.getReviewer2() != null) {
        // Check if reviewer2's score and comments should be updated
         if (reviewer2Score != null) {
            paper.setReviewer2Score(reviewer2Score);
        }
        if (reviewer2Comments != null) {
            paper.setReviewer2Comments(reviewer2Comments);
        }
        }

        // Save the updated paper
        return paperRepository.save(paper);
    }
    
    public Paper approvePaper(Long paperId) {
        // Fetch the paper by ID
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new RuntimeException("Paper not found with ID: " + paperId));
    
        // Fetch the conference associated with the paper
        Conference conference = paper.getConference();
    
        // Check if the conference is in the DECISION state
        if (conference.getState() != Conference.ConferenceState.DECISION) {
            throw new RuntimeException("Paper approval is not allowed as the conference is not in the DECISION state.");
        }
    
        // Update the paper status to APPROVED and indicate it needs modifications
        paper.setState(Paper.PaperState.APPROVED);
        paper.setNeedsModification(true);
    
        // Save the updated paper
        return paperRepository.save(paper);
    }
    
    public Paper rejectPaper(Long paperId) {
        // Fetch the paper by ID
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new RuntimeException("Paper not found with ID: " + paperId));
    
        // Fetch the conference associated with the paper
        Conference conference = paper.getConference();
    
        // Check if the conference is in the DECISION state
        if (conference.getState() != Conference.ConferenceState.DECISION) {
            throw new RuntimeException("Paper rejection is not allowed as the conference is not in the DECISION state.");
        }
    
        // Update the paper status to REJECTED
        paper.setState(Paper.PaperState.REJECTED);
    
        // Save the updated paper
        return paperRepository.save(paper);
    }
    
    public void submitFinalPaper(Long paperId, String finalContent, String addressingReviewer1Comments, String addressingReviewer2Comments) {
        // Fetch the paper by ID
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new RuntimeException("Paper not found with ID: " + paperId));
    
        // Fetch the conference associated with the paper
        Conference conference = paper.getConference();
    
        // Check if the conference is in the FINAL_SUBMISSION state
        if (conference.getState() != Conference.ConferenceState.FINAL_SUBMISSION) {
            throw new RuntimeException("Final submission is not allowed as the conference is not in the FINAL_SUBMISSION state.");
        }
    
        // Update the paper with the final submission only if the provided arguments are not null
        if (finalContent != null) {
            paper.setContent(finalContent);
        }
        if (addressingReviewer1Comments != null) {
            paper.setReviewer1Comments(addressingReviewer1Comments);
        }
        if (addressingReviewer2Comments != null) {
            paper.setReviewer2Comments(addressingReviewer2Comments);
        }
    
        // Save the updated paper
        paperRepository.save(paper);
    }
    
    public Paper acceptPaper(Long paperId) {
        Paper paper = paperRepository.findById(paperId)
            .orElseThrow(() -> new RuntimeException("Paper not found with ID: " + paperId));

        Conference conference = paper.getConference();
        if (conference.getState() != ConferenceState.FINAL) {
            throw new RuntimeException("Conference is not in the FINAL state.");
        }

        paper.setState(Paper.PaperState.ACCEPTED);
        return paperRepository.save(paper);
    }

    public List<Paper> searchPapers(String title, String authors, String abstractContent) {
        if (title != null && !title.isEmpty() && authors != null && !authors.isEmpty() && abstractContent != null && !abstractContent.isEmpty()) {
            // Search by title, authors, and abstract
            return paperRepository.findByTitleContainingAndAuthorNamesContainingAndAbstractTextContainingOrderByTitle(title, authors, abstractContent);
        } else if (title != null && !title.isEmpty() && authors != null && !authors.isEmpty()) {
            // Search by title and authors
            return paperRepository.findByTitleContainingAndAuthorNamesContainingOrderByTitle(title, authors);
        } else if (title != null && !title.isEmpty() && abstractContent != null && !abstractContent.isEmpty()) {
            // Search by title and abstract
            return paperRepository.findByTitleContainingAndAbstractTextContainingOrderByTitle(title, abstractContent);
        } else if (authors != null && !authors.isEmpty() && abstractContent != null && !abstractContent.isEmpty()) {
            // Search by authors and abstract
            return paperRepository.findByAuthorNamesContainingAndAbstractTextContainingOrderByTitle(authors, abstractContent);
        } else if (title != null && !title.isEmpty()) {
            // Search by title
            return paperRepository.findByTitleContainingOrderByTitle(title);
        } else if (authors != null && !authors.isEmpty()) {
            // Search by authors
            return paperRepository.findByAuthorNamesContainingOrderByTitle(authors);
        } else if (abstractContent != null && !abstractContent.isEmpty()) {
            // Search by abstract
            return paperRepository.findByAbstractTextContainingOrderByTitle(abstractContent);
        } else {
            // No search criteria, return all papers ordered by title
            return paperRepository.findAllByOrderByTitle();
        }
    }

    public Paper viewPaperById(Long paperId, Principal principal) {
    Paper paper = paperRepository.findById(paperId)
        .orElseThrow(() -> new RuntimeException("Paper not found with ID: " + paperId));
    
    return paper;
    }

    public void withdrawPaper(Long paperId) {
        Paper paper = paperRepository.findById(paperId)
            .orElseThrow(() -> new RuntimeException("Paper not found with ID: " + paperId));
    
        paperRepository.delete(paper);
    }
    
}
