package br.com.projetoperiodo.model.negocio.entidade;

import java.util.Date;

public interface EntidadeNegocio
{	
	public Date getUltimaAlteracao();
	
	public void setUltimaAlteracao(Date ultimaAlteracao);
	
	public long getChavePrimaria();
	
	public void setChavePrimaria(long chavePrimaria);
	
}