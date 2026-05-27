package cinepick.gui;

import cinepick.model.Movie;
import cinepick.model.Screening;
import cinepick.model.User;

import javax.swing.*;
import java.awt.*;

public class AdminFrame extends JFrame {
    private AppContext context;
    private User adminUser;

    private JTextArea outputArea;

    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color SIDEBAR_COLOR = new Color(35, 45, 75);
    private static final Color PRIMARY_COLOR = new Color(70, 90, 160);
    private static final Color TEXT_COLOR = new Color(40, 45, 60);

    public AdminFrame(AppContext context, User adminUser) {
        this.context = context;
        this.adminUser = adminUser;

        setTitle("CinePick - 관리자 메뉴");
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

        JLabel titleLabel = new JLabel("CinePick Admin");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_COLOR);

        JLabel adminLabel = new JLabel(adminUser.getName() + " 관리자님");
        adminLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        adminLabel.setForeground(new Color(90, 90, 90));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(adminLabel, BorderLayout.EAST);

        // ===== 왼쪽 사이드바 =====
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(SIDEBAR_COLOR);
        sidePanel.setBorder(BorderFactory.createEmptyBorder(24, 16, 24, 16));
        sidePanel.setPreferredSize(new Dimension(170, 0));

        JButton movieListButton = createSideButton("영화 목록");
        JButton addMovieButton = createSideButton("영화 등록");
        JButton updateMovieButton = createSideButton("영화 수정");
        JButton deleteMovieButton = createSideButton("영화 삭제");
        JButton screeningListButton = createSideButton("상영 정보");
        JButton addScreeningButton = createSideButton("상영 등록");
        JButton userListButton = createSideButton("회원 목록");
        JButton reservationListButton = createSideButton("예매 내역");
        JButton logoutButton = createSideButton("로그아웃");

