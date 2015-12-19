package br.com.projetoperiodo.servlets.cadastro;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.projetoperiodo.model.instituto.disciplina.Disciplina;
import br.com.projetoperiodo.model.instituto.professor.Professor;
import br.com.projetoperiodo.util.constantes.Constantes;
import br.com.projetoperiodo.util.exception.NegocioException;
import br.com.projetoperiodo.util.fachada.Fachada;

/**
 * Servlet implementation class ServletCadastroDisciplina
 */
public class ServletCadastroDisciplina extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final String LISTA_DISCIPLINAS = "listaDisciplinasSemProfessor";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletCadastroDisciplina() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession(Boolean.FALSE) == null) {
			request.getRequestDispatcher("/acesso.do").forward(request, response);
		}
		List<Disciplina> listaDisciplinas = Fachada.getInstance().listarDisciplinasSemProfessor();
		request.setAttribute(LISTA_DISCIPLINAS, listaDisciplinas);
		request.getRequestDispatcher("/WEB-INF/jsp/CadastroProfessorDisciplina.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession(Boolean.FALSE) == null) {
			request.getRequestDispatcher("/acesso.do").forward(request, response);
		}
		String descricaoDisciplina = request.getParameter("disciplina");
		Professor professor = (Professor)request.getSession(Boolean.FALSE).getAttribute(Constantes.ATRIBUTO_USUARIO_LOGADO);
		try {
			Disciplina disciplina = (Disciplina) Fachada.getInstance().buscarDisciplina(descricaoDisciplina);
			disciplina.setProfessor(professor);
			Fachada.getInstance().atualizarDisciplina(disciplina);
			request.getRequestDispatcher("/professor.do").forward(request, response);
		} catch (NegocioException e) {
			// TODO Tratar
			e.printStackTrace();
		}
	}

}