DROP DATABASE IF EXISTS MONITORIAIFPE;
CREATE DATABASE MONITORIAIFPE;
USE MONITORIAIFPE;
DROP TABLE IF EXISTS MONITORIAIFPE.TB_USUARIO;
CREATE TABLE MONITORIAIFPE.TB_USUARIO (
	USUARIO_ID INTEGER AUTO_INCREMENT,
	USUARIO_NOME VARCHAR(30) NOT NULL,
	USUARIO_SOBRENOME VARCHAR(30) NOT NULL,
	USUARIO_LOGIN VARCHAR(16) NOT NULL,
        PAPEL_USUARIO VARCHAR(1),
	USUARIO_SENHA VARCHAR(18) NOT NULL,
	USUARIO_EMAIL VARCHAR(30) NOT NULL,
	ULTIMA_ALTERACAO TIMESTAMP DEFAULT current_timestamp() NOT NULL,
	PRIMARY KEY(USUARIO_ID)
);

DROP TABLE IF EXISTS MONITORIAIFPE.TB_PERIODO;
CREATE TABLE MONITORIAIFPE.TB_PERIODO (
	PERIODO_ID INTEGER NOT NULL AUTO_INCREMENT,
	SEMESTRE ENUM('PRIMEIRO', 'SEGUNDO') NOT NULL,	
	PERIODO_ANO INTEGER NOT NULL,
	ULTIMA_ALTERACAO TIMESTAMP DEFAULT current_timestamp() NOT NULL,
	PRIMARY KEY(PERIODO_ID)
);


DROP TABLE IF EXISTS MONITORIAIFPE.TB_CURSO;
CREATE TABLE MONITORIAIFPE.TB_CURSO (
	CURSO_ID INTEGER NOT NULL AUTO_INCREMENT,
        CODIGO_CAMPUS VARCHAR(2) NOT NULL,
        CODIGO_CURSO VARCHAR(2) NOT NULL UNIQUE,
	CURSO_DS VARCHAR(100) NOT NULL,
	ULTIMA_ALTERACAO TIMESTAMP DEFAULT current_timestamp() NOT NULL,
	PRIMARY KEY(CURSO_ID)
);

DROP TABLE IF EXISTS MONITORIAIFPE.TB_PROFESSOR;
CREATE TABLE MONITORIAIFPE.TB_PROFESSOR( 
	PROFESSOR_ID INTEGER,
	PRIMARY KEY(PROFESSOR_ID),
	ULTIMA_ALTERACAO TIMESTAMP DEFAULT current_timestamp() NOT NULL,
	FOREIGN KEY (PROFESSOR_ID) REFERENCES TB_USUARIO(USUARIO_ID) 
);

DROP TABLE IF EXISTS MONITORIAIFPE.TB_COMPONENTE_CURRICULAR;
CREATE TABLE MONITORIAIFPE.TB_COMPONENTE_CURRICULAR (
    COMPONENTE_CURRICULAR_ID INTEGER NOT NULL AUTO_INCREMENT,
    CODIGO_COMP_CURRICULAR varchar(150) NOT NULL,
    COMP_CURRICULAR_DS varchar(150) NOT NULL,
    CURSO_ID INTEGER NOT NULL,
    PRIMARY KEY(COMPONENTE_CURRICULAR_ID),
    FOREIGN KEY (CURSO_ID) REFERENCES TB_CURSO(CURSO_ID),
    ULTIMA_ALTERACAO TIMESTAMP DEFAULT current_timestamp() NOT NULL
);

DROP TABLE IF EXISTS MONITORIAIFPE.TB_DISCIPLINA;
CREATE TABLE MONITORIAIFPE.TB_DISCIPLINA ( 
	DISCIPLINA_ID INT NOT NULL AUTO_INCREMENT,
        COMPONENTE_CURRICULAR_ID INTEGER NOT NULL,
	PROFESSOR_ID INT NOT NULL,
        PERIODO_ID INTEGER NOT NULL,
	PRIMARY KEY(DISCIPLINA_ID),
	FOREIGN KEY (COMPONENTE_CURRICULAR_ID) REFERENCES TB_COMPONENTE_CURRICULAR(COMPONENTE_CURRICULAR_ID),
        FOREIGN KEY (PROFESSOR_ID) REFERENCES TB_PROFESSOR(PROFESSOR_ID),
        FOREIGN KEY (PERIODO_ID) REFERENCES TB_PERIODO(PERIODO_ID),
	ULTIMA_ALTERACAO TIMESTAMP DEFAULT current_timestamp() NOT NULL
);

