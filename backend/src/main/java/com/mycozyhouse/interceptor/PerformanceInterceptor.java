package com.mycozyhouse.interceptor;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//@Component
//public class PerformanceInterceptor implements HandlerInterceptor {
//
//    /**
//     * 이 메소드는 HTTP 요청이 컨트롤러의 핸들러 메소드로 전달되기 전에 호출
//     * 요청이 처리되기 전의 시간을 기록하여 성능 측정을 위한 기준점을 설정
//     *
//     * @param request   HTTP 요청 객체
//     * @param response  HTTP 응답 객체
//     * @param handler   실제 요청을 처리할 핸들러 객체 (컨트롤러)
//     * @return true    요청 처리를 계속 진행하도록 허용
//     */
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        // 요청 처리 시작 시간을 기록
//        request.setAttribute("startTime", System.currentTimeMillis());
//        return true; // 요청 처리를 계속 진행하도록 허용
//    }
//
//    /**
//     * 이 메소드는 HTTP 요청 처리가 완료된 후, 뷰가 렌더링된 이후에 호출
//     * 요청 처리 시간을 계산하고 로그로 출력하여 성능을 모니터링
//     *
//     * @param request   HTTP 요청 객체
//     * @param response  HTTP 응답 객체
//     * @param handler   실제 요청을 처리한 핸들러 객체 (컨트롤러)
//     * @param ex        요청 처리 중 발생한 예외 (있을 경우)
//     */
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//        // 저장된 시작 시간 가져오기
//        long startTime = (Long) request.getAttribute("startTime");
//        // 현재 시간을 가져와서 처리 완료 시간을 계산
//        long endTime = System.currentTimeMillis();
//        // 실행 시간 계산
//        long executeTime = endTime - startTime;
//
//        // 처리 시간 로그 출력
//        System.out.println("[" + handler + "] executeTime : " + executeTime + "ms");
//    }
//}