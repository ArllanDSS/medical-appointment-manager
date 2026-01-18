package br.com.arllan.medsync.model;

public class Medico extends Pessoa {

    private String crm;
    private Especialidade especialidade;
    private Boolean ativo;

    public Medico(int id, String nome, String email, String crm, Especialidade especialidade) {

        super(id, nome, email);

        this.crm = crm;
        this.especialidade = especialidade;
        this.ativo = true;

    }

    public Medico(int id, String nome, String email, String crm, Especialidade especialidade, Boolean ativo) {

        super(id, nome, email);

        this.crm = crm;
        this.especialidade = especialidade;
        this.ativo = ativo;

    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return String.format("ID: %-5d | Nome: %-20s | Email: %-25s | CRM: %-10s | Especialidade: %-20s | Ativo: %-10s",
                getId(), getNome(), getEmail(), getCrm(), getEspecialidade(), isAtivo() ? "SIM" : "NÃO");
    }


}
