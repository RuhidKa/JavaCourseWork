package com.javacoursework.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {

  private static final String URL = "jdbc:postgresql://localhost:5432/database";
  private static final String USERNAME = "postgres";
  private static final String PASSWORD = "postgre";

  private static Connection conn = null;

  static {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    try {
      conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
      System.out.println("Connection to SQLite has been established.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void pushRecords(Double CPUUsage, Double MEMUsage, double DISKUsage) {
    String sql = "INSERT INTO usage(cpu,mem,disk) VALUES(?,?,?)";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setDouble(1, CPUUsage);
      pstmt.setDouble(2, MEMUsage);
      pstmt.setDouble(3, DISKUsage);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void dropTable() {
    try (Statement stmt = conn.createStatement()) {
      String sql = "DROP TABLE IF EXISTS usage";
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void createNewTable() {
    String sql = "CREATE TABLE IF NOT EXISTS usage (\n" +
        "	id SERIAL PRIMARY KEY,\n" +
        "	cpu real,\n" +
        "	mem real,\n" +
        "	disk real\n" +
        ");";

    try (Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void close() throws SQLException {
    conn.close();
  }
}
