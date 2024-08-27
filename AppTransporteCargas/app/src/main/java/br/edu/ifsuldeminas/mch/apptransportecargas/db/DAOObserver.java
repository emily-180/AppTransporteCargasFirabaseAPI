package br.edu.ifsuldeminas.mch.apptransportecargas.db;

import java.util.List;

import br.edu.ifsuldeminas.mch.apptransportecargas.model.User;

public interface DAOObserver {
    void loadOk(List<User> users);
    void loadErro();
    void saveOK();
    void saveErro();
    void updateOK();
    void updateErro();
}
