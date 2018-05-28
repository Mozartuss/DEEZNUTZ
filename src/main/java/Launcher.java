import GUI.FxUI;
import Music.BackgroundMusic;
import console.ScanException;
import core.BoardConfig;
import core.Game;
import core.GameImpl;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.*;


public class Launcher extends Application {
    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final Level logLevel = Level.INFO;
    private static Launcher launcher = new Launcher();
    private Scanner scanner = new Scanner(System.in);

    private static void startGameMultiThreaded(Game game) throws ScanException {
        logger.log(Level.INFO, "Start Game MultiThreaded");
        System.out.println("multi");
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    game.processInput();
                    game.run();
                } catch (ScanException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, 0, 1);
        game.ui.multiThreadCommandProcess();
    }

    public static void main(String[] args) throws Exception {
        Handler handler = (new FileHandler("Log.txt"));
        logger.setLevel(logLevel);
        SimpleFormatter simpleFormatter = new SimpleFormatter();
        handler.setFormatter(simpleFormatter);
        handler.setLevel(Level.WARNING);
        logger.addHandler(handler);

        if (args.length >= 1)
            if (args[0].equalsIgnoreCase("multi")) {
                startGameMultiThreaded(new GameImpl(true));
                return;
            }
        launcher.menu(args, launcher);
    }

    private static void startGameSingleThreaded(Game game) throws ScanException {
        logger.log(Level.INFO, "Start Game SingleThreaded");
        System.out.println("single");
        game.run();
    }

    private void menu(String[] args, Launcher launcher) {
        System.out.println("Wählen sie einen Spielmodus: [1] Spiel auf der Konsole [2] Spiel mit GUI [3] Verlassen");
        switch (scanner.nextInt()) {
            case 1:
                logger.log(Level.INFO, "Game type: Console");
                launcher.gameMode(args);
                break;
            case 2:
                logger.log(Level.INFO, "Game type: in Gui");
                launcher.startGUIGame(args);
                break;
            case 3:
                System.exit(0);
        }
    }

    private void gameMode(String[] args) {
        System.out.println("Wählen sie zwischen den Spielmodi: [1] Multithreaded [2] Siglethreaded [3] Verlassen ");
        switch (scanner.nextInt()) {
            case 1:
                logger.log(Level.INFO, "Game type: Console + MultiThreaded");
                startGameSingleThreaded(new GameImpl(true));
                break;
            case 2:
                logger.log(Level.INFO, "Game type: Console + SingleThreaded");
                startGameSingleThreaded(new GameImpl(false));
                break;
            case 3:
                System.exit(0);
        }
    }

    private void startGUIGame(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws ScanException {
        logger.log(Level.INFO, "Start Gui Game");
        BoardConfig boardConfig = new BoardConfig();
        FxUI fxUI = FxUI.createInstance(boardConfig.getBoardSize());
        final Game game = new GameImpl(true);
        BackgroundMusic.backgroundMusic.loop();
        game.setUi(fxUI);
        fxUI.setGameImpl((GameImpl) game);
        primaryStage.setScene(fxUI);
        primaryStage.setTitle("DEEZNUTZ");
        primaryStage.setAlwaysOnTop(false);
        fxUI.getWindow().setOnCloseRequest(evt -> System.exit(-1));
        primaryStage.show();
        startGameMultiThreaded(game);
    }
}