package sml.mestrado.ufpa.servicos;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sml.mestrado.ufpa.modelo.Parametro;
import sml.mestrado.ufpa.modelo.Requisicao;
import sml.mestrado.ufpa.repositorio.AspectManagerRepositorio;
import sml.mestrado.ufpa.util.ConverteUtil;

@Service
public class AspectManagerService {
	
	@Autowired
	private AspectManagerRepositorio aspectManagerRepositorio;
	
	private Requisicao requisicao;
	private Parametro parametro;
	
	public AspectManagerService() {
		requisicao = new Requisicao();
		parametro = new Parametro();
	}
	
	public void setParametrosDeEntrada(JoinPoint joinPoint, HttpServletRequest request){
		requisicao.setData(ConverteUtil.getDataFormatadaYYYYMMDD(Calendar.getInstance()));
		requisicao.setIpOrigem(request.getRemoteAddr());
		requisicao.setTipo(request.getMethod());
		parametro.setEntrada(ConverteUtil.entradaParaString(joinPoint, request));
		parametro.setMetodoInvocado(joinPoint.getSignature().getName());
		parametro.setClasseInvocada(joinPoint.getTarget().getClass().getName());
	}
	
	public void setRetornoParaCliente(JoinPoint joinPoint, Object result){
		parametro.setSaida(ConverteUtil.retornoParaString(result));
		parametro.setRequisicao(requisicao);
		requisicao.setParametro(parametro);
	}
	
	@Transactional
	public void persistirRequisicao(){
		parametro.setRequisicao(requisicao);
		requisicao.setParametro(parametro);
		aspectManagerRepositorio.persist(requisicao);
	}
	
	public Object setTempoDeExecucao(ProceedingJoinPoint joinPoint) throws Throwable{
		long start = System.currentTimeMillis();
		Object result = null;
		try {
			String className = joinPoint.getSignature().getDeclaringTypeName();
			String methodName = joinPoint.getSignature().getName();
			result = joinPoint.proceed();
			long elapsedTime = System.currentTimeMillis() - start;
			requisicao.setTempoExecucao(elapsedTime);
		} catch (IllegalArgumentException e) {
			throw e;
		}
		return result;
	}

}
