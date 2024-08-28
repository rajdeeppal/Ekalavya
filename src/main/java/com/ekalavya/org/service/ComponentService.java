package com.ekalavya.org.service;

import com.ekalavya.org.entity.Component;
import com.ekalavya.org.entity.Vertical;
import com.ekalavya.org.repository.ComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComponentService {

    @Autowired
    private ComponentRepository componentRepository;

    public Component save(Component component){
        return componentRepository.save(component);
    }

    public List<Component> findByVertical(Vertical verticalEntity) {
        return componentRepository.findByVertical(verticalEntity);
    }

    public Component findByName(String component) {
        return componentRepository.findByComponentName(component);
    }

    public Component findByComponentNameAndVertical(String componentName, Vertical vertical) {
        return componentRepository.findByComponentNameAndVertical(componentName, vertical);
    }
}
