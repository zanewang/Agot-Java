package got.ui.image;

import got.pojo.Action;
import got.pojo.Arm;
import got.pojo.MusterType;
import got.utility.Utility;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {

    private Map<String, Map<Arm, Image>> armyImages = new HashMap<String, Map<Arm, Image>>();
    private Map<String, Map<Arm, Image>> retreatArmyImages = new HashMap<String, Map<Arm, Image>>();
    private Map<Action, Image> actionImages = new HashMap<Action, Image>();
    private Map<MusterType, Image> musterImages = new HashMap<MusterType, Image>();
    private Map<String, Image> familyImages = new HashMap<String, Image>();
    private Map<String, Image> infoFamilyImages = new HashMap<String, Image>();
    private Image dialogImage;

    public void load() {
        String[] families = new String[] { "STARK", "LANNISTER", "BARATHEON", "TYREL", "GREYJOY" };
        for (String family : families) {
            Map<Arm, Image> images = new HashMap<Arm, Image>();
            Map<Arm,Image> retreatImages = new HashMap<Arm,Image>();
            for (Arm arm : Arm.values()) {
                Image image = Utility.loadImage("got/resource/arm/" + family + "_" + arm.toString() + ".png");
                images.put(arm,image );
                retreatImages.put(arm, Utility.rotateImage(image, 90.0));
            }
            armyImages.put(family, images);
            retreatArmyImages.put(family, retreatImages);
            Image familyImage = Utility.loadImage("got/resource/house/" + family + ".png");
            getFamilyImages().put(family, familyImage);
            getInfoFamilyImages().put(family, Utility.sacleImage(familyImage, 100.0 / 175, 100.0 / 175));
        }

        for (Action action : Action.values()) {
            getActionImages().put(action, Utility.loadImage("got/resource/action/" + action.toString() + ".png"));
        }

        for (MusterType type : MusterType.values()) {
            getMusterImages().put(type, Utility.loadImage("got/resource/muster/" + type.toString() + ".png"));
        }

        setDialogImage(Utility.loadImage("got/resource/dialog.png"));

    }

    public Map<String, Map<Arm, Image>> getArmyImages() {
        return armyImages;
    }

    public Map<Action, Image> getActionImages() {
        return actionImages;
    }

    public Map<MusterType, Image> getMusterImages() {
        return musterImages;
    }

    public Map<String, Image> getFamilyImages() {
        return familyImages;
    }

    public Image getDialogImage() {
        return dialogImage;
    }

    public void setDialogImage(Image dialogImage) {
        this.dialogImage = dialogImage;
    }

    public Map<String, Image> getInfoFamilyImages() {
        return infoFamilyImages;
    }

    public void setInfoFamilyImages(Map<String, Image> infoFamilyImages) {
        this.infoFamilyImages = infoFamilyImages;
    }

    public Map<String, Map<Arm, Image>> getRetreatArmyImages() {
        return retreatArmyImages;
    }
}
