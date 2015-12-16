DROP USER "PROJETO_PERIODO" CASCADE;
-- USER SQL
CREATE USER PROJETO_PERIODO IDENTIFIED BY PROJETO_PERIODO 
DEFAULT TABLESPACE "SYSTEM"
TEMPORARY TABLESPACE "TEMP";

-- QUOTAS

-- ROLES
-- SYSTEM PRIVILEGES
GRANT "DBA" TO PROJETO_PERIODO ;

----------------------------- Usuario --------------------------------- 

CREATE TABLE "PROJETO_PERIODO"."USUARIO" (
	USUARIO_ID NUMBER,
	USUARIO_NOME VARCHAR2(200),
	USUARIO_SOBRENOME VARCHAR2(200),
	USUARIO_LOGIN VARCHAR2(20),
	USUARIO_SENHA VARCHAR2(40),
	USUARIO_EMAIL VARCHAR2(50),
	ULTIMO_ACESSO TIMESTAMP,
	ULTIMA_ALTERACAO TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	PRIMARY KEY(USUARIO_ID)
);



----------------------------- Periodo ---------------------------------

CREATE TABLE "PROJETO_PERIODO"."PERIODO" (
	PERIODO_ID NUMBER NOT NULL,
	PERIODO_ANO NUMBER NOT NULL,
    SEMESTRE VARCHAR2(10) CHECK( SEMESTRE IN ('PRIMEIRO','SEGUNDO') ) NOT NULL,
	ULTIMA_ALTERACAO TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	PRIMARY KEY(PERIODO_ID)
);


----------------------------- Curso ---------------------------------


CREATE TABLE "PROJETO_PERIODO"."CURSO" (
	CURSO_ID NUMBER NOT NULL,
	GRAU VARCHAR2(10) CHECK( GRAU IN ('SUPERIOR','TECNICO') ) NOT NULL,
	CURSO_DS VARCHAR2(20) NOT NULL,
	ULTIMA_ALTERACAO TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	PRIMARY KEY(CURSO_ID)
);

----------------------------- Professor ---------------------------------

CREATE TABLE "PROJETO_PERIODO"."PROFESSOR"( 
	PROFESSOR_ID NUMBER,
	PRIMARY KEY(PROFESSOR_ID),
	ULTIMA_ALTERACAO TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	FOREIGN KEY (PROFESSOR_ID) REFERENCES "PROJETO_PERIODO"."USUARIO"(USUARIO_ID)
);

----------------------------- Disciplina ---------------------------------


CREATE TABLE "PROJETO_PERIODO"."DISCIPLINA" ( 
	DISCIPLINA_ID NUMBER NOT NULL,
	PROFESSOR_ID NUMBER,
	DISCIPLINA_DS VARCHAR2(200),
	CURSO_ID NUMBER NOT NULL,
	PRIMARY KEY(DISCIPLINA_ID),
	FOREIGN KEY (PROFESSOR_ID) REFERENCES "PROJETO_PERIODO"."PROFESSOR"(PROFESSOR_ID),
	ULTIMA_ALTERACAO TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	FOREIGN KEY (CURSO_ID) REFERENCES "PROJETO_PERIODO"."CURSO"(CURSO_ID)
);


------------------------------ Aluno -------------------------------

CREATE TABLE "PROJETO_PERIODO"."ALUNO" (
	ALUNO_ID NUMBER NOT NULL,
	ALUNO_MATRICULA VARCHAR2(20) NOT NULL,
	CURSO_ID NUMBER NOT NULL,
	PRIMARY KEY (ALUNO_ID),
	FOREIGN KEY (CURSO_ID) REFERENCES "PROJETO_PERIODO"."CURSO"(CURSO_ID),
	ULTIMA_ALTERACAO TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	FOREIGN KEY (ALUNO_ID) REFERENCES "PROJETO_PERIODO"."USUARIO"(USUARIO_ID)
);

CREATE TABLE "PROJETO_PERIODO"."DISCIPLINA_ALUNO" ( 
	ALUNO_ID NUMBER NOT NULL,
	DISCIPLINA_ID NUMBER NOT NULL,
	PRIMARY KEY (ALUNO_ID, DISCIPLINA_ID),
	FOREIGN KEY (ALUNO_ID) REFERENCES "PROJETO_PERIODO"."ALUNO"(ALUNO_ID),
	ULTIMA_ALTERACAO TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	FOREIGN KEY (DISCIPLINA_ID) REFERENCES "PROJETO_PERIODO"."DISCIPLINA"(DISCIPLINA_ID)
);

