package com.example.service_survey.controllers;

import com.example.service_survey.dto.SurveyAnalyticsDto;
import com.example.service_survey.services.SurveyAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/survey-analytics")
@RequiredArgsConstructor
@Slf4j
public class SurveyAnalyticsController {
    
    private final SurveyAnalyticsService analyticsService;
    
    @GetMapping("/{surveyId}")
    public ResponseEntity<SurveyAnalyticsDto> getSurveyAnalytics(@PathVariable Long surveyId) {
        log.info("REST request to get analytics for Survey : {}", surveyId);
        try {
            SurveyAnalyticsDto analytics = analyticsService.getSurveyAnalytics(surveyId);
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            log.error("Error getting survey analytics: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{surveyId}/response-rate")
    public ResponseEntity<Map<String, Long>> getResponseRateOverTime(
            @PathVariable Long surveyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        log.info("REST request to get response rate over time for Survey : {}", surveyId);
        try {
            Map<String, Long> responseRate = analyticsService.getResponseRateOverTime(surveyId, startDate, endDate);
            return ResponseEntity.ok(responseRate);
        } catch (Exception e) {
            log.error("Error getting response rate: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}