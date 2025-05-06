package com.example.service_survey.repositories;

import com.example.service_survey.models.QuestionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionResponseRepository extends JpaRepository<QuestionResponse, Long> {
    List<QuestionResponse> findBySurveyResponseId(Long surveyResponseId);
    List<QuestionResponse> findByQuestionId(Long questionId);
}