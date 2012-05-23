package got.ui;


public class IntRec {
    private int posX;
    private int posY;
    private int width;
    private int height;
    
    public IntRec(int x, int y , int width ,int height){
        this.posX = x;
        this.posY = y;  
        this.width = width;
        this.height = height;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
}

