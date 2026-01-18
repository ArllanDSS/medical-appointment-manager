package br.com.arllan.medsync.model;

import java.time.LocalDateTime;

public class Consulta {


    private Integer id;
    private Paciente paciente;
    private Medico medico;
    private LocalDateTime data;
    private String motivo;
    private StatusConsulta status;



    public Consulta(Integer id, Paciente paciente, Medico medico, LocalDateTime data, String motivo, StatusConsulta status) {

        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.data = data;
        this.motivo = motivo;
        this.status = status;

    }

    public Consulta(Paciente paciente, Medico medico, LocalDateTime dataConsulta, String motivoConsulta) {

        this.paciente = paciente;
        this.medico = medico;
        this.data = dataConsulta;
        this.motivo = motivoConsulta;
        this.status = StatusConsulta.AGENDADA;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public StatusConsulta getStatus() {
        return status;
    }

    public void setStatus(StatusConsulta statusConsulta) {
        this.status = statusConsulta;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime dataConsulta) {
        this.data = dataConsulta;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivoConsulta) {
        this.motivo = motivoConsulta;
    }

    @Override
    public String toString() {
        return String.format("Consulta [ID: %s | Data: %s | Paciente: %s | Médico: %s | Status: %s]",
                (id == null ? "N/A" : id),
                data,
                paciente.getNome(),
                medico.getNome(),
                status);
    }
}
