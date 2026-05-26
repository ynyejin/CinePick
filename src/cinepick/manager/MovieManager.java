package cinepick.manager;

import cinepick.model.Movie;

import java.util.ArrayList;

public class MovieManager {
    private ArrayList<Movie> movies;
    private int nextMovieId;

    public MovieManager() {
        movies = new ArrayList<>();
        nextMovieId = 1;

        // 테스트용 기본 영화 데이터
        addMovie("인사이드 아웃2", "애니메이션", 96, 4.8);
        addMovie("범죄도시4", "액션", 109, 4.5);
        addMovie("듄: 파트2", "SF", 166, 4.7);
    }

    // 영화 추가
    public Movie addMovie(String title, String genre, int runningTime, double rating) {
        Movie movie = new Movie(nextMovieId++, title, genre, runningTime, rating);
        movies.add(movie);
        return movie;
    }

    // 영화 삭제
    public boolean deleteMovie(int movieId) {
        Movie movie = findMovieById(movieId);

        if (movie == null) {
            return false;
        }

        movies.remove(movie);
        return true;
    }

    // 영화 수정
    public boolean updateMovie(int movieId, String title, String genre, int runningTime, double rating) {
        Movie movie = findMovieById(movieId);

        if (movie == null) {
            return false;
        }

        movie.setTitle(title);
        movie.setGenre(genre);
        movie.setRunningTime(runningTime);
        movie.setRating(rating);

        return true;
    }

    // 영화 번호로 검색
    public Movie findMovieById(int movieId) {
        for (Movie movie : movies) {
            if (movie.getMovieId() == movieId) {
                return movie;
            }
        }
        return null;
    }

    // 전체 영화 목록 반환
    public ArrayList<Movie> getMovies() {
        return movies;
    }

    // 영화 목록 출력
    public void printMovies() {
        if (movies.isEmpty()) {
            System.out.println("등록된 영화가 없습니다.");
            return;
        }

        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}