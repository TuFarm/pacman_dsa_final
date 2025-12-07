package entity;

import java.awt.*;
import main.GamePanel;

public class Entity {
    public GamePanel gp;
    public int x, y;
    public int width, height;
    public Image img;

    public int startX, startY;

    public Entity(GamePanel gp, int x, int y, int width, int height) {
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startX = x;
        this.startY = y;
    }

    public void draw(Graphics g) {
        if (img != null) {
            g.drawImage(img, x, y, width, height, null);
        }
    }

    public void reset() {
        this.x = startX;
        this.y = startY;
    }

    // check for collision, return the bounded rectangle
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
