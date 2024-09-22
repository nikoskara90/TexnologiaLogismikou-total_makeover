package com.icsd19080_icsd19235.controller;

import com.icsd19080_icsd19235.model.*;
import com.icsd19080_icsd19235.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/papers")
public class PaperController {

    @Autowired
    private PaperService paperService;

    @GetMapping
    public List<Paper> getAllPapers() {
        return paperService.getAllPapers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paper> getPaperById(@PathVariable Long id) {
        return paperService.getPaperById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{conferenceId}")
    public ResponseEntity<Paper> createPaper(@PathVariable Long conferenceId, @RequestBody Paper paper) {
        Paper createdPaper = paperService.createPaper(conferenceId, paper);
        return ResponseEntity.ok(createdPaper);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paper> updatePaper(
            @PathVariable("id") Long paperId,
            @RequestBody Paper updateRequest) {
        return ResponseEntity.ok(paperService.updatePaper(
            paperId, 
            updateRequest.getTitle(), 
            updateRequest.getAbstractText(), 
            updateRequest.getAuthorNames(), 
            updateRequest.getContent()
        ));
    }
    
    @PostMapping("/{paperId}/add-coauthors")
    public Paper addCoAuthors(@PathVariable Long paperId, @RequestBody Set<User> coAuthors) {
        return paperService.addCoAuthors(paperId, coAuthors);
    }

    @PutMapping("/{paperId}/submit")
    public ResponseEntity<String> submitPaper(@PathVariable Long paperId) {
        try {
            paperService.submitPaper(paperId);
            return ResponseEntity.ok("Paper submitted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PutMapping("/{paperId}/assign-reviewers")
    public ResponseEntity<String> assignReviewers(
            @PathVariable Long paperId,
            @RequestBody Paper request) {
        try {
            Long reviewerId1 = request.getReviewer1().getUserId();
            Long reviewerId2 = request.getReviewer2().getUserId();
    
            paperService.assignReviewers(paperId, reviewerId1, reviewerId2);
            return ResponseEntity.ok("Reviewers assigned successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PutMapping("/{paperId}/submit-review")
    public ResponseEntity<String> submitReview(
            @PathVariable Long paperId,
            @RequestBody Paper paper) {
        try {
            paperService.submitReview(paperId, 
                                      paper.getReviewer1Score(), 
                                      paper.getReviewer1Comments(), 
                                      paper.getReviewer2Score(), 
                                      paper.getReviewer2Comments());
            return ResponseEntity.ok("Review submitted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PutMapping("/{paperId}/approve")
    public ResponseEntity<String> approvePaper(@PathVariable Long paperId) {
        try {
            paperService.approvePaper(paperId);
            return ResponseEntity.ok("Paper approved successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PutMapping("/{paperId}/reject")
    public ResponseEntity<String> rejectPaper(@PathVariable Long paperId) {
        try {
            paperService.rejectPaper(paperId);
            return ResponseEntity.ok("Paper rejected successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PutMapping("/{paperId}/final-submit")
    public ResponseEntity<String> submitFinalPaper(
            @PathVariable Long paperId,
            @RequestBody Paper paper) {
        try {
            paperService.submitFinalPaper(
                    paperId, 
                    paper.getContent(), 
                    paper.getReviewer1Comments(), 
                    paper.getReviewer2Comments());
            return ResponseEntity.ok("Paper final submission successful.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PutMapping("/{paperId}/accept")
    public ResponseEntity<String> acceptPaper(@PathVariable Long paperId) {
        try {
            paperService.acceptPaper(paperId);
            return ResponseEntity.ok("Paper has been accepted.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Paper>> searchPapers(
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String authors,
        @RequestParam(required = false) String abstractContent) {
        List<Paper> papers = paperService.searchPapers(title, authors, abstractContent);
        return ResponseEntity.ok(papers);
    }

    @GetMapping("/{paperId}/view")
    public ResponseEntity<Paper> viewPaper(@PathVariable Long paperId, Principal principal) {
        Paper paper = paperService.viewPaperById(paperId, principal);
        return ResponseEntity.ok(paper);
    }

    @DeleteMapping("/{paperId}/withdraw")
    public ResponseEntity<String> withdrawPaper(@PathVariable Long paperId) {
        try {
            paperService.withdrawPaper(paperId);
            return ResponseEntity.ok("Paper has been withdrawn.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
