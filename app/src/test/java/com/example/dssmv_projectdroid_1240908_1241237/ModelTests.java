package com.example.dssmv_projectdroid_1240908_1241237;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.dssmv_projectdroid_1240908_1241237.data.model.Aula;
import com.example.dssmv_projectdroid_1240908_1241237.data.model.Desporto;
import com.example.dssmv_projectdroid_1240908_1241237.data.model.Inscricao;
import com.example.dssmv_projectdroid_1240908_1241237.data.model.Utilizador;

public class ModelTests {

    @Test
    public void testCriacaoUtilizadorAtleta() {
        Utilizador u = new Utilizador("João", "joao@isep.pt", "1234", false);
        assertEquals("João", u.getNome());
        assertFalse(u.isProfessor());
        assertTrue(u.isActive());
    }

    @Test
    public void testCriacaoDesporto() {
        Desporto d = new Desporto("1", "Yoga", "Aula relaxante", 15, "url_imagem");
        assertEquals("Yoga", d.getNome());
        assertEquals(15, d.getLotacaoMaxima());
        assertEquals("Aula relaxante", d.getDescricao());
    }

    @Test
    public void testModificacaoAula() {
        Aula aula = new Aula();
        aula.setLotacao(20);
        aula.setModalidade("CrossFit");
        aula.setData("16/11/2026");

        assertEquals(20, aula.getLotacao());
        assertEquals("CrossFit", aula.getModalidade());
        assertEquals("16/11/2026", aula.getData());
    }

    @Test
    public void testInscricaoDados() {
        Inscricao insc = new Inscricao("aula123", "Ricky", "16/11/2026", "Pilates");
        assertEquals("Ricky", insc.getUsername_aluno());
        assertEquals("aula123", insc.getId_aula());
        assertEquals("Pilates", insc.getModalidade());
    }
}