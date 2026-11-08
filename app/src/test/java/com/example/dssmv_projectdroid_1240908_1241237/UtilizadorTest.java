package com.example.dssmv_projectdroid_1240908_1241237;

import org.junit.Test;
import static org.junit.Assert.*;
import com.example.dssmv_projectdroid_1240908_1241237.data.model.Utilizador;

public class UtilizadorTest {
    @Test
    public void testCriacaoUtilizadorAtleta() {
        Utilizador u = new Utilizador("João", "joao@isep.pt", "1234", false);
        assertEquals("João", u.getNome());
        assertFalse(u.isProfessor());
        assertTrue(u.isActive());
    }
}