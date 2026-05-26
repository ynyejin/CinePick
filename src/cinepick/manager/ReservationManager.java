package cinepick.manager;

import cinepick.model.Reservation;
import cinepick.model.ReservationSeat;
import cinepick.model.Seat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReservationManager {
    private ArrayList<Reservation> reservations;
    private ArrayList<ReservationSeat> reservationSeats;

    private int nextReservationId;
    private int nextReservationSeatId;

    private SeatManager seatManager;
    private ScreeningManager screeningManager;

    public ReservationManager(SeatManager seatManager, ScreeningManager screeningManager) {
        this.reservations = new ArrayList<>();
        this.reservationSeats = new ArrayList<>();

        this.nextReservationId = 1;
        this.nextReservationSeatId = 1;

        this.seatManager = seatManager;
        this.screeningManager = screeningManager;
    }

    // 예매 생성
    public Reservation makeReservation(String userId, int screeningId, ArrayList<Integer> seatIds) {
        if (seatIds == null || seatIds.isEmpty()) {
            System.out.println("선택된 좌석이 없습니다.");
            return null;
        }

        // 1. 좌석들이 모두 해당 상영 정보에 속하고, 예약 가능한지 검사
        for (int seatId : seatIds) {
            Seat seat = seatManager.findSeatById(seatId);

            if (seat == null) {
                System.out.println("존재하지 않는 좌석입니다. 좌석 ID: " + seatId);
                return null;
            }

            if (seat.getScreeningId() != screeningId) {
                System.out.println("선택한 좌석이 해당 상영 정보의 좌석이 아닙니다. 좌석 ID: " + seatId);
                return null;
            }

            if (seat.isReserved()) {
                System.out.println("이미 예약된 좌석입니다. 좌석 번호: " + seat.getSeatNumber());
                return null;
            }
        }

        // 2. 예매 정보 생성
        String reservedAt = getCurrentTime();
        Reservation reservation = new Reservation(
                nextReservationId++,
                userId,
                screeningId,
                "RESERVED",
                reservedAt
        );

        reservations.add(reservation);

        // 3. 좌석 예약 처리 + 중간 테이블 데이터 생성
        for (int seatId : seatIds) {
            seatManager.reserveSeat(seatId);

            ReservationSeat reservationSeat = new ReservationSeat(
                    nextReservationSeatId++,
                    reservation.getReservationId(),
                    seatId
            );

            reservationSeats.add(reservationSeat);
        }

        // 4. 혼잡률 업데이트
        updateCongestionRate(screeningId);

        return reservation;
    }

    // 예매 취소
    public boolean cancelReservation(int reservationId) {
        Reservation reservation = findReservationById(reservationId);

        if (reservation == null) {
            System.out.println("존재하지 않는 예매 번호입니다.");
            return false;
        }

        if (reservation.getStatus().equals("CANCELED")) {
            System.out.println("이미 취소된 예매입니다.");
            return false;
        }

        reservation.cancel();

        // 해당 예매에 연결된 좌석 예약 취소
        ArrayList<ReservationSeat> seatsToCancel = getReservationSeatsByReservationId(reservationId);

        for (ReservationSeat reservationSeat : seatsToCancel) {
            seatManager.cancelSeat(reservationSeat.getSeatId());
        }

        updateCongestionRate(reservation.getScreeningId());

        return true;
    }

    // 예매 번호로 검색
    public Reservation findReservationById(int reservationId) {
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId() == reservationId) {
                return reservation;
            }
        }
        return null;
    }

    // 특정 회원의 예매 목록 반환
    public ArrayList<Reservation> getReservationsByUserId(String userId) {
        ArrayList<Reservation> result = new ArrayList<>();

        for (Reservation reservation : reservations) {
            if (reservation.getUserId().equals(userId)) {
                result.add(reservation);
            }
        }

        return result;
    }

    // 특정 예매에 포함된 좌석 목록 반환
    public ArrayList<ReservationSeat> getReservationSeatsByReservationId(int reservationId) {
        ArrayList<ReservationSeat> result = new ArrayList<>();

        for (ReservationSeat reservationSeat : reservationSeats) {
            if (reservationSeat.getReservationId() == reservationId) {
                result.add(reservationSeat);
            }
        }

        return result;
    }

    // 특정 예매의 좌석 번호 문자열 반환
    public String getSeatNumbersByReservationId(int reservationId) {
        ArrayList<ReservationSeat> result = getReservationSeatsByReservationId(reservationId);
        StringBuilder seatNumbers = new StringBuilder();

        for (ReservationSeat reservationSeat : result) {
            Seat seat = seatManager.findSeatById(reservationSeat.getSeatId());

            if (seat != null) {
                seatNumbers.append(seat.getSeatNumber()).append(" ");
            }
        }

        return seatNumbers.toString().trim();
    }

    // 혼잡률 업데이트
    private void updateCongestionRate(int screeningId) {
        int reservedSeatCount = seatManager.countReservedSeatsByScreeningId(screeningId);
        screeningManager.updateCongestionRate(screeningId, reservedSeatCount);
    }

    // 현재 시간 문자열 반환
    private String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    // 전체 예매 목록 반환
    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    // 전체 예매 좌석 목록 반환
    public ArrayList<ReservationSeat> getReservationSeats() {
        return reservationSeats;
    }

    // 특정 회원 예매 내역 출력
    public void printReservationsByUserId(String userId) {
        ArrayList<Reservation> result = getReservationsByUserId(userId);

        if (result.isEmpty()) {
            System.out.println("예매 내역이 없습니다.");
            return;
        }

        for (Reservation reservation : result) {
            System.out.println(reservation);
            System.out.println("좌석: " + getSeatNumbersByReservationId(reservation.getReservationId()));
            System.out.println("--------------------------------");
        }
    }

    // 전체 예매 내역 출력
    public void printAllReservations() {
        if (reservations.isEmpty()) {
            System.out.println("전체 예매 내역이 없습니다.");
            return;
        }

        for (Reservation reservation : reservations) {
            System.out.println(reservation);
            System.out.println("좌석: " + getSeatNumbersByReservationId(reservation.getReservationId()));
            System.out.println("--------------------------------");
        }
    }

    public void replaceReservations(ArrayList<Reservation> loadedReservations,
                                    ArrayList<ReservationSeat> loadedReservationSeats) {
        reservations.clear();
        reservations.addAll(loadedReservations);

        reservationSeats.clear();
        reservationSeats.addAll(loadedReservationSeats);

        nextReservationId = 1;
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId() >= nextReservationId) {
                nextReservationId = reservation.getReservationId() + 1;
            }
        }

        nextReservationSeatId = 1;
        for (ReservationSeat reservationSeat : reservationSeats) {
            if (reservationSeat.getReservationSeatId() >= nextReservationSeatId) {
                nextReservationSeatId = reservationSeat.getReservationSeatId() + 1;
            }
        }
    }
}