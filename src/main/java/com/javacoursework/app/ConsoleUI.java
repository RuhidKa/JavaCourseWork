package com.javacoursework.app;

import java.util.Scanner;

public class ConsoleUI implements Runnable {

  public Boolean flag = true;

  public Boolean getFlag() {
    return flag;
  }

  public static void clearScreen() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  public void run() {
    Scanner sc = new Scanner(System.in);
    while (flag) {
      String read = sc.nextLine();
      if (read.equals("exit")) {
        flag = false;
      }
    }
    sc.close();
  }
}