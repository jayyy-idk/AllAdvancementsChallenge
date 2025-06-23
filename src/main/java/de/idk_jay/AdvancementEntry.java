package de.idk_jay;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SerializableAs("AdvancementEntry")
public class AdvancementEntry implements ConfigurationSerializable {

    private final String playerName;
    private final String advancementTitle;
    private final String gameTimestamp;
    private final Date realTimestamp;

    public AdvancementEntry(String playerName, String advancementTitle, String gameTimestamp, Date realTimestamp) {
        this.playerName = playerName;
        this.advancementTitle = advancementTitle;
        this.gameTimestamp = gameTimestamp;
        this.realTimestamp = realTimestamp;
    }

    public String getPlayerName() { return playerName; }
    public String getAdvancementTitle() { return advancementTitle; }
    public String getGameTimestamp() { return gameTimestamp; }
    public Date getRealTimestamp() { return realTimestamp; }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("playerName", this.playerName);
        data.put("advancementTitle", this.advancementTitle);
        data.put("gameTimestamp", this.gameTimestamp);
        data.put("realTimestamp", this.realTimestamp);
        return data;
    }

    public static AdvancementEntry deserialize(Map<String, Object> data) {
        String playerName = (String) data.get("playerName");
        String advancementTitle = (String) data.get("advancementTitle");
        String gameTimestamp = (String) data.get("gameTimestamp");
        Date realTimestamp = (Date) data.get("realTimestamp");
        return new AdvancementEntry(playerName, advancementTitle, gameTimestamp, realTimestamp);
    }
}