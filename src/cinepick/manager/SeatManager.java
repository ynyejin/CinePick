package cinepick.manager;

import cinepick.model.Seat;

import java.util.ArrayList;

public class SeatManager {
    private ArrayList<Seat> seats;
    private int nextSeatId;

    public SeatManager() {
        seats = new ArrayList<>();
        nextSeatId = 1;

        // 테스트용 기본 상영 정보에 대한 좌석 생성
        createSeatsForScreening(1, 20);
        createSeatsForScreening(2, 20);
        createSeatsForScreening(3, 20);
    }

    // 특정 상영 정보에 좌석 생성
    public void createSeatsForScreening(int screeningId, int totalSeats) {
        for (int i = 1; i <= totalSeats; i++) {
            String seatNumber = makeSeatNumber(i);
            Seat seat = new Seat(nextSeatId++, screeningId, seatNumber, false);
            seats.add(seat);
        }
    }

    // 좌석 번호 생성: A1, A2, ..., B1, B2 형식
    private String makeSeatNumber(int index) {
        int rowIndex = (index - 1) / 5;
        int colIndex = (index - 1) % 5 + 1;

        char row = (char) ('A' + rowIndex);
        return row + String.valueOf(colIndex);
    }

    // 좌석 ID로 검색
    public Seat findSeatById(int seatId) {
        for (Seat seat : seats) {
            if (seat.getSeatId() == seatId) {
                return seat;
            }
        }
        return null;
    }

    // 특정 상영 정보의 좌석 목록 반환
    public ArrayList<Seat> getSeatsByScreeningId(int screeningId) {
        ArrayList<Seat> result = new ArrayList<>();

        for (Seat seat : seats) {
            if (seat.getScreeningId() == screeningId) {
                result.add(seat);
            }
        }

        return result;
    }

    // 좌석 예약 가능 여부 확인
    public boolean isSeatAvailable(int seatId) {
        Seat seat = findSeatById(seatId);

        if (seat == null) {
            return false;
        }

        return !seat.isReserved();
    }

    // 좌석 예약 처리
    public boolean reserveSeat(int seatId) {
        Seat seat = findSeatById(seatId);

        if (seat == null || seat.isReserved()) {
            return false;
        }

        seat.setReserved(true);
        return true;
    }

    // 좌석 예약 취소 처리
    public boolean cancelSeat(int seatId) {
        Seat seat = findSeatById(seatId);

        if (seat == null || !seat.isReserved()) {
            return false;
        }

        seat.setReserved(false);
        return true;
    }

    // 특정 상영 정보에서 예약된 좌석 수 계산
    public int countReservedSeatsByScreeningId(int screeningId) {
        int count = 0;

        for (Seat seat : seats) {
            if (seat.getScreeningId() == screeningId && seat.isReserved()) {
                count++;
            }
        }

        return count;
    }

    // 전체 좌석 목록 반환
    public ArrayList<Seat> getSeats() {
        return seats;
    }

    // 특정 상영 정보의 좌석 출력
    public void printSeatsByScreeningId(int screeningId) {
        ArrayList<Seat> result = getSeatsByScreeningId(screeningId);

        if (result.isEmpty()) {
            System.out.println("해당 상영 정보의 좌석이 없습니다.");
            return;
        }

        System.out.println("===== 좌석 목록 =====");

        for (int i = 0; i < result.size(); i++) {
            Seat seat = result.get(i);

            if (seat.isReserved()) {
                System.out.print("[XX] ");
            } else {
                System.out.print("[" + seat.getSeatNumber() + "] ");
            }

            if ((i + 1) % 5 == 0) {
                System.out.println();
            }
        }

        System.out.println();
    }

    // 특정 상영 정보에서 좌석 번호로 좌석 검색
    public Seat findSeatByScreeningIdAndSeatNumber(int screeningId, String seatNumber) {
        for (Seat seat : seats) {
            if (seat.getScreeningId() == screeningId &&
                    seat.getSeatNumber().equalsIgnoreCase(seatNumber)) {
                return seat;
            }
        }
        return null;
    }

    public void replaceSeats(ArrayList<Seat> loadedSeats) {
        seats.clear();
        seats.addAll(loadedSeats);

        nextSeatId = 1;
        for (Seat seat : seats) {
            if (seat.getSeatId() >= nextSeatId) {
                nextSeatId = seat.getSeatId() + 1;
            }
        }
    }
}