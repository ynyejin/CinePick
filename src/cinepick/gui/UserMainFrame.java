package cinepick.gui;

import cinepick.model.Movie;
import cinepick.model.Reservation;
import cinepick.model.Screening;
import cinepick.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;

public class UserMainFrame extends JFrame {
    private AppContext context;
    private User loginUser;

    private JPanel contentPanel;
    private JLabel welcomeLabel;

    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color SIDEBAR_COLOR = new Color(35, 45, 75);
    private static final Color PRIMARY_COLOR = new Color(70, 90, 160);
    private static final Color TEXT_COLOR = new Color(40, 45, 60);
    private static final Color CARD_COLOR = Color.WHITE;

    public UserMainFrame(AppContext context, User loginUser) {
        this.context = context;
        this.loginUser = loginUser;

        setTitle("CinePick - 사용자 메뉴");
        setSize(950, 650);
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

        JButton movieButton = createSideButton("영화 예매");
        JButton screeningButton = createSideButton("상영 정보");
        JButton myReservationButton = createSideButton("예매 내역");
        JButton logoutButton = createSideButton("로그아웃");

        sidePanel.add(movieButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(screeningButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(myReservationButton);
        sidePanel.add(Box.createVerticalGlue());
        sidePanel.add(logoutButton);

        // ===== 오른쪽 내용 영역 =====
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        rootPanel.add(headerPanel, BorderLayout.NORTH);
        rootPanel.add(sidePanel, BorderLayout.WEST);
        rootPanel.add(contentPanel, BorderLayout.CENTER);

        add(rootPanel);

        movieButton.addActionListener(e -> showMovieCards());
        screeningButton.addActionListener(e -> showScreeningTable());
        myReservationButton.addActionListener(e -> showReservationTable());

        logoutButton.addActionListener(e -> {
            context.saveAll();
            new LoginFrame(context).setVisible(true);
            dispose();
        });

        showMovieCards();
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

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    private void setContent(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // =========================
    // 영화 카드 화면
    // =========================
    private void showMovieCards() {
        JPanel wrapperPanel = new JPanel(new BorderLayout(0, 16));
        wrapperPanel.setBackground(BACKGROUND_COLOR);

        JLabel pageTitle = new JLabel("영화 예매");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        pageTitle.setForeground(TEXT_COLOR);

        JPanel cardListPanel = new JPanel(new GridLayout(0, 3, 16, 16));
        cardListPanel.setBackground(BACKGROUND_COLOR);

        for (Movie movie : context.movieManager.getMovies()) {
            cardListPanel.add(createMovieCard(movie));
        }

        JScrollPane scrollPane = new JScrollPane(cardListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        wrapperPanel.add(pageTitle, BorderLayout.NORTH);
        wrapperPanel.add(scrollPane, BorderLayout.CENTER);

        setContent(wrapperPanel);
    }

    private JPanel createMovieCard(Movie movie) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 235)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        JLabel posterLabel = new JLabel("MOVIE", SwingConstants.CENTER);
        posterLabel.setOpaque(true);
        posterLabel.setBackground(new Color(230, 233, 242));
        posterLabel.setForeground(new Color(80, 85, 100));
        posterLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        posterLabel.setPreferredSize(new Dimension(160, 120));

        JLabel titleLabel = new JLabel(movie.getTitle());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
        titleLabel.setForeground(TEXT_COLOR);

        JLabel infoLabel = new JLabel(
                "<html>장르: " + movie.getGenre() +
                        "<br>상영시간: " + movie.getRunningTime() + "분" +
                        "<br>평점: " + movie.getRating() +
                        "</html>"
        );
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        infoLabel.setForeground(new Color(90, 90, 90));

        JButton reserveButton = createPrimaryButton("예매하기");
        reserveButton.addActionListener(e -> chooseScreeningForMovie(movie));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 6));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(infoLabel);

        card.add(posterLabel, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);
        card.add(reserveButton, BorderLayout.SOUTH);

        return card;
    }

    private void chooseScreeningForMovie(Movie movie) {
        ArrayList<Screening> screenings = context.screeningManager.getScreeningsByMovieId(movie.getMovieId());

        if (screenings.isEmpty()) {
            JOptionPane.showMessageDialog(this, "해당 영화의 상영 정보가 없습니다.");
            return;
        }

        String[] options = new String[screenings.size()];

        for (int i = 0; i < screenings.size(); i++) {
            Screening screening = screenings.get(i);
            options[i] = screening.getScreeningId()
                    + "번 | " + screening.getTheater()
                    + " | " + screening.getScreeningTime()
                    + " | 혼잡률 " + screening.getCongestionRate() + "%";
        }

        String selected = (String) JOptionPane.showInputDialog(
                this,
                "상영 정보를 선택하세요.",
                movie.getTitle() + " 상영 정보",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (selected == null) {
            return;
        }

        int selectedIndex = -1;

        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(selected)) {
                selectedIndex = i;
                break;
            }
        }

        if (selectedIndex == -1) {
            return;
        }

        Screening selectedScreening = screenings.get(selectedIndex);
        new SeatSelectionFrame(context, loginUser, movie, selectedScreening, this).setVisible(true);
    }

