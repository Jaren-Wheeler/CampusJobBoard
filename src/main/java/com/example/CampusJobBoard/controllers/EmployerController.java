package com.example.CampusJobBoard.controllers;

import com.example.CampusJobBoard.entities.Job;
import com.example.CampusJobBoard.repositories.JobRepository;
import com.example.CampusJobBoard.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.CampusJobBoard.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
public class EmployerController {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public EmployerController(JobRepository jobRepository, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));


        List<Job> postings = jobRepository.findByUser_UserId(currentUser.getUserId());

        model.addAttribute("postings", postings);
        model.addAttribute("loggedInUser", currentUser);
        return"employer/dashboard";
    }

    @GetMapping("/dashboard/newPost")
    public String newPost(Model model) {
        model.addAttribute("posting", new Job());
        return "employer/newPost";
    }

    @PostMapping("/dashboard/newPost")
    public String newPost(@ModelAttribute("posting") Job job, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        job.setUser(currentUser);
        job.setStatus(Job.Status.PENDING);
        job.setCreatedAt(new java.util.Date());
        job.setUpdatedAt(new java.util.Date());

        jobRepository.save(job);

        redirectAttributes.addFlashAttribute("success", "Job Posted succesfully");
        return "redirect:/dashboard";
    }

    @GetMapping("/applications/{jobId}")
    public String ViewApplication(@PathVariable Long jobId, Model model) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

        model.addAttribute("job", job);
        model.addAttribute("applications", job.getApplications());

        return "employer/applications";
    }

    @GetMapping("/jobs/edit/{id}")
    public String showEditJobForm(@PathVariable Long id, Model model) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid job id: " + id));
        model.addAttribute("job", job);
        return "editJob";
    }

    @PostMapping("/jobs/edit/{id}")
    public String updateJob(
            @PathVariable Long id,
            @ModelAttribute Job job) {
        Job existingJob = jobRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid job id: " + id));

        existingJob.setJobTitle(job.getJobTitle());
        existingJob.setDescription(job.getDescription());
        existingJob.setLocation(job.getLocation());
        existingJob.setSalary(job.getSalary());
        existingJob.setCategory(job.getCategory());
        existingJob.setDeadline(job.getDeadline());

        existingJob.setUpdatedAt(new Date());

        jobRepository.save(existingJob);

        return "redirect:/dashboard";
    }

    @PostMapping("/jobs/delete/{id}")
    public String deleteJob(@PathVariable Long id) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid job id: " + id));
        jobRepository.delete(job);
        return "redirect:/dashboard";
    }
}