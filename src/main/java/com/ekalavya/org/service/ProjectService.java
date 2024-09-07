package com.ekalavya.org.service;

import com.ekalavya.org.DTO.ProjectDTO;
import com.ekalavya.org.entity.Project;
import com.ekalavya.org.entity.User;
import com.ekalavya.org.entity.Vertical;
import com.ekalavya.org.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

//    @Autowired
//    private UserService userService;

    @Autowired
    private VerticalService verticalService;

//    public List<Project> getInprogressProjectsByUserId(String userId) {
//        User user = userService.findById(userId);
//        return projectRepository.findActiveProjectByUser(user);
//    }

    public Project findByName(String projectName) {
        return projectRepository.findByProjectName(projectName);
    }

    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }

//    public Project addProject(ProjectDTO projectDTO){
//        User user = userService.findById(projectDTO.getUserId());
//        Vertical vertical = verticalService.findByName(projectDTO.getVerticalName());
//        Project project = new Project();
//        project.setProjectName(projectDTO.getProjectName());
//        project.setUser(user);
//        project.setVertical(vertical);
//        return projectRepository.save(project);
//    }
}
