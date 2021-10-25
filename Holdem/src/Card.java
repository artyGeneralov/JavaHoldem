
public class Card {

	int value;
	CardColors color;
	
	
	public Card()
	{
		//empty constructor
		this.value = -1;
		this.color = CardColors.HEARTS;
	}
	
	
	public Card(int value, CardColors color)
	{
		this.value = value;
		this.color = color;
	}
	
	// Note : values are returned 1 to 13, but are saved as 0 to 12 in the value variable
	public int getValue()
	{
		return this.value + 1;
	}
	
	public CardColors getColor()
	{
		return this.color;
	}
	
	public String getFullCard()
	{
		String name = new String();
		switch(this.value + 1)
		{
			case 1:
				name = "Ace";
				break;
			case 11:
				name = "Jack";
				break;
			case 12:
				name = "Queen";
				break;
			case 13:
				name = "King";
				break;
			default:
				return (this.value+ 1) + " of " + this.color;
		}
		return name + " of " + this.color;
	}
}
