package cinepick.gui;

import cinepick.model.Movie;
import cinepick.model.Reservation;
import cinepick.model.Screening;
import cinepick.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class AdminFrame extends JFrame {
    private AppContext context;
    private User adminUser;

    private JPanel contentPanel;

    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color SIDEBAR_COLOR = new Color(35, 45, 75);
    private static final Color PRIMARY_COLOR = new Color(70, 90, 160);
    private static final Color DANGER_COLOR = new Color(190, 70, 70);
    private static final Color TEXT_COLOR = new Color(40, 45, 60);

    public AdminFrame(AppContext context, User adminUser) {
        this.context = context;
        this.adminUser = adminUser;

        setTitle("CinePick - 관리자 메뉴");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(BACKGROUND_COLOR);

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

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(SIDEBAR_COLOR);
        sidePanel.setBorder(BorderFactory.createEmptyBorder(24, 16, 24, 16));
        sidePanel.setPreferredSize(new Dimension(170, 0));

        JButton movieButton = createSideButton("영화 관리");
        JButton screeningButton = createSideButton("상영 관리");
        JButton userButton = createSideButton("회원 목록");
        JButton reservationButton = createSideButton("예매 내역");
        JButton logoutButton = createSideButton("로그아웃");

        sidePanel.add(movieButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(screeningButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(userButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(reservationButton);
        sidePanel.add(Box.createVerticalGlue());
        sidePanel.add(logoutButton);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        rootPanel.add(headerPanel, BorderLayout.NORTH);
        rootPanel.add(sidePanel, BorderLayout.WEST);
        rootPanel.add(contentPanel, BorderLayout.CENTER);

        add(rootPanel);

        movieButton.addActionListener(e -> showMovieManagementPanel());
        screeningButton.addActionListener(e -> showScreeningManagementPanel());
        userButton.addActionListener(e -> showUserTable());
        reservationButton.addActionListener(e -> showReservationTable());

        logoutButton.addActionListener(e -> {
            context.saveAll();
            new LoginFrame(context).setVisible(true);
            dispose();
        });

        showMovieManagementPanel();
    }

    private JButton createSideButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setMaximumSize(new Dimension(138, 42));
        button.setPreferredSize(new Dimension(138, 42));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 38));
        return button;
    }

    private JButton createDangerButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBackground(DANGER_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 38));
        return button;
    }

    private void setContent(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(230, 230, 230));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(new Color(230, 233, 242));
        header.setForeground(TEXT_COLOR);

        return table;
    }

    // =========================
    // 영화 관리
    // =========================
    private void showMovieManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel pageTitle = new JLabel("영화 관리");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        pageTitle.setForeground(TEXT_COLOR);

        String[] columns = {"영화번호", "제목", "장르", "상영시간", "평점"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Movie movie : context.movieManager.getMovies()) {
            model.addRow(new Object[]{
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getGenre(),
                    movie.getRunningTime(),
                    movie.getRating()
            });
        }

        JTable table = createStyledTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton addButton = createPrimaryButton("영화 등록");
        JButton updateButton = createPrimaryButton("영화 수정");
        JButton deleteButton = createDangerButton("영화 삭제");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.add(pageTitle, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        setContent(panel);

        addButton.addActionListener(e -> addMovie());
        updateButton.addActionListener(e -> updateSelectedMovie(table));
        deleteButton.addActionListener(e -> deleteSelectedMovie(table));
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

        String director = JOptionPane.showInputDialog(this, "감독:");
        if (director == null || director.trim().isEmpty()) return;

        String actors = JOptionPane.showInputDialog(this, "출연 배우:");
        if (actors == null || actors.trim().isEmpty()) return;

        String description = JOptionPane.showInputDialog(this, "영화 소개:");
        if (description == null || description.trim().isEmpty()) return;

        try {
            int runningTime = Integer.parseInt(runningTimeText.trim());
            double rating = Double.parseDouble(ratingText.trim());

            context.movieManager.addMovie(
                    title.trim(),
                    genre.trim(),
                    runningTime,
                    rating,
                    director.trim(),
                    actors.trim(),
                    description.trim()
            );
            context.saveAll();

            JOptionPane.showMessageDialog(this, "영화가 등록되었습니다.");
            showMovieManagementPanel();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "상영 시간과 평점은 숫자로 입력해주세요.");
        }
    }

    private void updateSelectedMovie(JTable table) {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "수정할 영화를 선택해주세요.");
            return;
        }

        int movieId = (int) table.getValueAt(selectedRow, 0);
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

        String director = JOptionPane.showInputDialog(this, "새 감독:", movie.getDirector());
        if (director == null || director.trim().isEmpty()) return;

        String actors = JOptionPane.showInputDialog(this, "새 출연 배우:", movie.getActors());
        if (actors == null || actors.trim().isEmpty()) return;

        String description = JOptionPane.showInputDialog(this, "새 영화 소개:", movie.getDescription());
        if (description == null || description.trim().isEmpty()) return;

        try {
            int runningTime = Integer.parseInt(runningTimeText.trim());
            double rating = Double.parseDouble(ratingText.trim());

            context.movieManager.updateMovie(
                    movieId,
                    title.trim(),
                    genre.trim(),
                    runningTime,
                    rating,
                    director.trim(),
                    actors.trim(),
                    description.trim()
            );
            context.saveAll();

            JOptionPane.showMessageDialog(this, "영화 정보가 수정되었습니다.");
            showMovieManagementPanel();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "상영 시간과 평점은 숫자로 입력해주세요.");
        }
    }

    private void deleteSelectedMovie(JTable table) {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "삭제할 영화를 선택해주세요.");
            return;
        }

        int movieId = (int) table.getValueAt(selectedRow, 0);
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

        if (confirm != JOptionPane.YES_OPTION) return;

        context.movieManager.deleteMovie(movieId);
        context.saveAll();

        JOptionPane.showMessageDialog(this, "영화가 삭제되었습니다.");
        showMovieManagementPanel();
    }

    // =========================
    // 상영 관리
    // =========================
    private void showScreeningManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel pageTitle = new JLabel("상영 관리");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        pageTitle.setForeground(TEXT_COLOR);

        String[] columns = {"상영번호", "영화", "상영관", "상영시간", "좌석수", "혼잡률"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Screening screening : context.screeningManager.getScreenings()) {
            Movie movie = context.movieManager.findMovieById(screening.getMovieId());

            model.addRow(new Object[]{
                    screening.getScreeningId(),
                    movie != null ? movie.getTitle() : "알 수 없음",
                    screening.getTheater(),
                    screening.getScreeningTime(),
                    screening.getTotalSeats(),
                    screening.getCongestionRate() + "%"
            });
        }

        JTable table = createStyledTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton addButton = createPrimaryButton("상영 등록");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(addButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.add(pageTitle, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        setContent(panel);

        addButton.addActionListener(e -> addScreening());
    }

    private void addScreening() {
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
            showScreeningManagementPanel();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "영화 번호와 좌석 수는 숫자로 입력해주세요.");
        }
    }

    // =========================
    // 회원 목록
    // =========================
    private void showUserTable() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel pageTitle = new JLabel("회원 관리");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        pageTitle.setForeground(TEXT_COLOR);

        String[] columns = {"아이디", "이름", "멤버십 등급", "포인트", "할인율", "권한"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (User user : context.userManager.getUsers()) {
            model.addRow(new Object[]{
                    user.getUserId(),
                    user.getName(),
                    user.getMembershipGrade(),
                    user.getPoint(),
                    user.getDiscountRate() + "%",
                    user.getRole()
            });
        }

        JTable table = createStyledTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton updatePointButton = createPrimaryButton("포인트 수정");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(updatePointButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.add(pageTitle, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        setContent(panel);

        updatePointButton.addActionListener(e -> updateSelectedUserPoint(table));
    }

    // =========================
    // 전체 예매 내역
    // =========================
    private void showReservationTable() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel pageTitle = new JLabel("전체 예매 내역");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        pageTitle.setForeground(TEXT_COLOR);

        String[] columns = {"예매번호", "회원ID", "영화", "상영관", "상영시간", "좌석", "상태", "예매일시"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Reservation reservation : context.reservationManager.getReservations()) {
            Screening screening = context.screeningManager.findScreeningById(reservation.getScreeningId());
            Movie movie = null;

            if (screening != null) {
                movie = context.movieManager.findMovieById(screening.getMovieId());
            }

            model.addRow(new Object[]{
                    reservation.getReservationId(),
                    reservation.getUserId(),
                    movie != null ? movie.getTitle() : "알 수 없음",
                    screening != null ? screening.getTheater() : "알 수 없음",
                    screening != null ? screening.getScreeningTime() : "알 수 없음",
                    context.reservationManager.getSeatNumbersByReservationId(reservation.getReservationId()),
                    reservation.getStatus(),
                    reservation.getReservedAt()
            });
        }

        JTable table = createStyledTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(pageTitle, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        setContent(panel);
    }

    private void updateSelectedUserPoint(JTable table) {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "포인트를 수정할 회원을 선택해주세요.");
            return;
        }

        String userId = (String) table.getValueAt(selectedRow, 0);
        String role = (String) table.getValueAt(selectedRow, 5);

        if (role.equalsIgnoreCase("ADMIN")) {
            JOptionPane.showMessageDialog(this, "관리자 계정의 포인트는 수정할 수 없습니다.");
            return;
        }

        String pointText = JOptionPane.showInputDialog(this, "새 포인트를 입력하세요:");

        if (pointText == null || pointText.trim().isEmpty()) {
            return;
        }

        try {
            int point = Integer.parseInt(pointText.trim());

            if (point < 0) {
                JOptionPane.showMessageDialog(this, "포인트는 0 이상이어야 합니다.");
                return;
            }

            boolean result = context.userManager.updateUserPoint(userId, point);

            if (result) {
                context.saveAll();
                JOptionPane.showMessageDialog(this, "회원 포인트가 수정되었습니다.");
                showUserTable();
            } else {
                JOptionPane.showMessageDialog(this, "회원 포인트 수정에 실패했습니다.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "포인트는 숫자로 입력해주세요.");
        }
    }
}