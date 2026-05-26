package cinepick.console;

import cinepick.manager.MovieManager;
import cinepick.manager.ReservationManager;
import cinepick.manager.ScreeningManager;
import cinepick.manager.SeatManager;
import cinepick.manager.UserManager;
import cinepick.model.Movie;
import cinepick.model.Reservation;
import cinepick.model.Screening;
import cinepick.model.Seat;
import cinepick.model.User;
import cinepick.file.FileManager;

import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleApp {
    private Scanner scanner;

    private UserManager userManager;
    private MovieManager movieManager;
    private ScreeningManager screeningManager;
    private SeatManager seatManager;
    private ReservationManager reservationManager;
    private FileManager fileManager;

    private User loginUser;

    public ConsoleApp() {
        scanner = new Scanner(System.in);

        userManager = new UserManager();
        movieManager = new MovieManager();
        screeningManager = new ScreeningManager();
        seatManager = new SeatManager();
        reservationManager = new ReservationManager(seatManager, screeningManager);

        fileManager = new FileManager();
        fileManager.loadAll(userManager, movieManager, screeningManager, seatManager, reservationManager);

        loginUser = null;
    }

    public void start() {
        while (true) {
            System.out.println("\n===== CinePick 영화 예매 시스템 =====");
            System.out.println("1. 회원가입");
            System.out.println("2. 로그인");
            System.out.println("3. 관리자 로그인");
            System.out.println("0. 종료");

            int choice = readInt("메뉴 선택 >> ");

            switch (choice) {
                case 1:
                    register();
                    break;
                case 2:
                    loginAsUser();
                    break;
                case 3:
                    loginAsAdmin();
                    break;
                case 0:
                    fileManager.saveAll(userManager, movieManager, screeningManager, seatManager, reservationManager);
                    System.out.println("데이터가 저장되었습니다.");
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("잘못된 메뉴입니다.");
            }
        }
    }

    private void register() {
        System.out.println("\n===== 회원가입 =====");

        System.out.print("아이디: ");
        String userId = scanner.nextLine();

        System.out.print("비밀번호: ");
        String password = scanner.nextLine();

        System.out.print("이름: ");
        String name = scanner.nextLine();

        boolean result = userManager.registerUser(userId, password, name);

        if (result) {
            System.out.println("회원가입이 완료되었습니다.");
        } else {
            System.out.println("이미 사용 중인 아이디입니다.");
        }
    }

    private void loginAsUser() {
        System.out.println("\n===== 사용자 로그인 =====");

        System.out.print("아이디: ");
        String userId = scanner.nextLine();

        System.out.print("비밀번호: ");
        String password = scanner.nextLine();

        User user = userManager.login(userId, password);

        if (user == null) {
            System.out.println("아이디 또는 비밀번호가 올바르지 않습니다.");
            return;
        }

        if (!user.getRole().equals("USER")) {
            System.out.println("사용자 계정이 아닙니다.");
            return;
        }

        loginUser = user;
        System.out.println(loginUser.getName() + "님, 로그인되었습니다.");

        userMenu();
    }

    private void loginAsAdmin() {
        System.out.println("\n===== 관리자 로그인 =====");

        System.out.print("아이디: ");
        String userId = scanner.nextLine();

        System.out.print("비밀번호: ");
        String password = scanner.nextLine();

        User user = userManager.login(userId, password);

        if (user == null) {
            System.out.println("아이디 또는 비밀번호가 올바르지 않습니다.");
            return;
        }

        if (!user.getRole().equals("ADMIN")) {
            System.out.println("관리자 계정이 아닙니다.");
            return;
        }

        loginUser = user;
        System.out.println("관리자 로그인 성공");

        adminMenu();
    }

    private void userMenu() {
        while (true) {
            System.out.println("\n===== 사용자 메뉴 =====");
            System.out.println("1. 영화 목록 조회");
            System.out.println("2. 영화 예매");
            System.out.println("3. 내 예매 내역 조회");
            System.out.println("4. 예매 취소");
            System.out.println("5. 내 정보 조회");
            System.out.println("0. 로그아웃");

            int choice = readInt("메뉴 선택 >> ");

            switch (choice) {
                case 1:
                    showMovieList();
                    break;
                case 2:
                    makeReservation();
                    break;
                case 3:
                    showMyReservations();
                    break;
                case 4:
                    cancelReservation();
                    break;
                case 5:
                    showMyInfo();
                    break;
                case 0:
                    loginUser = null;
                    System.out.println("로그아웃되었습니다.");
                    return;
                default:
                    System.out.println("잘못된 메뉴입니다.");
            }
        }
    }

    private void adminMenu() {
        while (true) {
            System.out.println("\n===== 관리자 메뉴 =====");
            System.out.println("1. 영화 목록 조회");
            System.out.println("2. 영화 등록");
            System.out.println("3. 영화 수정");
            System.out.println("4. 영화 삭제");
            System.out.println("5. 상영 정보 목록 조회");
            System.out.println("6. 상영 정보 등록");
            System.out.println("7. 회원 목록 조회");
            System.out.println("8. 전체 예매 내역 조회");
            System.out.println("0. 로그아웃");

            int choice = readInt("메뉴 선택 >> ");

            switch (choice) {
                case 1:
                    showMovieList();
                    break;
                case 2:
                    addMovie();
                    break;
                case 3:
                    updateMovie();
                    break;
                case 4:
                    deleteMovie();
                    break;
                case 5:
                    screeningManager.printScreenings();
                    break;
                case 6:
                    addScreening();
                    break;
                case 7:
                    userManager.printUsers();
                    break;
                case 8:
                    reservationManager.printAllReservations();
                    break;
                case 0:
                    loginUser = null;
                    System.out.println("관리자 로그아웃되었습니다.");
                    return;
                default:
                    System.out.println("잘못된 메뉴입니다.");
            }
        }
    }

    private void showMovieList() {
        System.out.println("\n===== 영화 목록 =====");
        movieManager.printMovies();
    }

    private void makeReservation() {
        System.out.println("\n===== 영화 예매 =====");

        // 1. 영화 선택
        showMovieList();
        int movieId = readInt("예매할 영화 번호 선택 >> ");

        Movie movie = movieManager.findMovieById(movieId);

        if (movie == null) {
            System.out.println("존재하지 않는 영화입니다.");
            return;
        }

        // 2. 상영 정보 선택
        System.out.println("\n===== 상영 정보 =====");
        screeningManager.printScreeningsByMovieId(movieId);

        int screeningId = readInt("상영 정보 번호 선택 >> ");

        Screening screening = screeningManager.findScreeningById(screeningId);

        if (screening == null || screening.getMovieId() != movieId) {
            System.out.println("올바르지 않은 상영 정보입니다.");
            return;
        }

        // 3. 좌석 선택
        seatManager.printSeatsByScreeningId(screeningId);

        System.out.println("예매할 좌석 번호를 입력하세요.");
        System.out.println("여러 좌석은 공백으로 구분합니다. 예: A1 A2 A3");
        System.out.print("좌석 선택 >> ");
        String seatInput = scanner.nextLine();

        String[] seatNumbers = seatInput.trim().split("\\s+");
        ArrayList<Integer> seatIds = new ArrayList<>();

        for (String seatNumber : seatNumbers) {
            Seat seat = seatManager.findSeatByScreeningIdAndSeatNumber(screeningId, seatNumber);

            if (seat == null) {
                System.out.println("존재하지 않는 좌석입니다: " + seatNumber);
                return;
            }

            if (seat.isReserved()) {
                System.out.println("이미 예약된 좌석입니다: " + seatNumber);
                return;
            }

            seatIds.add(seat.getSeatId());
        }

        // 4. 예매 생성
        Reservation reservation = reservationManager.makeReservation(
                loginUser.getUserId(),
                screeningId,
                seatIds
        );

        if (reservation == null) {
            System.out.println("예매에 실패했습니다.");
            return;
        }

        // 5. 포인트 적립
        loginUser.addPoint(100);

        System.out.println("\n예매가 완료되었습니다.");
        System.out.println("예매번호: " + reservation.getReservationId());
        System.out.println("영화: " + movie.getTitle());
        System.out.println("상영관: " + screening.getTheater());
        System.out.println("상영 시간: " + screening.getScreeningTime());
        System.out.println("좌석: " + reservationManager.getSeatNumbersByReservationId(reservation.getReservationId()));
        System.out.println("혼잡률: " + screening.getCongestionRate() + "%");
        System.out.println("100 포인트가 적립되었습니다.");
    }

    private void showMyReservations() {
        System.out.println("\n===== 내 예매 내역 =====");
        reservationManager.printReservationsByUserId(loginUser.getUserId());
    }

    private void cancelReservation() {
        System.out.println("\n===== 예매 취소 =====");

        showMyReservations();

        int reservationId = readInt("취소할 예매 번호 입력 >> ");

        Reservation reservation = reservationManager.findReservationById(reservationId);

        if (reservation == null) {
            System.out.println("존재하지 않는 예매입니다.");
            return;
        }

        if (!reservation.getUserId().equals(loginUser.getUserId())) {
            System.out.println("본인의 예매만 취소할 수 있습니다.");
            return;
        }

        boolean result = reservationManager.cancelReservation(reservationId);

        if (result) {
            System.out.println("예매가 취소되었습니다.");
        } else {
            System.out.println("예매 취소에 실패했습니다.");
        }
    }

    private void showMyInfo() {
        System.out.println("\n===== 내 정보 =====");
        System.out.println(loginUser);
    }

    private void addMovie() {
        System.out.println("\n===== 영화 등록 =====");

        System.out.print("영화 제목: ");
        String title = scanner.nextLine();

        System.out.print("장르: ");
        String genre = scanner.nextLine();

        int runningTime = readInt("상영 시간(분): ");
        double rating = readDouble("평점: ");

        Movie movie = movieManager.addMovie(title, genre, runningTime, rating);

        System.out.println("영화가 등록되었습니다.");
        System.out.println(movie);
    }

    private void updateMovie() {
        System.out.println("\n===== 영화 수정 =====");

        showMovieList();

        int movieId = readInt("수정할 영화 번호 입력 >> ");

        Movie movie = movieManager.findMovieById(movieId);

        if (movie == null) {
            System.out.println("존재하지 않는 영화입니다.");
            return;
        }

        System.out.print("새 영화 제목: ");
        String title = scanner.nextLine();

        System.out.print("새 장르: ");
        String genre = scanner.nextLine();

        int runningTime = readInt("새 상영 시간(분): ");
        double rating = readDouble("새 평점: ");

        boolean result = movieManager.updateMovie(movieId, title, genre, runningTime, rating);

        if (result) {
            System.out.println("영화 정보가 수정되었습니다.");
        } else {
            System.out.println("영화 수정에 실패했습니다.");
        }
    }

    private void deleteMovie() {
        System.out.println("\n===== 영화 삭제 =====");

        showMovieList();

        int movieId = readInt("삭제할 영화 번호 입력 >> ");

        boolean result = movieManager.deleteMovie(movieId);

        if (result) {
            System.out.println("영화가 삭제되었습니다.");
        } else {
            System.out.println("영화 삭제에 실패했습니다.");
        }
    }

    private void addScreening() {
        System.out.println("\n===== 상영 정보 등록 =====");

        showMovieList();

        int movieId = readInt("영화 번호 선택 >> ");

        Movie movie = movieManager.findMovieById(movieId);

        if (movie == null) {
            System.out.println("존재하지 않는 영화입니다.");
            return;
        }

        System.out.print("상영관: ");
        String theater = scanner.nextLine();

        System.out.print("상영 시간 예: 2026-06-01 14:00 >> ");
        String screeningTime = scanner.nextLine();

        int totalSeats = readInt("전체 좌석 수: ");

        Screening screening = screeningManager.addScreening(movieId, theater, screeningTime, totalSeats);

        // 상영 정보가 추가되면 해당 상영 정보에 대한 좌석도 생성
        seatManager.createSeatsForScreening(screening.getScreeningId(), totalSeats);

        System.out.println("상영 정보가 등록되었습니다.");
        System.out.println(screening);
    }

    private int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해주세요.");
            }
        }
    }

    private double readDouble(String message) {
        while (true) {
            try {
                System.out.print(message);
                double value = Double.parseDouble(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해주세요.");
            }
        }
    }
}