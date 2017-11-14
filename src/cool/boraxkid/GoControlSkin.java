package cool.boraxkid;

import javafx.scene.control.SkinBase;

//class definition for a skin for the Go control
//NOTE: to keep JavaFX happy we dont use the skin here
class GoControlSkin extends SkinBase<GoControl> {
    // default constructor for the class
    public GoControlSkin(GoControl gc) {
        super(gc);
    }
}
