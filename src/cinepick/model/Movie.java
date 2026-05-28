package cinepick.model;

public class Movie {
    private int movieId;
    private String title;
    private String genre;
    private int runningTime;
    private double rating;
    private String director;
    private String actors;
    private String description;

    public Movie(int movieId, String title, String genre, int runningTime, double rating,
                 String director, String actors, String description) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.runningTime = runningTime;
        this.rating = rating;
        this.director = director;
        this.actors = actors;
        this.description = description;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "[" + movieId + "] " + title +
                " | 장르: " + genre +
                " | 상영시간: " + runningTime + "분" +
                " | 평점: " + rating +
                " | 감독: " + director;
    }
}