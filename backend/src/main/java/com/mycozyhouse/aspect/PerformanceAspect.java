package com.mycozyhouse.aspect;

import com.mycozyhouse.filter.RequestIdFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Slf4j
@Aspect
@Component
public class PerformanceAspect {

    // 메소드 깊이를 추적하기 위한 스레드 로컬 변수
    private ThreadLocal<Integer> callDepth = ThreadLocal.withInitial(() -> 0);

    @Around("execution(public * com.mycozyhouse..*(..))")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메소드 실행 시작 시간 기록
        long start = System.currentTimeMillis();

        // 호출 깊이 증가
        int depth = callDepth.get();
        callDepth.set(depth + 1);

        // HTTP 요청 정보 및 요청 ID 가져오기 (있을 경우)
        HttpServletRequest request = null;
        String requestId = null;
        if (RequestContextHolder.getRequestAttributes() != null) {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            requestId = (String) request.getAttribute(RequestIdFilter.REQUEST_ID_HEADER);  // 요청 ID 가져오기
        }

        // HTTP 요청 정보 출력
        String httpRequestInfo = (request != null)
                ? " [HTTP " + request.getMethod() + " " + request.getRequestURI() + "]"
                : "";

        log.info("{}{} started execution", requestId != null ? "[" + requestId + "] " : "", "Depth[" + depth + "] " + "-> ".repeat(depth)  + joinPoint.getSignature().toShortString() + httpRequestInfo);

        try {
            // 메소드 실행
            Object result = joinPoint.proceed();

            // 메소드 실행 종료 시간 기록
            long executionTime = System.currentTimeMillis() - start;

            // 정상 흐름의 로그 출력
            logExecutionDetails(joinPoint, executionTime, null, depth, request, requestId);

            return result;
        } catch (Throwable throwable) {
            // 예외 흐름의 로그 출력
            long executionTime = System.currentTimeMillis() - start;
            logExecutionDetails(joinPoint, executionTime, throwable, depth, request, requestId);

            // 예외를 다시 던져 비즈니스 로직 흐름에 영향을 주지 않음
            throw throwable;
        } finally {
            // 호출 깊이 감소
            callDepth.set(depth);
        }
    }

    private void logExecutionDetails(ProceedingJoinPoint joinPoint, long executionTime, Throwable throwable, int depth, HttpServletRequest request, String requestId) {
        // 메소드 시그니처 및 클래스 정보
        String methodInfo = joinPoint.getSignature().toShortString();

        // 심볼과 깊이 번호로 표현하는 메소드 깊이 표시
        String depthInfo = "Depth[" + depth + "] " + "<-  ".repeat(depth);

        // 요청 ID 출력
        String requestIdInfo = (requestId != null) ? "[" + requestId + "] " : "";

        // HTTP 요청 정보 출력
        String httpRequestInfo = (request != null)
                ? " [HTTP " + request.getMethod() + " " + request.getRequestURI() + "]"
                : "";

        // 정상적인 경우의 로그 메시지
        if (throwable == null) {
            log.info("{}{} end executed in {} ms", requestIdInfo, depthInfo + methodInfo , executionTime);
        } else {
            // 예외 발생 시 로그 메시지
            log.error("{}{} failed in {} ms with exception: {}", requestIdInfo, depthInfo + methodInfo + httpRequestInfo, executionTime, throwable.getMessage());
        }
    }

}