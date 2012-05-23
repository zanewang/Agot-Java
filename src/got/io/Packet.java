package got.io;

import got.pojo.event.GameStatus;

import java.io.Serializable;

public class Packet implements Serializable {

    private MessageType type;
    private String pName;
    private GameStatus gameStatus;
    private String info;
    private boolean isReply = false;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void setReply(boolean isReply) {
        this.isReply = isReply;
    }

    public boolean isReply() {
        return isReply;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

}
