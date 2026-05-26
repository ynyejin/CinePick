package cinepick.model;

public class Reservation {
    private int reservationId;
    private String userId;
    private int screeningId;
    private String status; // RESERVED 또는 CANCELED
    private String reservedAt;

    public Reservation(int reservationId, String userId, int screeningId, String status, String reservedAt) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.screeningId = screeningId;
        this.status = status;
        this.reservedAt = reservedAt;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(int screeningId) {
        this.screeningId = screeningId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(String reservedAt) {
        this.reservedAt = reservedAt;
    }

    public void cancel() {
        this.status = "CANCELED";
    }

    @Override
    public String toString() {
        return "예매번호: " + reservationId +
                " | 회원ID: " + userId +
                " | 상영번호: " + screeningId +
                " | 상태: " + status +
                " | 예매일시: " + reservedAt;
    }
}