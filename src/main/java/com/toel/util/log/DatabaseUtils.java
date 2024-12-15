package com.toel.util.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DatabaseUtils {
    static Logger logger = LogManager.getLogger(DatabaseUtils.class);

    // private static Connection conn;

    public static Connection getConnection() throws SQLException,
            ClassNotFoundException {
        // String connectionUrl = "jdbc:mysql://103.72.98.197:3306/ebookLibrary";
        // String USER = "khauser"; 
        // String PASSWORD = "12345678";
        String connectionUrl = "jdbc:mysql://localhost:3306/ebookLibrary";
        String USER = "root"; 
        String PASSWORD = "system";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(connectionUrl, USER, PASSWORD);
        System.out.println("Connection thành công: "+ conn);

        return conn;
    }

    public static void closeObject(Statement obj) {
        try {
            if (obj != null) {
                obj.close();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public static void closeObject(ResultSet obj) {
        try {
            if (obj != null) {
                obj.close();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public static void closeObject(Connection obj) {
        try {
            if (obj != null && !obj.isClosed()) {
                if (!obj.getAutoCommit()) {
                    obj.rollback();
                }
                obj.close();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
