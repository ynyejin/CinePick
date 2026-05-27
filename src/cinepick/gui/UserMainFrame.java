package cinepick.gui;

import cinepick.model.Movie;
import cinepick.model.User;

import javax.swing.*;
import java.awt.*;

public class UserMainFrame extends JFrame {
    private AppContext context;
    private User loginUser;
    private JTextArea outputArea;

    public UserMainFrame(AppContext context, User loginUser) {
        this.context = context;
        this.loginUser = loginUser;

        setTitle("CinePick - 사용자 메뉴");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(loginUser.getName() + "님 환영합니다");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 8, 8));

        JButton movieButton = new JButton("영화 목록");
        JButton reservationButton = new JButton("예매하기");
        JButton myReservationButton = new JButton("예매 내역");
        JButton logoutButton = new JButton("로그아웃");

        buttonPanel.add(movieButton);
        buttonPanel.add(reservationButton);
        buttonPanel.add(myReservationButton);
        buttonPanel.add(logoutButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(outputArea);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);

        movieButton.addActionListener(e -> showMovies());

        reservationButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "다음 단계에서 예매 화면을 연결할 예정입니다.");
        });

        myReservationButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "다음 단계에서 예매 내역 화면을 연결할 예정입니다.");
        });

        logoutButton.addActionListener(e -> {
            context.saveAll();
            new LoginFrame(context).setVisible(true);
            dispose();
        });
    }

    private void showMovies() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== 영화 목록 =====\n\n");

        for (Movie movie : context.movieManager.getMovies()) {
            sb.append(movie).append("\n");
        }

        outputArea.setText(sb.toString());
    }
}