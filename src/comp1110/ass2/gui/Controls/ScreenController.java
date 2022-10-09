package comp1110.ass2.gui.Controls;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class ScreenController {
    public final HashMap<String, Pane> screenMap = new HashMap<>();
    public final Scene main;
    public ScreenController(Scene main) {
        this.main = main;
    }

    public void addScreen(String name, Pane pane){
        screenMap.put(name, pane);
    }

    protected void removeScreen(String name){
        screenMap.remove(name);
    }

    public void activate(String name){
        main.setRoot( screenMap.get(name) );
    }
}