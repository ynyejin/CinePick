package cinepick.model;

public class ReservationSeat {
    private int reservationSeatId;
    private int reservationId;
    private int seatId;

    public ReservationSeat(int reservationSeatId, int reservationId, int seatId) {
        this.reservationSeatId = reservationSeatId;
        this.reservationId = reservationId;
        this.seatId = seatId;
    }

    public int getReservationSeatId() {
        return reservationSeatId;
    }

    public void setReservationSeatId(int reservationSeatId) {
        this.reservationSeatId = reservationSeatId;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    @Override
    public String toString() {
        return "예매좌석ID: " + reservationSeatId +
                " | 예매번호: " + reservationId +
                " | 좌석ID: " + seatId;
    }
}