package dao;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import model.Atividade;
import model.Disciplina;
import model.Funcionario;
import model.Monitor;
import model.Orientador;
import model.RelatorioFrequencia;
import model.Semana;
import util.constantes.Modalidade;

public class RelatorioFrequenciaDao implements DAO
{
	Connection connection;
	public RelatorioFrequenciaDao(Connection connection) {
		this.connection = connection;
	}
	@Override
	public void inserir(Object object) {
		RelatorioFrequencia relatorioFrequencia = (RelatorioFrequencia)object;
		final String SQL_INSERT = "INSERT INTO PROJETO_PERIODO.RELATORIO_FREQUENCIA"
				   + "(RELATORIO_ANO, RELATORIO_MES, RELATORIO_CARGA_HORARIA, RELATORIO_EDITAL,"
				   + " ORIENTADOR_SIAPE, FUNCIONARIO_SIAPE, DATA_ENTREGA, ALUNO_MATRICULA)"
				   + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement ptmt = null;
		ResultSet generatedKeys = null;
		try {
			
			ptmt = connection.prepareStatement(SQL_INSERT, 
							Statement.RETURN_GENERATED_KEYS);
			ptmt.setInt(1, relatorioFrequencia.getAno());
			ptmt.setInt(2, relatorioFrequencia.getMes());
			ptmt.setInt(3, relatorioFrequencia.getCargaHorariaMensal());
			ptmt.setString(4, relatorioFrequencia.getEdital());
			ptmt.setString(5, relatorioFrequencia.getOrientador().getSiape());
			ptmt.setString(6,  relatorioFrequencia.getFuncionario().getSiape());
			ptmt.setDate(7, new java.sql.Date(relatorioFrequencia.getDataEntregaRelatorio().getTime()));
			ptmt.setString(8, relatorioFrequencia.getMonitor().getMatricula());
			ptmt.executeUpdate();
			connection.commit();
			try {
				generatedKeys = ptmt.getGeneratedKeys();
				if ( generatedKeys.next() ) {
					inserirSemanasRelatorio(relatorioFrequencia.getSemanas(), 
									generatedKeys.getInt(1) );
				}
			} catch ( SQLException e ) {
				e.printStackTrace();
				throw new SQLException("Insert failed, no id obtained.", e);
			}
			
			
		} catch ( SQLException e ) {
			try {
				e.printStackTrace();
				connection.rollback();
			} catch ( SQLException ee) {
				throw new RuntimeException(ee);
			}
		} finally { 
			try {
				
				ptmt.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		
	}
	
	final private void inserirSemanasRelatorio(Collection<Semana> semanas, int relatorio_id) {
		final String SQL_INSERT = "INSERT INTO PROJETO_PERIODO.SEMANA"
						  + "(SEMANA_DESCRICAO, SEMANA_OBS, RELATORIO_CD)"
						  + "VALUES(?, ?, ?)";
		PreparedStatement ptmt = null;
		ResultSet generatedKeys;
		try {
			ptmt = connection.prepareStatement(SQL_INSERT, 
							Statement.RETURN_GENERATED_KEYS);
			for ( Semana semana: semanas )  {
				ptmt.setString(1, semana.getDescricao());
				ptmt.setString(2, semana.getObservacoes());
				ptmt.setInt(3, relatorio_id);
				ptmt.executeUpdate();
				connection.commit();
				try {
					generatedKeys = ptmt.getGeneratedKeys();
					if ( generatedKeys.next() ) {
						inserirAtividadesSemana(semana.getAtividades(), 
										generatedKeys.getInt(1) );
					}
				} catch ( SQLException e ) {
					e.printStackTrace();
					throw new SQLException("Insert failed, no id obtained.", e);
				}	
			}
			
		} catch ( SQLException e ) {
				try {
					e.printStackTrace();
					connection.rollback();
			} catch ( SQLException ee) {
				throw new RuntimeException(ee);
			}
		} finally { 
			try {
				ptmt.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	final private void inserirAtividadesSemana(Collection<Atividade> atividades, int semana_id) {
		final String SQL_INSERT = "INSERT INTO ATIVIDADE (ATIVIDADE_DATA, "
						        + "HORARIO_ENTRADA, HORARIO_SAIDA, SEMANA_CD)"
						        + "VALUES( ?, ?, ?, ?)";
		PreparedStatement ptmt = null;
		try {
			ptmt = connection.prepareStatement(SQL_INSERT);
			for ( Atividade atividade : atividades ) {

				ptmt.setDate(1, new java.sql.Date(atividade.getData().getTime()));
				ptmt.setString(2, atividade.getHorarioEntrada());
				ptmt.setString(3, atividade.getHorarioSaida());
				ptmt.setInt(4, semana_id);
				ptmt.addBatch();
			}
			try {
				ptmt.executeBatch();
			} catch( BatchUpdateException e ) {
				e.printStackTrace();
				throw new SQLException(e);
			}
			connection.commit();
		} catch ( SQLException e ) {
			try {
				e.printStackTrace();
				connection.rollback();
			} catch ( SQLException ee) {
				throw new RuntimeException(ee);
			}
		} finally { 
			try {
				ptmt.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		
	}
	
	@Override
	public void atualizar(Object object) {

		// TODO Auto-generated method stub
		
	}

	@Override
	public void remover(Object object) {

		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<Object> listar() {

		final String SQL_SELECT_RELATORIO = "SELECT * " 
						+ "FROM RELATORIO_FREQUENCIA AS R "
						+ "INNER JOIN MONITOR AS M ON M.ALUNO_MATRICULA = R.ALUNO_MATRICULA "
						+ "INNER JOIN ALUNO AS A ON M.ALUNO_MATRICULA = M.ALUNO_MATRICULA "
						+ "INNER JOIN USUARIO AS U ON U.USUARIO_LOGIN = A.USUARIO_LOGIN "
						+ "INNER JOIN ORIENTADOR AS O ON O.ORIENTADOR_SIAPE = R.ORIENTADOR_SIAPE "
						+ "INNER JOIN FUNCIONARIO AS F ON F.FUNCIONARIO_SIAPE = R.FUNCIONARIO_SIAPE "
						+ "INNER JOIN DISCIPLINA AS D ON D.DISCIPLINA_CD = M.DISCIPLINA_CD ";

		
		final String SQL_SELECT_SEMANA = "SELECT * FROM SEMANA"
						               + "WHERE RELATORIO_CD = ?";
		final String SQL_SELECT_ATIVIDADE = "SELECT * FROM ATIVIDADE"
						                  + "WHERE SEMANA_CD = ?";
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		Collection<Object> relatorios = new ArrayList<>();
		try {
			ptmt = connection.prepareStatement(SQL_SELECT_RELATORIO);			
			rs = ptmt.executeQuery();
			while ( rs.next() ) {
				RelatorioFrequencia relatorio = new RelatorioFrequencia();
				relatorio.setChavePrimaria(rs.getInt("RELATORIO_CD"));
				relatorio.setAno(rs.getInt("RELATORIO_ANO"));
				relatorio.setMes(rs.getInt("RELATORIO_MES"));
				relatorio.setEdital(rs.getString("RELATORIO_EDITAL"));
				relatorio.setCargaHorariaMensal(rs.getInt("RELATORIO_CARGA_HORARIA"));
				relatorios.add(relatorio);
			}
		} catch ( SQLException e ) {
			throw new RuntimeException(e);
		} finally {
			try {
				ptmt.close();
			} catch (SQLException e ) {
				throw new RuntimeException(e);
			}
		}
		return relatorios;
	}
	@Override
	public Object buscar(Object object) {

		// TODO Auto-generated method stub
		return null;
	}

}