package gameObject;

/**
 * Klasa GameObject,m po ktorej dzidzicza wszystkie klasy obiektow gry.
 * @author Marek Gajdamowicz
 */
public class GameObject {

    private double x;
    private double y;

    /**
     * Konstruktor klasy
     * @param x polozenie obiektu wzgledem osi x
     * @param y polozenie obiektu wzgledem osi y
     */
    public GameObject(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
