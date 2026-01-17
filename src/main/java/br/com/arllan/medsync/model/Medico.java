package br.com.arllan.medsync.model;

public class Medico extends Pessoa {

    private String crm;
    private Especialidade especialidade;

    public Medico(int id, String nome, String email, String crm, Especialidade especialidade) {

        super(id, nome, email);

        this.crm = crm;
        this.especialidade = especialidade;

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


}
