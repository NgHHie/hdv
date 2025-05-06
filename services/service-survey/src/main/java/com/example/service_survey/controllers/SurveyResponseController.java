package com.example.service_survey.controllers;

import com.example.service_survey.dto.SubmitSurveyResponseRequest;
import com.example.service_survey.dto.SurveyResponseDto;
import com.example.service_survey.services.SurveyResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/survey-responses")
@RequiredArgsConstructor
@Slf4j
public class SurveyResponseController {
    
    private final SurveyResponseService responseService;
    
    @PostMapping
    public ResponseEntity<SurveyResponseDto> submitSurveyResponse(@Valid @RequestBody SubmitSurveyResponseRequest request) {
        log.info("REST request to submit Survey Response for survey: {}, customer: {}", 
                request.getSurveyId(), request.getCustomerId());
        
        try {
            SurveyResponseDto responseDto = responseService.submitSurveyResponse(request);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error submitting survey response: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<SurveyResponseDto>> getAllSurveyResponses() {
        log.info("REST request to get all Survey Responses");
        List<SurveyResponseDto> responses = responseService.getAllSurveyResponses();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SurveyResponseDto> getSurveyResponseById(@PathVariable Long id) {
        log.info("REST request to get Survey Response : {}", id);
        try {
            SurveyResponseDto response = responseService.getSurveyResponseById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting survey response: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<SurveyResponseDto>> getSurveyResponsesByCustomerId(@PathVariable Long customerId) {
        log.info("REST request to get Survey Responses for customer : {}", customerId);
        List<SurveyResponseDto> responses = responseService.getSurveyResponsesByCustomerId(customerId);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/survey/{surveyId}")
    public ResponseEntity<List<SurveyResponseDto>> getSurveyResponsesBySurveyId(@PathVariable Long surveyId) {
        log.info("REST request to get Survey Responses for survey : {}", surveyId);
        List<SurveyResponseDto> responses = responseService.getSurveyResponsesBySurveyId(surveyId);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/related")
    public ResponseEntity<List<SurveyResponseDto>> getSurveyResponsesByRelatedEntity(
            @RequestParam Long relatedEntityId,
            @RequestParam String relatedEntityType) {
        log.info("REST request to get Survey Responses for related entity: {}, type: {}", 
                relatedEntityId, relatedEntityType);
        
        List<SurveyResponseDto> responses = responseService.getSurveyResponsesByRelatedEntity(
                relatedEntityId, relatedEntityType);
        return ResponseEntity.ok(responses);
    }
}