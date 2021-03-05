package com.ebbrechtair.util;

import com.ebbrechtair.secrets.SqlSecrets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnector {
    public static Connection getSQLConnection() throws SQLException {
        SqlSecrets sqlLoginData = new SqlSecrets();
        Connection connect = DriverManager.getConnection(sqlLoginData.getUrl(),sqlLoginData.getUserName(),sqlLoginData.getPassword());
        return connect;
    }
}
