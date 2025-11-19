package com.example.CampusJobBoard.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


import java.util.Set;

/**
 * Represents a user account within the Campus Job Board system.
 *
 * <p>This entity maps directly to the `users` table in the database and
 * stores authentication and authorization information for each user.</p>
 *
 * <p>Each user has a specific role (Student, Employer, or Admin),
 * which determines access level throughout the application.</p>
 */
@Entity
@Table(name = "users")
public class User {

    /** Primary key — automatically generated user ID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    /** Full legal name of the user. */
    @NotBlank(message = "Full name is required")
    private String fullName;

    /** Unique email address used for login and account recovery. */
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    /** BCrypt-hashed password for authentication. */
    @NotBlank
    private String password;

    /** Role assigned to the user determining their access privileges. */
    @Enumerated(EnumType.STRING)
    private Role role;

    /** Indicates whether the user’s account is active or inactive. */
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    /** Supported user roles. */
    public enum Role {
        STUDENT, EMPLOYER, ADMIN, SUPER_ADMIN
    }

    /** Possible account statuses. */
    public enum Status {
        ACTIVE, INACTIVE
    }

    // one user can have many jobs
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Job> jobs;

    // map to job application table. One user has many applications
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JobApplication> applications;



    // --- Getters and Setters ---
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }


}
