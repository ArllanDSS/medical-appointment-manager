import br.com.arllan.medsync.exception.ValidacaoException;
import br.com.arllan.medsync.model.*;
import br.com.arllan.medsync.repository.*;
import br.com.arllan.medsync.service.ConsultaService;

import java.time.LocalDateTime;
import java.util.List;

public class TesteGeralSistema {
    public static void main(String[] args) {
        // 1. Instanciando os repositórios
        MedicoDAO medicoDAO = new MedicoDAO();
        PacienteDAO pacienteDAO = new PacienteDAO();
        ConsultaDAO consultaDAO = new ConsultaDAO();
        ConsultaService service = new ConsultaService();

        System.out.println("=== INICIANDO TESTE COMPLETO DO MEDSYNC ===\n");

        // STEP 1: Persistindo as entidades base
        Medico medico = new Medico("Dr. Arllan", "arllan@medsync.com", "CRM-123", Especialidade.CARDIOLOGIA);
        medicoDAO.salvar(medico);
        medico = medicoDAO.buscarPorCrm("CRM-123"); // Recupera com ID gerado

        Paciente paciente = new Paciente("João da Silva", "12345678901", "joao@email.com");
        pacienteDAO.salvar(paciente);
        paciente = pacienteDAO.buscarPorCpf("12345678901"); // Recupera com ID gerado

        System.out.println("✅ Médico e Paciente salvos com sucesso.");

        LocalDateTime dataInicial = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0).withSecond(0).withNano(0);
        Consulta consulta = new Consulta(paciente, medico, dataInicial, "Consulta de rotina");

        try {
            service.agendar(consulta);
        } catch (ValidacaoException e) {
            // Aqui capturamos a nossa exceção personalizada!
            System.err.println("Falha na Regra de Negócio: " + e.getMessage());
        }

        System.out.println("✅ Consulta agendada para: " + dataInicial);

        // STEP 3: Testando o Reagendamento
        System.out.println("\n--- TESTANDO REAGENDAMENTO ---");
        LocalDateTime novaData = dataInicial.plusDays(3).withHour(15).withMinute(30);

        boolean reagendou = consultaDAO.reagendar(consulta.getId(), novaData);

        if (reagendou) {
            // Buscamos novamente do banco para validar a persistência da nova data
            Consulta cAtualizada = consultaDAO.buscarPorId(consulta.getId());
            System.out.println("✅ Reagendamento confirmado no banco.");
            System.out.println("Data Antiga: " + dataInicial);
            System.out.println("Data Atualizada: " + cAtualizada.getData());
        } else {
            System.out.println("❌ Falha ao reagendar consulta.");
        }

        // STEP 4: Listagem Geral com JOIN (Verificando Hidratação de Objetos)
        System.out.println("\n--- RELATÓRIO FINAL DE CONSULTAS ---");
        List<Consulta> lista = consultaDAO.listarTodas();

        for (Consulta c : lista) {
            System.out.println("ID: " + c.getId());
            System.out.println("Paciente: " + c.getPaciente().getNome());
            System.out.println("Médico: " + c.getMedico().getNome());
            System.out.println("Data: " + c.getData());
            System.out.println("Status: " + c.getStatus());
            System.out.println("-----------------------------------");
        }

        // STEP 5: Testando Cancelamento
        System.out.println("\n--- TESTANDO CANCELAMENTO ---");
        if (consultaDAO.cancelar(consulta.getId())) {
            Consulta cCancelada = consultaDAO.buscarPorId(consulta.getId());
            System.out.println("✅ Status atualizado para: " + cCancelada.getStatus());
        }
    }
}