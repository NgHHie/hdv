package com.example.service_survey.controllers;

import com.example.service_survey.dto.CreateSurveyRequest;
import com.example.service_survey.dto.SurveyDto;
import com.example.service_survey.services.SurveyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/surveys")
@RequiredArgsConstructor
@Slf4j
public class SurveyController {
    
    private final SurveyService surveyService;
    
    @PostMapping
    public ResponseEntity<SurveyDto> createSurvey(@Valid @RequestBody CreateSurveyRequest request) {
        log.info("REST request tạo Khảo sát mới: {}", request.getTitle());
        
        try {
            SurveyDto surveyDto = surveyService.createSurvey(request);
            return new ResponseEntity<>(surveyDto, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Lỗi khi tạo khảo sát: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<SurveyDto>> getAllSurveys() {
        log.info("REST request lấy tất cả Khảo sát");
        List<SurveyDto> surveys = surveyService.getAllSurveys();
        return ResponseEntity.ok(surveys);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<SurveyDto>> getActiveSurveys() {
        log.info("REST request lấy tất cả Khảo sát đang hoạt động");
        List<SurveyDto> surveys = surveyService.getActiveSurveys();
        return ResponseEntity.ok(surveys);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SurveyDto> getSurveyById(@PathVariable Long id) {
        log.info("REST request lấy Khảo sát với id: {}", id);
        try {
            SurveyDto survey = surveyService.getSurveyById(id);
            return ResponseEntity.ok(survey);
        } catch (Exception e) {
            log.error("Lỗi khi lấy khảo sát: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<SurveyDto> updateSurveyStatus(
            @PathVariable Long id,
            @RequestParam Boolean active) {
        log.info("REST request cập nhật trạng thái Khảo sát: id={}, active={}", id, active);
        try {
            SurveyDto survey = surveyService.updateSurveyStatus(id, active);
            return ResponseEntity.ok(survey);
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật trạng thái khảo sát: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        log.info("REST request xóa Khảo sát với id: {}", id);
        try {
            surveyService.deleteSurvey(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Lỗi khi xóa khảo sát: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
