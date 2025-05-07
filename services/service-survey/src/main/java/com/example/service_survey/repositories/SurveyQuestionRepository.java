package com.example.service_survey.repositories;

import com.example.service_survey.models.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Integer> {
    List<SurveyQuestion> findBySurveyId(Integer surveyId);
    List<SurveyQuestion> findBySurveyIdOrderByDisplayOrderAsc(Integer surveyId);
}