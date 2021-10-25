import java.util.Random;

public class Deck {
	final int NUMBER_OF_CARDS = 52;
	Card[] cards = new Card[NUMBER_OF_CARDS];
	
	public Deck()
	{
		createNewDeck();
	}

	public void createNewDeck() 
	{
		int mult = 0;
		for(CardColors color : CardColors.values())
		{
			for(int i = 0; i < NUMBER_OF_CARDS/4; i++)
				this.cards[i+mult] = new Card(i, color);
			mult+=13;
		}
	}
	
	@Override
	public String toString()
	{
		String s = "";
		int i = 1;
		for(Card c : this.cards)
		{
			if(c == null)
				break;
			s += i + ".) "+c.getFullCard() + "\n";
			i++;
		}
		return s;
	}
	
	public void shuffleDeck()
	{
		Card[] temp = new Card[NUMBER_OF_CARDS];
		Random rnd = new Random();
		int randNum;
		
		// copy the current cards array into a temporary array
		temp = copyDeck(this.cards);
		
		for(int i = 0; i < NUMBER_OF_CARDS; i++)
		{
			// 0 ---> NUMBER_OF_CARDS - 1
			do
				randNum = rnd.nextInt(NUMBER_OF_CARDS);
			while(temp[randNum] == null); // test to see if the card was already picked
			
			this.cards[i] = temp[randNum]; // insert into the main card array
			temp[randNum] = null; // remember the card was picked
		}
	}
	
	// private method, returns a copy of a card array d - for convenience
	private Card[] copyDeck(Card[] d)
	{
		Card[] temp = new Card[NUMBER_OF_CARDS];
		for(int i = 0; i < NUMBER_OF_CARDS; i++)
			temp[i] = d[i];
		return temp;	
	}
	
	public Card dealTopCard()
	{
		// c is the top card
		Card c = new Card();
		if(this.cards[0] != null)
			c = cards[0];
		
		// shift the deck array to the left and add null values in the end
		int i = 0;
		while(i < NUMBER_OF_CARDS - 1 && this.cards[i+1] != null)
			this.cards[i] = this.cards[++i];
		
		this.cards[i] = null;
		return c;
	}
	
	
	//Arranges an array of cards small to large and returns the array.
	public Card[] arrangeCards(Card[] theCards)
	{
		Card tempCard = new Card();
		int prevVal, nextVal;
		for(int i = 0; i < theCards.length - 1; i++)
		{
			for(int j = i; j < theCards.length; j++)
			{
				prevVal = theCards[i].getValue() == 1 ? 14 : theCards[i].getValue();
				nextVal = theCards[j].getValue() == 1 ? 14 : theCards[j].getValue();
				if(prevVal < nextVal)
				{
					tempCard = theCards[i];
					theCards[i] = theCards[j];
					theCards[j] = tempCard;
				}
			}
		}
		return theCards;
	}
}
