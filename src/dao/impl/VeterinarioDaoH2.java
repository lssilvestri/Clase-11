package dao.impl;

import dao.IDao;
import db.H2Connection;
import model.Veterinario;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeterinarioDaoH2 implements IDao<Veterinario> {
    public static final Logger logger = Logger.getLogger(VeterinarioDaoH2.class);
    public static final String INSERT = "INSERT INTO VETERINARIOS VALUES (DEFAULT,?,?,?,?)";
    public static final String SELECT_ALL= "SELECT * FROM VETERINARIOS";
    @Override
    public Veterinario guardar(Veterinario veterinario) {
        Connection connection = null;
        Veterinario veterinarioARetornar = null;
        try{
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, veterinario.getNumeroLicencia());
            preparedStatement.setString(2, veterinario.getNombre());
            preparedStatement.setString(3, veterinario.getApellido());
            preparedStatement.setString(4, veterinario.getEspecialidad());
            preparedStatement.executeUpdate();
            connection.commit();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while(resultSet.next()){
                Integer id = resultSet.getInt(1);
                veterinarioARetornar = new Veterinario(id, veterinario.getNumeroLicencia(), veterinario.getNombre(), veterinario.getApellido(), veterinario.getEspecialidad());
            }
            logger.info("Veterinario: "+  veterinarioARetornar);

        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.error(ex.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    logger.error(ex.getMessage());
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return veterinarioARetornar;
    }

    @Override
    public List<Veterinario> buscarTodos() {
        Connection connection = null;
        List<Veterinario> listaVeterinarios = new ArrayList<>();
        Veterinario veterinario = null;
        try{
            connection = H2Connection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL);

            while (resultSet.next()){
                Integer id = resultSet.getInt(1);
                String nroLicencia = resultSet.getString(2);
                String nombre = resultSet.getString(3);
                String apellido = resultSet.getString(4);
                String especialidad = resultSet.getString(5);
                veterinario = new Veterinario(id, nroLicencia, nombre, apellido, especialidad);
                logger.info(veterinario);
                listaVeterinarios.add(veterinario);
            }

        }catch (Exception e){
            logger.error(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                logger.error(ex.getMessage());
            }
        }
        return listaVeterinarios;
    }
}
