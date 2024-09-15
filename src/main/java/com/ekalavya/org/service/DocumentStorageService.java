package com.ekalavya.org.service;

import com.ekalavya.org.entity.Document;
import com.ekalavya.org.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DocumentStorageService {

    @Autowired
    private DocumentRepository documentRepository;

    public Document storeFile(MultipartFile file) throws IOException {
        Document document = new Document();
        document.setFileName(file.getOriginalFilename());
        document.setData(file.getBytes());

        return documentRepository.save(document);
    }
}
