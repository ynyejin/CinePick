package cinepick.gui;

import cinepick.model.Movie;
import cinepick.model.Reservation;
import cinepick.model.Screening;
import cinepick.model.Seat;
import cinepick.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SeatSelectionFrame extends JFrame {
    private AppContext context;
    private User loginUser;
    private Movie movie;
    private Screening screening;
    private UserMainFrame parentFrame;

    private ArrayList<Integer> selectedSeatIds;
    private JLabel selectedSeatLabel;

    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color AVAILABLE_COLOR = new Color(235, 238, 245);
    private static final Color SELECTED_COLOR = new Color(70, 130, 180);
    private static final Color RESERVED_COLOR = new Color(170, 170, 170);
    private static final Color PRIMARY_COLOR = new Color(45, 70, 140);
    private static final int TICKET_PRICE = 12000;

    public SeatSelectionFrame(AppContext context, User loginUser, Movie movie, Screening screening, UserMainFrame parentFrame) {
        this.context = context;
        this.loginUser = loginUser;
        this.movie = movie;
        this.screening = screening;
        this.parentFrame = parentFrame;
        this.selectedSeatIds = new ArrayList<>();

        setTitle("CinePick - 좌석 선택");
        setSize(560, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel titleLabel = new JLabel(movie.getTitle() + " 좌석 선택", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(new Color(40, 45, 60));

        JLabel infoLabel = new JLabel(
                screening.getTheater() + " | " + screening.getScreeningTime(),
                SwingConstants.CENTER
        );
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        infoLabel.setForeground(new Color(90, 90, 90));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);
        headerPanel.add(titleLabel);
        headerPanel.add(infoLabel);

        JLabel screenLabel = new JLabel("SCREEN", SwingConstants.CENTER);
        screenLabel.setOpaque(true);
        screenLabel.setBackground(new Color(60, 60, 70));
        screenLabel.setForeground(Color.WHITE);
        screenLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        screenLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        JPanel seatPanel = new JPanel(new GridLayout(0, 5, 8, 8));
        seatPanel.setOpaque(false);
        seatPanel.setBorder(BorderFactory.createEmptyBorder(18, 30, 18, 30));

        ArrayList<Seat> seats = context.seatManager.getSeatsByScreeningId(screening.getScreeningId());

        for (Seat seat : seats) {
            JButton seatButton = new JButton(seat.getSeatNumber());
            seatButton.setFont(new Font("SansSerif", Font.BOLD, 13));
            seatButton.setFocusPainted(false);
            seatButton.setOpaque(true);
            seatButton.setBorderPainted(false);

            if (seat.isReserved()) {
                seatButton.setText("XX");
                seatButton.setEnabled(false);
                seatButton.setBackground(RESERVED_COLOR);
                seatButton.setForeground(Color.WHITE);
            } else {
                seatButton.setBackground(AVAILABLE_COLOR);
                seatButton.setForeground(new Color(40, 45, 60));

                seatButton.addActionListener(e -> toggleSeatSelection(seat, seatButton));
            }

            seatPanel.add(seatButton);
        }

        selectedSeatLabel = new JLabel("선택 좌석: 없음", SwingConstants.CENTER);
        selectedSeatLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton reserveButton = new JButton("예매하기");
        reserveButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        reserveButton.setBackground(PRIMARY_COLOR);
        reserveButton.setForeground(Color.WHITE);
        reserveButton.setFocusPainted(false);
        reserveButton.setOpaque(true);
        reserveButton.setBorderPainted(false);

        JButton closeButton = new JButton("닫기");
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        closeButton.setBackground(new Color(120, 120, 120));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setOpaque(true);
        closeButton.setBorderPainted(false);

        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        bottomPanel.setOpaque(false);
        bottomPanel.add(selectedSeatLabel);
        bottomPanel.add(reserveButton);
        bottomPanel.add(closeButton);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(screenLabel, BorderLayout.NORTH);
        centerPanel.add(seatPanel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        reserveButton.addActionListener(e -> reserve());
        closeButton.addActionListener(e -> dispose());
    }

    private void toggleSeatSelection(Seat seat, JButton seatButton) {
        int seatId = seat.getSeatId();

        if (selectedSeatIds.contains(seatId)) {
            selectedSeatIds.remove(Integer.valueOf(seatId));
            seatButton.setBackground(AVAILABLE_COLOR);
            seatButton.setForeground(new Color(40, 45, 60));
        } else {
            selectedSeatIds.add(seatId);
            seatButton.setBackground(SELECTED_COLOR);
            seatButton.setForeground(Color.WHITE);
        }

        updateSelectedSeatLabel();
    }

    private void updateSelectedSeatLabel() {
        if (selectedSeatIds.isEmpty()) {
            selectedSeatLabel.setText("선택 좌석: 없음");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("선택 좌석: ");

        for (int seatId : selectedSeatIds) {
            Seat seat = context.seatManager.findSeatById(seatId);
            if (seat != null) {
                sb.append(seat.getSeatNumber()).append(" ");
            }
        }

        selectedSeatLabel.setText(sb.toString());
    }

    private void reserve() {
        if (selectedSeatIds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "좌석을 선택해주세요.");
            return;
        }

        Reservation reservation = context.reservationManager.makeReservation(
                loginUser.getUserId(),
                screening.getScreeningId(),
                selectedSeatIds
        );

        if (reservation == null) {
            JOptionPane.showMessageDialog(this, "예매에 실패했습니다.");
            return;
        }

        int seatCount = selectedSeatIds.size();
        int totalPrice = TICKET_PRICE * seatCount;

        String membershipGrade = loginUser.getMembershipGrade();
        int discountRate = loginUser.getDiscountRate();
        int discountAmount = totalPrice * discountRate / 100;
        int finalPrice = totalPrice - discountAmount;

        loginUser.addPoint(100);
        context.saveAll();

        JOptionPane.showMessageDialog(
                this,
                "예매가 완료되었습니다.\n" +
                        "예매번호: " + reservation.getReservationId() + "\n" +
                        "영화: " + movie.getTitle() + "\n" +
                        "좌석: " + context.reservationManager.getSeatNumbersByReservationId(reservation.getReservationId()) + "\n" +
                        "좌석 수: " + seatCount + "매\n" +
                        "티켓 가격: " + String.format("%,d", TICKET_PRICE) + "원\n" +
                        "총 금액: " + String.format("%,d", totalPrice) + "원\n" +
                        "멤버십 등급: " + membershipGrade + "\n" +
                        "할인율: " + discountRate + "%\n" +
                        "할인 금액: " + String.format("%,d", discountAmount) + "원\n" +
                        "최종 결제 금액: " + String.format("%,d", finalPrice) + "원\n" +
                        "혼잡률: " + screening.getCongestionRate() + "%\n" +
                        "100 포인트가 적립되었습니다."
        );

        parentFrame.refreshUserInfo();
        dispose();
    }
}