DROP TABLE IF EXISTS MONITORIAIFPE.TB_ALUNO;
CREATE TABLE MONITORIAIFPE.TB_ALUNO (
    ALUNO_ID INTEGER NOT NULL,
    ALUNO_MATRICULA VARCHAR(20) NOT NULL,
    CURSO_ID INTEGER NOT NULL,
    PRIMARY KEY (ALUNO_ID),
    FOREIGN KEY (CURSO_ID) REFERENCES TB_CURSO(CURSO_ID),
    ULTIMA_ALTERACAO TIMESTAMP DEFAULT current_timestamp() NOT NULL,
    FOREIGN KEY (ALUNO_ID) REFERENCES TB_USUARIO(USUARIO_ID)  
);

DROP TABLE IF EXISTS MONITORIAIFPE.TB_BOLETIM_CURRICULAR;
CREATE TABLE MONITORIAIFPE.TB_BOLETIM_CURRICULAR ( 
    BOLETIM_CURRICULAR_ID INT NOT NULL AUTO_INCREMENT,
    DISCIPLINA_ID INTEGER NOT NULL,
    ALUNO_ID INTEGER NOT NULL,
    NOTA_BOLETIM DOUBLE NOT NULL,
    FREQUENCIA_BOLETIM DOUBLE NOT NULL,
    PRIMARY KEY(BOLETIM_CURRICULAR_ID),
    FOREIGN KEY(DISCIPLINA_ID)REFERENCES TB_DISCIPLINA(DISCIPLINA_ID),
    FOREIGN KEY(ALUNO_ID)REFERENCES TB_ALUNO(ALUNO_ID),
    ULTIMA_ALTERACAO TIMESTAMP DEFAULT current_timestamp() NOT NULL
);

DROP TABLE IF EXISTS MONITORIAIFPE.TB_MONITORIA;
CREATE TABLE MONITORIAIFPE.TB_MONITORIA ( 
	MONITORIA_ID INT NOT NULL AUTO_INCREMENT,
	ALUNO_ID INT NOT NULL,
	MODALIDADE ENUM('BOLSISTA', 'VOLUNTARIO'),
	DISCIPLINA_ID INT NOT NULL,
	PERIODO_ID INTEGER NOT NULL,
	PRIMARY KEY(MONITORIA_ID),
	ULTIMA_ALTERACAO TIMESTAMP DEFAULT current_timestamp() NOT NULL,
	FOREIGN KEY(ALUNO_ID) REFERENCES TB_ALUNO(ALUNO_ID) ,
	FOREIGN KEY(PERIODO_ID) REFERENCES TB_PERIODO(PERIODO_ID),
	FOREIGN KEY(DISCIPLINA_ID) REFERENCES TB_DISCIPLINA(DISCIPLINA_ID)
);

DROP TABLE IF EXISTS MONITORIAIFPE.TB_ATIVIDADE;
CREATE TABLE MONITORIAIFPE.TB_ATIVIDADE (
	ATIVIDADE_ID INT NOT NULL AUTO_INCREMENT,
	ATIVIDADE_DATA DATE NOT NULL,
	HORARIO_ENTRADA TIME NOT NULL,
	HORARIO_SAIDA TIME NOT NULL,
        ATIVIDADE_DESCRICAO VARCHAR(140) NOT NULL,
	ATIVIDADE_OBSERVACAO VARCHAR(140),
        SITUACAO_ATIVIDADE ENUM('APROVADA', 'AGUARDANDO_APROVACAO') NOT NULL,	
	MONITORIA_ID INT,
	PRIMARY KEY(ATIVIDADE_ID),
	ULTIMA_ALTERACAO TIMESTAMP DEFAULT current_timestamp() NOT NULL,
	FOREIGN KEY (MONITORIA_ID) REFERENCES TB_MONITORIA(MONITORIA_ID) 
);