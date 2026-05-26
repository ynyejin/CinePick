package cinepick.manager;

import cinepick.model.Screening;

import java.util.ArrayList;

public class ScreeningManager {
    private ArrayList<Screening> screenings;
    private int nextScreeningId;

    public ScreeningManager() {
        screenings = new ArrayList<>();
        nextScreeningId = 1;

        // 테스트용 기본 상영 정보
        addScreening(1, "1관", "2026-06-01 14:00", 20);
        addScreening(1, "1관", "2026-06-01 18:00", 20);
        addScreening(2, "2관", "2026-06-01 16:00", 20);
    }

    // 상영 정보 추가
    public Screening addScreening(int movieId, String theater, String screeningTime, int totalSeats) {
        Screening screening = new Screening(
                nextScreeningId++,
                movieId,
                theater,
                screeningTime,
                totalSeats,
                0
        );

        screenings.add(screening);
        return screening;
    }

    // 상영 정보 삭제
    public boolean deleteScreening(int screeningId) {
        Screening screening = findScreeningById(screeningId);

        if (screening == null) {
            return false;
        }

        screenings.remove(screening);
        return true;
    }

    // 상영 정보 번호로 검색
    public Screening findScreeningById(int screeningId) {
        for (Screening screening : screenings) {
            if (screening.getScreeningId() == screeningId) {
                return screening;
            }
        }
        return null;
    }

    // 특정 영화의 상영 정보 목록 반환
    public ArrayList<Screening> getScreeningsByMovieId(int movieId) {
        ArrayList<Screening> result = new ArrayList<>();

        for (Screening screening : screenings) {
            if (screening.getMovieId() == movieId) {
                result.add(screening);
            }
        }

        return result;
    }

    // 혼잡률 업데이트
    public void updateCongestionRate(int screeningId, int reservedSeatCount) {
        Screening screening = findScreeningById(screeningId);

        if (screening != null) {
            screening.updateCongestionRate(reservedSeatCount);
        }
    }

    // 전체 상영 정보 목록 반환
    public ArrayList<Screening> getScreenings() {
        return screenings;
    }

    // 전체 상영 정보 출력
    public void printScreenings() {
        if (screenings.isEmpty()) {
            System.out.println("등록된 상영 정보가 없습니다.");
            return;
        }

        for (Screening screening : screenings) {
            System.out.println(screening);
        }
    }

    // 특정 영화의 상영 정보 출력
    public void printScreeningsByMovieId(int movieId) {
        ArrayList<Screening> result = getScreeningsByMovieId(movieId);

        if (result.isEmpty()) {
            System.out.println("해당 영화의 상영 정보가 없습니다.");
            return;
        }

        for (Screening screening : result) {
            System.out.println(screening);
        }
    }
}