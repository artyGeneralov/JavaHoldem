import java.util.Scanner;

public class TexasHoldem {
	final static int TABLE_SIZE = 5;
	final static int FLOP_SIZE = 3;
	final static int TURN_SIZE = 1;
	final static int RIVER_SIZE = 1;
	
	
	
	static Card[] table = new Card[TABLE_SIZE];
	
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		Deck deck = new Deck();
		deck.shuffleDeck();
		Player player1 = new Player();
		Player playerComp = new Player();
		
		//Hands are dealt
		player1.dealHand(deck);
		playerComp.dealHand(deck);
		
		
		int current_table_pos;
		//Flop
		
		
			//Deal 3 cards to the table
			for(current_table_pos = 0; current_table_pos < FLOP_SIZE; current_table_pos++)
				table[current_table_pos] = deck.dealTopCard();
			
			
		//Turn
			//Deal 1 card to the table
			table[current_table_pos++] = deck.dealTopCard();
			
			
		//River
			//Deal 1 card to the table
			table[current_table_pos] = deck.dealTopCard();
			
			Card[] testHand = {
					new Card(1,CardColors.HEARTS),
					new Card(12, CardColors.SPADES),
					new Card(11, CardColors.CLUBS),
					new Card(2, CardColors.DIAMONDS),
					new Card(3, CardColors.HEARTS)
			};
			
			checkHand(testHand);
	}
	
	
	// State machine for the hands
	// Returns a HandValues enum value
	public static HandValues checkHand(Card[] hand)
	{
		Deck deck = new Deck();
		hand = deck.arrangeCards(hand);
		
		boolean hasFlush, hasStraight;
		int n_of_a_kind;
		
		int cardVal;
		hasFlush = true;
		hasStraight = true;
		n_of_a_kind = 1;
		int pairVal = 0;
		for(int i = 0; i < hand.length - 1; i++)
		{
			cardVal = hand[i].getValue() == 1 ? 14 : hand[i].getValue();
			int tempVal = hand[i+1].getValue() == 1 ? 14 : hand[i+1].getValue();
			CardColors tempCol = hand[i+1].getColor();
			if(tempCol != hand[i].getColor())
				hasFlush = false;
			if(tempVal != cardVal - 1)
				hasStraight = false;
			if(cardVal == tempVal && (pairVal == 0 || pairVal == cardVal))
			{
				pairVal = tempVal;
				n_of_a_kind++;
			}
		}
		
		// Royal Flush and Straight Flush
		if(hasFlush && hasStraight)
		{
			
			//check if royal
			boolean isRoyal = true;
			if(hand[hand.length - 1].getValue() == 1)
			{
				for(int i = hand.length - 2; i > 0; i--)
				{
					int curVal = hand[i].getValue() == 1 ? 14 : hand[i].getValue();
					int nextVal = hand[i-1].getValue() == 1 ? 14 : hand[i].getValue();
					if(curVal != nextVal + 1)
					{
						isRoyal = false;
						break;
					}	
				}
			}
			else
				isRoyal = false;
			
			if(isRoyal)
			{
				System.out.println("Royal Flush");
				return HandValues.ROYAL_FLUSH;
			}
			else
			{
				System.out.println("Straight Flush");
				return HandValues.STRAIGHT_FLUSH;
			}
		}
		
		if(n_of_a_kind == 4)
		{
			System.out.println("Four of a Kind");
			return HandValues.FOUR_OF_A_KIND;
		}
		
		
		if(n_of_a_kind == 3)
		{
			
			//check if full house
			int curValue = hand[0].getValue();
			int pairs = 1;
			for(int i = 1; i < hand.length; i++)
			{
				if(hand[i].getValue() != curValue)
				{
					pairs++;
					curValue = hand[i].getValue();
				}
			}
			if(pairs == 2)
			{
				System.out.println("Full House");
				return HandValues.FULL_HOUSE;
			}
			
			//check if straight or flush 
			if(hasStraight)
			{
				System.out.println("Straight");
				return HandValues.STRAIGHT;
			}
			if(hasFlush)
			{
				System.out.println("Flush");
				return HandValues.FLUSH;
			}
			
			System.out.println("Three of a kind");
			return HandValues.THREE_OF_A_KIND;
		}
			
		if(n_of_a_kind == 2)
		{
			//check if full house
			int curValue = hand[0].getValue();
			int pairs = 1;
			for(int i = 1; i < hand.length; i++)
			{
				if(hand[i].getValue() != curValue)
				{
					pairs++;
					curValue = hand[i].getValue();
				}
			}
			if(pairs == 2)
			{
				System.out.println("Full House");
				return HandValues.FULL_HOUSE;
			}
			
			//check if there is another pair
			curValue = hand[0].getValue();
			pairs = 0;
			for(int i = 1; i < hand.length; i++)
			{
				if(hand[i].getValue() == curValue)
					pairs++;
				else
					curValue = hand[i].getValue();
			}
			if(pairs == 2)
			{
				System.out.println("Two pairs");
				return HandValues.TWO_PAIRS;
			}
			
			System.out.println("Pair");
			return HandValues.PAIR;
		}
		System.out.println("High Card");
		return HandValues.HIGH_CARD;
	}
}
