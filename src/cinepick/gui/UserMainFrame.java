package cinepick.gui;

import cinepick.model.Movie;
import cinepick.model.Reservation;
import cinepick.model.Screening;
import cinepick.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class UserMainFrame extends JFrame {
    private AppContext context;
    private User loginUser;

    private JTextArea outputArea;
    private JLabel welcomeLabel;

    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color SIDEBAR_COLOR = new Color(35, 45, 75);
    private static final Color PRIMARY_COLOR = new Color(70, 90, 160);
    private static final Color TEXT_COLOR = new Color(40, 45, 60);

    public UserMainFrame(AppContext context, User loginUser) {
        this.context = context;
        this.loginUser = loginUser;

        setTitle("CinePick - 사용자 메뉴");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(BACKGROUND_COLOR);

        // ===== 상단 헤더 =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        JLabel titleLabel = new JLabel("CinePick");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_COLOR);

        welcomeLabel = new JLabel(loginUser.getName() + "님 환영합니다  |  보유 포인트: " + loginUser.getPoint());
        welcomeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        welcomeLabel.setForeground(new Color(90, 90, 90));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(welcomeLabel, BorderLayout.EAST);

        // ===== 왼쪽 메뉴 =====
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(SIDEBAR_COLOR);
        sidePanel.setBorder(BorderFactory.createEmptyBorder(24, 16, 24, 16));
        sidePanel.setPreferredSize(new Dimension(160, 0));

        JButton movieButton = createSideButton("영화 목록");
        JButton screeningButton = createSideButton("상영 정보");
        JButton reservationButton = createSideButton("예매하기");
        JButton myReservationButton = createSideButton("예매 내역");
        JButton logoutButton = createSideButton("로그아웃");

        sidePanel.add(movieButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(screeningButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(reservationButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(myReservationButton);
        sidePanel.add(Box.createVerticalGlue());
        sidePanel.add(logoutButton);

        // ===== 중앙 출력 영역 =====
        JPanel contentPanel = new JPanel(new BorderLayout(0, 12));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel contentTitle = new JLabel("정보 보기");
        contentTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        contentTitle.setForeground(TEXT_COLOR);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        outputArea.setForeground(TEXT_COLOR);
        outputArea.setBackground(Color.WHITE);
        outputArea.setLineWrap(false);
        outputArea.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 235)));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        contentPanel.add(contentTitle, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        rootPanel.add(headerPanel, BorderLayout.NORTH);
        rootPanel.add(sidePanel, BorderLayout.WEST);
        rootPanel.add(contentPanel, BorderLayout.CENTER);

        add(rootPanel);

        movieButton.addActionListener(e -> showMovies());
        screeningButton.addActionListener(e -> showScreenings());
        reservationButton.addActionListener(e -> makeReservation());
        myReservationButton.addActionListener(e -> showMyReservations());

        logoutButton.addActionListener(e -> {
            context.saveAll();
            new LoginFrame(context).setVisible(true);
            dispose();
        });

        showMovies();
    }

    private JButton createSideButton(String text) {
        JButton button = new JButton(text);

        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setMaximumSize(new Dimension(128, 42));
        button.setPreferredSize(new Dimension(128, 42));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        return button;
    }

    private void showMovies() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== 영화 목록 =====\n\n");

        sb.append(String.format("%-6s %-20s %-12s %-10s %-8s\n",
                "번호", "제목", "장르", "상영시간", "평점"));
        sb.append("--------------------------------------------------------------\n");

        for (Movie movie : context.movieManager.getMovies()) {
            sb.append(String.format("%-6d %-20s %-12s %-10s %-8.1f\n",
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getGenre(),
                    movie.getRunningTime() + "분",
                    movie.getRating()));
        }

        outputArea.setText(sb.toString());
    }

    private void showScreenings() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== 전체 상영 정보 =====\n\n");

        sb.append(String.format("%-8s %-20s %-10s %-20s %-10s %-8s\n",
                "상영번호", "영화", "상영관", "상영시간", "좌석수", "혼잡률"));
        sb.append("--------------------------------------------------------------------------------\n");

        for (Screening screening : context.screeningManager.getScreenings()) {
            Movie movie = context.movieManager.findMovieById(screening.getMovieId());

            sb.append(String.format("%-8d %-20s %-10s %-20s %-10d %-8s\n",
                    screening.getScreeningId(),
                    movie != null ? movie.getTitle() : "알 수 없음",
                    screening.getTheater(),
                    screening.getScreeningTime(),
                    screening.getTotalSeats(),
                    screening.getCongestionRate() + "%"));
        }

        outputArea.setText(sb.toString());
    }

    private void makeReservation() {
        String movieIdText = JOptionPane.showInputDialog(this, "예매할 영화 번호를 입력하세요.");
        if (movieIdText == null || movieIdText.trim().isEmpty()) return;

        try {
            int movieId = Integer.parseInt(movieIdText.trim());
            Movie movie = context.movieManager.findMovieById(movieId);

            if (movie == null) {
                JOptionPane.showMessageDialog(this, "존재하지 않는 영화입니다.");
                return;
            }

            ArrayList<Screening> screenings = context.screeningManager.getScreeningsByMovieId(movieId);

            if (screenings.isEmpty()) {
                JOptionPane.showMessageDialog(this, "해당 영화의 상영 정보가 없습니다.");
                return;
            }

            StringBuilder screeningInfo = new StringBuilder();
            screeningInfo.append("상영 정보를 선택하세요.\n\n");

            for (Screening screening : screenings) {
                screeningInfo.append("상영번호: ").append(screening.getScreeningId())
                        .append(" | 상영관: ").append(screening.getTheater())
                        .append(" | 시간: ").append(screening.getScreeningTime())
                        .append(" | 혼잡률: ").append(screening.getCongestionRate()).append("%")
                        .append("\n");
            }

            String screeningIdText = JOptionPane.showInputDialog(this, screeningInfo.toString());
            if (screeningIdText == null || screeningIdText.trim().isEmpty()) return;

            int screeningId = Integer.parseInt(screeningIdText.trim());
            Screening screening = context.screeningManager.findScreeningById(screeningId);

            if (screening == null || screening.getMovieId() != movieId) {
                JOptionPane.showMessageDialog(this, "올바르지 않은 상영 정보입니다.");
                return;
            }

            new SeatSelectionFrame(context, loginUser, movie, screening, this).setVisible(true);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "번호는 숫자로 입력해주세요.");
        }
    }

    public void refreshUserInfo() {
        welcomeLabel.setText(loginUser.getName() + "님 환영합니다  |  보유 포인트: " + loginUser.getPoint());
        showMyReservations();
    }

    private void showMyReservations() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== 내 예매 내역 =====\n\n");

        ArrayList<Reservation> reservations = context.reservationManager.getReservationsByUserId(loginUser.getUserId());

        if (reservations.isEmpty()) {
            sb.append("예매 내역이 없습니다.\n");
            outputArea.setText(sb.toString());
            return;
        }

        for (Reservation reservation : reservations) {
            Screening screening = context.screeningManager.findScreeningById(reservation.getScreeningId());
            Movie movie = null;

            if (screening != null) {
                movie = context.movieManager.findMovieById(screening.getMovieId());
            }

            sb.append("예매번호: ").append(reservation.getReservationId()).append("\n");
            sb.append("영화: ").append(movie != null ? movie.getTitle() : "알 수 없음").append("\n");
            sb.append("상영관: ").append(screening != null ? screening.getTheater() : "알 수 없음").append("\n");
            sb.append("상영 시간: ").append(screening != null ? screening.getScreeningTime() : "알 수 없음").append("\n");
            sb.append("좌석: ").append(context.reservationManager.getSeatNumbersByReservationId(reservation.getReservationId())).append("\n");
            sb.append("상태: ").append(reservation.getStatus()).append("\n");
            sb.append("예매 일시: ").append(reservation.getReservedAt()).append("\n");
            sb.append("--------------------------------\n");
        }

        outputArea.setText(sb.toString());
    }
}