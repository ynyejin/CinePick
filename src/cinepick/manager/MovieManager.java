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
        addMovie("인사이드 아웃2", "애니메이션", 96, 4.8,
                "켈시 만", "에이미 포엘러, 마야 호크",
                "사춘기에 접어든 라일리의 머릿속에서 새로운 감정들이 등장하며 벌어지는 이야기");

        addMovie("범죄도시4", "액션", 109, 4.5,
                "허명행", "마동석, 김무열, 박지환",
                "괴물형사 마석도가 온라인 불법 도박 조직을 추적하며 벌어지는 범죄 액션 영화");

        addMovie("듄: 파트2", "SF", 166, 4.7,
                "드니 빌뇌브", "티모시 샬라메, 젠데이아",
                "폴 아트레이데스가 자신의 운명을 받아들이고 거대한 전쟁에 휘말리는 이야기");
    }

    // 영화 추가
    public Movie addMovie(String title, String genre, int runningTime, double rating,
                          String director, String actors, String description) {
        Movie movie = new Movie(nextMovieId++, title, genre, runningTime, rating, director, actors, description);
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
    public boolean updateMovie(int movieId, String title, String genre, int runningTime, double rating,
                               String director, String actors, String description) {
        Movie movie = findMovieById(movieId);

        if (movie == null) {
            return false;
        }

        movie.setTitle(title);
        movie.setGenre(genre);
        movie.setRunningTime(runningTime);
        movie.setRating(rating);
        movie.setDirector(director);
        movie.setActors(actors);
        movie.setDescription(description);

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

    public void replaceMovies(ArrayList<Movie> loadedMovies) {
        movies.clear();
        movies.addAll(loadedMovies);

        nextMovieId = 1;
        for (Movie movie : movies) {
            if (movie.getMovieId() >= nextMovieId) {
                nextMovieId = movie.getMovieId() + 1;
            }
        }
    }
}