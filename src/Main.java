import java.util.ArrayList;
import java.util.*;

public class Main {
    static int[] constants = {10, 11, 13, 16, 20, 25, 31, 38, 46, 55, 65, 67};
    static int kind_of_Flush = -1;
    static Random rd = new Random();

    public static void main(String[] args) {
        Player[] players = {new Player("Yanon"), new Player("Mark")};
        play_a_single_game(players);
    }

    public static int calc_value_of_straight(Card[] sevenCardArray) {
        int len = 1;
        int max = sevenCardArray[sevenCardArray.length - 1].getValue();
        Card[] new_array = sevenCardArray;
        if (max == 14) {
            new_array = new Card[sevenCardArray.length + 1];
            new_array[0] = new Card(1, sevenCardArray[sevenCardArray.length - 1].getKind());
            System.arraycopy(sevenCardArray, 0, new_array, 1, sevenCardArray.length);
        }
        for (int i = new_array.length - 1; i > 0; i--) {
            if (new_array[i].getValue() - 1 == new_array[i - 1].getValue()) {
                len++;
                if (len == 5) {
                    return 99 + max;
                }
            } else if (new_array[i].getValue() != new_array[i - 1].getValue()) {
                len = 1;
                max = new_array[i - 1].getValue();
            }
        }
        return 0;
    }

    public static int calc_value_of_straight_flush(Card[] sevenCardArray) {
        if (kind_of_Flush == -1) {
            return 0;
        }
        Card[] new_array = sevenCardArray.clone();
        sort_by_kind(new_array);
        ArrayList<Card> to_check_for_straight = new ArrayList<>(0);
        for (Card card : new_array) {
            if (card.getKind() == kind_of_Flush) {
                to_check_for_straight.add(card);
            }
        }
        Card[] to_check_list = new Card[to_check_for_straight.size()];
        for (int j = 0; j < to_check_for_straight.size(); j++) {
            to_check_list[j] = to_check_for_straight.get(j);
        }
        kind_of_Flush = -1;
        int val = calc_value_of_straight(to_check_list);
        if (val == 0)
            return 0;
        return val + 187;
    }


    public static int calc_value_of_reps(Card[] sevenCardsArray, Player player) {
        int[] reps = new int[3];
        int[] values = new int[3];
        int current_rep_index = 0;
        boolean last_was_a_pair = false;
        for (int i = sevenCardsArray.length - 1; i > 0; i--) {
            if (sevenCardsArray[i].getValue() == sevenCardsArray[i - 1].getValue()) {
                last_was_a_pair = true;
                reps[current_rep_index]++;
                values[current_rep_index] = sevenCardsArray[i].getValue();
            } else if (last_was_a_pair) {
                last_was_a_pair = false;
                current_rep_index++;
            }
        }
        reps[0]++;
        reps[1]++;
        reps[2]++;
//        System.out.println(reps[0] + ": " + values[0] + '\n' + reps[1] + ": " + values[1] + '\n' + reps[2] + ": " + values[2] + '\n');
        if (reps[0] == 4) {
            player.amount_of_kickers = 1;
            player.forbidden_kickers.add(values[0]);
            return values[0] + 276; //four of a kind 0000 no kicker with the value values[0] 1 kicker
        }
        if (reps[2] <= 2) {
            if (reps[1] == 1) {
                if (reps[0] == 1)
                    return 0; //high card
                if (reps[0] == 2) {
                    player.amount_of_kickers = 3;
                    player.forbidden_kickers.add(values[0]);
                    return values[0] - 2; //pair 00 no kicker with the value values[0] 3 kickers
                }
                player.amount_of_kickers = 2;
                player.forbidden_kickers.add(values[0]);
                return values[0] + 89; //three of a kind 000 no kicker with value values[0] 2 kickers
            }
            if (reps[1] == 2) {
                if (reps[0] == 2) {
                    player.amount_of_kickers = 1;
                    player.forbidden_kickers.add(values[0]);
                    player.forbidden_kickers.add(values[1]);
                    return constants[values[0] - 3] + values[1]; //two pair 0011 no kickers with value values[0:1] 1 kicker
                }
                return 120 + 12 * (values[0] - 2) + values[1];//full house 00011
            }
            if (reps[1] == 3) {
                if (reps[0] == 2)
                    return 120 + 12 * (values[1] - 2) + values[0] - 1; //full house 11100
                return 120 + 12 * (values[0] - 2) + values[1]; //full house 00011
            }
            player.amount_of_kickers = 1;
            player.forbidden_kickers.add(values[1]);
            return values[1] + 276; //four of a kind 1111 no kicker with value values[1] 1 kicker
        }
        if (reps[0] == 3)
            return 120 + 12 * (values[0] - 2) + values[1]; //full house 00011
        if (reps[1] == 3)
            return 120 + 12 * (values[1] - 2) + values[0] - 1; //full house 11100
        return 120 + 12 * (values[2] - 2) + values[0] - 1; // full house 22200
    }

