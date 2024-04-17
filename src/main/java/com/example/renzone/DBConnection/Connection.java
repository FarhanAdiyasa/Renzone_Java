package com.example.renzone.DBConnection;

import java.sql.*;

public class Connection {
    public java.sql.Connection conn;
    public Statement stat;
    public ResultSet result;
    public PreparedStatement pstat;
    private static java.sql.Connection connection ;
    public Connection(){
        try{
            String url = "jdbc:sqlserver://CLIENTINTERNAL\\MSSQLSERVER02;database=Renzone;user=sa;password=polman;";
            conn = DriverManager.getConnection(url);
            stat = conn.createStatement();
        }catch (Exception e){
            System.out.println("Error saat connect database: "+e);
        }
    }
    public static void main(String[] args){
        Connection connection = new Connection();
        System.out.println("Connection berhasil");
    }
    public static java.sql.Connection getConnect () {
        try {
            String url = "jdbc:sqlserver://CLIENTINTERNAL\\MSSQLSERVER02;database=Renzone;user=sa;password=polman;";
            connection = DriverManager.getConnection(url);
        } catch (Exception e) {
            System.out.println("Error saat connect database: " + e);
        }
        return  connection;
    }

    public void close() throws SQLException {
        conn.close();
    }
}
