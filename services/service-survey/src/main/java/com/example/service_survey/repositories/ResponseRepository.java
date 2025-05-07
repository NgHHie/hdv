package com.example.service_survey.repositories;

import com.example.service_survey.models.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findByCustomerId(Long customerId);
    List<Response> findBySurveyId(Long surveyId);
}
