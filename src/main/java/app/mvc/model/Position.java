package app.mvc.model;

public class Position {
    private int posX; 
    private int posY;
    public Position(int x,int y) {
        this.posX=x;
        this.posY=y;
    }
    public void modifierPosition(int x1,int y1) {
        this.posX=x1;
        this.posY=y1;
    }
    public int getPosX() {
        return posX;
    }
    public int getPosY() {
        return posY;
    }
    public void setPosX(int ligne){
        this.posX=ligne;
    }
    public void setPosY(int colonne){
        this.posY=colonne;
    }
}
