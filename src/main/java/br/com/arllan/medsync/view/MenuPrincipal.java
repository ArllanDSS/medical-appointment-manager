package br.com.arllan.medsync.view;

import br.com.arllan.medsync.exception.ValidacaoException;
import br.com.arllan.medsync.model.*;
import br.com.arllan.medsync.repository.ConsultaDAO;
import br.com.arllan.medsync.repository.MedicoDAO;
import br.com.arllan.medsync.repository.PacienteDAO;
import br.com.arllan.medsync.service.ConsultaService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {

    private Scanner scanner = new Scanner(System.in);
    private ConsultaService consultaService = new ConsultaService();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void exibirMenu() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- MEDSYNC: SISTEMA DE AGENDAMENTO ---");
            System.out.println("1. Cadastrar Médico");
            System.out.println("2. Cadastrar Paciente");
            System.out.println("3. Agendar Consulta");
            System.out.println("4. Listar Consultas");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine()); // Evita bug do buffer

                switch (opcao) {
                    case 1 -> cadastrarMedico();
                    case 2 -> cadastrarPaciente();
                    case 3 -> iniciarAgendamento();
                    case 4 -> listarConsultas();
                    case 0 -> System.out.println("Encerrando sistema...");
                    default -> System.out.println("Opção em desenvolvimento ou inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: Por favor, digite apenas números.");
            } catch (Exception e) {
                System.out.println("Erro inesperado: " + e.getMessage());
            }
        }
    }

    private void iniciarAgendamento() {
        try {
            System.out.println("\n--- INICIANDO AGENDAMENTO ---");

            // Mostra os médicos disponíveis antes de pedir o ID
            System.out.println("\nMédicos disponíveis:");
            listarMedicos();
            System.out.print("Digite o ID do Médico: ");
            int idMedico = Integer.parseInt(scanner.nextLine());

            // Mostra os pacientes disponíveis antes de pedir o ID
            System.out.println("\nPacientes cadastrados:");
            listarPacientes();
            System.out.print("Digite o ID do Paciente: ");
            int idPaciente = Integer.parseInt(scanner.nextLine());

            System.out.print("Data e Hora (dd/MM/yyyy HH:mm): ");
            String dataStr = scanner.nextLine();
            LocalDateTime dataHora = LocalDateTime.parse(dataStr, formatter);

            System.out.print("Motivo da consulta: ");
            String motivo = scanner.nextLine();

            // Montando os objetos "rasos" apenas com ID para o Service validar
            Paciente p = new Paciente(); p.setId(idPaciente);
            Medico m = new Medico(); m.setId(idMedico);

            Consulta novaConsulta = new Consulta(p, m, dataHora, motivo);

            consultaService.agendar(novaConsulta);
            System.out.println("✅ Consulta agendada com sucesso!");

        } catch (ValidacaoException e) {
            System.err.println("❌ Erro de Negócio: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro ao processar dados: Verifique o formato da data.");
        }
    }

    private void listarMedicos() {
        MedicoDAO medicoDAO = new MedicoDAO();
        List<Medico> medicos = medicoDAO.listarAtivos();

        System.out.println("\n---------------------------------------------------------------------------");
        System.out.printf("| %-5s | %-20s | %-15s | %-20s |\n", "ID", "NOME", "CRM", "ESPECIALIDADE");
        System.out.println("---------------------------------------------------------------------------");

        for (Medico m : medicos) {
            System.out.printf("| %-5d | %-20.20s | %-15s | %-20s |\n",
                    m.getId(), m.getNome(), m.getCrm(), m.getEspecialidade());
        }
        System.out.println("---------------------------------------------------------------------------");
    }

    private void listarPacientes() {
        PacienteDAO pacienteDAO = new PacienteDAO();
        List<Paciente> pacientes = pacienteDAO.listarAtivos();

        System.out.println("\n---------------------------------------------------------------------------");
        System.out.printf("| %-5s | %-20s | %-15s | %-20s |\n", "ID", "NOME", "CPF", "EMAIL");
        System.out.println("---------------------------------------------------------------------------");

        for (Paciente p : pacientes) {
            System.out.printf("| %-5d | %-20.20s | %-15s | %-20.20s |\n",
                    p.getId(), p.getNome(), p.getCpf(), p.getEmail());
        }
        System.out.println("---------------------------------------------------------------------------");
    }

    private void cadastrarMedico() {
        System.out.println("\n--- CADASTRO DE MÉDICO ---");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("CRM: ");
        String crm = scanner.nextLine();

        System.out.println("Selecione a Especialidade:");
        // Listando dinamicamente os valores do Enum
        Especialidade[] especialidades = Especialidade.values();
        for (int i = 0; i < especialidades.length; i++) {
            System.out.println((i + 1) + ". " + especialidades[i]);
        }

        System.out.print("Opção: ");
        int opcaoEsp = Integer.parseInt(scanner.nextLine());
        Especialidade especialidade = especialidades[opcaoEsp - 1];

        Medico novoMedico = new Medico(nome, email, crm, especialidade);
        new MedicoDAO().salvar(novoMedico);

        System.out.println("✅ Médico cadastrado com sucesso!");
    }

    private void cadastrarPaciente() {
        System.out.println("\n--- CADASTRO DE PACIENTE ---");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        Paciente novoPaciente = new Paciente(nome, cpf, email);
        new PacienteDAO().salvar(novoPaciente);

        System.out.println("✅ Paciente cadastrado com sucesso!");
    }

    private void listarConsultas() {
        ConsultaDAO consultaDAO = new ConsultaDAO();
        List<Consulta> consultas = consultaDAO.listarTodas();

        if (consultas.isEmpty()) {
            System.out.println("\nNão existem consultas agendadas.");
            return;
        }

        System.out.println("\n" + "=".repeat(95));
        System.out.printf("| %-3s | %-20s | %-20s | %-18s | %-12s |\n",
                "ID", "PACIENTE", "MÉDICO", "DATA/HORA", "STATUS");
        System.out.println("=".repeat(95));

        for (Consulta c : consultas) {
            String dataFormatada = c.getData().format(formatter);

            System.out.printf("| %-3d | %-20.20s | %-20.20s | %-18s | %-12s |\n",
                    c.getId(),
                    c.getPaciente().getNome(),
                    c.getMedico().getNome(),
                    dataFormatada,
                    c.getStatus());
        }
        System.out.println("=".repeat(95));
    }

}