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

    private void styleFormLabel(JLabel label) {
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(TEXT_COLOR);
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 225)),
                BorderFactory.createEmptyBorder(7, 9, 7, 9)
        ));
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
        JDialog dialog = new JDialog(this, "영화 등록", true);
        dialog.setSize(500, 650);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JLabel titleLabel = new JLabel("영화 등록");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_COLOR);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        JLabel titleInputLabel = new JLabel("영화 제목");
        JTextField titleField = new JTextField();

        JLabel genreLabel = new JLabel("장르");
        JTextField genreField = new JTextField();

        JLabel runningTimeLabel = new JLabel("상영 시간(분)");
        JTextField runningTimeField = new JTextField();

        JLabel ratingLabel = new JLabel("평점");
        JTextField ratingField = new JTextField();

        JLabel directorLabel = new JLabel("감독");
        JTextField directorField = new JTextField();

        JLabel actorsLabel = new JLabel("출연 배우");
        JTextField actorsField = new JTextField();

        JLabel descriptionLabel = new JLabel("영화 소개");
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setRows(4);
        descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(8, 9, 8, 9));

        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionScrollPane.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 225)));

        styleFormLabel(titleInputLabel);
        styleFormLabel(genreLabel);
        styleFormLabel(runningTimeLabel);
        styleFormLabel(ratingLabel);
        styleFormLabel(directorLabel);
        styleFormLabel(actorsLabel);
        styleFormLabel(descriptionLabel);

        styleTextField(titleField);
        styleTextField(genreField);
        styleTextField(runningTimeField);
        styleTextField(ratingField);
        styleTextField(directorField);
        styleTextField(actorsField);

        gbc.gridy = 0;
        formPanel.add(titleInputLabel, gbc);
        gbc.gridy = 1;
        formPanel.add(titleField, gbc);

        gbc.gridy = 2;
        formPanel.add(genreLabel, gbc);
        gbc.gridy = 3;
        formPanel.add(genreField, gbc);

        gbc.gridy = 4;
        formPanel.add(runningTimeLabel, gbc);
        gbc.gridy = 5;
        formPanel.add(runningTimeField, gbc);

        gbc.gridy = 6;
        formPanel.add(ratingLabel, gbc);
        gbc.gridy = 7;
        formPanel.add(ratingField, gbc);

        gbc.gridy = 8;
        formPanel.add(directorLabel, gbc);
        gbc.gridy = 9;
        formPanel.add(directorField, gbc);

        gbc.gridy = 10;
        formPanel.add(actorsLabel, gbc);
        gbc.gridy = 11;
        formPanel.add(actorsField, gbc);

        gbc.gridy = 12;
        formPanel.add(descriptionLabel, gbc);
        gbc.gridy = 13;
        formPanel.add(descriptionScrollPane, gbc);

        JButton saveButton = createPrimaryButton("등록");
        JButton cancelButton = createDangerButton("취소");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(panel);

        saveButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String genre = genreField.getText().trim();
            String runningTimeText = runningTimeField.getText().trim();
            String ratingText = ratingField.getText().trim();
            String director = directorField.getText().trim();
            String actors = actorsField.getText().trim();
            String description = descriptionArea.getText().trim();

            if (title.isEmpty() || genre.isEmpty() || runningTimeText.isEmpty() ||
                    ratingText.isEmpty() || director.isEmpty() || actors.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "모든 항목을 입력해주세요.");
                return;
            }

            try {
                int runningTime = Integer.parseInt(runningTimeText);
                double rating = Double.parseDouble(ratingText);

                if (runningTime <= 0) {
                    JOptionPane.showMessageDialog(dialog, "상영 시간은 1분 이상이어야 합니다.");
                    return;
                }

                if (rating < 0 || rating > 5) {
                    JOptionPane.showMessageDialog(dialog, "평점은 0점 이상 5점 이하로 입력해주세요.");
                    return;
                }

                context.movieManager.addMovie(
                        title,
                        genre,
                        runningTime,
                        rating,
                        director,
                        actors,
                        description
                );

                context.saveAll();

                JOptionPane.showMessageDialog(dialog, "영화가 등록되었습니다.");
                dialog.dispose();
                showMovieManagementPanel();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "상영 시간과 평점은 숫자로 입력해주세요.");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
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

        JDialog dialog = new JDialog(this, "영화 정보 수정", true);
        dialog.setSize(500, 650);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JLabel titleLabel = new JLabel("영화 정보 수정");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_COLOR);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        JLabel movieIdLabel = new JLabel("영화 번호: " + movie.getMovieId());
        movieIdLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        movieIdLabel.setForeground(TEXT_COLOR);

        JLabel titleInputLabel = new JLabel("영화 제목");
        JTextField titleField = new JTextField(movie.getTitle());

        JLabel genreLabel = new JLabel("장르");
        JTextField genreField = new JTextField(movie.getGenre());

        JLabel runningTimeLabel = new JLabel("상영 시간(분)");
        JTextField runningTimeField = new JTextField(String.valueOf(movie.getRunningTime()));

        JLabel ratingLabel = new JLabel("평점");
        JTextField ratingField = new JTextField(String.valueOf(movie.getRating()));

        JLabel directorLabel = new JLabel("감독");
        JTextField directorField = new JTextField(movie.getDirector());

        JLabel actorsLabel = new JLabel("출연 배우");
        JTextField actorsField = new JTextField(movie.getActors());

        JLabel descriptionLabel = new JLabel("영화 소개");
        JTextArea descriptionArea = new JTextArea(movie.getDescription());
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setRows(4);

        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionScrollPane.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 225)));

        styleFormLabel(titleInputLabel);
        styleFormLabel(genreLabel);
        styleFormLabel(runningTimeLabel);
        styleFormLabel(ratingLabel);
        styleFormLabel(directorLabel);
        styleFormLabel(actorsLabel);
        styleFormLabel(descriptionLabel);

        styleTextField(titleField);
        styleTextField(genreField);
        styleTextField(runningTimeField);
        styleTextField(ratingField);
        styleTextField(directorField);
        styleTextField(actorsField);

        gbc.gridy = 0;
        formPanel.add(movieIdLabel, gbc);

        gbc.gridy = 1;
        formPanel.add(titleInputLabel, gbc);
        gbc.gridy = 2;
        formPanel.add(titleField, gbc);

        gbc.gridy = 3;
        formPanel.add(genreLabel, gbc);
        gbc.gridy = 4;
        formPanel.add(genreField, gbc);

        gbc.gridy = 5;
        formPanel.add(runningTimeLabel, gbc);
        gbc.gridy = 6;
        formPanel.add(runningTimeField, gbc);

        gbc.gridy = 7;
        formPanel.add(ratingLabel, gbc);
        gbc.gridy = 8;
        formPanel.add(ratingField, gbc);

        gbc.gridy = 9;
        formPanel.add(directorLabel, gbc);
        gbc.gridy = 10;
        formPanel.add(directorField, gbc);

        gbc.gridy = 11;
        formPanel.add(actorsLabel, gbc);
        gbc.gridy = 12;
        formPanel.add(actorsField, gbc);

        gbc.gridy = 13;
        formPanel.add(descriptionLabel, gbc);
        gbc.gridy = 14;
        formPanel.add(descriptionScrollPane, gbc);

        JButton saveButton = createPrimaryButton("저장");
        JButton cancelButton = createDangerButton("취소");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(panel);

        saveButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String genre = genreField.getText().trim();
            String runningTimeText = runningTimeField.getText().trim();
            String ratingText = ratingField.getText().trim();
            String director = directorField.getText().trim();
            String actors = actorsField.getText().trim();
            String description = descriptionArea.getText().trim();

            if (title.isEmpty() || genre.isEmpty() || runningTimeText.isEmpty() ||
                    ratingText.isEmpty() || director.isEmpty() || actors.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "모든 항목을 입력해주세요.");
                return;
            }

            try {
                int runningTime = Integer.parseInt(runningTimeText);
                double rating = Double.parseDouble(ratingText);

                if (runningTime <= 0) {
                    JOptionPane.showMessageDialog(dialog, "상영 시간은 1분 이상이어야 합니다.");
                    return;
                }

                if (rating < 0 || rating > 5) {
                    JOptionPane.showMessageDialog(dialog, "평점은 0점 이상 5점 이하로 입력해주세요.");
                    return;
                }

                boolean result = context.movieManager.updateMovie(
                        movieId,
                        title,
                        genre,
                        runningTime,
                        rating,
                        director,
                        actors,
                        description
                );

                if (result) {
                    context.saveAll();
                    JOptionPane.showMessageDialog(dialog, "영화 정보가 수정되었습니다.");
                    dialog.dispose();
                    showMovieManagementPanel();
                } else {
                    JOptionPane.showMessageDialog(dialog, "영화 수정에 실패했습니다.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "상영 시간과 평점은 숫자로 입력해주세요.");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
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

        String[] columns = {"아이디", "이름", "직원 여부", "멤버십 등급", "포인트", "할인율", "권한"};

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
                    user.isEmployee() ? "직원" : "일반",
                    user.getMembershipGrade(),
                    user.getPoint(),
                    user.getDiscountRate() + "%",
                    user.getRole()
            });
        }

        JTable table = createStyledTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton updatePointButton = createPrimaryButton("포인트 수정");
        JButton updateEmployeeButton = createPrimaryButton("직원 여부 변경");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(updatePointButton);
        buttonPanel.add(updateEmployeeButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.add(pageTitle, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        setContent(panel);

        updatePointButton.addActionListener(e -> updateSelectedUserPoint(table));
        updateEmployeeButton.addActionListener(e -> updateSelectedUserEmployee(table));
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
        String role = (String) table.getValueAt(selectedRow, 6);

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

    private void updateSelectedUserEmployee(JTable table) {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "직원 여부를 변경할 회원을 선택해주세요.");
            return;
        }

        String userId = (String) table.getValueAt(selectedRow, 0);
        String currentEmployeeStatus = (String) table.getValueAt(selectedRow, 2);
        String role = (String) table.getValueAt(selectedRow, 6);

        if (role.equalsIgnoreCase("ADMIN")) {
            JOptionPane.showMessageDialog(this, "관리자 계정은 직원 여부를 변경할 수 없습니다.");
            return;
        }

        boolean currentEmployee = currentEmployeeStatus.equals("직원");
        boolean newEmployee = !currentEmployee;

        String message = newEmployee
                ? userId + " 회원을 직원으로 설정하시겠습니까?"
                : userId + " 회원의 직원 설정을 해제하시겠습니까?";

        int confirm = JOptionPane.showConfirmDialog(
                this,
                message,
                "직원 여부 변경",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        boolean result = context.userManager.updateEmployeeStatus(userId, newEmployee);

        if (result) {
            context.saveAll();
            JOptionPane.showMessageDialog(this, "직원 여부가 변경되었습니다.");
            showUserTable();
        } else {
            JOptionPane.showMessageDialog(this, "직원 여부 변경에 실패했습니다.");
        }
    }
}