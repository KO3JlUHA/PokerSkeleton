public class Card {
    String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    String[] kinds = {"hearts", "spades", "diamonds", "cleavers"};
    public int kind;
    public int value;

    public Card(int value, int kind) {
        this.kind = kind;
        this.value = value;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return values[this.value - 1] + " of " + kinds[this.kind];
    }
}
