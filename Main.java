
import java.util.*;
import java.awt.*;

public class Main 
{
	private final int LENGTH = 9;

	private int puzzle[][] = new int[LENGTH][LENGTH];
	private StringBuffer possibilities[][] = new StringBuffer[LENGTH][LENGTH];
	private int amount = 0;

	private String input = ""
			+ "500007604"
			+ "002840300"
			+ "040960015"
			+ "070001920"
			+ "020000060"
			+ "085300040"
			+ "150078030"
			+ "007039800"
			+ "804100006";
	
/*Easy:
	  = ""
			+ "500007604"
			+ "002840300"
			+ "040960015"
			+ "070001920"
			+ "020000060"
			+ "085300040"
			+ "150078030"
			+ "007039800"
			+ "804100006";
ans:			
593217684
612845379
748963215
476581923
321794568
985326147
159678432
267439851
834152796
	 */

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		new Main();
	}

	public Main()
	{
		// Inserting sudoku into array
		for (int i = 0; i < LENGTH; i++)
		{
			for(int j = 0; j < LENGTH; j++)
			{
				int character = Integer.parseInt(input.charAt(LENGTH*i + j)+"");
				puzzle[j][i] = character;
				if (character == 0)
				{
					amount++;
					possibilities[j][i] = new StringBuffer("123456789");
				}
			}
		}


		check(0, 0);
		check(3, 1);
		check(6, 2);
		check(1, 3);
		check(4, 4);
		check(7, 5);
		check(2, 6);
		check(5, 7);
		check(8, 8);


	}

	private void check(int xPos, int yPos)
	{
		if (amount <= 0)
			return;
		
		int topLeftx = xPos;
		int topLefty = yPos;
		while (topLeftx % 3 != 0)
			topLeftx--;
		while (topLefty % 3 != 0)
			topLefty--;

		checkBox(topLeftx, topLefty);
		checkRow(yPos);
		checkColumn(xPos);


		System.out.println(amount);
		printPuzzle(puzzle);



	}
	
	private void checkSpecific(int num)
	{

	}


	private void checkBox(int topLeftx, int topLefty)
	{
		ArrayList<Integer> tempNotPoss = new ArrayList<Integer>();

		// Finds all not possibilities inside 3x3
		for (int i = topLefty; i < topLefty + 3; i++)
		{
			for (int j = topLeftx; j < topLeftx + 3; j++)
			{
				if (puzzle[j][i] != 0)
					tempNotPoss.add(puzzle[j][i]);
			}
		}

		// Removes those possibilities from the potential list of possibilities of every square in the 3x3
		for (int i = topLefty; i < topLefty + 3; i++)
		{
			for (int j = topLeftx; j < topLeftx + 3; j++)
			{
				if (puzzle[j][i] == 0)
				{
					for (int number : tempNotPoss)
					{
						removePoss(j, i, number);
					}
				}
			}
		}

	}

	private void checkColumn(int xPos)
	{
		ArrayList<Integer> tempNotPoss = new ArrayList<Integer>();
		for (int i = 0; i < LENGTH; i++)
		{
			if (puzzle[xPos][i] != 0)
				tempNotPoss.add(puzzle[xPos][i]);
		}

		for (int i = 0; i < LENGTH; i++)
		{
			if (puzzle[xPos][i] == 0)
			{
				for (int number : tempNotPoss)
				{
					removePoss(xPos, i, number);
				}
			}
		}
	}
	


	private void checkRow(int yPos)
	{
		ArrayList<Integer> tempNotPoss = new ArrayList<Integer>();
		for (int j = 0; j < LENGTH; j++)
		{
			if (puzzle[j][yPos] != 0)
				tempNotPoss.add(puzzle[j][yPos]);
		}

		for (int j = 0; j < LENGTH; j++)
		{
			if (puzzle[j][yPos] == 0)
			{
				for (int number : tempNotPoss)
				{
					removePoss(j, yPos, number);
				}
			}
		}
	}
	
	private void removePoss(int xPos, int yPos, int number)
	{
		
	possibilities[xPos][yPos].replace(number-1, number, " ");
	if (possibilities[xPos][yPos].toString().trim().length() == 1)
	{
		int finalPoss = Integer.parseInt(possibilities[xPos][yPos].toString().trim());
		puzzle[xPos][yPos] = finalPoss;
		possibilities[xPos][yPos].replace(finalPoss - 1, finalPoss, " ");
		amount--;
		
		if (amount <= 0)
			return;
		
		check(xPos, yPos);
	}
	}
	private void printArray (int[] a)
	{
		for (int i = 0; i < a.length; i++)
			System.out.println(a[i]);
	}
	
	private void printPuzzle (int[][] a)
	{
		for (int i = 0; i < LENGTH; i++)
		{
			for(int j = 0; j < LENGTH; j++)
			{
				System.out.print(a[j][i]);
			}
			System.out.println();
		}
	}

}
