-- 1. Limpar tudo
TRUNCATE TABLE consultas, medicos, pacientes RESTART IDENTITY CASCADE;

-- 2. Inserir Médicos
INSERT INTO medicos (nome, email, crm, especialidade, ativo) VALUES
('Ana Beatriz', 'ana@medsync.com', 'CRM-101', 'CARDIOLOGIA', true),
('Marcos Paulo', 'marcos@medsync.com', 'CRM-202', 'PEDIATRIA', true),
('Julia Costa', 'julia@medsync.com', 'CRM-303', 'DERMATOLOGIA', true);

-- 3. Inserir Pacientes
INSERT INTO pacientes (nome, cpf, email, ativo) VALUES
('Carlos Oliveira', '11122233344', 'carlos@gmail.com', true),
('Mariana Souza', '55566677788', 'mariana@gmail.com', true),
('Fernando Lima', '99900011122', 'fernando@gmail.com', true),
('Beatriz Ramos', '33344455566', 'beatriz@gmail.com', true);

-- 4. Inserir algumas Consultas de teste
INSERT INTO consultas (id_medico, id_paciente, data_hora, motivo, status) VALUES
(1, 1, '2026-01-20 09:00:00', 'Check-up anual', 'AGENDADA'),
(1, 2, '2026-01-20 10:00:00', 'Dores no peito', 'AGENDADA'),
(2, 3, '2026-01-21 14:00:00', 'Rotina infantil', 'AGENDADA');