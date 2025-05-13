package com.smartclinic.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for establishing a connection to the database.
 * Provides a method to get a connection to the MySQL database.
 */
public class DBConnection {

    // Database connection URL
    private static final String URL = "jdbc:mysql://localhost:3306/smart_clinic";

    // Database user credentials (ensure these are kept secure and not hardcoded in production)
    private static final String USER = "root";  // MySQL username
    private static final String PASS = "tiger"; // MySQL password

    /**
     * Establishes a connection to the MySQL database.
     * @return Connection The established database connection.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Attempt to establish a connection to the database
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            // Throw an exception if the connection fails
            System.out.println("[✗] Error establishing database connection: " + e.getMessage());
            throw e; // Propagate the exception after logging the error
        }
    }
}
