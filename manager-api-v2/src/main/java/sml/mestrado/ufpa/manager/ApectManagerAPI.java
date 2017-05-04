package sml.mestrado.ufpa.manager;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sml.mestrado.ufpa.servicos.AspectManagerService;
@Aspect
@Component
public class ApectManagerAPI {
	
	@Autowired
	private AspectManagerService service;

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void restController() {
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
	public void logBefore(JoinPoint joinPoint, HttpServletRequest request){
		service.setParametrosDeEntrada(joinPoint, request);
	}
	
	@AfterReturning(pointcut = "restController() && allMethod()", returning = "result")
	public void logAfter(JoinPoint joinPoint, Object result) {
		service.setRetornoParaCliente(joinPoint, result);
		service.persistirRequisicao();
	}
	
	@Around("restController() && allMethod()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		return service.setTempoDeExecucao(joinPoint);
	}	

}
