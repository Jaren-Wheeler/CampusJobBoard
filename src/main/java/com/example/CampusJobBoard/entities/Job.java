package com.example.CampusJobBoard.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name="job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long JobId;

    private String JobTitle;

    private String Description;

    private String Location;

    private int Salary;

    private String Category;

    private Date Deadline;

    public enum Status {
        PENDING, APPROVED, REJECTED
    };
    @Enumerated(EnumType.STRING)
    private Status status;

    private Date CreatedAt;

    private Date UpdatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UserId")
    private User user;

    // mapping to job application table. One job has many applications
    @OneToMany(mappedBy="job", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JobApplication> applications;



    // GETTERS AND SETTERS //

    public Long getJobId() {
        return JobId;
    }

    public void setJobId(Long jobId) {
        JobId = jobId;
    }

    public String getJobTitle() {
        return JobTitle;
    }

    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getSalary() {
        return Salary;
    }

    public void setSalary(int salary) {
        Salary = salary;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public Date getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        UpdatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<JobApplication> getApplications() {
        return applications;
    }

    public void setApplications(Set<JobApplication> applications) {
        this.applications = applications;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public Date getDeadline() {
        return Deadline;
    }

    public void setDeadline(Date deadline) {
        Deadline = deadline;
    }

    public Date getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        CreatedAt = createdAt;
    }

}
