package comp1110.ass2.gui.Controls;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.HashMap;

// Author: Saquib Khan, influenced by third party.
public class ScreenController {
    // HashMap of name and their respective pane
    public final HashMap<String, Pane> screenMap = new HashMap<>();
    public final Scene main;
    public ScreenController(Scene main) {
        this.main = main;
    }
    public void addScreen(String name, Pane pane){
        screenMap.put(name, pane);
    }
    public void activate(String name){
        main.setRoot( screenMap.get(name) );
    }
}