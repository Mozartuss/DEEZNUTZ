package GUI;

import console.UI;
import core.BoardView;
import core.GameImpl;
import core.MoveCommand;
import core.XY;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class FxUI extends Scene implements UI {

    private static final int CELL_SIZE = 20;
    private static int miniSuirrelEnergy = 200;
    private final Canvas boardCanvas;
    private final Label msgLabel;
    private GameImpl gameimpl;
    private MoveCommand command;

    private FxUI(Parent parent, Canvas boardCanvas, Label msgLabel) {
        super(parent);
        this.boardCanvas = boardCanvas;
        this.msgLabel = msgLabel;
    }

    public static FxUI createInstance(XY boardSize) {
        Canvas boardCanvas = new Canvas(boardSize.getX() * CELL_SIZE, boardSize.getY() * CELL_SIZE);
        Label statusLabel = new Label();
        VBox top = new VBox();
        top.getChildren().add(boardCanvas);
        top.getChildren().add(statusLabel);
        statusLabel.setText("Info Anzeige");
        final FxUI fxUI = new FxUI(top, boardCanvas, statusLabel);
        fxUI.setOnKeyPressed(
                keyEvent -> {
                    switch (keyEvent.getCode()) {
                        case W:
                        case UP:
                            fxUI.gameimpl.up();
                            break;
                        case D:
                        case RIGHT:
                            fxUI.gameimpl.right();
                            break;
                        case S:
                        case DOWN:
                            fxUI.gameimpl.down();
                            break;
                        case A:
                        case LEFT:
                            fxUI.gameimpl.left();
                            break;
                        case SPACE:
                            fxUI.gameimpl.masterEnergy();
                            break;
                        case Q:
                        case ESCAPE:
                            fxUI.gameimpl.exit();
                            break;
                        case X:
                            fxUI.gameimpl.all();
                            break;
                        case H:
                            fxUI.gameimpl.help();
                            break;
                    }
                }
        );
        return fxUI;
    }


    @Override
    public void render(final BoardView view) {
        Platform.runLater(() -> repaintBoardCanvas(view));
    }

    @Override
    public void multiThreadCommandProcess() {
    }


    public void setGameImpl(GameImpl game) {
        this.gameimpl = game;
    }

    private void repaintBoardCanvas(BoardView view) {
        GraphicsContext gc = boardCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, boardCanvas.getWidth(), boardCanvas.getHeight());
        XY viewSize = view.getSize();
        for (int x = 0; x < viewSize.getX(); x++) {
            for (int y = 0; y < viewSize.getY(); y++) {
                if (view.getEntityType(x, y) != null) {
                    switch (view.getEntityType(x, y)) {
                        case MASTER_SQUIRREL:
                            gc.setFill(Color.BLACK);
                            gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                            break;
                        case MINI_SQUIRREL:
                            gc.setFill(Color.BLACK);
                            gc.fillOval(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                            break;
                        case GOOD_PLANT:
                            gc.setFill(Color.FORESTGREEN);
                            gc.fillOval(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                            break;
                        case BAD_PLANT:
                            gc.setFill(Color.DARKGREEN);
                            gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                            break;
                        case BAD_BEAST:
                            gc.setFill(Color.SADDLEBROWN);
                            gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                            break;
                        case GOOD_BEAST:
                            gc.setFill(Color.ROSYBROWN);
                            gc.fillOval(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                            break;
                        case WALL:
                            gc.setFill(Color.GRAY);
                            gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                            break;
                    }
                }
            }
        }
    }

    public void message(final String msg) {
        Platform.runLater(() -> msgLabel.setText(msg));
    }

    @Override
    public MoveCommand getCommand() {
        if (command == null)
            return new MoveCommand(new XY(0, 0));
        else {
            MoveCommand temp = command;
            command = null;
            return temp;
        }
    }
}
