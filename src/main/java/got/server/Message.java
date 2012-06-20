package got.server;

import java.io.Serializable;

public class Message implements Serializable{

    private String msg;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

}