    public static int calc_flash(Card[] sevenCardArray) {
        int[] kind_rep_amount = new int[4];
        int[] kind_max = new int[4];
        for (int i = sevenCardArray.length - 1; i >= 0; i--) {
            kind_rep_amount[sevenCardArray[i].getKind()]++;
            if (kind_max[sevenCardArray[i].getKind()] == 0) {
                kind_max[sevenCardArray[i].getKind()] = sevenCardArray[i].getValue();
            }
        }
        for (int i = 0; i < kind_rep_amount.length; i++) {
            if (kind_rep_amount[i] >= 5) {
                kind_of_Flush = i;
                return kind_max[i] + 107;
            }
        }
        return 0;
    }

    public static double calc_value_of_high_card(Card[] sevenCardArray, Player player) {
        double to_return = 0;
        int k = 0;
        int index = 0;
        while (k<player.amount_of_kickers){
            if (!player.forbidden_kickers.contains(sevenCardArray[sevenCardArray.length - 1 - index].getValue())){
                to_return += (sevenCardArray[sevenCardArray.length - 1 - index].getValue() / Math.pow(10, 2 * (k + 1)));
                k++;
            }
            index++;
        }
        return to_return;
    }

    public static void sort_by_size(Card[] cards) {
        for (int k = 0; k < cards.length - 1; k++) {
            for (int i = 0; i < cards.length - 1; i++) {
                if (cards[i].getValue() > cards[i + 1].getValue()) {
                    Card temp = cards[i];
                    cards[i] = cards[i + 1];
                    cards[i + 1] = temp;
                }
            }
        }
    }

    public static void sort_by_kind(Card[] cards) {
        for (int k = 0; k < cards.length - 1; k++) {
            for (int i = 0; i < cards.length - 1; i++) {
                if (cards[i].getKind() > cards[i + 1].getKind()) {
                    Card temp = cards[i];
                    cards[i] = cards[i + 1];
                    cards[i + 1] = temp;
                }
            }
        }
    }

    public static void stable_sort(Card[] cards) {
        sort_by_kind(cards);
        sort_by_size(cards);
    }

    public static void calc_points_of_players(Player[] players, Card[] table) {
        Card[] sevenCardArray = new Card[7];
        for (Player player : players) {
            System.arraycopy(table, 0, sevenCardArray, 0, table.length);
            System.arraycopy(player.cards, 0, sevenCardArray, table.length, player.cards.length);
            stable_sort(sevenCardArray);
            player.points = Math.max(player.points, calc_value_of_reps(sevenCardArray, player));
            player.points = Math.max(player.points, calc_value_of_straight(sevenCardArray));
            player.points = Math.max(player.points, calc_flash(sevenCardArray));
            player.points = Math.max(player.points, calc_value_of_straight_flush(sevenCardArray));
            player.high_card_points = calc_value_of_high_card(sevenCardArray, player);
        }
    }

    public static Card pullCard(ArrayList<Card> deck) {
        int rand = rd.nextInt(deck.size());
        Card pulled = deck.get(rand);
        deck.remove(pulled);
        return pulled;
    }

    public static ArrayList<Card> generateDeck() {
        ArrayList<Card> deck = new ArrayList<>();
        for (int kind = 0; kind < 4; kind++) {
            for (int value = 2; value <= 14; value++) {
                deck.add(new Card(value, kind));
            }
        }
        return deck;
    }

    public static void deal_to_players(Player[] players, ArrayList<Card> deck) {
        for (int i = 0; i < 2; i++) {
            for (Player player : players) {
                player.cards[i] = pullCard(deck);
            }
        }
    }

    public static Card[] generate_table(ArrayList<Card> deck) {
        Card[] table = new Card[5];
        for (int i = 0; i < 5; i++) {
            table[i] = pullCard(deck);
        }
        return table;
    }

    public static void show_cards(Card[] c, int start_of_show, int end_of_show) {
        for (int i = start_of_show; i < end_of_show; i++) {
            System.out.println(c[i]);
        }

    }

    public static void show_your_hand(Player player) {
        System.out.println("you got:");
        System.out.println("--------------------");
        show_cards(player.cards, 0, 2);
        System.out.println("--------------------\n");
    }

    public static void declare_winner(Player[] players) {
        Player winner = players[0];
        boolean was_tie = false;
        for (int i = 1; i < players.length; i++) {
            Player player = players[i];
            if (player.points > winner.points || (player.points == winner.points && player.high_card_points > winner.high_card_points)) {
                winner = player;
                was_tie = false;
            } else if (player.points == winner.points && player.high_card_points == winner.high_card_points) {
                was_tie = true;
            }
        }
        if (!was_tie) {
            System.out.println("the winner is: " + winner);
            return;
        }
        System.out.println("TIE");
    }

    public static void play_a_single_game(Player[] players) {
        ArrayList<Card> deck = generateDeck();
        deal_to_players(players, deck);
        show_your_hand(players[0]);
        Card[] table = generate_table(deck);
        System.out.println("table is:\n--------------------");
        show_cards(table, 0, 3);
        show_cards(table, 3, 4);
        show_cards(table, 4, 5);
        System.out.println("--------------------");
        show_your_hand(players[1]);
        calc_points_of_players(players, table);
        declare_winner(players);
    }
}
