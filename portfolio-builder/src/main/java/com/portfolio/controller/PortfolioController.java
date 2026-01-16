package com.portfolio.controller;

import com.portfolio.model.User;
import com.portfolio.model.Project;
import com.portfolio.repository.ProjectRepository;
import com.portfolio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@Controller
public class PortfolioController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("/portfolio/{username}")
    public String viewPortfolio(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username).orElseThrow();
        List<Project> projects = projectRepository.findByUserIdOrderByDisplayOrderAsc(user.getId());
        
        model.addAttribute("user", user);
        model.addAttribute("projects", projects);
        return "portfolio";
    }
}