        sidePanel.add(movieListButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(addMovieButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(updateMovieButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(deleteMovieButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(screeningListButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(addScreeningButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(userListButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(reservationListButton);
        sidePanel.add(Box.createVerticalGlue());
        sidePanel.add(logoutButton);

        // ===== 중앙 출력 영역 =====
        JPanel contentPanel = new JPanel(new BorderLayout(0, 12));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel contentTitle = new JLabel("관리자 정보 보기");
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

        movieListButton.addActionListener(e -> showMovies());
        addMovieButton.addActionListener(e -> addMovie());
        updateMovieButton.addActionListener(e -> updateMovie());
        deleteMovieButton.addActionListener(e -> deleteMovie());
        screeningListButton.addActionListener(e -> showScreenings());
        addScreeningButton.addActionListener(e -> addScreening());
        userListButton.addActionListener(e -> showUsers());
        reservationListButton.addActionListener(e -> showReservations());

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
        button.setMaximumSize(new Dimension(138, 40));
        button.setPreferredSize(new Dimension(138, 40));
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

    private void addMovie() {
        String title = JOptionPane.showInputDialog(this, "영화 제목:");
        if (title == null || title.trim().isEmpty()) return;

        String genre = JOptionPane.showInputDialog(this, "장르:");
        if (genre == null || genre.trim().isEmpty()) return;

        String runningTimeText = JOptionPane.showInputDialog(this, "상영 시간(분):");
        if (runningTimeText == null || runningTimeText.trim().isEmpty()) return;

        String ratingText = JOptionPane.showInputDialog(this, "평점:");
        if (ratingText == null || ratingText.trim().isEmpty()) return;

        try {
            int runningTime = Integer.parseInt(runningTimeText.trim());
            double rating = Double.parseDouble(ratingText.trim());

            context.movieManager.addMovie(title.trim(), genre.trim(), runningTime, rating);
            context.saveAll();

            JOptionPane.showMessageDialog(this, "영화가 등록되었습니다.");
            showMovies();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "상영 시간과 평점은 숫자로 입력해주세요.");
        }
    }

    private void updateMovie() {
        showMovies();

        String movieIdText = JOptionPane.showInputDialog(this, "수정할 영화 번호:");
        if (movieIdText == null || movieIdText.trim().isEmpty()) return;

        try {
            int movieId = Integer.parseInt(movieIdText.trim());
            Movie movie = context.movieManager.findMovieById(movieId);

            if (movie == null) {
                JOptionPane.showMessageDialog(this, "존재하지 않는 영화입니다.");
                return;
            }

            String title = JOptionPane.showInputDialog(this, "새 영화 제목:", movie.getTitle());
            if (title == null || title.trim().isEmpty()) return;

            String genre = JOptionPane.showInputDialog(this, "새 장르:", movie.getGenre());
            if (genre == null || genre.trim().isEmpty()) return;

            String runningTimeText = JOptionPane.showInputDialog(this, "새 상영 시간(분):", movie.getRunningTime());
            if (runningTimeText == null || runningTimeText.trim().isEmpty()) return;

            String ratingText = JOptionPane.showInputDialog(this, "새 평점:", movie.getRating());
            if (ratingText == null || ratingText.trim().isEmpty()) return;

            int runningTime = Integer.parseInt(runningTimeText.trim());
            double rating = Double.parseDouble(ratingText.trim());

            boolean result = context.movieManager.updateMovie(
                    movieId,
                    title.trim(),
                    genre.trim(),
                    runningTime,
                    rating
            );

            if (result) {
                context.saveAll();
                JOptionPane.showMessageDialog(this, "영화 정보가 수정되었습니다.");
                showMovies();
            } else {
                JOptionPane.showMessageDialog(this, "영화 수정에 실패했습니다.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "번호, 상영 시간, 평점은 숫자로 입력해주세요.");
        }
    }

    private void deleteMovie() {
        showMovies();

        String movieIdText = JOptionPane.showInputDialog(this, "삭제할 영화 번호:");
        if (movieIdText == null || movieIdText.trim().isEmpty()) return;

        try {
            int movieId = Integer.parseInt(movieIdText.trim());
            Movie movie = context.movieManager.findMovieById(movieId);

            if (movie == null) {
                JOptionPane.showMessageDialog(this, "존재하지 않는 영화입니다.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    movie.getTitle() + " 영화를 삭제하시겠습니까?",
                    "영화 삭제 확인",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            boolean result = context.movieManager.deleteMovie(movieId);

            if (result) {
                context.saveAll();
                JOptionPane.showMessageDialog(this, "영화가 삭제되었습니다.");
                showMovies();
            } else {
                JOptionPane.showMessageDialog(this, "영화 삭제에 실패했습니다.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "영화 번호는 숫자로 입력해주세요.");
        }
    }

    private void showScreenings() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== 상영 정보 목록 =====\n\n");

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

    private void addScreening() {
        showMovies();

        String movieIdText = JOptionPane.showInputDialog(this, "상영 정보를 등록할 영화 번호:");
        if (movieIdText == null || movieIdText.trim().isEmpty()) return;

        try {
            int movieId = Integer.parseInt(movieIdText.trim());
            Movie movie = context.movieManager.findMovieById(movieId);

            if (movie == null) {
                JOptionPane.showMessageDialog(this, "존재하지 않는 영화입니다.");
                return;
            }

            String theater = JOptionPane.showInputDialog(this, "상영관:");
            if (theater == null || theater.trim().isEmpty()) return;

            String screeningTime = JOptionPane.showInputDialog(this, "상영 시간 예: 2026-06-01 14:00");
            if (screeningTime == null || screeningTime.trim().isEmpty()) return;

            String totalSeatsText = JOptionPane.showInputDialog(this, "전체 좌석 수:");
            if (totalSeatsText == null || totalSeatsText.trim().isEmpty()) return;

            int totalSeats = Integer.parseInt(totalSeatsText.trim());

            Screening screening = context.screeningManager.addScreening(
                    movieId,
                    theater.trim(),
                    screeningTime.trim(),
                    totalSeats
            );

            context.seatManager.createSeatsForScreening(screening.getScreeningId(), totalSeats);
            context.saveAll();

            JOptionPane.showMessageDialog(this, "상영 정보가 등록되었습니다.");
            showScreenings();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "영화 번호와 좌석 수는 숫자로 입력해주세요.");
        }
    }

    private void showUsers() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== 회원 목록 =====\n\n");

        sb.append(String.format("%-16s %-12s %-8s %-10s\n",
                "아이디", "이름", "포인트", "권한"));
        sb.append("------------------------------------------------\n");

        for (User user : context.userManager.getUsers()) {
            sb.append(String.format("%-16s %-12s %-8d %-10s\n",
                    user.getUserId(),
                    user.getName(),
                    user.getPoint(),
                    user.getRole()));
        }

        outputArea.setText(sb.toString());
    }

    private void showReservations() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== 전체 예매 내역 =====\n\n");

        if (context.reservationManager.getReservations().isEmpty()) {
            sb.append("예매 내역이 없습니다.\n");
            outputArea.setText(sb.toString());
            return;
        }

        for (cinepick.model.Reservation reservation : context.reservationManager.getReservations()) {
            Screening screening = context.screeningManager.findScreeningById(reservation.getScreeningId());
            Movie movie = null;

            if (screening != null) {
                movie = context.movieManager.findMovieById(screening.getMovieId());
            }

            sb.append("예매번호: ").append(reservation.getReservationId()).append("\n");
            sb.append("회원ID: ").append(reservation.getUserId()).append("\n");
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