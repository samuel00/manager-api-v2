package sml.mestrado.ufpa.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class ConverteUtil {

	public static Calendar getDataFormatadaYYYYMMDD(Calendar instance) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formatted = format1.format(instance.getTime());
		try {
			instance.setTime(format1.parse(formatted));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return instance;
	}

	public static String entradaParaString(JoinPoint joinPoint, HttpServletRequest request) {
		String entrada = null;
		try {
			if (request.getMethod().equals("GET")) {
				entrada = extraiParametros(joinPoint);
			} else {//Converte Para Json
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				entrada = ow.writeValueAsString(joinPoint.getArgs()[0]);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return entrada;
	}
	
	private static String extraiParametros(JoinPoint joinPoint) {
		String paranetrosDeEntrada= "";
		Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
        Method method = methodSignature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        assert args.length == parameterAnnotations.length;
        for (int argIndex = 0; argIndex < args.length; argIndex++) {
            for (Annotation annotation : parameterAnnotations[argIndex]) {
                if (!(annotation instanceof RequestParam))
                    continue;
                RequestParam requestParam = (RequestParam) annotation;
                paranetrosDeEntrada += requestParam.value() + " = " + args[argIndex] + "\n";
            }
        }
		return paranetrosDeEntrada;
	}

	public static String retornoParaString(Object result) {
		String valorRetorno = null;
		if (null != result) {
			if (result.toString().endsWith("@" + Integer.toHexString(result.hashCode()))) {
				valorRetorno = ReflectionToStringBuilder.toString(result);
			} else {
				valorRetorno = result.toString();
			}
		}
		return valorRetorno;
	}

}
