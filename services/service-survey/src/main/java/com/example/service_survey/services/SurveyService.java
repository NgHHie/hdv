package com.example.service_survey.services;

import com.example.service_survey.dto.CreateSurveyRequest;
import com.example.service_survey.dto.QuestionDto;
import com.example.service_survey.dto.SurveyDto;
import com.example.service_survey.models.Question;
import com.example.service_survey.models.Survey;
import com.example.service_survey.repositories.QuestionRepository;
import com.example.service_survey.repositories.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyService {
    
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    
    /**
     * Tạo một khảo sát mới với các câu hỏi
     */
    @Transactional
    public SurveyDto createSurvey(CreateSurveyRequest request) {
        log.info("Đang tạo khảo sát mới: {}", request.getTitle());
        
        // Tạo khảo sát
        Survey survey = Survey.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .active(true)
                .build();
        
        Survey savedSurvey = surveyRepository.save(survey);
        
        // Tạo các câu hỏi
        List<Question> questions = new ArrayList<>();
        
        for (int i = 0; i < request.getQuestions().size(); i++) {
            CreateSurveyRequest.CreateQuestionRequest questionRequest = request.getQuestions().get(i);
            
            Question question = Question.builder()
                    .survey(savedSurvey)
                    .questionText(questionRequest.getQuestionText())
                    .required(questionRequest.getRequired() != null ? questionRequest.getRequired() : false)
                    .questionOrder(questionRequest.getQuestionOrder() != null ? questionRequest.getQuestionOrder() : i + 1)
                    .build();
            
            questions.add(question);
        }
        
        questionRepository.saveAll(questions);
        
        // Gán danh sách câu hỏi cho khảo sát
        savedSurvey.setQuestions(questions);
        
        return mapToSurveyDto(savedSurvey);
    }
    
    /**
     * Lấy tất cả khảo sát
     */
    public List<SurveyDto> getAllSurveys() {
        log.info("Đang lấy tất cả khảo sát");
        
        List<Survey> surveys = surveyRepository.findAll();
        return surveys.stream()
                .map(this::mapToSurveyDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Lấy tất cả khảo sát đang hoạt động
     */
    public List<SurveyDto> getActiveSurveys() {
        log.info("Đang lấy tất cả khảo sát đang hoạt động");
        
        List<Survey> surveys = surveyRepository.findByActive(true);
        return surveys.stream()
                .map(this::mapToSurveyDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Lấy khảo sát theo ID
     */
    public SurveyDto getSurveyById(Long id) {
        log.info("Đang lấy khảo sát với id: {}", id);
        
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khảo sát với id: " + id));
        
        return mapToSurveyDto(survey);
    }
    
    /**
     * Cập nhật trạng thái khảo sát (kích hoạt/vô hiệu hóa)
     */
    @Transactional
    public SurveyDto updateSurveyStatus(Long id, Boolean active) {
        log.info("Đang cập nhật trạng thái khảo sát cho id {}: active={}", id, active);
        
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khảo sát với id: " + id));
        
        survey.setActive(active);
        Survey updatedSurvey = surveyRepository.save(survey);
        
        return mapToSurveyDto(updatedSurvey);
    }
    
    /**
     * Xóa một khảo sát
     */
    @Transactional
    public void deleteSurvey(Long id) {
        log.info("Đang xóa khảo sát với id: {}", id);
        
        if (!surveyRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy khảo sát với id: " + id);
        }
        
        surveyRepository.deleteById(id);
    }
    
    /**
     * Chuyển đổi entity thành DTO
     */
    private SurveyDto mapToSurveyDto(Survey survey) {
        List<QuestionDto> questionDtos = new ArrayList<>();
        
        if (survey.getQuestions() != null) {
            for (Question question : survey.getQuestions()) {
                QuestionDto questionDto = QuestionDto.builder()
                        .id(question.getId())
                        .surveyId(question.getSurvey().getId())
                        .questionText(question.getQuestionText())
                        .required(question.getRequired())
                        .questionOrder(question.getQuestionOrder())
                        .build();
                
                questionDtos.add(questionDto);
            }
        }
        
        return SurveyDto.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .active(survey.getActive())
                .createdAt(survey.getCreatedAt())
                .updatedAt(survey.getUpdatedAt())
                .questions(questionDtos)
                .build();
    }
}