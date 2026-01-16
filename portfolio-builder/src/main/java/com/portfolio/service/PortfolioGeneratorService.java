package com.portfolio.service;

import com.portfolio.model.User;
import com.portfolio.model.Project;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class PortfolioGeneratorService {

    public String generateHTML(User user, List<Project> projects) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
        html.append("<title>").append(user.getFullName() != null ? user.getFullName() : user.getUsername()).append(" - Portfolio</title>");
        html.append("<style>");
        html.append("*{margin:0;padding:0;box-sizing:border-box;}");
        html.append("body{font-family:Arial,sans-serif;background:linear-gradient(135deg,#667eea 0%,#764ba2 100%);color:#fff;padding:40px;}");
        html.append(".container{max-width:900px;margin:auto;background:rgba(255,255,255,0.1);padding:40px;border-radius:15px;backdrop-filter:blur(10px);}");
        html.append("h1{font-size:48px;margin-bottom:10px;text-shadow:2px 2px 4px rgba(0,0,0,0.3);}");
        html.append("h2{color:#a8dadc;margin-top:30px;border-bottom:2px solid rgba(255,255,255,0.3);padding-bottom:10px;}");
        html.append(".contact{margin:20px 0;line-height:1.8;}");
        html.append(".project{background:rgba(255,255,255,0.15);padding:20px;margin:15px 0;border-left:4px solid #a8dadc;border-radius:5px;}");
        html.append(".skills{display:flex;flex-wrap:wrap;gap:10px;margin-top:15px;}");
        html.append(".skill{background:rgba(255,255,255,0.2);padding:8px 16px;border-radius:20px;}");
        html.append("</style></head><body><div class='container'>");
        
        html.append("<h1>").append(user.getFullName() != null ? user.getFullName() : user.getUsername()).append("</h1>");
        if (user.getTitle() != null) html.append("<h3>").append(user.getTitle()).append("</h3>");
        
        html.append("<div class='contact'>");
        if (user.getEmail() != null) html.append("<p>📧 ").append(user.getEmail()).append("</p>");
        if (user.getPhone() != null) html.append("<p>📱 ").append(user.getPhone()).append("</p>");
        if (user.getLocation() != null) html.append("<p>📍 ").append(user.getLocation()).append("</p>");
        html.append("</div>");
        
        if (user.getBio() != null) {
            html.append("<h2>About Me</h2><p>").append(user.getBio()).append("</p>");
        }
        
        if (user.getSkills() != null && !user.getSkills().isEmpty()) {
            html.append("<h2>Skills</h2><div class='skills'>");
            for (String skill : user.getSkills().split(",")) {
                html.append("<span class='skill'>").append(skill.trim()).append("</span>");
            }
            html.append("</div>");
        }
        
        if (!projects.isEmpty()) {
            html.append("<h2>Projects</h2>");
            for (Project project : projects) {
                html.append("<div class='project'>");
                html.append("<h3>").append(project.getTitle()).append("</h3>");
                html.append("<p>").append(project.getDescription()).append("</p>");
                if (project.getTechnologies() != null) {
                    html.append("<p><strong>Technologies:</strong> ").append(project.getTechnologies()).append("</p>");
                }
                html.append("</div>");
            }
        }
        
        if (user.getExperience() != null && !user.getExperience().isEmpty()) {
            html.append("<h2>Experience</h2><p style='white-space:pre-line;'>").append(user.getExperience()).append("</p>");
        }
        
        if (user.getEducation() != null && !user.getEducation().isEmpty()) {
            html.append("<h2>Education</h2><p style='white-space:pre-line;'>").append(user.getEducation()).append("</p>");
        }
        
        html.append("</div></body></html>");
        return html.toString();
    }

    public byte[] generatePDF(User user, List<Project> projects) throws IOException {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            document.addPage(page);
            
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float y = 750;
                
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
                contentStream.newLineAtOffset(50, y);
                contentStream.showText(user.getFullName() != null ? user.getFullName() : user.getUsername());
                contentStream.endText();
                
                y -= 30;
                if (user.getTitle() != null) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 14);
                    contentStream.newLineAtOffset(50, y);
                    contentStream.showText(user.getTitle());
                    contentStream.endText();
                    y -= 25;
                }
                
                if (user.getEmail() != null) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.newLineAtOffset(50, y);
                    contentStream.showText("Email: " + user.getEmail());
                    contentStream.endText();
                    y -= 20;
                }
            }
            
            document.save(baos);
            return baos.toByteArray();
        }
    }
    
    public byte[] generateZIP(User user, List<Project> projects) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {
            
            // Add HTML file
            String html = generateHTML(user, projects);
            ZipEntry htmlEntry = new ZipEntry("portfolio.html");
            zos.putNextEntry(htmlEntry);
            zos.write(html.getBytes());
            zos.closeEntry();
            
            // Add README file
            String readme = "# " + (user.getFullName() != null ? user.getFullName() : user.getUsername()) + " - Portfolio\n\n";
            readme += "This is your generated portfolio.\n\n";
            readme += "## Files Included:\n";
            readme += "- portfolio.html - Your portfolio webpage (open in browser)\n";
            readme += "- README.txt - This file\n\n";
            readme += "## How to Use:\n";
            readme += "1. Open portfolio.html in any web browser\n";
            readme += "2. Share the HTML file with employers\n";
            readme += "3. Host it on GitHub Pages or any web hosting service\n\n";
            readme += "Generated by Portfolio Builder";
            
            ZipEntry readmeEntry = new ZipEntry("README.txt");
            zos.putNextEntry(readmeEntry);
            zos.write(readme.getBytes());
            zos.closeEntry();
            
            zos.finish();
            return baos.toByteArray();
        }
    }
}
