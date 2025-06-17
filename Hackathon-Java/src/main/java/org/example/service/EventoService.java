package org.example.service;

import org.example.dao.EventoDAO;
import org.example.model.Evento;

public class EventoService {
    private EventoDAO eventoDAO = new EventoDAO();

    public void create(Evento evento) {
        eventoDAO.create(evento);
    }
}
