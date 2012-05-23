package got.logic;



import java.net.URL;

import org.apache.log4j.PropertyConfigurator;

public class LogInitiator {
    public void init() {
        URL url = this.getClass().getResource("log4j.properties");
        PropertyConfigurator.configure(url);
    }

}