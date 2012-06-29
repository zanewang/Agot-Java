package got.ui.update;

import java.util.ArrayList;
import java.util.List;

public class GameData {
    private List<Observer> observers = new ArrayList<Observer>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void updateAll() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
