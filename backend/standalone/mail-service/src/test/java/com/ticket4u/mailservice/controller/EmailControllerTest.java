// ./gradlew test --tests EmailControllerTest

package com.ticket4u.mailservice.controller;

import com.ticket4u.mailservice.dto.request.EmailRequest;
import com.ticket4u.mailservice.dto.response.EmailResponse;
import com.ticket4u.mailservice.enums.EmailStatus;
import com.ticket4u.mailservice.exception.GlobalExceptionHandler;
import com.ticket4u.mailservice.service.AuditService;
import com.ticket4u.mailservice.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailController Tests")
class EmailControllerTest {

	private MockMvc mockMvc;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Mock
	private EmailService emailService;

	@Mock
	private AuditService auditService;

	@InjectMocks
	private EmailController emailController;

	private EmailRequest validEmailRequest;
	private EmailResponse sentResponse;
	private EmailResponse queuedResponse;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(emailController)
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();

		objectMapper.findAndRegisterModules();

		Map<String, Object> templateData = new HashMap<>();
		templateData.put("otp", "123456");
		templateData.put("userName", "Test User");

		validEmailRequest = EmailRequest.builder()
				.to("test@example.com")
				.subject("Test Subject")
				.templateCode("OTP")
				.templateData(templateData)
				.async(false)
				.build();

		sentResponse = EmailResponse.builder()
				.messageId("msg-12345")
				.status(EmailStatus.SENT)
				.message("Email đã được gửi thành công")
				.timestamp(Instant.now())
				.build();

