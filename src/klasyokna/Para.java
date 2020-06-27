package klasyokna;

//pożyczone z zajęć klasa
public class Para<T> {
    private T x;
    private T y;

    public Para(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }
}
