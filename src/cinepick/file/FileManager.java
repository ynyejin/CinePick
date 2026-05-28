package cinepick.file;

import cinepick.manager.MovieManager;
import cinepick.manager.ReservationManager;
import cinepick.manager.ScreeningManager;
import cinepick.manager.SeatManager;
import cinepick.manager.UserManager;
import cinepick.model.Admin;
import cinepick.model.Movie;
import cinepick.model.Reservation;
import cinepick.model.ReservationSeat;
import cinepick.model.Screening;
import cinepick.model.Seat;
import cinepick.model.User;

import java.io.*;
import java.util.ArrayList;

public class FileManager {
    private static final String DATA_DIR = "data";

    private static final String USERS_FILE = DATA_DIR + "/users.txt";
    private static final String MOVIES_FILE = DATA_DIR + "/movies.txt";
    private static final String SCREENINGS_FILE = DATA_DIR + "/screenings.txt";
    private static final String SEATS_FILE = DATA_DIR + "/seats.txt";
    private static final String RESERVATIONS_FILE = DATA_DIR + "/reservations.txt";
    private static final String RESERVATION_SEATS_FILE = DATA_DIR + "/reservation_seats.txt";

    public void saveAll(UserManager userManager,
                        MovieManager movieManager,
                        ScreeningManager screeningManager,
                        SeatManager seatManager,
                        ReservationManager reservationManager) {
        createDataDirectory();

        saveUsers(userManager.getUsers());
        saveMovies(movieManager.getMovies());
        saveScreenings(screeningManager.getScreenings());
        saveSeats(seatManager.getSeats());
        saveReservations(reservationManager.getReservations());
        saveReservationSeats(reservationManager.getReservationSeats());
    }

    public void loadAll(UserManager userManager,
                        MovieManager movieManager,
                        ScreeningManager screeningManager,
                        SeatManager seatManager,
                        ReservationManager reservationManager) {
        createDataDirectory();

        ArrayList<User> users = loadUsers();
        ArrayList<Movie> movies = loadMovies();
        ArrayList<Screening> screenings = loadScreenings();
        ArrayList<Seat> seats = loadSeats();
        ArrayList<Reservation> reservations = loadReservations();
        ArrayList<ReservationSeat> reservationSeats = loadReservationSeats();

        if (!users.isEmpty()) {
            userManager.replaceUsers(users);
        }

        if (!movies.isEmpty()) {
            movieManager.replaceMovies(movies);
        }

        if (!screenings.isEmpty()) {
            screeningManager.replaceScreenings(screenings);
        }

        if (!seats.isEmpty()) {
            seatManager.replaceSeats(seats);
        }

        if (!reservations.isEmpty() || !reservationSeats.isEmpty()) {
            reservationManager.replaceReservations(reservations, reservationSeats);
        }
    }

