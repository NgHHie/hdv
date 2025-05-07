package com.example.service_survey.repositories;

import com.example.service_survey.models.ResponseAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseAnswerRepository extends JpaRepository<ResponseAnswer, Long> {
    List<ResponseAnswer> findByResponseId(Long responseId);
    List<ResponseAnswer> findByQuestionId(Long questionId);
}