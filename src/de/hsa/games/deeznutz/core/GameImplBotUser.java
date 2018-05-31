package de.hsa.games.deeznutz.core;

import de.hsa.games.deeznutz.Game;
import de.hsa.games.deeznutz.console.ConsoleUI;
import de.hsa.games.deeznutz.console.ScanException;
import de.hsa.games.deeznutz.entities.HandOperatedMasterSquirrel;
import de.hsa.games.deeznutz.entities.MasterSquirrelBot;

public class GameImplBotUser extends Game {
    private HandOperatedMasterSquirrel masterSquirrel;
    private MasterSquirrelBot mainMasterSquirrelBot;
    private String mainMasterSquirrelBotInfo;

    public GameImplBotUser(boolean threaded, BoardConfig boardConfig) {
        super(new State(boardConfig));
        super.threaded = threaded;
        ui = new ConsoleUI(state, threaded);

        // create and insert hand operated squirrel
        masterSquirrel = new HandOperatedMasterSquirrel(XYsupport.generateRandomLocation(state.getBoard().getConfig().getBoardSize(), state.getBoard().getEntities()));
        state.getBoard().insertMasterSquirrel(masterSquirrel);

        // create and insert bot
        mainMasterSquirrelBot = state.getBoard().createBot(boardConfig.getMainBotPath());
        state.getBoard().insert(mainMasterSquirrelBot);
        mainMasterSquirrelBotInfo = boardConfig.getMainBotPath();
    }

    @Override
    public void processInput() throws ScanException {
            MoveCommand moveCommand = ui.getCommand();
            masterSquirrel.setMoveCommand(moveCommand);
    }

    @Override
    public void render() {
        ui.render(state.flattenedBoard());
    }

    @Override
    public String message() {
        return "Player Energy: " + masterSquirrel.getEnergy() + "\nVS.\n" +
                mainMasterSquirrelBotInfo + " Energy: " + mainMasterSquirrelBot.getEnergy();
    }

}
