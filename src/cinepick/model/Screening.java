package cinepick.model;

public class Screening {
    private int screeningId;
    private int movieId;
    private String theater;
    private String screeningTime;
    private int totalSeats;
    private int congestionRate;

    public Screening(int screeningId, int movieId, String theater, String screeningTime, int totalSeats, int congestionRate) {
        this.screeningId = screeningId;
        this.movieId = movieId;
        this.theater = theater;
        this.screeningTime = screeningTime;
        this.totalSeats = totalSeats;
        this.congestionRate = congestionRate;
    }

    public int getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(int screeningId) {
        this.screeningId = screeningId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTheater() {
        return theater;
    }

    public void setTheater(String theater) {
        this.theater = theater;
    }

    public String getScreeningTime() {
        return screeningTime;
    }

    public void setScreeningTime(String screeningTime) {
        this.screeningTime = screeningTime;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getCongestionRate() {
        return congestionRate;
    }

    public void setCongestionRate(int congestionRate) {
        this.congestionRate = congestionRate;
    }

    public void updateCongestionRate(int reservedSeatCount) {
        if (totalSeats == 0) {
            this.congestionRate = 0;
        } else {
            this.congestionRate = (reservedSeatCount * 100) / totalSeats;
        }
    }

    @Override
    public String toString() {
        return "[" + screeningId + "] 영화번호: " + movieId +
                " | 상영관: " + theater +
                " | 시간: " + screeningTime +
                " | 전체 좌석: " + totalSeats +
                " | 혼잡률: " + congestionRate + "%";
    }
}