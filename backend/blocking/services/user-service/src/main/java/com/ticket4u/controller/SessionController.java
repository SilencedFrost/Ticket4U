package com.ticket4u.controller;

import com.ticket4u.dto.session.SessionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
@Slf4j

public class SessionController {
    /**
     * GET /api/v1/sessions
     * To get current logged in account's active sessions
     * @return List of SessionResponse
     */
    @GetMapping
    public ResponseEntity<List<SessionResponse>> getCurrentUserSessions() {
        // TODO: Gọi Service lấy danh sách session của user đang đăng nhập
        // Map user_agent -> deviceClient, updated_at -> lastAccess
        // get session_id --> hash SHA256 --> trim and display 6 first letter
        return null;
    }

    /**
     * DELETE /api/v1/sessions/{display-id}
     * To delete a specific session using the first 6 characters of the hashed session id
     * @param displayId 6-character string (case preserved)
     * @return ResponseEntity<?> HTTP 200 OK with empty body on success
     */
    @DeleteMapping("/{display-id}")
    public ResponseEntity<?> deleteSession(@PathVariable String displayId) {
        // Ghi log để debug xem Frontend gửi lên đúng định dạng/case hay không
        log.info("Request to delete session with hash prefix: {}", displayId);

        return null;
        //TODO: return ResponseEntity.ok().build();
    }
}
