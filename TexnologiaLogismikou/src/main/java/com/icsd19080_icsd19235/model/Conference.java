package com.icsd19080_icsd19235.model;

import javax.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Conference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conference_id")
    private Long conferenceId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private ConferenceState state = ConferenceState.CREATED;

    @ManyToMany
    @JoinTable(
        name = "conference_pc_chair", 
        joinColumns = @JoinColumn(name = "conference_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> pcChair;

    @ManyToMany 
    @JoinTable(
        name = "conference_pc_members",
        joinColumns = @JoinColumn(name = "conference_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> pcMembers;
    
    @ManyToMany
    @JoinTable(
        name = "conference_papers",
        joinColumns = @JoinColumn(name = "conference_id"),
        inverseJoinColumns = @JoinColumn(name = "paper_id"))
    private Set<Paper> papers = new HashSet<>();

    public Conference() {
        this.creationDate = new Date();
    }
    
    public Conference(String name, String description, Set<User> pcChair, Set<Paper> papers, Set<User> pcMembers) {
        this.name = name;
        this.description = description;
        this.pcChair = pcChair;
        this.papers = papers;
        this.pcMembers = pcMembers;
        this.creationDate = new Date();
    }

    public Long getConferenceId() { return conferenceId; }
    public void setConferenceId(Long conferenceId) { this.conferenceId = conferenceId; }

    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Set<User> getPcChair() { return pcChair; }
    public void setPcChair(Set<User> pcChair) { this.pcChair = pcChair; }

    public Set<Paper> getPapers() { return papers; }
    public void setPapers(Set<Paper> papers) { this.papers = papers; }

    public Set<User> getPcMembers() { return pcMembers; }
    public void setPcMembers(Set<User> pcMembers) { this.pcMembers = pcMembers; }
    
    public ConferenceState getState() { return state; }
    public void setState(ConferenceState state) { this.state = state; }

    public enum ConferenceState {
        CREATED, SUBMISSION, ASSIGNMENT, REVIEW, DECISION, FINAL_SUBMISSION, FINAL
    }
}
