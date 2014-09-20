/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teste.jdbc;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Klaus Boeing
 */
public class ContatoDao implements Closeable {

    private Connection con;

    public ContatoDao() {
        this.con = new ConnectionFactory().getConnection();
    }

    @Override
    public void close() {
        try {
            this.con.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void deleta(Contato contato) throws SQLException {
        String sql = "delete from agenda.contatos "
                + " where id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, contato.getId());

            // executa
            stmt.execute();
        }
    }

    public void edita(Contato contato) throws SQLException {
        String sql = "update agenda.contatos "
                + " set nome = ?, email = ?, endereco = ?, dataNascimento=? where id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getEmail());
            stmt.setString(3, contato.getEndereco());
            stmt.setDate(4, new java.sql.Date(contato.getDataNascimento().getTimeInMillis()));
            stmt.setLong(5, contato.getId());

            // executa
            stmt.execute();
        }
    }

    public void adiciona(Contato contato) throws SQLException {
        String sql = "insert into agenda.contatos "
                + "(id,nome,email,endereco,dataNascimento)"
                + " values (?,?,?,?,?)";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            Long id = getProximoId();
            stmt.setLong(1, id);
            stmt.setString(2, contato.getNome());
            stmt.setString(3, contato.getEmail());
            stmt.setString(4, contato.getEndereco());
            stmt.setDate(5, new java.sql.Date(contato.getDataNascimento().getTimeInMillis()));

            // executa
            stmt.execute();
        }
    }

    public Long getProximoId() throws SQLException {
        String sql = "select max(id) from agenda.contatos ";
        try (PreparedStatement st = con.prepareStatement(sql);
                ResultSet set = st.executeQuery()) {
            if (set.next()) {
                return set.getLong(1) + 1;
            }
        }
        return 1L;
    }
}