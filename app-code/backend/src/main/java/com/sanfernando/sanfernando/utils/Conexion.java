package com.sanfernando.sanfernando.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
public class Conexion {

    private Connection con;

    @Value("${DB_HOST:localhost}")
    private String dbHost;

    @Value("${DB_PORT:5432}")
    private String dbPort;

    @Value("${DB_DATABASE:san-fernando-db}")
    private String dbName;

    @Value("${DB_USERNAME:postgres}")
    private String username;

    @Value("${DB_PASSWORD:123456}")
    private String password;

    public void startConexion() {
        try {
            String url = String.format("jdbc:postgresql://%s:%s/%s", dbHost, dbPort, dbName);
            Connection con = DriverManager.getConnection(url, username, password);
            this.con = con;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConexion() {
        try {
            if (this.con != null && !this.con.isClosed()) {
                this.con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
