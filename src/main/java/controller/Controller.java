// src/main/java/controller/Controller.java
package controller;

import exception.DAOException;

public interface Controller {
    void start(String username) throws DAOException;
}
