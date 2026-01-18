package br.com.arllan.medsync.repository;

import br.com.arllan.medsync.model.Consulta;
import br.com.arllan.medsync.model.Medico;
import br.com.arllan.medsync.model.Paciente;
import br.com.arllan.medsync.model.StatusConsulta;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {

    // Auxiliar para transformar a linha do banco num objeto Consulta completo
    private Consulta mapearConsulta(ResultSet rs) throws SQLException {

        Medico medico = new Medico(
                rs.getInt("id_medico"),
                rs.getString("nome_medico"),
                null,
                null,
                null,
                true
        );

        Paciente paciente = new Paciente(
                rs.getInt("id_paciente"),
                rs.getString("nome_paciente"),
                null,
                null,
                true
        );

        LocalDateTime dataHora = rs.getTimestamp("data_hora").toLocalDateTime();

        return new Consulta(
                rs.getInt("id"),
                paciente,
                medico,
                dataHora,
                rs.getString("motivo"),
                StatusConsulta.valueOf(rs.getString("status"))
        );
    }

    public void agendar(Consulta consulta) {
        String sql = "INSERT INTO consultas (id_medico, id_paciente, data_hora, motivo, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // IDs dos objetos Médico e Paciente
            stmt.setInt(1, consulta.getMedico().getId());
            stmt.setInt(2, consulta.getPaciente().getId());

            // LocalDateTime para Timestamp (Ponto Crítico!)
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(consulta.getData()));

            stmt.setString(4, consulta.getMotivo());
            stmt.setString(5, consulta.getStatus().name());

            stmt.executeUpdate();

            // Recuperam o ID gerado para manter o objeto sincronizado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    consulta.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao agendar consulta", e);
        }
    }

    public List<Consulta> listarTodas() {
        List<Consulta> consultas = new ArrayList<>();
        // Selecionamos dados da consulta e os nomes das outras tabelas
        String sql = "SELECT c.*, m.nome as nome_medico, p.nome as nome_paciente " +
                "FROM consultas c " +
                "JOIN medicos m ON c.id_medico = m.id " +
                "JOIN pacientes p ON c.id_paciente = p.id " +
                "ORDER BY c.data_hora";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                consultas.add(mapearConsulta(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar consultas", e);
        }
        return consultas;
    }

    public Consulta buscarPorId(int id) {
        String sql = "SELECT c.*, m.nome as nome_medico, p.nome as nome_paciente " +
                "FROM consultas c " +
                "JOIN medicos m ON c.id_medico = m.id " +
                "JOIN pacientes p ON c.id_paciente = p.id " +
                "WHERE c.id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearConsulta(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar consulta por ID", e);
        }
        return null;
    }

    public boolean cancelar(int id) {
        String sql = "UPDATE consultas SET status = 'CANCELADA' WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cancelar consulta", e);
        }
    }

    public boolean reagendar(int id, LocalDateTime novaData) {
        String sql = "UPDATE consultas SET data_hora = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, java.sql.Timestamp.valueOf(novaData));
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao reagendar consulta", e);
        }
    }

    public boolean existeConsultaNoHorario(int idMedico, LocalDateTime dataHora) {
        // Contamos quantas consultas ativas existem para aquele médico naquele horário
        String sql = "SELECT COUNT(*) FROM consultas WHERE id_medico = ? AND data_hora = ? AND status != 'CANCELADA'";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMedico);
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(dataHora));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt(1);
                    return total > 0; // Se for maior que 0, o horário está ocupado
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar disponibilidade do médico", e);
        }
        return false;
    }

    public boolean existeConsultaPacienteNoDia(int idPaciente, LocalDateTime dataHora) {
        // CAST(data_hora AS DATE) extrai apenas a data, ignorando as horas
        String sql = "SELECT COUNT(*) FROM consultas WHERE id_paciente = ? " +
                "AND CAST(data_hora AS DATE) = CAST(? AS DATE) AND status != 'CANCELADA'";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPaciente);
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(dataHora));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar consultas do paciente no dia", e);
        }
        return false;
    }


}
