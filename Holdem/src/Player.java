
public class Player {
	final int PLAYER_HAND_SIZE = 2;
	final int MAX_HAND_SIZE = 5;
	Card[] playerHand = new Card[PLAYER_HAND_SIZE];
	Card[] completeHand = new Card[MAX_HAND_SIZE];
	
	public Player()
	{
		// empty constructor
	}
	
	
	public void dealHand(Deck deck)
	{
		for(int i = 0; i < playerHand.length; i++)
			playerHand[i] = deck.dealTopCard();
	}
	
	public String showHand()
	{
		String s = "";
		for(Card c : playerHand)
			s += c.getFullCard() + "\n";
		return s;
	}
}
