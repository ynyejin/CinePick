package cinepick.model;

public class Seat {
    private int seatId;
    private int screeningId;
    private String seatNumber;
    private boolean reserved;

    public Seat(int seatId, int screeningId, String seatNumber, boolean reserved) {
        this.seatId = seatId;
        this.screeningId = screeningId;
        this.seatNumber = seatNumber;
        this.reserved = reserved;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public int getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(int screeningId) {
        this.screeningId = screeningId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    @Override
    public String toString() {
        return seatNumber + (reserved ? " [예약됨]" : " [예약가능]");
    }
}