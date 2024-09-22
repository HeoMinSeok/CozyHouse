package com.mycozyhouse.exhandler.advice;

import com.mycozyhouse.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

// 원래는 예외가 발생하면 was까지 예외가 던져지고 was에서 오류페이지 정보를 찾아서 에러를 호출하지만
// 스프링의 커스텀 예외를 활용하면 예외가 발생한 지점에서 직접적으로 http상태코드와 메세지를 반환할 수 잇다.
@Slf4j
@RestControllerAdvice//대상을 지정 안하면 글로벌예외처리, (annotations = 컨트롤.class) , ("패키지")
public class ExControllerAdvice {

    // IllegalArgumentException이 발생했을 때 호출
    // @ResponseStatus를 사용해 HTTP 상태 코드를 400 (Bad Request)로 설정
    //@ResponseStatus(HttpStatus.BAD_REQUEST) 안해주면 예외처리가 정상적으로 되어서 200으로 나감
    // 예외 메시지를 담은 ErrorResult 객체를 반환
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class) //(IllegalArgumentException.class)생략가능
    public ErrorResult illegalExHandle(IllegalArgumentException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    // UserException이 발생했을 때 호출
    // @ResponseEntity를 사용해 HTTP 상태 코드와 함께 ErrrResult 객체를 반환한다.
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandle(BadRequestException e) {
        log.error("[exceptionHandle] ex", e); // 예외 로그를 남긴다.
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResult> handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-NOT-FOUND", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("DUPLICATE_EMAIL-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    // 이 메서드는 모든 Exception 예외를 처리한다.
    // @ResponseStatus를 사용해 HTTP 상태 코드를 500 (Internal Server Error)로 설정하고,
    // 에러 메시지를 담은 ErrorResult 객체를 반환한다.
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }
}

/*
    1xx (정보 응답)
        100 Continue: 클라이언트가 요청을 계속 진행해도 좋다는 응답.
        101 Switching Protocols: 서버가 클라이언트의 프로토콜 변경 요청을 수용.
    2xx (성공)
        200 OK: 요청이 성공적으로 처리됨.
        201 Created: 요청이 성공적으로 처리되어 새로운 자원이 생성됨.
        202 Accepted: 요청이 수락되었지만 처리가 완료되지 않음.
        204 No Content: 요청이 성공했지만 반환할 데이터가 없음.
    3xx (리다이렉션)
        300 Multiple Choices: 요청한 리소스에 여러 가지 선택이 가능함.
        301 Moved Permanently: 요청한 자원이 영구적으로 다른 URI로 이동.
        302 Found: 요청한 자원이 일시적으로 다른 URI로 이동.
        304 Not Modified: 클라이언트가 캐시한 버전이 유효함.
    4xx (클라이언트 오류)
        400 Bad Request: 클라이언트 요청이 잘못되었음을 의미.
        401 Unauthorized: 요청한 자원에 대한 인증이 필요함.
        403 Forbidden: 서버가 요청을 이해했지만 권한이 없어 요청을 거부.
        404 Not Found: 요청한 리소스를 찾을 수 없음.
        409 Conflict: 요청이 현재 서버 상태와 충돌함.
        422 Unprocessable Entity: 요청은 잘 형성되었지만 의미적으로 유효하지 않음.
    5xx (서버 오류)
        500 Internal Server Error: 서버가 요청을 처리하는 중에 예기치 않은 오류가 발생.
        501 Not Implemented: 서버가 요청된 기능을 지원하지 않음.
        502 Bad Gateway: 서버가 게이트웨이나 프록시로서 요청을 처리하는 중에 상위 서버로부터 잘못된 응답을 받음.
        503 Service Unavailable: 서버가 현재 요청을 처리할 수 없음 (예: 과부하, 유지보수 중).
        504 Gateway Timeout: 서버가 게이트웨이로서 요청을 처리하는 중에 타임아웃 발생.*/


/*
1. 400 Bad Request
    예외: IllegalArgumentException
    설명: 클라이언트가 잘못된 요청을 보냈을 때 발생합니다. 예를 들어, 잘못된 형식의 데이터가 포함된 경우.
2. 401 Unauthorized
    예외: AuthenticationException
    설명: 인증이 필요한 리소스에 대해 인증되지 않은 사용자가 접근할 때 발생합니다.
3. 403 Forbidden
    예외: AccessDeniedException
    설명: 서버가 요청을 이해했지만, 사용자가 권한이 없어 요청을 거부할 때 발생합니다.
4. 404 Not Found
    예외: ResourceNotFoundException
    설명: 요청한 리소스가 서버에 존재하지 않을 때 발생합니다.
5. 409 Conflict
    예외: DataIntegrityViolationException
    설명: 요청이 현재 서버 상태와 충돌할 때 발생합니다. 예를 들어, 중복된 데이터 삽입 시.
6. 422 Unprocessable Entity
    예외: MethodArgumentNotValidException
    설명: 요청 데이터는 잘 형성되었지만, 의미적으로 유효하지 않을 때 발생합니다. 예를 들어, 필수 필드 누락.
7. 500 Internal Server Error
    예외: RuntimeException
    설명: 서버에서 예기치 않은 오류가 발생했을 때 발생합니다.
8. 503 Service Unavailable
    예외: ServiceUnavailableException
    설명: 서버가 요청을 처리할 수 없을 때 발생합니다. 예를 들어, 유지보수 중이거나 과부하 상태.


*/