		queuedResponse = EmailResponse.builder()
				.messageId("msg-67890")
				.status(EmailStatus.QUEUED)
				.message("Email đã được đưa vào hàng đợi")
				.timestamp(Instant.now())
				.build();
	}

	@Nested
	@DisplayName("GROUP A: Send Email - Success Scenarios")
	class GroupA_SuccessScenarios {

		@Test
		@DisplayName("TC01: Gửi email sync thành công")
		void tc01_sendEmailSync_Success() throws Exception {
			when(emailService.send(any(EmailRequest.class))).thenReturn(sentResponse);
			doNothing().when(auditService).logEmailRequest(any(EmailRequest.class));

			mockMvc.perform(post("/api/v1/mail/send")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(validEmailRequest)))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.messageId").value("msg-12345"))
					.andExpect(jsonPath("$.status").value("SENT"))
					.andExpect(jsonPath("$.message").value("Email đã được gửi thành công"))
					.andExpect(jsonPath("$.timestamp").exists());

			verify(auditService, times(1)).logEmailRequest(any(EmailRequest.class));
			verify(emailService, times(1)).send(any(EmailRequest.class));
		}

		@Test
		@DisplayName("TC02: Gửi email async thành công")
		void tc02_sendEmailAsync_Success() throws Exception {
			validEmailRequest.setAsync(true);
			when(emailService.send(any(EmailRequest.class))).thenReturn(queuedResponse);
			doNothing().when(auditService).logEmailRequest(any(EmailRequest.class));

			mockMvc.perform(post("/api/v1/mail/send")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(validEmailRequest)))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.messageId").value("msg-67890"))
					.andExpect(jsonPath("$.status").value("QUEUED"))
					.andExpect(jsonPath("$.message").value("Email đã được đưa vào hàng đợi"))
					.andExpect(jsonPath("$.timestamp").exists());

			verify(auditService, times(1)).logEmailRequest(any(EmailRequest.class));
			verify(emailService, times(1)).send(any(EmailRequest.class));
		}

		@Test
		@DisplayName("TC03: Gửi email với CC recipients")
		void tc03_sendEmailWithCc_Success() throws Exception {
			validEmailRequest.setCc(new String[]{"cc1@test.com", "cc2@test.com"});
			when(emailService.send(any(EmailRequest.class))).thenReturn(sentResponse);
			doNothing().when(auditService).logEmailRequest(any(EmailRequest.class));

			mockMvc.perform(post("/api/v1/mail/send")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(validEmailRequest)))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.status").value("SENT"));

			verify(emailService, times(1)).send(argThat(request ->
					request.getCc() != null &&
					request.getCc().length == 2 &&
					request.getCc()[0].equals("cc1@test.com") &&
					request.getCc()[1].equals("cc2@test.com")
			));
		}

		@Test
		@DisplayName("TC47: Integration - Send email và verify audit logged")
		void tc47_integration_SendEmailAndVerifyAudit() throws Exception {
			when(emailService.send(any(EmailRequest.class))).thenReturn(sentResponse);
			doNothing().when(auditService).logEmailRequest(any(EmailRequest.class));

			mockMvc.perform(post("/api/v1/mail/send")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(validEmailRequest)))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.messageId").exists())
					.andExpect(jsonPath("$.status").value("SENT"));

			verify(auditService, times(1)).logEmailRequest(argThat(request ->
					request.getTo().equals("test@example.com") &&
					request.getSubject().equals("Test Subject") &&
					request.getTemplateCode().equals("OTP")
			));

			verify(emailService, times(1)).send(argThat(request ->
					request.getTo().equals("test@example.com") &&
					!request.isAsync()
			));
		}

		@Test
		@DisplayName("TC48: Integration - Send async và check QUEUED status")
		void tc48_integration_SendAsyncAndCheckQueued() throws Exception {
			validEmailRequest.setAsync(true);
			when(emailService.send(any(EmailRequest.class))).thenReturn(queuedResponse);
			doNothing().when(auditService).logEmailRequest(any(EmailRequest.class));

			mockMvc.perform(post("/api/v1/mail/send")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(validEmailRequest)))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.status").value("QUEUED"))
					.andExpect(jsonPath("$.messageId").exists());

			verify(emailService, times(1)).send(argThat(EmailRequest::isAsync));
		}
	}

	@Nested
	@DisplayName("GROUP B: Send Email - Validation Errors")
	class GroupB_ValidationErrors {

		@Test
		@DisplayName("TC10: Missing recipient (to = null) - 400 Bad Request")
		void tc10_missingRecipient_BadRequest() throws Exception {
			validEmailRequest.setTo(null);

			mockMvc.perform(post("/api/v1/mail/send")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(validEmailRequest)))
					.andDo(print())
					.andExpect(status().isBadRequest());

			verify(emailService, never()).send(any(EmailRequest.class));
			verify(auditService, never()).logEmailRequest(any(EmailRequest.class));
		}

		@Test
		@DisplayName("TC11: Empty recipient (to = empty string) - 400 Bad Request")
		void tc11_emptyRecipient_BadRequest() throws Exception {
			validEmailRequest.setTo("");

			mockMvc.perform(post("/api/v1/mail/send")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(validEmailRequest)))
					.andDo(print())
					.andExpect(status().isBadRequest());

			verify(emailService, never()).send(any(EmailRequest.class));
		}

		@Test
		@DisplayName("TC12: Invalid email format - 400 Bad Request")
		void tc12_invalidEmailFormat_BadRequest() throws Exception {
			validEmailRequest.setTo("invalid-email-format");

			mockMvc.perform(post("/api/v1/mail/send")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(validEmailRequest)))
					.andDo(print())
					.andExpect(status().isBadRequest());

			verify(emailService, never()).send(any(EmailRequest.class));
		}

		@Test
		@DisplayName("TC13: Missing subject - 400 Bad Request")
		void tc13_missingSubject_BadRequest() throws Exception {
			validEmailRequest.setSubject(null);

			mockMvc.perform(post("/api/v1/mail/send")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(validEmailRequest)))
					.andDo(print())
					.andExpect(status().isBadRequest());

			verify(emailService, never()).send(any(EmailRequest.class));
		}

		@Test
		@DisplayName("TC14: Missing template code - 400 Bad Request")
		void tc14_missingTemplateCode_BadRequest() throws Exception {
			validEmailRequest.setTemplateCode(null);

			mockMvc.perform(post("/api/v1/mail/send")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(validEmailRequest)))
					.andDo(print())
					.andExpect(status().isBadRequest());

			verify(emailService, never()).send(any(EmailRequest.class));
		}
	}
}
