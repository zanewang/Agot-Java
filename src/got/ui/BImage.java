package got.ui;


import got.utility.Utility;

import java.awt.Image;


public class BImage {
    private IntRec rec;
    private Image image;
    private Boolean show;

    public BImage(IntRec rec) {
        this.rec = rec;
        this.show = false;
    }

    public BImage(IntRec rec, Image image) {
        this.rec = rec;
        this.image = image;
    }

    public IntRec getRec() {
        return rec;
    }

    public void setRec(IntRec rec) {
        this.rec = rec;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        double widthRatio = rec.getWidth() * 1.0 / image.getWidth(null);
        double heightRatio = rec.getHeight() * 1.0 / image.getHeight(null);
        this.image = Utility.sacleImage(image, widthRatio, heightRatio);
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

}