    private void createDataDirectory() {
        File directory = new File(DATA_DIR);

        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private void saveUsers(ArrayList<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                writer.println(
                        user.getUserId() + "|" +
                                user.getPassword() + "|" +
                                user.getName() + "|" +
                                user.getPoint() + "|" +
                                user.getRole()
                );
            }
        } catch (IOException e) {
            System.out.println("회원 정보 저장 중 오류가 발생했습니다.");
        }
    }

    private ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();
        File file = new File(USERS_FILE);

        if (!file.exists()) {
            return users;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");

                if (data.length < 5) {
                    continue;
                }

                String userId = data[0];
                String password = data[1];
                String name = data[2];
                int point = Integer.parseInt(data[3]);
                String role = data[4];

                if (role.equals("ADMIN")) {
                    users.add(new Admin(userId, password, name, point, "ADMIN_CODE"));
                } else {
                    users.add(new User(userId, password, name, point, role));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("회원 정보 불러오기 중 오류가 발생했습니다.");
        }

        return users;
    }

    private void saveMovies(ArrayList<Movie> movies) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(MOVIES_FILE))) {
            for (Movie movie : movies) {
                writer.println(
                        movie.getMovieId() + "|" +
                                movie.getTitle() + "|" +
                                movie.getGenre() + "|" +
                                movie.getRunningTime() + "|" +
                                movie.getRating() + "|" +
                                movie.getDirector() + "|" +
                                movie.getActors() + "|" +
                                movie.getDescription()
                );
            }
        } catch (IOException e) {
            System.out.println("영화 정보 저장 중 오류가 발생했습니다.");
        }
    }

    private ArrayList<Movie> loadMovies() {
        ArrayList<Movie> movies = new ArrayList<>();
        File file = new File(MOVIES_FILE);

        if (!file.exists()) {
            return movies;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");

                if (data.length < 5) {
                    continue;
                }

                int movieId = Integer.parseInt(data[0]);
                String title = data[1];
                String genre = data[2];
                int runningTime = Integer.parseInt(data[3]);
                double rating = Double.parseDouble(data[4]);

                String director = data.length > 5 ? data[5] : "정보 없음";
                String actors = data.length > 6 ? data[6] : "정보 없음";
                String description = data.length > 7 ? data[7] : "정보 없음";

                movies.add(new Movie(movieId, title, genre, runningTime, rating, director, actors, description));
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("영화 정보 불러오기 중 오류가 발생했습니다.");
        }

        return movies;
    }

    private void saveScreenings(ArrayList<Screening> screenings) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SCREENINGS_FILE))) {
            for (Screening screening : screenings) {
                writer.println(
                        screening.getScreeningId() + "|" +
                                screening.getMovieId() + "|" +
                                screening.getTheater() + "|" +
                                screening.getScreeningTime() + "|" +
                                screening.getTotalSeats() + "|" +
                                screening.getCongestionRate()
                );
            }
        } catch (IOException e) {
            System.out.println("상영 정보 저장 중 오류가 발생했습니다.");
        }
    }

    private ArrayList<Screening> loadScreenings() {
        ArrayList<Screening> screenings = new ArrayList<>();
        File file = new File(SCREENINGS_FILE);

        if (!file.exists()) {
            return screenings;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");

                if (data.length < 6) {
                    continue;
                }

                int screeningId = Integer.parseInt(data[0]);
                int movieId = Integer.parseInt(data[1]);
                String theater = data[2];
                String screeningTime = data[3];
                int totalSeats = Integer.parseInt(data[4]);
                int congestionRate = Integer.parseInt(data[5]);

                screenings.add(new Screening(screeningId, movieId, theater, screeningTime, totalSeats, congestionRate));
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("상영 정보 불러오기 중 오류가 발생했습니다.");
        }

        return screenings;
    }

    private void saveSeats(ArrayList<Seat> seats) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SEATS_FILE))) {
            for (Seat seat : seats) {
                writer.println(
                        seat.getSeatId() + "|" +
                                seat.getScreeningId() + "|" +
                                seat.getSeatNumber() + "|" +
                                seat.isReserved()
                );
            }
        } catch (IOException e) {
            System.out.println("좌석 정보 저장 중 오류가 발생했습니다.");
        }
    }

    private ArrayList<Seat> loadSeats() {
        ArrayList<Seat> seats = new ArrayList<>();
        File file = new File(SEATS_FILE);

        if (!file.exists()) {
            return seats;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");

                if (data.length < 4) {
                    continue;
                }

                int seatId = Integer.parseInt(data[0]);
                int screeningId = Integer.parseInt(data[1]);
                String seatNumber = data[2];
                boolean reserved = Boolean.parseBoolean(data[3]);

                seats.add(new Seat(seatId, screeningId, seatNumber, reserved));
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("좌석 정보 불러오기 중 오류가 발생했습니다.");
        }

        return seats;
    }

    private void saveReservations(ArrayList<Reservation> reservations) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RESERVATIONS_FILE))) {
            for (Reservation reservation : reservations) {
                writer.println(
                        reservation.getReservationId() + "|" +
                                reservation.getUserId() + "|" +
                                reservation.getScreeningId() + "|" +
                                reservation.getStatus() + "|" +
                                reservation.getReservedAt()
                );
            }
        } catch (IOException e) {
            System.out.println("예매 정보 저장 중 오류가 발생했습니다.");
        }
    }

    private ArrayList<Reservation> loadReservations() {
        ArrayList<Reservation> reservations = new ArrayList<>();
        File file = new File(RESERVATIONS_FILE);

        if (!file.exists()) {
            return reservations;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");

                if (data.length < 5) {
                    continue;
                }

                int reservationId = Integer.parseInt(data[0]);
                String userId = data[1];
                int screeningId = Integer.parseInt(data[2]);
                String status = data[3];
                String reservedAt = data[4];

                reservations.add(new Reservation(reservationId, userId, screeningId, status, reservedAt));
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("예매 정보 불러오기 중 오류가 발생했습니다.");
        }

        return reservations;
    }

    private void saveReservationSeats(ArrayList<ReservationSeat> reservationSeats) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RESERVATION_SEATS_FILE))) {
            for (ReservationSeat reservationSeat : reservationSeats) {
                writer.println(
                        reservationSeat.getReservationSeatId() + "|" +
                                reservationSeat.getReservationId() + "|" +
                                reservationSeat.getSeatId()
                );
            }
        } catch (IOException e) {
            System.out.println("예매 좌석 정보 저장 중 오류가 발생했습니다.");
        }
    }

    private ArrayList<ReservationSeat> loadReservationSeats() {
        ArrayList<ReservationSeat> reservationSeats = new ArrayList<>();
        File file = new File(RESERVATION_SEATS_FILE);

        if (!file.exists()) {
            return reservationSeats;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");

                if (data.length < 3) {
                    continue;
                }

                int reservationSeatId = Integer.parseInt(data[0]);
                int reservationId = Integer.parseInt(data[1]);
                int seatId = Integer.parseInt(data[2]);

                reservationSeats.add(new ReservationSeat(reservationSeatId, reservationId, seatId));
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("예매 좌석 정보 불러오기 중 오류가 발생했습니다.");
        }

        return reservationSeats;
    }
}