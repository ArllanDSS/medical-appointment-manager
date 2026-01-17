package br.com.arllan.medsync.model;

public class Paciente extends Pessoa {

    private String cpf;

    public Paciente(int id, String nome, String email, String cpf) {

        super(id, nome, email);
        this.cpf = cpf;

    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }


}
