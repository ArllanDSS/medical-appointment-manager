package br.com.arllan.medsync.repository;

import br.com.arllan.medsync.model.Especialidade;
import br.com.arllan.medsync.model.Medico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO {

    private Medico mapearMedico(ResultSet rs) throws SQLException {
        Medico m = new Medico(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("crm"),
                Especialidade.valueOf(rs.getString("especialidade")),
                rs.getBoolean("ativo")
        );


        return m;
    }

    public void salvar(Medico medico){

        String sql = "INSERT INTO medicos (nome, email, crm, especialidade) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, medico.getNome());
            stmt.setString(2, medico.getEmail());
            stmt.setString(3, medico.getCrm());
            stmt.setString(4, medico.getEspecialidade().name());

            stmt.execute();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar médico: " + medico.getNome(), e);
        }

    }

    public boolean atualizar(Medico medico) {

        String sql = "UPDATE medicos SET nome = ?, email = ?, especialidade = ?, ativo = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, medico.getNome());
            stmt.setString(2, medico.getEmail());
            stmt.setString(3, medico.getEspecialidade().name());
            stmt.setBoolean(4, medico.isAtivo());
            stmt.setInt(5, medico.getId());

            // 3. Execução do comando
            int linhasAfetadas = stmt.executeUpdate();

            // Retorna true se encontrou o médico e atualizou, false caso contrário
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro técnico ao atualizar médico: " + e.getMessage(), e);
        }
    }


    public boolean excluir(int id) {

        String sql = "UPDATE medicos SET ativo = false WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))

        {
            stmt.setInt(1, id);

            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;



        } catch (SQLException e) {

            throw new RuntimeException("Erro ao desativar médico no banco de dados.", e);
        }
    }

    public List<Medico> listarTodosMedicos() {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT * FROM medicos";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()){

            while(rs.next()){

                medicos.add(mapearMedico(rs));

            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar medicos!");
        }

        return medicos;
    }

    public List<Medico> listarAtivos() {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT * FROM medicos WHERE ativo = true"; // Filtro de segurança

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                medicos.add(mapearMedico(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar lista de médicos ativos", e);
        }
        return medicos;
    }

    public Medico buscarPorId(int id) {
        String sql = "SELECT * FROM medicos WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearMedico(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar médico por ID: " + id, e);
        }
        return null; // Retorna null se não encontrar
    }

    public Medico buscarPorCrm(String crm) {
        String sql = "SELECT * FROM medicos WHERE crm = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, crm);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearMedico(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar médico por CRM: " + crm, e);
        }
        return null; // Retorna null se não encontrar

    }




}
