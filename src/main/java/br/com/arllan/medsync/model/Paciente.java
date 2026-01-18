package br.com.arllan.medsync.model;

public class Paciente extends Pessoa {

    private String cpf;
    private Boolean ativo;

    public Paciente(int id, String nome, String cpf, String email, Boolean ativo) {

        super(id, nome, email);
        this.cpf = cpf;
        this.ativo = ativo;

    }

    public Paciente(String nome, String cpf, String email) {
        super(nome, email);
        this.cpf = cpf;
        this.ativo = true;
    }

    public Paciente() {
        super();
    }


    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return String.format("ID: %-5d | Nome: %-20s | Email: %-25s |  Ativo: %-10s",
                getId(), getNome(), getEmail(), isAtivo() ? "SIM" : "NÃO");
    }
}
