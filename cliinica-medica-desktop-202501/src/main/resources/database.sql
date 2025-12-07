CREATE DATABASE clinica
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

use clinica;
CREATE TABLE especialidades (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255)
);

CREATE TABLE medicos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    crm VARCHAR(20) NOT NULL UNIQUE,
    especialidade_id BIGINT NOT NULL,
    
    CONSTRAINT fk_medico_especialidade
        FOREIGN KEY (especialidade_id)
        REFERENCES especialidades(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);
CREATE TABLE IF NOT EXISTS pacientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    data_nascimento DATE, 
    cpf VARCHAR(14) NOT NULL,
    telefone VARCHAR(20)
);
CREATE TABLE IF NOT EXISTS atendentes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL
);

INSERT INTO atendentes (nome, usuario, senha) 
SELECT 'Administrador', 'admin', '123' 
WHERE NOT EXISTS (SELECT * FROM atendentes WHERE usuario = 'admin');


CREATE TABLE IF NOT EXISTS convenios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_empresa VARCHAR(100) NOT NULL,
    cnpj VARCHAR(20),
    telefone VARCHAR(20)
);

INSERT INTO convenios (nome_empresa, cnpj, telefone) VALUES ('Particular', '00000000000', '0000-0000');
INSERT INTO convenios (nome_empresa, cnpj, telefone) VALUES ('Unimed', '11111111111', '0800-1111');

ALTER TABLE convenios
ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ATIVO';

CREATE TABLE IF NOT EXISTS consultas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_hora DATETIME NOT NULL,
    observacoes TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'AGENDADA',
    id_medico BIGINT NOT NULL,
    id_paciente BIGINT NOT NULL,
    id_convenio BIGINT NOT NULL,
    
    CONSTRAINT fk_consulta_medico FOREIGN KEY (id_medico) REFERENCES medicos(id),
    CONSTRAINT fk_consulta_paciente FOREIGN KEY (id_paciente) REFERENCES pacientes(id),
    CONSTRAINT fk_consulta_convenio FOREIGN KEY (id_convenio) REFERENCES convenios(id)
);

/// CODIGO AQUI PEDRO


USE clinica;

DROP TABLE IF EXISTS prontuarios;

DROP TABLE IF EXISTS consultas;

CREATE TABLE consultas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    
    data_hora DATETIME NOT NULL,
    observacoes TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'AGENDADA',
    
    medico_id BIGINT NOT NULL,
    paciente_id BIGINT NOT NULL,
    convenio_id BIGINT NOT NULL,
    
    CONSTRAINT fk_consulta_medico FOREIGN KEY (medico_id) REFERENCES medicos(id),
    CONSTRAINT fk_consulta_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    CONSTRAINT fk_consulta_convenio FOREIGN KEY (convenio_id) REFERENCES convenios(id)
);

CREATE TABLE prontuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    historico TEXT,
    receituario TEXT,
    exames TEXT,
    consulta_id BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (consulta_id) REFERENCES consultas(id)
);

ALTER TABLE medicos ADD COLUMN usuario VARCHAR(50) UNIQUE;
ALTER TABLE medicos ADD COLUMN senha VARCHAR(100);

-- Define o login padrão como o próprio CRM e senha '123'
UPDATE medicos SET usuario = crm, senha = '123' WHERE usuario IS NULL;