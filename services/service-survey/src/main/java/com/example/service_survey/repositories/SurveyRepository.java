package com.example.service_survey.repositories;

import com.example.service_survey.models.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    List<Survey> findByActive(Boolean active);
}