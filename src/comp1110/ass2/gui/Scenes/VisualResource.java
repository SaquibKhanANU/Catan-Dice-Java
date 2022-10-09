package comp1110.ass2.gui.Scenes;

import comp1110.ass2.CatanEnum.ResourceType;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import static comp1110.ass2.CatanEnum.ResourceType.ORE;

public class VisualResource{
    int x; // The x coordinate on the display
    int y; // The y coordinate on the display
    ResourceType type; // The type of resource

    // Construct the visual resource, specifying the x and y coordinates
    // of the image on the display
    public Image visualResource(ResourceType type, int x, int y) {
        Image image = null;
        if (type == ORE) {
            image = new Image("comp1110/ass2/assets/test.jpg");
            ImageView view = new ImageView();
            view.setFitWidth(196);
            view.setFitHeight(125);
            view.setX(x);
            view.setY(y);
            view.setImage(image);
        }
        return image;
    }
}