    // =========================
    // 상영 정보 테이블 화면
    // =========================
    private void showScreeningTable() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel pageTitle = new JLabel("상영 정보");
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

        panel.add(pageTitle, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        setContent(panel);
    }

    // =========================
    // 예매 내역 테이블 + 취소
    // =========================
    private void showReservationTable() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel pageTitle = new JLabel("내 예매 내역");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        pageTitle.setForeground(TEXT_COLOR);

        String[] columns = {"예매번호", "영화", "상영관", "상영시간", "좌석", "상태", "예매일시"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ArrayList<Reservation> reservations = context.reservationManager.getReservationsByUserId(loginUser.getUserId());

        for (Reservation reservation : reservations) {
            Screening screening = context.screeningManager.findScreeningById(reservation.getScreeningId());
            Movie movie = null;

            if (screening != null) {
                movie = context.movieManager.findMovieById(screening.getMovieId());
            }

            model.addRow(new Object[]{
                    reservation.getReservationId(),
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

        JButton cancelButton = createPrimaryButton("선택한 예매 취소");
        cancelButton.setPreferredSize(new Dimension(160, 40));

        cancelButton.addActionListener(e -> cancelSelectedReservation(table));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.add(cancelButton);

        panel.add(pageTitle, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        setContent(panel);
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

    private void cancelSelectedReservation(JTable table) {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "취소할 예매를 선택해주세요.");
            return;
        }

        int reservationId = (int) table.getValueAt(selectedRow, 0);
        String status = (String) table.getValueAt(selectedRow, 5);

        if (status.equals("CANCELED")) {
            JOptionPane.showMessageDialog(this, "이미 취소된 예매입니다.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "예매번호 " + reservationId + "번 예매를 취소하시겠습니까?",
                "예매 취소 확인",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Reservation reservation = context.reservationManager.findReservationById(reservationId);

        if (reservation == null) {
            JOptionPane.showMessageDialog(this, "존재하지 않는 예매입니다.");
            return;
        }

        if (!reservation.getUserId().equals(loginUser.getUserId())) {
            JOptionPane.showMessageDialog(this, "본인의 예매만 취소할 수 있습니다.");
            return;
        }

        boolean result = context.reservationManager.cancelReservation(reservationId);

        if (result) {
            context.saveAll();
            JOptionPane.showMessageDialog(this, "예매가 취소되었습니다.");
            showReservationTable();
        } else {
            JOptionPane.showMessageDialog(this, "예매 취소에 실패했습니다.");
        }
    }

    public void refreshUserInfo() {
        welcomeLabel.setText(loginUser.getName() + "님 환영합니다  |  보유 포인트: " + loginUser.getPoint());
        showReservationTable();
    }
}