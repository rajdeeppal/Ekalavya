package com.ekalavya.org.service;

import com.ekalavya.org.DTO.ResolutionTaskResponseDTO;
import com.ekalavya.org.entity.Document;
import com.ekalavya.org.entity.M_Resolution_Update;
import com.ekalavya.org.entity.Project;
import com.ekalavya.org.entity.User;
import com.ekalavya.org.repository.MResolutionUpdateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResolutionTaskService {

    @Autowired
    private UserService userService;

    @Autowired
    MResolutionUpdateRepository mResolutionUpdateRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DocumentStorageService documentStorageService;

    public String uploadResolutionDocs(String userId, MultipartFile resolutionDoc, String projectName) throws IOException {
        try {
            Document passbookDoc = documentStorageService.storeFile(resolutionDoc);
            Project project = projectService.findByName(projectName);
            User user = userService.findByEmplId(userId);
            if(user != null && project != null){
                M_Resolution_Update mResolutionUpdate =  new M_Resolution_Update();
                mResolutionUpdate.setResolutionDoc(passbookDoc);
                mResolutionUpdate.setProject(project);
                mResolutionUpdate.setUser(user);
                mResolutionUpdateRepository.save(mResolutionUpdate);
                return "SUCCESS";
            }
        }catch (Exception e){
            log.error("Exception occurred : {}", e.getMessage());
        }
        return "FAILURE";
    }

    public List<ResolutionTaskResponseDTO> getLatest5ResolutionsByProjectName(String projectName) {
        List<M_Resolution_Update> updates = mResolutionUpdateRepository.findByProjectNameOrderByTimestampDesc(projectName, PageRequest.of(0, 5));

        return updates.stream().map(update -> new ResolutionTaskResponseDTO(
                update.getProject().getProjectName(),
                update.getUser().getUsername(),
                update.getUploadTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                update.getResolutionDoc().getId(),
                update.getResolutionDoc().getFileName()
        )).collect(Collectors.toList());
    }
}
