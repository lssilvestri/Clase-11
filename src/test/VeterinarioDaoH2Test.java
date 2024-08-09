package test;

import dao.impl.VeterinarioDaoH2;
import model.Veterinario;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.VeterinarioService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VeterinarioDaoH2Test {
    static Logger logger = Logger.getLogger(VeterinarioDaoH2Test.class);
    VeterinarioService veterinarioService = new VeterinarioService(new VeterinarioDaoH2());
    @BeforeAll
    static void cargarTablas(){
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:./vet;INIT=RUNSCRIPT FROM 'create.sql'", "sa", "sa");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Test guardar")
    void caso1(){
        //dado
        Veterinario veterinario = new Veterinario("ajshdjh","Luciana", "perez", "cirujano");
        // cuando
        Veterinario veterinario1 = veterinarioService.guardarVeterinario(veterinario);
        // entonces
        assertNotNull(veterinario1);
        assertNotNull(veterinario1.getId());
    }

    @Test
    @DisplayName("Testear buscar todos")
    void caso2(){
        // cuando
        List<Veterinario> veterinarios = new ArrayList<>();
        veterinarios = veterinarioService.buscarTodos();
        assertTrue(veterinarios.size()!=0);
    }

}