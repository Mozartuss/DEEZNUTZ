import GUI.FxUI;
import console.ConsoleUI;
import console.ScanException;
import core.BoardConfig;
import core.MoveCommand;
import core.State;
import core.XY;
import entities.HandOperatedMasterSquirrel;
import entities.MasterSquirrel;

import java.io.IOException;


public class GameImpl extends Game {

    private MasterSquirrel masterSquirrel;


    GameImpl(boolean threaded, boolean gui) {
        super(new State(), threaded);
        if(gui) {
            FxUI fxUI = FxUI.createInstance(state.getBoard().getConfig().getBoardSize());

        } else
            ui = new ConsoleUI(state, threaded);

        masterSquirrel = new HandOperatedMasterSquirrel(XY.generateRandomLocation(state.getBoard().getConfig().getBoardSize(), state.getBoard().getEntities()));

        state.getBoard().insertMasterSquirrel(masterSquirrel);
    }


    @Override
    protected void processInput() throws IOException, ScanException {

        MoveCommand moveCommand;

        moveCommand = ui.getCommand();
        masterSquirrel.setMoveCommand(moveCommand);

    }

    @Override
    protected void render() {
        ui.render(state.flattenedBoard());
    }

}
