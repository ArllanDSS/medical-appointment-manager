package br.com.arllan.medsync.service;

import br.com.arllan.medsync.exception.ValidacaoException;
import br.com.arllan.medsync.model.Consulta;
import br.com.arllan.medsync.model.Medico;
import br.com.arllan.medsync.model.Paciente;
import br.com.arllan.medsync.repository.ConsultaDAO;
import br.com.arllan.medsync.repository.MedicoDAO;
import br.com.arllan.medsync.repository.PacienteDAO;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class ConsultaService {

    private ConsultaDAO consultaDAO = new ConsultaDAO();
    private MedicoDAO medicoDAO = new MedicoDAO();
    private PacienteDAO pacienteDAO = new PacienteDAO();

    public void agendar(Consulta consulta) {
        validarRegrasAgendamento(consulta);
        consultaDAO.agendar(consulta);
    }

    private void validarRegrasAgendamento(Consulta consulta) {

        LocalDateTime data = consulta.getData();

        if (data.isBefore(LocalDateTime.now())) {
            throw new ValidacaoException("Não é possível agendar consultas para o passado.");
        }

        if (data.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            throw new ValidacaoException("A clínica não funciona aos domingos.");
        }

        if (data.getHour() < 8 || data.getHour() > 18) {
            throw new ValidacaoException("Horário fora do período de funcionamento (08:00 às 18:00).");
        }

        // O Service consulta o MedicoDAO para obter informações atualizadas
        Medico medico = medicoDAO.buscarPorId(consulta.getMedico().getId());
        if (medico == null) {
            throw new ValidacaoException("Médico não encontrado no sistema.");
        }
        if (!medico.isAtivo()) {
            throw new ValidacaoException("Não é possível agendar com um médico que não está ativo.");
        }

        Paciente paciente = pacienteDAO.buscarPorId(consulta.getPaciente().getId());
        if (paciente == null) {
            throw new ValidacaoException("Paciente não encontrado no sistema.");
        }
        if (!paciente.isAtivo()) {
            throw new ValidacaoException("Não é possível agendar para um paciente que não está ativo.");
        }

        if (consultaDAO.existeConsultaNoHorario(consulta.getMedico().getId(), consulta.getData())) {
            throw new ValidacaoException("O médico já possui outra consulta agendada para este mesmo horário!");
        }

        if (consultaDAO.existeConsultaPacienteNoDia(consulta.getPaciente().getId(), consulta.getData())) {
            throw new ValidacaoException("O paciente já possui uma consulta agendada para este dia!");
        }

    }

    public void cancelar(int id) {
        // Aqui poderiam ser adicionadas regras como "cancelar apenas com 24h de antecedência"
        boolean sucesso = consultaDAO.cancelar(id);
        if (!sucesso) {
            throw new ValidacaoException("Não foi possível cancelar a consulta. Verifique o ID.");
        }
    }
}