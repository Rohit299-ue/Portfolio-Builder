package com.portfolio.controller;

import com.portfolio.model.User;
import com.portfolio.model.Project;
import com.portfolio.repository.ProjectRepository;
import com.portfolio.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ResumeParserService resumeParserService;

    @Autowired
    private PortfolioGeneratorService portfolioGeneratorService;

    @GetMapping
    public String dashboard(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName()).orElseThrow();
        List<Project> projects = projectRepository.findByUserIdOrderByDisplayOrderAsc(user.getId());
        
        model.addAttribute("user", user);
        model.addAttribute("projects", projects);
        return "dashboard";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User updatedUser, Authentication auth) {
        User user = userService.findByUsername(auth.getName()).orElseThrow();
        
        user.setFullName(updatedUser.getFullName());
        user.setPhone(updatedUser.getPhone());
        user.setTitle(updatedUser.getTitle());
        user.setBio(updatedUser.getBio());
        user.setAboutText(updatedUser.getAboutText());
        user.setLocation(updatedUser.getLocation());
        user.setLinkedinUrl(updatedUser.getLinkedinUrl());
        user.setGithubUrl(updatedUser.getGithubUrl());
        user.setSkills(updatedUser.getSkills());
        user.setExperience(updatedUser.getExperience());
        user.setEducation(updatedUser.getEducation());
        
        userService.updateUser(user);
        return "redirect:/dashboard?success=true";
    }

    @PostMapping("/upload-hero-photo")
    public String uploadHeroPhoto(@RequestParam("heroPhoto") MultipartFile file, Authentication auth) {
        try {
            User user = userService.findByUsername(auth.getName()).orElseThrow();
            
            if (file.isEmpty()) {
                return "redirect:/dashboard?photo=empty";
            }
            
            // Save photo to uploads directory (absolute path)
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            java.io.File directory = new java.io.File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
                System.out.println("Created uploads directory at: " + uploadDir);
            }
            
            String fileName = user.getUsername() + "_hero_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = uploadDir + fileName;
            
            System.out.println("Saving hero photo to: " + filePath);
            file.transferTo(new java.io.File(filePath));
            System.out.println("Hero photo saved successfully!");
            
            user.setHeroPhotoFileName(fileName);
            userService.updateUser(user);
            
            return "redirect:/dashboard?heroPhoto=success";
        } catch (Exception e) {
            System.err.println("Error uploading hero photo: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/dashboard?heroPhoto=error";
        }
    }

    @PostMapping("/upload-about-photo")
    public String uploadAboutPhoto(@RequestParam("aboutPhoto") MultipartFile file, Authentication auth) {
        try {
            User user = userService.findByUsername(auth.getName()).orElseThrow();
            
            if (file.isEmpty()) {
                return "redirect:/dashboard?photo=empty";
            }
            
            // Save photo to uploads directory (absolute path)
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            java.io.File directory = new java.io.File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
                System.out.println("Created uploads directory at: " + uploadDir);
            }
            
            String fileName = user.getUsername() + "_about_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = uploadDir + fileName;
            
            System.out.println("Saving about photo to: " + filePath);
            file.transferTo(new java.io.File(filePath));
            System.out.println("About photo saved successfully!");
            
            user.setAboutPhotoFileName(fileName);
            userService.updateUser(user);
            
            return "redirect:/dashboard?aboutPhoto=success";
        } catch (Exception e) {
            System.err.println("Error uploading about photo: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/dashboard?aboutPhoto=error";
        }
    }

    @PostMapping("/upload-resume")
    public String uploadResume(@RequestParam("resume") MultipartFile file, Authentication auth) {
        try {
            User user = userService.findByUsername(auth.getName()).orElseThrow();
            
            String resumeContent = resumeParserService.parseResume(file);
            String extractedSkills = resumeParserService.extractSkills(resumeContent);
            String extractedExperience = resumeParserService.extractExperience(resumeContent);
            String extractedEducation = resumeParserService.extractEducation(resumeContent);
            
            user.setResumeFileName(file.getOriginalFilename());
            user.setResumeContent(resumeContent);
            
            if (user.getSkills() == null || user.getSkills().isEmpty()) {
                user.setSkills(extractedSkills);
            }
            if (user.getExperience() == null || user.getExperience().isEmpty()) {
                user.setExperience(extractedExperience);
            }
            if (user.getEducation() == null || user.getEducation().isEmpty()) {
                user.setEducation(extractedEducation);
            }
            
            userService.updateUser(user);
            return "redirect:/dashboard?resume=success";
        } catch (IOException e) {
            return "redirect:/dashboard?resume=error";
        }
    }

    @PostMapping("/projects")
    public String addProject(@ModelAttribute Project project, Authentication auth) {
        User user = userService.findByUsername(auth.getName()).orElseThrow();
        project.setUser(user);
        projectRepository.save(project);
        return "redirect:/dashboard?project=added";
    }

    @GetMapping("/download/html")
    public ResponseEntity<String> downloadHTML(Authentication auth) {
        User user = userService.findByUsername(auth.getName()).orElseThrow();
        List<Project> projects = projectRepository.findByUserIdOrderByDisplayOrderAsc(user.getId());
        
        String html = portfolioGeneratorService.generateHTML(user, projects);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=portfolio.html")
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    @GetMapping("/download/pdf")
    public ResponseEntity<ByteArrayResource> downloadPDF(Authentication auth) throws IOException {
        User user = userService.findByUsername(auth.getName()).orElseThrow();
        List<Project> projects = projectRepository.findByUserIdOrderByDisplayOrderAsc(user.getId());
        
        byte[] pdf = portfolioGeneratorService.generatePDF(user, projects);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=portfolio.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new ByteArrayResource(pdf));
    }
    
    @GetMapping("/download/zip")
    public ResponseEntity<ByteArrayResource> downloadZIP(Authentication auth) throws IOException {
        User user = userService.findByUsername(auth.getName()).orElseThrow();
        List<Project> projects = projectRepository.findByUserIdOrderByDisplayOrderAsc(user.getId());
        
        byte[] zip = portfolioGeneratorService.generateZIP(user, projects);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=portfolio.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(zip));
    }
}
