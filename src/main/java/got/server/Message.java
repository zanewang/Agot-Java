package got.server;

import got.client.ClientData;
import got.user.Account;

import java.io.Serializable;

public class Message implements Serializable {

    private Account account;

    private MessageType type;

    private String msg;

    private ClientData clientData;

    /**
     * @return the account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * @param account
     *            the account to set
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * @return the type
     */
    public MessageType getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(MessageType type) {
        this.type = type;
    }

    /**
     * @param msg
     *            the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param clientData the clientData to set
     */
    public void setClientData(ClientData clientData) {
        this.clientData = clientData;
    }

    /**
     * @return the clientData
     */
    public ClientData getClientData() {
        return clientData;
    }


}
