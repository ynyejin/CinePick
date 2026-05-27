package cinepick.gui;

import cinepick.file.FileManager;
import cinepick.manager.MovieManager;
import cinepick.manager.ReservationManager;
import cinepick.manager.ScreeningManager;
import cinepick.manager.SeatManager;
import cinepick.manager.UserManager;

public class AppContext {
    public UserManager userManager;
    public MovieManager movieManager;
    public ScreeningManager screeningManager;
    public SeatManager seatManager;
    public ReservationManager reservationManager;
    public FileManager fileManager;

    public AppContext() {
        userManager = new UserManager();
        movieManager = new MovieManager();
        screeningManager = new ScreeningManager();
        seatManager = new SeatManager();
        reservationManager = new ReservationManager(seatManager, screeningManager);

        fileManager = new FileManager();
        fileManager.loadAll(userManager, movieManager, screeningManager, seatManager, reservationManager);
    }

    public void saveAll() {
        fileManager.saveAll(userManager, movieManager, screeningManager, seatManager, reservationManager);
    }
}