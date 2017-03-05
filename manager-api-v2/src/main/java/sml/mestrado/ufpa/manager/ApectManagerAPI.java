package sml.mestrado.ufpa.manager;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

@Aspect
@Component
public class ApectManagerAPI {

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void restController() {
		System.out.println("Iniciando Monitoramento");
	}

	@Pointcut("execution(* *.*(..))")
	protected void allMethod() {
	}

	@Pointcut("execution(public * *(..))")
	protected void loggingPublicOperation() {
	}

	@Pointcut("execution(* *.*(..))")
	protected void loggingAllOperation() {
	}
	
	@Before("restController() && allMethod() && args(request)")
	public void logBefore(JoinPoint joinPoint, HttpServletRequest request) throws ParseException, JsonProcessingException {
		System.out.println("Método Acessado :  " + joinPoint.getSignature().getName());
		System.out.println("Nome da Classe :  " + joinPoint.getSignature().getDeclaringTypeName());
		//System.out.println"Arguments [{}]", joinPoint.getArgs()[0]);
		System.out.println("Argumentos : " + Arrays.toString(joinPoint.getArgs()));
		System.out.println("Classe Alvo : " + joinPoint.getTarget().getClass().getName());

		if (null != request) {
			System.out.println("Início Header da Requisição ");
			System.out.println("Tipo de Requisição : " + request.getMethod());
			System.out.println("IP : " + request.getRemoteAddr());
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = headerNames.nextElement();
				String headerValue = request.getHeader(headerName);
				System.out.println("Nome do Header: " + headerName + " Valor do Header : " + headerValue);
			}
			System.out.println("Path de Requisição :" + request.getServletPath());
			System.out.println("Fim Header da Requisição ");
		}
		
	}
	
	@AfterReturning(pointcut = "restController() && allMethod()", returning = "result")
	public void logAfter(JoinPoint joinPoint, Object result) {
		String returnValue = this.getValue(result);
		System.out.println("Retorno do Método : " + returnValue);
	}
	
	@AfterThrowing(pointcut = "restController() && allMethod()", throwing = "exception")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
		System.err.println("Exceção Lançada em " + joinPoint.getSignature().getName() + " ()");
		System.err.println("Causa : " + exception.getCause());
	}
	
	@Around("restController() && allMethod()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		
		long start = System.currentTimeMillis();
		try {
			String className = joinPoint.getSignature().getDeclaringTypeName();
			String methodName = joinPoint.getSignature().getName();
			Object result = joinPoint.proceed();
			long elapsedTime = System.currentTimeMillis() - start;
			System.out.println("Método " + className + "." + methodName + " ()" + " Executado Em : "
					+ elapsedTime + " ms");
			return result;
		} catch (IllegalArgumentException e) {
			System.err.println("Argumento Ilegal " + Arrays.toString(joinPoint.getArgs()) + " Em : "
					+ joinPoint.getSignature().getName() + "()");
			throw e;
		}
	}	
	
	
	private String getValue(Object result) {
		String returnValue = null;
		if (null != result) {
			if (result.toString().endsWith("@" + Integer.toHexString(result.hashCode()))) {
				returnValue = ReflectionToStringBuilder.toString(result);
			} else {
				returnValue = result.toString();
			}
		}
		return returnValue;
	}

}
