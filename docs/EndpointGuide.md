# How Roles Work in This Project

### Students
- Can apply to jobs
- Can view jobs
- Can manage their profile
- Cannot see employer or admin routes

### Employers
- Can post jobs
- Can manage applications for their jobs
- Cannot see student routes
- Cannot see admin routes

### Admin
- Has access to everything
- Can view and manage all users
- Can view and manage all jobs
- Can use employer and student endpoints
- Can access admin-only endpoints

# How to Secure Endpoints

Use @PreAuthorize on controller methods:

- Endpoint for students only:
  @PreAuthorize("hasRole('STUDENT')")

- Employers only:
  @PreAuthorize("hasRole('EMPLOYER')")

- Admin-only:
  @PreAuthorize("hasRole('ADMIN')")

- Employer + Admin:
  @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")

- Student + Admin:
  @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")

- Everyone logged in:
  @PreAuthorize("hasAnyRole('ADMIN','EMPLOYER','STUDENT')")

Admin automatically has access to everything since we always include ADMIN in the allowed roles where needed.
