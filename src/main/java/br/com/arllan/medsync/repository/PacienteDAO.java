package br.com.arllan.medsync.repository;

import br.com.arllan.medsync.model.Paciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    private Paciente mapearPaciente(ResultSet rs) throws SQLException {

        Paciente p = new Paciente(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("cpf"),
                rs.getString("email"),
                rs.getBoolean("ativo")
                );
        return p;

    }

    public Paciente buscarPorCpf(String cpf){

        String sql = "SELECT * FROM pacientes WHERE cpf = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);){

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return mapearPaciente(rs);
                }
            }

        } catch (SQLException e){
            throw new RuntimeException("Erro ao consultar Pacientes por cpf: " + cpf, e);
        }

        return null;
    }

    public Paciente buscarPorId(int id){

        String sql = "SELECT * FROM pacientes WHERE id = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);){

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return mapearPaciente(rs);
                }
            }

        } catch (SQLException e){
            throw new RuntimeException("Erro ao consultar Pacientes por id: " + id, e);
        }

        return null;
    }

    public void salvar(Paciente paciente) {
        // 1. Informamos ao JDBC que queremos o ID de volta
        String sql = "INSERT INTO pacientes (nome, cpf, email) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getCpf());
            stmt.setString(3, paciente.getEmail());
            stmt.executeUpdate();

            // 2. Recuperamos o ID gerado pelo banco e colocamos no objeto
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    paciente.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar Paciente", e);
        }
    }

    public boolean atualizar(Paciente paciente){

        String sql = "UPDATE pacientes SET nome = ?, cpf = ?, email = ? WHERE id = ? ";

        try(Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);){

            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getCpf());
            stmt.setString(3, paciente.getEmail());
            stmt.setInt(4, paciente.getId());

            int  linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException e){
            throw new RuntimeException("Erro ao atualizar Paciente: " + paciente, e);
        }


    }

    public boolean excluir(int id){
        String sql = "UPDATE pacientes SET ativo = false WHERE id = ?";

        try(Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);){

            stmt.setInt(1, id);

            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException e){
            throw new RuntimeException("Erro ao excluir Paciente: ", e);
        }


    }

    public List<Paciente> listarTodos(){

        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM pacientes";

        try(Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();){

            while(rs.next()){
                pacientes.add(mapearPaciente(rs));
            }

        } catch (SQLException e){
            throw new RuntimeException("Erro ao listar Pacientes", e);
        }

        return pacientes;

    }

    public List<Paciente> listarAtivos() {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM pacientes WHERE ativo = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pacientes.add(mapearPaciente(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar Pacientes ativos!", e);
        }
        return pacientes;
    }



}
