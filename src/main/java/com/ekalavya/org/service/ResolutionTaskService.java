package com.ekalavya.org.service;

import com.ekalavya.org.entity.Document;
import com.ekalavya.org.entity.M_Resolution_Update;
import com.ekalavya.org.entity.Project;
import com.ekalavya.org.entity.User;
import com.ekalavya.org.repository.MResolutionUpdateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResolutionTaskService {

    @Autowired
    private UserService userService;

    @Autowired
    MResolutionUpdateRepository mResolutionUpdateRepository;

    @Autowired
    private ProjectService projectService;

    public void uploadResolutionTask(String userId, Project project, Document passbookDoc) {
        User user = userService.findByEmplId(userId);
        if(user != null && project != null){
            M_Resolution_Update mResolutionUpdate =  new M_Resolution_Update();
            mResolutionUpdate.setResolutionDoc(passbookDoc);
            mResolutionUpdate.setProject(project);
            mResolutionUpdate.setUser(user);
            mResolutionUpdateRepository.save(mResolutionUpdate);
        }
    }
}
