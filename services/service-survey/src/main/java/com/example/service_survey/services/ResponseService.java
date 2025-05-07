package com.example.service_survey.services;

import com.example.service_survey.dto.*;
import com.example.service_survey.models.*;
import com.example.service_survey.repositories.QuestionRepository;
import com.example.service_survey.repositories.ResponseAnswerRepository;
import com.example.service_survey.repositories.ResponseRepository;
import com.example.service_survey.repositories.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResponseService {
    
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final ResponseRepository responseRepository;
    private final ResponseAnswerRepository answerRepository;
    
    /**
     * Gửi phản hồi khảo sát
     */
    @Transactional
    public ResponseDto submitSurveyResponse(SubmitSurveyResponseRequest request) {
        log.info("Đang gửi phản hồi khảo sát cho khảo sát: {}, khách hàng: {}", request.getSurveyId(), request.getCustomerId());
        
        // Xác thực khảo sát tồn tại
        Survey survey = surveyRepository.findById(request.getSurveyId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khảo sát với id: " + request.getSurveyId()));
        
        if (!survey.getActive()) {
            throw new RuntimeException("Khảo sát không hoạt động");
        }
        
        // Tạo phản hồi khảo sát
        Response response = Response.builder()
                .survey(survey)
                .customerId(request.getCustomerId())
                .build();
        
        Response savedResponse = responseRepository.save(response);
        
        // Lấy tất cả câu hỏi để xác thực
        List<Question> questions = questionRepository.findBySurveyId(survey.getId());
        Map<Long, Question> questionMap = questions.stream()
                .collect(Collectors.toMap(Question::getId, Function.identity()));
        
        // Xử lý câu trả lời
        List<ResponseAnswer> answers = new ArrayList<>();
        
        for (SubmitSurveyResponseRequest.QuestionAnswerRequest answerRequest : request.getAnswers()) {
            // Xác thực câu hỏi tồn tại và thuộc về khảo sát
            Question question = questionMap.get(answerRequest.getQuestionId());
            if (question == null) {
                throw new RuntimeException("Không tìm thấy câu hỏi với id: " + answerRequest.getQuestionId());
            }
            
            ResponseAnswer answer = ResponseAnswer.builder()
                    .response(savedResponse)
                    .question(question)
                    .answerText(answerRequest.getAnswerText())
                    .build();
            
            answers.add(answer);
        }
        
        // Kiểm tra xem các câu hỏi bắt buộc đã được trả lời chưa
        for (Question question : questions) {
            if (Boolean.TRUE.equals(question.getRequired())) {
                boolean answered = answers.stream()
                        .anyMatch(answer -> answer.getQuestion().getId().equals(question.getId()) &&
                                answer.getAnswerText() != null && !answer.getAnswerText().isEmpty());
                
                if (!answered) {
                    throw new RuntimeException("Câu hỏi bắt buộc chưa được trả lời: " + question.getQuestionText());
                }
            }
        }
        
        // Lưu câu trả lời
        answerRepository.saveAll(answers);
        savedResponse.setAnswers(answers);
        
        return mapToResponseDto(savedResponse);
    }
    
    /**
     * Lấy tất cả phản hồi khảo sát
     */
    public List<ResponseDto> getAllResponses() {
        log.info("Đang lấy tất cả phản hồi khảo sát");
        
        List<Response> responses = responseRepository.findAll();
        return responses.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Lấy phản hồi khảo sát theo ID
     */
    public ResponseDto getResponseById(Long id) {
        log.info("Đang lấy phản hồi khảo sát với id: {}", id);
        
        Response response = responseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phản hồi khảo sát với id: " + id));
        
        return mapToResponseDto(response);
    }
    
    /**
     * Lấy phản hồi khảo sát theo ID khách hàng
     */
    public List<ResponseDto> getResponsesByCustomerId(Long customerId) {
        log.info("Đang lấy phản hồi khảo sát cho khách hàng: {}", customerId);
        
        List<Response> responses = responseRepository.findByCustomerId(customerId);
        return responses.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Lấy phản hồi khảo sát theo ID khảo sát
     */
    public List<ResponseDto> getResponsesBySurveyId(Long surveyId) {
        log.info("Đang lấy phản hồi khảo sát cho khảo sát: {}", surveyId);
        
        List<Response> responses = responseRepository.findBySurveyId(surveyId);
        return responses.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Chuyển đổi entity thành DTO
     */
    private ResponseDto mapToResponseDto(Response response) {
        List<ResponseAnswerDto> answerDtos = new ArrayList<>();
        
        if (response.getAnswers() != null) {
            for (ResponseAnswer answer : response.getAnswers()) {
                ResponseAnswerDto answerDto = ResponseAnswerDto.builder()
                        .id(answer.getId())
                        .responseId(answer.getResponse().getId())
                        .questionId(answer.getQuestion().getId())
                        .questionText(answer.getQuestion().getQuestionText())
                        .answerText(answer.getAnswerText())
                        .build();
                
                answerDtos.add(answerDto);
            }
        }
        
        return ResponseDto.builder()
                .id(response.getId())
                .surveyId(response.getSurvey().getId())
                .surveyTitle(response.getSurvey().getTitle())
                .customerId(response.getCustomerId())
                .createdAt(response.getCreatedAt())
                .answers(answerDtos)
                .build();
    }
}
