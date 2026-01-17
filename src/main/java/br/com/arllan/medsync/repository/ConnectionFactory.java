package br.com.arllan.medsync.repository;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionFactory {

    public static Connection getConnection() {

        try {

            Properties prop = new Properties();

            InputStream in = ConnectionFactory.class.getClassLoader().getResourceAsStream("config.properties");

            if (in == null) {
                throw new RuntimeException("Arquivo config.properties não encontrado!");
            }
            prop.load(in);

            return DriverManager.getConnection(
                    prop.getProperty("db.url"),
                    prop.getProperty("db.user"),
                    prop.getProperty("db.password")
            );

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar configurações ou conectar ao banco!");
        }

    }

}
