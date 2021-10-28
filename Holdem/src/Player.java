
public class Player {
	final int PLAYER_HAND_SIZE = 2;
	final int MAX_HAND_SIZE = 5;
	private Card[] playerHand = new Card[PLAYER_HAND_SIZE];
	private Card[] bestHand = new Card[MAX_HAND_SIZE];
	private HandValues currentHighestHandValue;
	
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
	
	public Card[] getHandArray()
	{
		return this.playerHand;
	}
	
	public int getHighestHandValue()
	{
		return this.currentHighestHandValue.getValue();
	}
	
	public String getHighestHandName()
	{
		return this.currentHighestHandValue.getHandText();
	}
	
	public void setHighestHandValue(HandValues value)
	{
		this.currentHighestHandValue = value;
	}
	
	public void setBestHand(Card[] bestHand)
	{
		this.bestHand = bestHand.clone();
	}
	
	public Card[] getBestHandArray()
	{
		return this.bestHand;
	}
}
