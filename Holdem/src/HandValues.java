
public enum HandValues {
	HIGH_CARD (1, "High Card"),
	PAIR (2, "Pair"),
	TWO_PAIRS (3, "Two Pairs"),
	THREE_OF_A_KIND (4, "Three of a Kind"),
	STRAIGHT (5, "Straight"),
	FLUSH (6, "Flush"),
	FULL_HOUSE (7, "Full House"),
	FOUR_OF_A_KIND (8, "Four of a Kind"),
	STRAIGHT_FLUSH (9, "Straight Flush"),
	ROYAL_FLUSH (10, "Royal Flush");
	
	private final int handValue;
	private final String handText;
	
	HandValues(int handValue, String handText)
	{
		this.handValue = handValue;
		this.handText = handText;
	}
	
	public int getValue()
	{
		return this.handValue;
	}
	
	public String getHandText()
	{
		return this.handText;
	}
	
}
