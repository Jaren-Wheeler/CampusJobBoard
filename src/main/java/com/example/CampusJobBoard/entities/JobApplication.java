package com.example.CampusJobBoard.entities;

import jakarta.persistence.*;

@Entity
@Table(name="JobApplication")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobApplicationId;

    public enum Status {
        SUBMITTED, ACCEPTED, REJECTED
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.SUBMITTED;

    // connect to user table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UserId")
    private User user;

    // connect to job table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="JobId")
    private Job job;



    // GETTERS AND SETTERS //

    public Long getJobApplicationId() {
        return jobApplicationId;
    }

    public void setJobApplicationId(Long jobApplicationId) {
        this.jobApplicationId = jobApplicationId;
    }

    public Status getStatus() {return status;}

    public void setStatus(Status status) {this.status = status;}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }


}
