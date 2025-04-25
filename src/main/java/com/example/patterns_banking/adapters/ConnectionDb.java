package com.example.patterns_banking.adapters;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDb {
    private static final String DB_URL = "jdbc:h2:mem:patterns-banking";
    private static final String DB_USER = "sa";
    private static final String DB_PW = "";

    public static java.sql.Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PW);
    }
}
