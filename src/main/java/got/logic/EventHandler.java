package got.logic;

import got.io.Packet;

import org.jgroups.Address;


public interface EventHandler {

    void handler(Address address, Packet packet) throws Exception;
    
}
