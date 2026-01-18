-- Limpeza prévia para scripts de desenvolvimento (Opcional)
DROP TABLE IF EXISTS consultas;
DROP TABLE IF EXISTS medicos;
DROP TABLE IF EXISTS pacientes;

-- Tabela de Médicos
CREATE TABLE medicos (
id SERIAL PRIMARY KEY,
nome VARCHAR(100) NOT NULL,
email VARCHAR(100) UNIQUE NOT NULL,
crm VARCHAR(20) UNIQUE NOT NULL,
especialidade VARCHAR(50) NOT NULL,
ativo BOOLEAN DEFAULT true
);

-- Tabela de Pacientes
CREATE TABLE pacientes (
id SERIAL PRIMARY KEY,
nome VARCHAR(100) NOT NULL,
cpf VARCHAR(11) UNIQUE NOT NULL,
email VARCHAR(100) UNIQUE NOT NULL,
ativo BOOLEAN DEFAULT true
);

-- Tabela de Consultas
CREATE TABLE consultas (
id SERIAL PRIMARY KEY,
id_medico INTEGER NOT NULL,
id_paciente INTEGER NOT NULL,
data_hora TIMESTAMP NOT NULL,
motivo TEXT,
status VARCHAR(20) DEFAULT 'AGENDADA',


CONSTRAINT fk_medico FOREIGN KEY (id_medico) REFERENCES medicos(id),
CONSTRAINT fk_paciente FOREIGN KEY (id_paciente) REFERENCES pacientes(id),


CONSTRAINT chk_status CHECK (status IN ('AGENDADA', 'CANCELADA', 'REALIZADA'))
);


CREATE INDEX idx_consulta_data ON consultas(data_hora);
CREATE INDEX idx_medico_crm ON medicos(crm);
CREATE INDEX idx_paciente_cpf ON pacientes(cpf);