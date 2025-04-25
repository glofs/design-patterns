package com.example.patterns_banking.repositories;

import com.example.patterns_banking.adapters.ConnectionDb;
import com.example.patterns_banking.models.Account;
import com.example.patterns_banking.models.AccountType;

import java.sql.*;

public class AccountRepository {

    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS accounts (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "number VARCHAR(50), " +
                    "type VARCHAR(50), " +
                    "balance NUMERIC, "+
                    "isActive BIT)";

    private static final String INSERT_SQL = "INSERT INTO accounts (number, type,isActive,balance) VALUES (?,?,?,?)";
    private static AccountRepository instance;

    // contructor privado que sera usado desde getInstance y para evitar crear mas instancias y cumplir con el patron singleto
    private AccountRepository() {
        try (Connection connection = ConnectionDb.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static AccountRepository getInstance() {
        if (instance == null) {
            instance = new AccountRepository();
        }
        return instance;
    }

    public Account save(Account account) {
        try (Connection conn = ConnectionDb.getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, account.getNumber());
                pstmt.setString(2,AccountType.SAVING.name());
                pstmt.setBigDecimal(3, account.getBalance());
                pstmt.setBoolean(4, account.getIsActive());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating account failed, no rows affected.");
                }

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        account.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating account failed, no ID obtained.");
                    }
                }
            }

            return account;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving account", e);
        }
    }


}
