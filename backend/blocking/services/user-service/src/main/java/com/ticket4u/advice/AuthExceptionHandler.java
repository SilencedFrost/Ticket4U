package com.ticket4u.advice;

import com.ticket4u.exception.AccountAlreadyActiveException;
import com.ticket4u.exception.InvalidGoogleTokenException;
import com.ticket4u.exception.UnauthorizedException;
import com.ticket4u.exception.UserNotFoundException;
import com.ticket4u.exception.VerificationTokenExpiredException;
import com.ticket4u.exception.VerificationTokenNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class AuthExceptionHandler {
    // 401 - Sai email hoặc mật khẩu khi đăng nhập
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)  // 401
                .body(Map.of("message", "auth.error.unauthorized"));
    }

    // 403 - Tài khoản chưa xác thực email, bị vô hiệu hóa
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> handleDisabled(DisabledException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", "auth.error.account_not_verified"));
    }

    // 401 - Token Google OAuth2 không hợp lệ hoặc đã hết hạn
    @ExceptionHandler(InvalidGoogleTokenException.class)
    public ResponseEntity<?> handleInvalidGoogleToken(InvalidGoogleTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", e.getMessage()));
    }

    // 401 - Không có quyền truy cập (token JWT không hợp lệ, hết hạn, hoặc thiếu)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorized(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", e.getMessage()));
    }

    // 400 - Token xác thực không tồn tại hoặc không hợp lệ (email verification, password reset)
    @ExceptionHandler(VerificationTokenNotFoundException.class)
    public ResponseEntity<?> handleTokenNotFound(VerificationTokenNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", e.getMessage()));
    }

    // 410 - Token xác thực đã hết hạn (email verification, password reset)
    @ExceptionHandler(VerificationTokenExpiredException.class)
    public ResponseEntity<?> handleTokenExpired(VerificationTokenExpiredException e) {
        return ResponseEntity.status(HttpStatus.GONE)
                .body(Map.of("message", e.getMessage()));
    }

    // 409 - Tài khoản đã được kích hoạt trước đó, không cần xác thực lại
    @ExceptionHandler(AccountAlreadyActiveException.class)
    public ResponseEntity<?> handleAccountAlreadyActive(AccountAlreadyActiveException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", e.getMessage()));
    }

    // 404 - Không tìm thấy người dùng theo email hoặc ID
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", e.getMessage()));
    }
}
