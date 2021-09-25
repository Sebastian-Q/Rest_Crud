package com.example.dataBase;

import com.mysql.cj.jdbc.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class ConnectionMysql {
    public static Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new Driver());
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/ventas?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
    }

    public static void main(String[] args) {
        try{
            Connection con = ConnectionMysql.getConnection();
            System.out.println("Conexion exitosa!");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
