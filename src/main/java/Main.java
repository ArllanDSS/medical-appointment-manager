import br.com.arllan.medsync.model.Paciente;
import br.com.arllan.medsync.repository.PacienteDAO;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        PacienteDAO dao = new PacienteDAO();

        System.out.println("--- INICIANDO TESTE CRUD PACIENTE ---");

        // 1. CRIAR E SALVAR (ID será gerado aqui se usar a melhoria acima)
        Paciente novo = new Paciente("Carlos Alberto", "98765432100", "carlos@email.com");
        dao.salvar(novo);
        System.out.println("1. Paciente salvo. ID gerado: " + novo.getId());

        // 2. BUSCAR E VALIDAR
        Paciente pDoBanco = dao.buscarPorCpf("98765432100");
        if (pDoBanco != null) {
            System.out.println("2. Encontrado: " + pDoBanco.getNome());

            // 3. ATUALIZAR
            pDoBanco.setNome("Carlos Alberto Alterado");
            if (dao.atualizar(pDoBanco)) {
                System.out.println("3. Nome atualizado no banco.");
            }

            // 4. LISTAR E EXCLUIR
            List<Paciente> ativos = dao.listarAtivos();
            System.out.println("4. Total de ativos antes da exclusão: " + ativos.size());

            if (dao.excluir(pDoBanco.getId())) {
                System.out.println("5. Paciente desativado com sucesso.");
            }

            // 6. VERIFICAR SE SUMIU DOS ATIVOS
            List<Paciente> ativosDepois = dao.listarAtivos();
            System.out.println("6. Total de ativos após exclusão: " + ativosDepois.size());

        } else {
            System.out.println("❌ ERRO: Paciente não foi encontrado após o salvamento.");
        }
    }
}