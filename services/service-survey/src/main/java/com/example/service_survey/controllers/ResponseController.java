package com.example.service_survey.controllers;

import com.example.service_survey.dto.ResponseDto;
import com.example.service_survey.dto.SubmitSurveyResponseRequest;
import com.example.service_survey.services.ResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/surveys/survey-responses")
@RequiredArgsConstructor
@Slf4j
public class ResponseController {
    
    private final ResponseService responseService;
    
    @PostMapping
    public ResponseEntity<ResponseDto> submitSurveyResponse(@Valid @RequestBody SubmitSurveyResponseRequest request) {
        log.info("REST request gửi Phản hồi Khảo sát cho khảo sát: {}, khách hàng: {}", 
                request.getSurveyId(), request.getCustomerId());
        
        try {
            ResponseDto responseDto = responseService.submitSurveyResponse(request);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Lỗi khi gửi phản hồi khảo sát: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<ResponseDto>> getAllResponses() {
        log.info("REST request lấy tất cả Phản hồi Khảo sát");
        List<ResponseDto> responses = responseService.getAllResponses();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getResponseById(@PathVariable Long id) {
        log.info("REST request lấy Phản hồi Khảo sát với id: {}", id);
        try {
            ResponseDto response = responseService.getResponseById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Lỗi khi lấy phản hồi khảo sát: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ResponseDto>> getResponsesByCustomerId(@PathVariable Long customerId) {
        log.info("REST request lấy Phản hồi Khảo sát cho khách hàng: {}", customerId);
        List<ResponseDto> responses = responseService.getResponsesByCustomerId(customerId);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/survey/{surveyId}")
    public ResponseEntity<List<ResponseDto>> getResponsesBySurveyId(@PathVariable Long surveyId) {
        log.info("REST request lấy Phản hồi Khảo sát cho khảo sát: {}", surveyId);
        List<ResponseDto> responses = responseService.getResponsesBySurveyId(surveyId);
        return ResponseEntity.ok(responses);
    }
}
