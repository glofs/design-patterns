package com.example.patterns_banking.repositories;

import com.example.patterns_banking.adapters.ConnectionDb;
import com.example.patterns_banking.models.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerRepository {

    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS customers (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(50), " +
                    "email VARCHAR(50) NOT NULL UNIQUE)";

    private static final String INSERT_SQL = "INSERT INTO customers (name, email) VALUES (?, ?)";

    private static CustomerRepository instance;

    private CustomerRepository() {
        try (Connection conn = ConnectionDb.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }


    public static CustomerRepository getInstance() {
        if (instance == null) {
            instance = new CustomerRepository();
        }

        return instance;
    }

    public Customer save(Customer customer) {
        try (Connection conn = ConnectionDb.getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, customer.getName());
                pstmt.setString(2, customer.getEmail());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating account failed, no rows affected.");
                }

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        customer.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating account failed, no ID obtained.");
                    }
                }
            }

            return customer;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving account", e);
        }
    }
}
