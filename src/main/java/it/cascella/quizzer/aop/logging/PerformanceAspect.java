package it.cascella.quizzer.aop.logging;


import it.cascella.quizzer.exceptions.QuizzerException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.id.uuid.UuidGenerator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Slf4j
@Aspect
@Component
public class PerformanceAspect {

    @Around("@within(it.cascella.quizzer.aop.logging.LogTime)|| @annotation(it.cascella.quizzer.aop.logging.LogTime)")
    public Object timeExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long elapsedTime = System.currentTimeMillis() - start;
        log.info(joinPoint.getSignature().getName() + " executed in " + elapsedTime + " ms");

        return result;
    }

    @Around("@within(args)|| @annotation(args)")
    public Object logArguments(ProceedingJoinPoint joinPoint, LogArgs args) throws Throwable {
        UUID uuid  = UUID.randomUUID();
        log.atLevel(args.loglevel()).log("CALL CODE: {} \n Called Method: {} \n with Args: {}",uuid.toString(),joinPoint.getSignature().getName(),Arrays.toString(joinPoint.getArgs()));
        try {
            Object proceed = joinPoint.proceed();

            log.atLevel(args.loglevel()).log("CALL CODE:{} With Resaults: {}",uuid.toString(),proceed);
            return proceed;
        }catch (QuizzerException a){
            if (a.getCode() >= 400 && a.getCode() < 500 ) {
                log.warn("Client Error: {}", a.getMessage());
            }
            if (a.getCode() >= 500 ) {
                log.error("Server Error: {}", a.getMessage());
            }
            throw a;
        }
    }
}
