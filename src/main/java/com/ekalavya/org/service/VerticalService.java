package com.ekalavya.org.service;

import com.ekalavya.org.entity.Vertical;
import com.ekalavya.org.repository.VerticalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VerticalService {

    @Autowired
    private VerticalRepository verticalRepository;

    public Vertical findByName(String verticalName){
        return verticalRepository.findByVerticalName(verticalName);
    }

    public Vertical save(Vertical vertical){
        return verticalRepository.save(vertical);
    }

    public List<Vertical> findAll() {
        return verticalRepository.findAll();
    }
}
