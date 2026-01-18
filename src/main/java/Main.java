import br.com.arllan.medsync.model.Especialidade;
import br.com.arllan.medsync.model.Medico;
import br.com.arllan.medsync.repository.ConnectionFactory;
import br.com.arllan.medsync.repository.ConsultaDAO;
import br.com.arllan.medsync.repository.MedicoDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- TESTE DE CONEXÃO MEDSYNC ---");

        // Usamos o try-with-resources para garantir que a conexão feche após o teste
        try (Connection conn = ConnectionFactory.getConnection()) {

            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ SUCESSO: Conexão com o banco 'med_db' estabelecida!");

                // Opcional: Mostrar qual banco estamos usando
                System.out.println("Catálogo atual: " + conn.getCatalog());
            }


            MedicoDAO medicoDAO = new MedicoDAO();

            Medico m = medicoDAO.buscarPorCrm("9999");
            medicoDAO.excluir(m.getId());

            medicoDAO.listarTodosMedicos().forEach(System.out::println);

        } catch (SQLException e) {
            System.err.println("❌ ERRO: Não foi possível conectar ao banco de dados.");
            System.err.println("Motivo: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ ERRO INESPERADO: Verifique se o arquivo .properties está correto.");
            e.printStackTrace();
        }
    }
}