package com.example.service_survey.repositories;

import com.example.service_survey.models.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {
    List<QuestionOption> findByQuestionId(Long questionId);
    List<QuestionOption> findByQuestionIdOrderByDisplayOrderAsc(Long questionId);
}