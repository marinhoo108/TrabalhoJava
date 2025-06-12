package com.agenciaviagens;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/agencia_viagens?useTimezone=true&serverTimezone=UTC";
    private static final String USER = "root"; // Altere para o seu usuário do MySQL
    private static final String PASSWORD = "password"; // Altere para a sua senha do MySQL

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
    }
}

