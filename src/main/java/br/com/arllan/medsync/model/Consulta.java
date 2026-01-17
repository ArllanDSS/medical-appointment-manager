package br.com.arllan.medsync.model;

import java.time.LocalDateTime;

public class Consulta {


    private Paciente paciente;
    private Medico medico;

    private StatusConsulta statusConsulta;

    private LocalDateTime dataConsulta;

    public Consulta(Paciente paciente, Medico medico, LocalDateTime dataConsulta) {
        this.paciente = paciente;
        this.medico = medico;
        this.dataConsulta = dataConsulta;

        this.statusConsulta = StatusConsulta.AGENDADA;

    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public StatusConsulta getStatusConsulta() {
        return statusConsulta;
    }

    public void setStatusConsulta(StatusConsulta statusConsulta) {
        this.statusConsulta = statusConsulta;
    }

    public LocalDateTime getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(LocalDateTime dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
