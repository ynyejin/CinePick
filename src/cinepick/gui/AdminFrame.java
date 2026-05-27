package cinepick.gui;

import cinepick.model.Movie;
import cinepick.model.User;

import javax.swing.*;
import java.awt.*;

public class AdminFrame extends JFrame {
    private AppContext context;
    private User adminUser;
    private JTextArea outputArea;

    public AdminFrame(AppContext context, User adminUser) {
        this.context = context;
        this.adminUser = adminUser;

        setTitle("CinePick - 관리자 메뉴");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("관리자 페이지");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 8, 8));

        JButton movieButton = createMenuButton("영화 목록");
        JButton addMovieButton = createMenuButton("영화 등록");
        JButton userButton = createMenuButton("회원 목록");
        JButton logoutButton = createMenuButton("로그아웃");

        buttonPanel.add(movieButton);
        buttonPanel.add(addMovieButton);
        buttonPanel.add(userButton);
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
        addMovieButton.addActionListener(e -> addMovie());
        userButton.addActionListener(e -> showUsers());

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

    private void showUsers() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== 회원 목록 =====\n\n");

        for (User user : context.userManager.getUsers()) {
            sb.append(user).append("\n");
        }

        outputArea.setText(sb.toString());
    }

    private void addMovie() {
        String title = JOptionPane.showInputDialog(this, "영화 제목:");
        if (title == null || title.trim().isEmpty()) return;

        String genre = JOptionPane.showInputDialog(this, "장르:");
        if (genre == null || genre.trim().isEmpty()) return;

        String runningTimeText = JOptionPane.showInputDialog(this, "상영 시간(분):");
        String ratingText = JOptionPane.showInputDialog(this, "평점:");

        try {
            int runningTime = Integer.parseInt(runningTimeText);
            double rating = Double.parseDouble(ratingText);

            context.movieManager.addMovie(title, genre, runningTime, rating);
            context.saveAll();

            JOptionPane.showMessageDialog(this, "영화가 등록되었습니다.");
            showMovies();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "상영 시간과 평점은 숫자로 입력해주세요.");
        }
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(45, 70, 140));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 42));
        return button;
    }
}