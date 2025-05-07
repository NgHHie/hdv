package com.example.service_survey.repositories;

import com.example.service_survey.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findBySurveyId(Long surveyId);
    List<Question> findBySurveyIdOrderByQuestionOrderAsc(Long surveyId);
}