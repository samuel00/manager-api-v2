package sml.mestrado.ufpa.repositorio;

import org.springframework.stereotype.Repository;

@Repository
public class AspectManagerRepositorio extends AbstractRepositorio<Integer, Object>{
	
	public void persistiRequisicao(Object object){
		persist(object);
	}

}
