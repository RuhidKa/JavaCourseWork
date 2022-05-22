package com.javacoursework.app;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

    static Boolean flag = true;

    public static void main(String args[])
            throws IOException, InterruptedException, SQLException {
        DataBase db = new DataBase();
        db.dropTable();
        db.createNewTable();
        ConsoleUI console = new ConsoleUI();
        Runnable rConsole = console;
        Thread ConsoleThread = new Thread(rConsole);
        ConsoleThread.start();
        while (console.getFlag()) {
            double mem = parseUsage("mem");
            double cpu = parseUsage("cpu");
            double disk = parseUsage("disk");
            db.pushRecords(mem, cpu, disk);
            Thread.sleep(1000);
        }
        ConsoleThread.join();
        db.close();
    }

    public static double parseUsage(String type)
            throws IOException, InterruptedException {
        if (type == "disk") {
            final String command = "df -h --total | grep total | awk '{print $5}'";
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
            pb.redirectError();
            Process proc = pb.start();
            BufferedReader bf = new BufferedReader(
                    new InputStreamReader(proc.getInputStream()));
            String s;
            s = bf.readLine();
            String value = "";
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) != '%')
                    value += s.charAt(i);
            }
            return Double.parseDouble(value);
        } else {
            ProcessBuilder pb = new ProcessBuilder("ps", "-eo", "%" + type);
            pb.redirectError();
            Process proc = pb.start();
            BufferedReader bf = new BufferedReader(
                    new InputStreamReader(proc.getInputStream()));
            String s;
            ArrayList<String> UsageList = new ArrayList<>();
            Double num = .0;
            while ((s = bf.readLine()) != null) {
                if (s.contains("%" + type.toUpperCase())) {
                    continue;
                }
                UsageList.add(s.replaceAll("\\s+", ""));
            }
            for (String str : UsageList) {
                num += Double.parseDouble(str);
            }
            return (num > 100 ? 100 : num);
        }
    }
}
