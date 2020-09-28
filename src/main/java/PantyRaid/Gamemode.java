package PantyRaid;

public enum Gamemode {
    NORMAL("Normal"),
    HARDCORE("Hardcore"),
    SPRINT("Sprint"),
    SMITE("Smite"),
    BOUNTYHUNTER("Bountyhunter"),
    ZOMPOCALYPSE("Zompocalypse");

    private final String gamemodeName;

    Gamemode(String gamemodeName) {
        this.gamemodeName = gamemodeName;
    }

    public String getGamemodeName() {
        return gamemodeName;
    }
}
