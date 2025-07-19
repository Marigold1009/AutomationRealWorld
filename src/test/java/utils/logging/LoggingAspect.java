package utils.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.io.IOException;
import java.io.StringWriter;
import java.time.Duration;
import java.time.LocalDateTime;

@Aspect
public class LoggingAspect {

  private LocalDateTime beforeActionTime;
  private StringWriter logWriter = LogFactory.getWriter();

  @Before("execution(* selenium.core.BasePage+.*(..))")
  public void logBefore(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    StringBuilder builder = new StringBuilder();
    for (Object value : args) {
      builder.append("\"").append(value.toString()).append("\", ");
    }
    if (builder.toString().endsWith(", ")) {
      builder.delete(builder.length() - 2, builder.length());
    }
    Signature signature = joinPoint.getSignature();
    if (builder.length() == 0) {
      logWriter.write(
          "Starting action: "
              + signature.getDeclaringTypeName()
              + "."
              + signature.getName()
              + "()\n");
    } else {
      logWriter.write(
          "Starting action: "
              + signature.getDeclaringTypeName()
              + "."
              + signature.getName()
              + "("
              + builder
              + ")\n");
    }
    beforeActionTime = LocalDateTime.now();
  }

  @AfterThrowing(pointcut = "execution(* selenium.core.BasePage+.*(..))", throwing = "error")
  public void afterFailureAction(JoinPoint joinPoint, Throwable error) throws IOException {
    String actionDuration = elapseDuration(beforeActionTime);
    logWriter.write("Action failed: " + error + ". (" + actionDuration + " sec).\n");
  }

  @AfterReturning(pointcut = "execution(* selenium.core.BasePage+.*(..))", returning = "result")
  public void afterAction(JoinPoint joinPoint, Object result) throws IOException {
    String actionDuration = elapseDuration(beforeActionTime);
    logWriter.write("Action Done! (" + actionDuration + " sec).\n");
  }

  public String elapseDuration(LocalDateTime before) {
    LocalDateTime after = LocalDateTime.now();
    Duration d = Duration.between(before, after);
    return String.valueOf((float) d.toMillis() / 1000);
  }
}
