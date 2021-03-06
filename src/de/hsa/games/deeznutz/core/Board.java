package de.hsa.games.deeznutz.core;

import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.entities.*;
import de.hsa.games.deeznutz.entities.Character;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Board {
    private final static Logger logger = Logger.getLogger(Launcher.class.getName());
    private BoardConfig boardConfig;
    private int gameTime;
    private ArrayList<Entity> entities;
    private ArrayList<MasterSquirrel> masters;

    public Board(BoardConfig boardConfig) {

        this.boardConfig = boardConfig;
        this.gameTime = boardConfig.getGameDuration();
        entities = new ArrayList<>();
        masters = new ArrayList<>();

        //fill upper and bottom border with walls
        for (int i = 0; i < boardConfig.getWidth(); i++) {
            insert(new Wall(new XY(i, 0)));
            insert(new Wall(new XY(i, boardConfig.getHeight() - 1)));
        }

        //fill left and right border with walls
        for (int i = 0; i < boardConfig.getHeight(); i++) {
            insert(new Wall(new XY(0, i)));
            insert(new Wall(new XY(boardConfig.getWidth() - 1, i)));
        }

        //and now some random walls inside the walled board
        for (int i = 0; i < boardConfig.getWallQuant(); i++) {
            insert(new Wall(XYsupport.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

        for (int i = 0; i < boardConfig.getBadBeastQuant(); i++) {
            insert(new BadBeast(XYsupport.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

        for (int i = 0; i < boardConfig.getGoodBeastQuant(); i++) {
            insert(new GoodBeast(XYsupport.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

        for (int i = 0; i < boardConfig.getBadPlantQuant(); i++) {
            insert(new BadPlant(XYsupport.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

        for (int i = 0; i < boardConfig.getGoodPlantQuant(); i++) {
            insert(new GoodPlant(XYsupport.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }
    }

    /**
     * Moving all entities inside of ArrayList.
     * Checking if entity is a character since only characters have a nextStep() implementation.
     */
    void update(EntityContext entityContext) {
        for (Entity entity: new ArrayList<>(entities))
            if (entity instanceof Character)
                ((Character) entity).nextStep(entityContext);
    }

    public void insert(Entity entity) {
        logger.fine("Entity ID: " + entity.getId() + " Energy: " + entity.getEnergy() + " Loc: " + entity.getLocation() + " inserted.");
        entities.add(entity);
    }

    void remove(Entity entity) {
        logger.fine("Entity ID: " + entity.getId() + " Energy: " + entity.getEnergy() + " Loc: " + entity.getLocation() + " removed.");
        entities.remove(entity);
    }

    /**
     * Generating an Entity Array from ArrayList.
     * Used by Board.class, FlattenedBoard.class, etc.
     *
     * @return the newly generated array instead of reference
     */
    public Entity[] getEntities() {
        Entity[] newArray = new Entity[entities.size()];
        int index = 0;
        for (Entity entity: entities)
            if (entity != null)
                newArray[index++] = entity;
        return newArray;
    }

    public BoardConfig getConfig() {
        return boardConfig;
    }

    public void insertMaster(MasterSquirrel masterSquirrel) {
        logger.finest("Inserting MasterSquirrel...");

        // insert in masters container
        masters.add(masterSquirrel);

        // insert in main container
        insert(masterSquirrel);
    }

    public MasterSquirrel getMainMasterSquirrel() {
        return masters.get(0);
    }

    public MasterSquirrel getSecondaryMasterSquirrel() {
        return masters.get(1);
    }

    public FlattenedBoard flatten() { //todo: wozu brauchen wir das?
        return new FlattenedBoard(this);
    }

    ArrayList<MasterSquirrel> getMasters() {
        return masters;
    }

    int getGameTime() {
        return gameTime;
    }

    void reducegameTime(){
        gameTime--;
    }

    public String printGameTime(){
        return "\n RemainingGameTime: " + getGameTime();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Entity entity: entities)
            s.append(entity).append("\n");
        return s.toString();
    }

}
