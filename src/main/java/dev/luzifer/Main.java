package dev.luzifer;

import dev.luzifer.spring.Application;
import org.springframework.boot.SpringApplication;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Main {

    private static final JFrame LOG_WINDOW = new JFrame();

    public static void main(String[] args) {

        JFrame window = new JFrame("Webservice");
        JPanel panel = new JPanel(new FlowLayout());
        JButton logButton = new JButton("Log");
        JLabel statusLabel = new JLabel("Status: RUNNING");

        window.setSize(200, 200);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        logButton.addActionListener(action -> LOG_WINDOW.setVisible(!LOG_WINDOW.isVisible()));

        panel.add(statusLabel);
        panel.add(logButton);
        window.add(panel);

        setupLogWindow();

        SpringApplication.run(Application.class);
    }

    private static void setupLogWindow() {

        LOG_WINDOW.setTitle("Log");
        LOG_WINDOW.setSize(1500, 500);
        LOG_WINDOW.setVisible(false);
        LOG_WINDOW.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        LOG_WINDOW.add(panel);

        JTextArea log = new JTextArea(25, 150);
        JScrollPane scrollPane = new JScrollPane(log);
        log.setEditable(false);
        log.setBackground(Color.LIGHT_GRAY);

        panel.add(scrollPane);

        setupConsoleRedirection(log);
    }

    private static void setupConsoleRedirection(JTextArea log) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        System.setOut(ps);

        Thread thread = new Thread(() -> {

            while(true) {
                if(baos.size() > 0) {
                    log.append(baos.toString());
                    baos.reset();
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        });
        thread.setDaemon(true);
        thread.start();
    }

}
