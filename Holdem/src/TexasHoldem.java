import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
					new Card(12,CardColors.CLUBS),
					new Card(12, CardColors.SPADES),
					new Card(11, CardColors.SPADES),
					new Card(10, CardColors.SPADES),
					new Card(9, CardColors.SPADES),
					new Card(8, CardColors.SPADES),
					new Card(1, CardColors.HEARTS)
			};
			
			for(Card c : getHighestHand(testHand))
				System.out.println(c.getFullCard() + ",\n");
				
	}
	
	
	public static Card[] getHighestHand(Card[] table)
	{
		final int HAND_SIZE = 5;
		List<Card[]> possibleHands = generateCardCombinations(table, HAND_SIZE);
		Card[] highestHand = new Card[HAND_SIZE];
		int highestValue = 0;
		for(Card[] hand : possibleHands)
		{
			int currentHandVal = checkHand(hand).getValue();
			if(currentHandVal > highestValue)
			{
				highestValue = currentHandVal;
				highestHand = hand;
			}
			else
			{
				//TODO: set by high card
			}
		}
		return highestHand;
	}
	
	
	
	
	/*
	 * Function to create all possible combinations of r cards from a given cards array
	 * */
	private static List<Card[]> generateCardCombinations(Card[] cards, int r)
	{
		List<Card[]> combinations = new ArrayList<>();
		combMaker(combinations, cards, new Card[r], 0, cards.length - 1, 0);
		return combinations;
	}
	
	
	
	
	/*
	 * helper function to "generateCardCombinations" function
	 * 
	 * receives:
	 * 1.) ArrayList of Card object arrays
	 * 2.) an array of Card objects from which to generate combinations
	 * 3.) a Card object array as a placeholder for each combination (INITIALLY EMPTY)
	 * 4.) a start variable to indicate the current value to insert into temp array (INITIALLY 0!)
	 * 5.) end value to know that our start index doesn't go out of array bounds (INITIALLY cards.length -1 !)
	 * 6.) index value that tracks which index in temp is being altered (INITIALLY 0)
	 * 
	 * function is recursive and pushes each combination into the combinations List
	 * */
	private static void combMaker(List<Card[]> combinations, Card[] cards, Card[] temp, int start, int end, int index)
	{
		if(index == temp.length)
		{
			Card[] combination = temp.clone();
			combinations.add(combination);
		}
		else if(start <= end) //will simply end the function and return to the previous recursion level when start > end
		{
			temp[index] = cards[start];
			// push next element into next position
			combMaker(combinations,cards,temp,start+1,end,index+1);
			// push next element into current position
			combMaker(combinations,cards,temp,start+1,end,index);
		}
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
				//System.out.println("Royal Flush");
				return HandValues.ROYAL_FLUSH;
			}
			else
			{
				//System.out.println("Straight Flush");
				return HandValues.STRAIGHT_FLUSH;
			}
		}
		
		if(n_of_a_kind == 4)
		{
			//System.out.println("Four of a Kind");
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
				//System.out.println("Full House");
				return HandValues.FULL_HOUSE;
			}
			
			//check if straight or flush 
			if(hasStraight)
			{
				//System.out.println("Straight");
				return HandValues.STRAIGHT;
			}
			if(hasFlush)
			{
				//System.out.println("Flush");
				return HandValues.FLUSH;
			}
			
			//System.out.println("Three of a kind");
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
				//System.out.println("Full House");
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
				//System.out.println("Two pairs");
				return HandValues.TWO_PAIRS;
			}
			
			//System.out.println("Pair");
			return HandValues.PAIR;
		}
		//System.out.println("High Card");
		return HandValues.HIGH_CARD;		
	}
	
	
	/*
	 * Private helper functions -> calculate factorial, and m choose n.
	 */
	private int fact(int n)
	{
		if(n == 1)
			return 1;
		return n*n-1;
	}
	
	private int choose(int n, int m)
	{
		return fact(n) / (fact (m) * fact(n-m));
	}
}
