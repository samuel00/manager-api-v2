package sml.mestrado.ufpa.configuracoes;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class InicializadorDaAplicacao extends AbstractAnnotationConfigDispatcherServletInitializer{

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { ConfiguracaoDaAplicacao.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}
