package com.icsd19080_icsd19235.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

@Entity
public class Paper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paperId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "title")
    private String title;

    @Column(name = "abstract_text")
    private String abstractText;

    @Column(name = "content")
    private String content;

    @Column(name = "author_names")
    private String authorNames;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "reviewer1_id")
    private User reviewer1;

    @ManyToOne
    @JoinColumn(name = "reviewer2_id")
    private User reviewer2;

    @Column(name = "reviewer1_comments")
    private String reviewer1Comments;

    @Column(name = "reviewer2_comments")
    private String reviewer2Comments;

    @Column(name = "reviewer1_score")
    private Integer reviewer1Score;

    @Column(name = "reviewer2_score")
    private Integer reviewer2Score;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private PaperState state;

    @ManyToMany
    @JoinTable(
        name = "paper_coauthors",
        joinColumns = @JoinColumn(name = "paper_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> coAuthors = new HashSet<>();
    

    public enum PaperState {
        CREATED, SUBMITTED, REVIEWED, REJECTED, APPROVED, ACCEPTED
    }

    @Column(name = "submitted")
    private Boolean submitted;    

    @Column(name = "needs_modification")
    private boolean needsModification;

    @ManyToOne
    @JoinColumn(name = "conference_id")
    @JsonIgnore
    private Conference conference;

    public Long getConferenceId() {
        return conference != null ? conference.getConferenceId() : null;
    }

    public Conference getConference() { return conference; }
    public void setConference(Conference conference) { this.conference = conference; }

    public Paper() {
        this.creationDate = new Date();
        this.state = PaperState.CREATED;
        this.submitted = false;
    }

    public Paper(String title, String abstractText, String content, User user) {
        this();
        this.title = title;
        this.abstractText = abstractText;
        this.content = content;
        this.user = user;
        if (user != null) { this.authorNames = user.getFullName(); }
    }

    public Long getPaperId() { return paperId; }
    public void setPaperId(Long paperId) { this.paperId = paperId; }
    
    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAbstractText() { return abstractText; }
    public void setAbstractText(String abstractText) { this.abstractText = abstractText; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAuthorNames() { return authorNames; }
    public void setAuthorNames(String authorNames) { this.authorNames = authorNames; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public User getReviewer1() { return reviewer1; }
    public void setReviewer1(User reviewer1) { this.reviewer1 = reviewer1; }

    public User getReviewer2() { return reviewer2; }
    public void setReviewer2(User reviewer2) { this.reviewer2 = reviewer2; }

    public String getReviewer1Comments() { return reviewer1Comments; }
    public void setReviewer1Comments(String reviewer1Comments) { this.reviewer1Comments = reviewer1Comments; }
    
    public String getReviewer2Comments() { return reviewer2Comments; }
    public void setReviewer2Comments(String reviewer2Comments) { this.reviewer2Comments = reviewer2Comments; }
    
    public Integer getReviewer1Score() { return reviewer1Score; }
    public void setReviewer1Score(Integer reviewer1Score) { this.reviewer1Score = reviewer1Score; }
    
    public Integer getReviewer2Score() { return reviewer2Score; }
    public void setReviewer2Score(Integer reviewer2Score) { this.reviewer2Score = reviewer2Score; }

    public PaperState getState() { return state; }
    public void setState(PaperState state) { this.state = state; }

    public Set<User> getCoAuthors() { return coAuthors;}
    public void setCoAuthors(Set<User> coAuthors) {this.coAuthors = coAuthors;}  
    
    public boolean getSubmitted(){return submitted;}
    public void setSubmitted(boolean submitted){this.submitted = submitted;}

    public boolean getNeedsModification() {return needsModification;}
    public void setNeedsModification(boolean needsModification) {this.needsModification = needsModification;}
}   
