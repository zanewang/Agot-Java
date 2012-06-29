package got.logic;

import got.server.Message;


public interface EventHandler {

    void handler(Message message) throws Exception;
    
}
