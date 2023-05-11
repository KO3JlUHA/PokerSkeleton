import java.util.ArrayList;
import java.util.Arrays;

public class Player {
    public int points = 0;
    public int high_card_points = 0;
    public Card[] cards = new Card[2];
    public String name;

    public int amount_of_kickers = 5;
    public ArrayList<Integer> forbidden_kickers = new ArrayList<>(0);
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
                ", high_card_points=" + high_card_points +
                ", cards=" + Arrays.toString(cards) +
                ", name='" + name + '\'' +
                ", amount_of_kickers=" + amount_of_kickers +
                ", forbidden_kickers=" + forbidden_kickers +
                '}';
    }
}
