package com.example.service_survey.repositories;

import com.example.service_survey.models.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {
    List<SurveyResponse> findByCustomerId(Long customerId);
    List<SurveyResponse> findBySurveyId(Long surveyId);
    List<SurveyResponse> findByRelatedEntityIdAndRelatedEntityType(Long relatedEntityId, String relatedEntityType);
    List<SurveyResponse> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}