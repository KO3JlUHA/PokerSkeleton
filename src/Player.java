import java.util.Arrays;

public class Player {
    public int points = 0;
    public double high_card = 0;
    public Card[] cards = new Card[2];
    public String name;

    public Player(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Player{" +
                "points=" + points +
                ", cards=" + Arrays.toString(cards) +
                ", name='" + name + '\'' +
                '}';
    }
}
