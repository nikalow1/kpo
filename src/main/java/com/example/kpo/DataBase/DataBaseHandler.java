package com.example.kpo.DataBase;

import com.example.kpo.DataBase.DataBase;

import java.sql.*;


public class DataBaseHandler extends DataBase {
    static Connection dbConnection;


    public static Connection getDbConnection()
            throws ClassNotFoundException, SQLException{
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;

        Class.forName("com.mysql.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

}