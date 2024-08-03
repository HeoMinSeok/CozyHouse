package com.mycozhouse.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//중복 호출 되는 지 확인
@Aspect
@Component
public class PerformanceAspect {

    @Around("execution(* com.mycozhouse.repository.*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메소드 실행 시작 시간 기록
        long start = System.currentTimeMillis();

        // 메소드 실행
        Object proceed = joinPoint.proceed();

        // 메소드 실행 종료 시간 기록
        long executionTime = System.currentTimeMillis() - start;

        // 실행 시간 출력
        System.out.println(joinPoint.getSignature() + " executed in " + executionTime + "ms");
        return proceed;
    }
}