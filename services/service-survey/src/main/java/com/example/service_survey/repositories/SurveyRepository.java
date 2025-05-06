package com.example.service_survey.repositories;

import com.example.service_survey.models.Survey;
import com.example.service_survey.models.SurveyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    List<Survey> findBySurveyType(SurveyType surveyType);
    List<Survey> findByIsActiveTrue();
    List<Survey> findByIsActiveTrueAndSurveyType(SurveyType surveyType);
}