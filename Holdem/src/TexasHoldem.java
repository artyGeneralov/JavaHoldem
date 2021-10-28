import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TexasHoldem {
	/*
	 * In this class, Ace = 0, 
	 * 2 = 1 
	 * ... 
	 * King = 12
	 * */
	
	final static int TABLE_SIZE = 5;
	final static int FLOP_SIZE = 3;
	final static int TURN_SIZE = 1;
	final static int RIVER_SIZE = 1;
	final static int INITIAL_MONEY = 1000;
	
	// Cash should be seen globaly
	static int compCash = INITIAL_MONEY;
	static int playerCash = INITIAL_MONEY;
	static int bettingPool = 0;
	
	
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		Deck deck = new Deck();
		Player player1 = new Player();
		Player playerComp = new Player();
		char answer;
		System.out.println("Start a game? y/n");
		answer = input.next().charAt(0);
		while(answer == 'y')
		{
			gameLoop(deck, player1, playerComp);
			System.out.println("Another hand? y/n");
			answer = input.next().charAt(0);
			player1 = new Player();
			playerComp = new Player();
			deck.createNewDeck();
			deck.shuffleDeck();
		}
		
				
	}
	
	
	//The computers choice on how much to bet, and whether to keep the game going.
	public static int computerBet(Player computerPlayer, boolean isFirst, int playerBet)
	{
		int compDeterminationScore = 0;
		final int DETERMINATION_CHANGE_JUMP = 2;
		//float humanDeterminationScore;
		int answer = 1;
		
		// make a test for the first hand.
		if(isFirst)
		{
			boolean hasPair = true;
			boolean sameColor;
			int highestCard;
			Card[] compHand = computerPlayer.getHandArray();
			// check if he has a pair
			if(compHand[0].getValue() == compHand[1].getValue())
			{
				hasPair = false;
				// increase determination
				compDeterminationScore += DETERMINATION_CHANGE_JUMP;
			}
			
			if(!hasPair)
			{
				//check for same color
				if(compHand[0].getColor() == compHand[1].getColor())
				{
					//increase determination
					compDeterminationScore += DETERMINATION_CHANGE_JUMP;
				}
			}
			
			//check for highest card
			highestCard = Math.max(compHand[0].getValue(), compHand[1].getValue());
			
			// increase or decrease determination by the value of the highest card
			if(highestCard == 1 || highestCard >= 12)
			{
				//increase determination a lot
				compDeterminationScore += DETERMINATION_CHANGE_JUMP * 2;
			}
			else if(highestCard >= 2 && highestCard <= 4)
			{
				//decrease determination
				compDeterminationScore -= DETERMINATION_CHANGE_JUMP;
			}
			else if(highestCard >= 9 && highestCard <= 11)
			{
				//increase determination
				compDeterminationScore += DETERMINATION_CHANGE_JUMP;
			}
			
			/*
			 * if(highestCard >= 5 && highestCard <= 8)
			 *		//unchanged determination
			 */
			
		}
		else // if not the first hand
		{
			// set determination by hand value.
			compDeterminationScore = computerPlayer.getHighestHandValue();
		}
		
		Random rnd = new Random();
		int delta = rnd.nextInt(100);
		compDeterminationScore *= delta;
		if(compDeterminationScore <= 100)
		{
			int failRand = rnd.nextInt(10);
				if(failRand < 7)
					answer = 0;
				else
					answer = (int) (compCash * (compDeterminationScore / 100f));
		}
		else if(compDeterminationScore > 100 && compDeterminationScore <600)
		{
			int failRand = rnd.nextInt(10);
			if(failRand < 2)
				answer = 0;
			else
				answer = (int)(compCash *(compDeterminationScore / 100f) * (failRand / 10f));
		}
		else
		{
			int r = rnd.nextInt(5);
			answer = (int)(compCash * (compDeterminationScore / 100f) * (r/10f));
		}
		//set answer by determination, human bet, 
		return answer;
	}
	
	public static void gameLoop(Deck deck, Player humanPlayer, Player compPlayer)
	{
		int initialHumanCash = playerCash;
		int initialCompCash = compCash;
		Scanner input = new Scanner(System.in);
		deck.shuffleDeck();
		int playerChoice;
		Card[] table = new Card[TABLE_SIZE]; //5
		
		int compBet = -1;
		int tablePos = 0;
		humanPlayer.dealHand(deck);
		compPlayer.dealHand(deck);
		
		//init empty table
		for(int i = 0; i < TABLE_SIZE; i++)
			table[i] = null;
		
		// show the player his hand
		// ask whether he chooses to play or fold
		System.out.println("\n********************************\n");
		System.out.println("First Betting Phase: ");
		System.out.printf("Your money: %d, Computer money: %d\n", playerCash, compCash);
		System.out.printf("Your hand is: %s\n", humanPlayer.showHand());
		System.out.println("Proceed to bet or fold? ");
		
		playerChoice = evalPlayerChoice(input.next());
		
		if(playerChoice == 0)
		{
			System.out.println("You've folded");
			return;
		}
		playerCash -= playerChoice;
		bettingPool += playerChoice;
		
		// computer choice:
		
		compBet = computerBet(compPlayer, true, playerChoice);
		if(compBet == 0)
		{
			//computer folded
			System.out.println("Computer has folded");
			return;
		}
		compCash -= compBet;
		bettingPool += compBet;
		System.out.printf("Computer has bet: %d\n", compBet);
		
		// Flop
		System.out.println("\n********************************\n");
		System.out.println("Flop: ");
		//deal cards
		for(; tablePos < 3; tablePos++)
			table[tablePos] = deck.dealTopCard();
		
		System.out.println("Cards on table: ");
		for(int i = 0; i < table.length && table[i] != null; i++)
			System.out.print(table[i].getFullCard() + ", ");
		
		System.out.println("Your hand: ");
		System.out.println(humanPlayer.showHand());
		
		{
			Card[] temp = {humanPlayer.getHandArray()[0],humanPlayer.getHandArray()[1], table[0], table[1], table[2]};
			humanPlayer.setBestHand(getHighestHand(temp));
			humanPlayer.setHighestHandValue(checkHand(getHighestHand(temp)));
		}
		
		{
			Card[] temp = {compPlayer.getHandArray()[0],compPlayer.getHandArray()[1], table[0], table[1], table[2]};
			compPlayer.setBestHand(getHighestHand(temp));
			compPlayer.setHighestHandValue(checkHand(getHighestHand(temp)));
		}
		
												
		
		System.out.print("Your highest hand possible: ");
		for(Card c : humanPlayer.getBestHandArray())
			System.out.printf("%s, ", c.getFullCard());
		System.out.printf(" --> %s\n", humanPlayer.getHighestHandName());
		
		System.out.println("Proceed to bet or fold? ");
		playerChoice = evalPlayerChoice(input.next());
		
		if(playerChoice == 0)
		{
			System.out.printf("You've folded, Computer wins %d moneys", bettingPool);
			compCash += bettingPool;
			return;
		}
		playerCash -= playerChoice;
		bettingPool += playerChoice;
		
		//computer choice:
		
		compBet = computerBet(compPlayer, false, playerChoice);
		if(compBet == 0)
		{
			//computer folded
			System.out.printf("Computer has folded, you win %d moneys\n", bettingPool);
			playerCash += bettingPool;
			return;
		}
		compCash -= compBet;
		bettingPool += compBet;
		System.out.printf("Computer has bet: %d\n", compBet);
		
		// Turn
		System.out.println("\n********************************\n");
		System.out.println("Turn: ");
		//deal cards
		table[tablePos] = deck.dealTopCard();
		tablePos++;
		System.out.println("Cards on table: ");
		for(int i = 0; i < table.length && table[i] != null; i++)
			System.out.print(table[i].getFullCard() + ", ");
		
		System.out.println("Your hand: ");
		System.out.println(humanPlayer.showHand());
		
		{
			Card[] temp = {humanPlayer.getHandArray()[0],humanPlayer.getHandArray()[1], table[0], table[1], table[2], table[3]};
			humanPlayer.setBestHand(getHighestHand(temp));
			humanPlayer.setHighestHandValue(checkHand(getHighestHand(temp)));
		}
		
		{
			Card[] temp = {compPlayer.getHandArray()[0],compPlayer.getHandArray()[1], table[0], table[1], table[2], table[3]};
			compPlayer.setBestHand(getHighestHand(temp));
			compPlayer.setHighestHandValue(checkHand(getHighestHand(temp)));
		}
		
												
		
		System.out.print("Your highest hand possible: ");
		for(Card c : humanPlayer.getBestHandArray())
			System.out.printf("%s, ", c.getFullCard());
		System.out.printf(" --> %s\n", humanPlayer.getHighestHandName());
		System.out.println("Proceed to bet or fold? ");
		playerChoice = evalPlayerChoice(input.next());
		
		if(playerChoice == 0)
		{
			System.out.printf("You've folded, Computer wins %d moneys", bettingPool);
			compCash += bettingPool;
			return;
		}
		playerCash -= playerChoice;
		bettingPool += playerChoice;
		
		//computer choice:
		
		compBet = computerBet(compPlayer, false, playerChoice);
		if(compBet == 0)
		{
			//computer folded
			System.out.printf("Computer has folded, you win %d moneys\n", bettingPool);
			playerCash += bettingPool;
			return;
		}
		compCash -= compBet;
		bettingPool += compBet;
		System.out.printf("Computer has bet: %d\n", compBet);
		
		// River
		System.out.println("\n********************************\n");
		System.out.println("River: ");
		//deal cards
		table[tablePos] = deck.dealTopCard();
		
		System.out.println("Cards on table: ");
		for(int i = 0; i < table.length && table[i] != null; i++)
			System.out.print(table[i].getFullCard() + ", ");
		
		System.out.println("Your hand: ");
		System.out.println(humanPlayer.showHand());
		
		{
			Card[] temp = {humanPlayer.getHandArray()[0],humanPlayer.getHandArray()[1], table[0], table[1], table[2], table[3]
																								,table[4]};
			humanPlayer.setBestHand(getHighestHand(temp));
			humanPlayer.setHighestHandValue(checkHand(getHighestHand(temp)));
		}
		
		{
			Card[] temp = {compPlayer.getHandArray()[0],compPlayer.getHandArray()[1], table[0], table[1], table[2], table[3]
																								,table[4]};
			compPlayer.setBestHand(getHighestHand(temp));
			compPlayer.setHighestHandValue(checkHand(getHighestHand(temp)));
		}
												
		
		System.out.print("Your highest hand possible: ");
		for(Card c : humanPlayer.getBestHandArray())
			System.out.printf("%s, ", c.getFullCard());
		System.out.printf(" --> %s\n", humanPlayer.getHighestHandName());
		
		System.out.println("Proceed to bet or fold? ");
		playerChoice = evalPlayerChoice(input.next());
		
		if(playerChoice == 0)
		{
			System.out.printf("You've folded, Computer wins %d moneys", bettingPool);
			compCash += bettingPool;
			return;
		}
		playerCash -= playerChoice;
		bettingPool += playerChoice;
		
		//computer choice:
		
		compBet = computerBet(compPlayer, false, playerChoice);
		if(compBet == 0)
		{
			//computer folded
			System.out.printf("Computer has folded, you win %d moneys\n", bettingPool);
			playerCash += bettingPool;
			return;
		}
		compCash -= compBet;
		bettingPool += compBet;
		System.out.printf("Computer has bet: %d\n", compBet);
		
		
		//last evaluation

		if(compPlayer.getHighestHandValue() > humanPlayer.getHighestHandValue())
		{
			System.out.printf("Computer player wins %d moneys!\n",bettingPool);
			compCash += bettingPool;
		}
		else if(compPlayer.getHighestHandValue() < humanPlayer.getHighestHandValue())
		{
			System.out.printf("You win %d moneys!\n", bettingPool);
			playerCash += bettingPool;
		}
		else
		{
			System.out.println("It's a tie!, you both get your moneys back!");
			playerCash += initialHumanCash;
			compCash += initialCompCash;
		}
			
		return;
	}
	
	// returns the integer value of player input, or 0 if fold.
	private static int evalPlayerChoice(String choice)
	{
		if(choice.charAt(0) == '0' || choice == "fold" || choice == "Fold" || choice == "FOLD")
			return 0;
		return Integer.valueOf(choice);
	}
	
	// returns the higher hand between two players
	public static Card[] comparePlayerHands(Player player1, Player player2)
	{
		return player1.getHighestHandValue() > player2.getHighestHandValue() ?
					player1.getHandArray() 	
						: 
					player2.getHandArray();
	}
	
	
	
	public static Card[] getHighestHand(Card[] table)
	{
		final int HAND_SIZE = 5;
		List<Card[]> possibleHands = generateCardCombinations(table, HAND_SIZE);
		Card[] highestHand = new Card[HAND_SIZE];
		int highestValue = 0;
		int highestNumber = -1;
		Deck deck = new Deck();
		for(Card[] hand : possibleHands)
		{
			int currentHandVal = checkHand(hand).getValue();
			if(currentHandVal > highestValue)
			{
				highestValue = currentHandVal;
				highestHand = hand;
			}
			else if (currentHandVal == highestValue)
			{	
				hand = deck.arrangeCards(hand); 
				highestHand = deck.arrangeCards(highestHand);
				int handVal = hand[hand.length - 1].getValue() == 1 ? 14 : hand[hand.length - 1].getValue();
				if(handVal > highestNumber)
				{
					highestHand = hand;
					highestNumber = handVal;
				}
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
	
}
