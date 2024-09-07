package com.ekalavya.org.controller;

import com.ekalavya.org.DTO.ResponseModel;
import com.ekalavya.org.entity.Project;
import com.ekalavya.org.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseModel getAllProjects(){
        List<Project> projects = projectService.getAllProjects();
        ResponseModel responseModel = new ResponseModel();
        responseModel.setData(projects);
        return responseModel;
    }
}
