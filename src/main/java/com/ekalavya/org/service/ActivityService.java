package com.ekalavya.org.service;

import com.ekalavya.org.entity.Activity;
import com.ekalavya.org.entity.Component;
import com.ekalavya.org.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public List<Activity> findByComponent(Component componentEntity) {
        return activityRepository.findByComponent(componentEntity);
    }

    public Activity findByActivityNameAndComponent(String activityName, Component component) {
        return activityRepository.findByActivityNameAndComponent(activityName, component);
    }

    public Activity save(Activity activity) {
        return activityRepository.save(activity);
    }

    public Activity findByName(String activity) {
        return activityRepository.findByActivityName(activity);
    }
}