------------------------------ Monitoria -------------------------------

CREATE TABLE "PROJETO_PERIODO"."MONITORIA" ( 
	MONITOR_ID NUMBER NOT NULL,
	ALUNO_ID NUMBER NOT NULL,
	MODALIDADE VARCHAR2(10) CHECK( MODALIDADE IN ('BOLSISTA','VOLUNTARIO') ) NOT NULL,
	DISCIPLINA_ID INT NOT NULL,
	PERIODO_ID NUMBER NOT NULL,
	HABILITADO CHAR CHECK (HABILITADO in (0,1)) NOT NULL,
	HORARIO_ENTRADA VARCHAR(5),
	HORARIO_SAIDA VARCHAR2(5),
	PRIMARY KEY(MONITOR_ID),
	ULTIMA_ALTERACAO TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	FOREIGN KEY(ALUNO_ID) REFERENCES "PROJETO_PERIODO"."ALUNO"(ALUNO_ID),
	FOREIGN KEY(PERIODO_ID) REFERENCES "PROJETO_PERIODO"."PERIODO"(PERIODO_ID),
	FOREIGN KEY(DISCIPLINA_ID) REFERENCES "PROJETO_PERIODO"."DISCIPLINA"(DISCIPLINA_ID)
);


-------------------------- Relatório Frequência -------------------------

CREATE TABLE "PROJETO_PERIODO"."RELATORIO_FREQUENCIA" (
	RELATORIO_ID NUMBER NOT NULL,
	RELATORIO_MES NUMBER(2),
	MONITOR_ID NUMBER NOT NULL,
	SITUACAO_RELATORIO VARCHAR2(10) CHECK( SITUACAO_RELATORIO IN ('APROVADO','ESPERA') ) NOT NULL,
	PRIMARY KEY (RELATORIO_ID),
	ULTIMA_ALTERACAO TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  FOREIGN KEY (MONITOR_ID) REFERENCES "PROJETO_PERIODO"."MONITORIA"(MONITOR_ID)
);

-------------------------------- Semana -----------------------------------


CREATE TABLE "PROJETO_PERIODO"."SEMANA"(
	SEMANA_ID NUMBER NOT NULL,
	SEMANA_DESCRICAO VARCHAR2(200),
	SEMANA_OBS VARCHAR2(200),
	RELATORIO_ID NUMBER,
	PRIMARY KEY(SEMANA_ID),
	ULTIMA_ALTERACAO TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	FOREIGN KEY (RELATORIO_ID) REFERENCES "PROJETO_PERIODO"."RELATORIO_FREQUENCIA"(RELATORIO_ID)
);

----------------------------- Atividade ---------------------------


CREATE TABLE "PROJETO_PERIODO"."ATIVIDADE" (
	ATIVIDADE_ID NUMBER NOT NULL,
	ATIVIDADE_DATA DATE,
	HORARIO_ENTRADA VARCHAR2(5),
	HORARIO_SAIDA VARCHAR2(5),
	SEMANA_ID NUMBER,
	PRIMARY KEY(ATIVIDADE_ID),
	ULTIMA_ALTERACAO TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	FOREIGN KEY (SEMANA_ID) REFERENCES "PROJETO_PERIODO"."SEMANA"(SEMANA_ID)
);



----------------------------- Documento ---------------------------------


CREATE TABLE "PROJETO_PERIODO"."TEMPLATE_DOCUMENTO" (
   TEMPLATE_ID NUMBER NOT NULL,
   TEMPLATE_CONTENT BLOB,
   TEMPLATE_NOME VARCHAR2(30),
   PRIMARY KEY(TEMPLATE_ID),
   ULTIMA_ALTERACAO TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);


-----------------------------------------------------------------

CREATE SEQUENCE "PROJETO_PERIODO"."HIBERNATE_SEQUENCE" INCREMENT BY 1 START WITH 10;
GRANT ALL ON "PROJETO_PERIODO"."HIBERNATE_SEQUENCE" TO PROJETO_PERIODO;
CREATE PUBLIC SYNONYM HIBERNATE_SEQUENCE FOR PROJETO_PERIODO.HIBERNATE_SEQUENCE;


COMMIT;