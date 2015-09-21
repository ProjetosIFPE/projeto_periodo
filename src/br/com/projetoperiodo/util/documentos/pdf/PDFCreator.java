package br.com.projetoperiodo.util.documentos.pdf;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import br.com.projetoperiodo.model.instituto.aluno.monitor.Monitor;
import br.com.projetoperiodo.model.instituto.curso.Curso;
import br.com.projetoperiodo.model.instituto.disciplina.Disciplina;
import br.com.projetoperiodo.model.instituto.orientador.Orientador;
import br.com.projetoperiodo.model.relatorio.frequencia.RelatorioFrequencia;

public class PDFCreator {

	private static PDFCreator creator = null;
	private PdfReader reader;
	private static final String SOURCE = "selection.pdf";
	private static final String DEST = "relatorio.pdf";

	public static final int NOME_MONITOR_X = 124;
	public static final int NOME_MONITOR_Y = 675;

	public static final int NOME_DISCIPLINA_X = 132;
	public static final int NOME_DISCIPLINA_Y = 658;
	
	public static final int NOME_ORIENTADOR_X = 142;
	public static final int NOME_ORIENTADOR_Y = 641;
	
	public static final int MATRICULA_X = 546;
	public static final int MATRICULA_Y = 675;
	
	public static final int NOME_CURSO_X = 534;
	public static final int NOME_CURSO_Y = 658;
	
	private PDFCreator() {
		try {
			reader = new PdfReader(SOURCE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void closeReader() {
		reader.close();
	}

	public static PDFCreator getInstancia() {
		if (creator == null) {
			creator = new PDFCreator();
		}
		return creator;
	}

	private void preencherNomeMonitor(String nome, PdfContentByte canvas) {
		Phrase phrase = new Phrase(nome);
		ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase, NOME_MONITOR_X, NOME_MONITOR_Y, 0);
	}
	
	private void preencherNomeDisciplina(String nome, PdfContentByte canvas) {
		FontSelector seletor = new FontSelector();
		Font font = FontFactory.getFont(FontFactory.HELVETICA, 11);
		seletor.addFont(font);
		Phrase phrase = seletor.process(nome);
		ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase, NOME_DISCIPLINA_X, NOME_DISCIPLINA_Y, 0);
	}
	
	private void preencherNomeOrientador(String nome, PdfContentByte canvas) {
		FontSelector seletor = new FontSelector();
		Font font = FontFactory.getFont(FontFactory.HELVETICA, 11);
		seletor.addFont(font);
		Phrase phrase = seletor.process(nome);
		ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase, NOME_ORIENTADOR_X, NOME_ORIENTADOR_Y, 0);
	}
	
	private void preencherMatricula(String nome , PdfContentByte canvas) {
		FontSelector seletor = new FontSelector();
		Font font = FontFactory.getFont(FontFactory.HELVETICA, 11);
		seletor.addFont(font);
		Phrase phrase = seletor.process(nome);
		ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, phrase, MATRICULA_X, MATRICULA_Y, 0);
	}
	
	private void preencherNomeCurso(String nome,  PdfContentByte canvas ) {
		FontSelector seletor = new FontSelector();
		Font font = FontFactory.getFont(FontFactory.HELVETICA, 11);
		seletor.addFont(font);
		Phrase phrase = seletor.process(nome);
		ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, phrase, NOME_CURSO_X, NOME_CURSO_Y, 0);
	}

	public void gerarRelatorio(RelatorioFrequencia relatorio) {
		PdfStamper stamper = null;
		try {
			stamper = new PdfStamper(reader, new FileOutputStream(DEST));
			PdfContentByte canvas = stamper.getOverContent(1);
			preencherNomeMonitor(relatorio.getMonitor().getNome(), canvas);
			preencherNomeDisciplina(relatorio.getMonitor().
					getDisciplina().getDescricao(), canvas);
			preencherNomeOrientador(relatorio.getOrientador().getNome(), canvas);
			preencherMatricula(relatorio.getMonitor().getMatricula(), canvas);
			preencherNomeCurso(relatorio.getMonitor().getCurso().getDescricao(), canvas);
		} catch (DocumentException | IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				stamper.close();
			} catch (DocumentException | IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	
	public static void main(String[] args)  {
		RelatorioFrequencia relatorio = new RelatorioFrequencia();
		Monitor monitor = new Monitor();
		Disciplina disciplina = new Disciplina();
		Orientador orientador = new Orientador();
		Curso curso = new Curso();
		curso.setDescricao("Análise de Sistemas");
		orientador.setNome("Marcos Costa");
		disciplina.setDescricao("Introdução à Programação");
		monitor.setNome("Edmilson Santana");
		monitor.setDisciplina(disciplina);
		monitor.setMatricula("20141Y6-RC0323");
		monitor.setCurso(curso);
		relatorio.setOrientador(orientador);
		relatorio.setMonitor(monitor);
		PDFCreator.getInstancia().gerarRelatorio(relatorio);
	}
}
