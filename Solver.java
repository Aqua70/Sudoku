
import java.util.*;
import java.awt.*;

public class Solver 
{
	private final int LENGTH = 9;

	private int puzzle[][] = new int[LENGTH][LENGTH];
	private StringBuffer possibilities[][] = new StringBuffer[LENGTH][LENGTH];
	private int amount = 0;
	static long startTime = 0;



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

Medium:""
			+ "608000300"
			+ "000201050"
			+ "000706840"
			+ "012000000"
			+ "040913020"
			+ "000000490"
			+ "035107000"
			+ "090308000"
			+ "001000904";

ans:
628459317
374281659
159736842
912564783
847913526
563872491
435197268
296348175
781625934

Hard:""
					+ "000100064"
					+ "450080000"
					+ "007000300"
					+ "600007003"
					+ "040236010"
					+ "500400002"
					+ "003000500"
					+ "000020047"
					+ "810009000"

382195764
456783129
197642358
621957483
748236915
539418672
273864591
965321847
814579236



	 */

	public Solver(String input)
	{
		this(input, true, 0, 0, 0, null, null);
	}

	public Solver(String input, boolean first, int xPos, int yPos, int amount, StringBuffer[][] possibilities, int[][] puzzle)
	{
		// Inserting sudoku into array
		if (first)
		{	
			startTime = System.currentTimeMillis();
			for (int i = 0; i < LENGTH; i++)
			{
				for(int j = 0; j < LENGTH; j++)
				{
					int character = Integer.parseInt(input.charAt(LENGTH*i + j)+"");
					this.puzzle[j][i] = character;
					if (character == 0)
					{
						this.amount++;
						this.possibilities[j][i] = new StringBuffer("123456789");
					}
				}
			}
			// Find possibilities of all grids
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
		else
		{
			this.puzzle = puzzle;
			this.possibilities = possibilities;
			this.amount = amount;
			checkSpecific(xPos, yPos, this.puzzle[xPos][yPos]);
		}

		if (this.amount == 0)
		{
			printPuzzle();
			System.out.println("That was easy!");
			System.out.println(System.currentTimeMillis() - startTime);
			System.exit(0);
		}
		else
		{
			for (int i = yPos; i < LENGTH; i++)
			{
				for(int j = xPos; j < LENGTH; j++)
				{
					if (this.puzzle[j][i] == 0)
					{
						recurs(j, i);
					}
				}
			}
		}
	}

	private void recurs(int xPos, int yPos)
	{
		amount--;
		char chararr[] = possibilities[xPos][yPos].toString().toCharArray();
		for (char character: chararr)
		{
			if (possibilities[xPos][yPos].length() != 0)
			{
				int numAddedToPuzzle = Integer.parseInt(character+"");
				puzzle[xPos][yPos] = numAddedToPuzzle;

				StringBuffer[][] copyb = new StringBuffer[LENGTH][LENGTH];
				copyArray(copyb, possibilities);

				int[][] copi = new int[LENGTH][LENGTH];
				copyArray(copi, puzzle);


				new Solver("", false, xPos, yPos, amount, copyb, copi);
			}
		}
	}

	private void printPuzzle()
	{
		for (int i = 0; i < LENGTH; i++)
		{
			for(int j = 0; j < LENGTH; j++)
			{
				System.out.print(puzzle[j][i]);
			}
			System.out.println();
		}
	}

	private void check(int xPos, int yPos)
	{
		if (amount == 0)
			return;

		checkBox(xPos, yPos);
		checkRow(yPos);
		checkColumn(xPos);
	}

	private void checkSpecific(int xPos, int yPos, int number)
	{
		if (amount == 0)
			return;

		checkSpecificBox(xPos, yPos, number);
		checkSpecificRow(yPos, number);
		checkSpecificColumn(xPos, number);

	}

	private void checkSpecificBox(int xPos, int yPos, int number)
	{
		int topLeftx = xPos;
		int topLefty = yPos;
		while (topLeftx % 3 != 0)
			topLeftx--;
		while (topLefty % 3 != 0)
			topLefty--;

		for (int i = topLefty; i < topLefty + 3; i++)
		{
			for (int j = topLeftx; j < topLeftx + 3; j++)
			{
				if (puzzle[j][i] == 0)
				{
					removePoss(j, i, number);
				}
			}
		}
	}

	private void checkSpecificColumn(int xPos, int number)
	{
		for (int i = 0; i < LENGTH; i++)
		{
			if (puzzle[xPos][i] == 0)
			{
				removePoss(xPos, i, number);
			}
		}
	}

	private void checkSpecificRow(int yPos, int number)
	{
		for (int j = 0; j < LENGTH; j++)
		{
			if (puzzle[j][yPos] == 0)
			{
				removePoss(j, yPos, number);
			}
		}


	}

	private void checkBox(int xPos, int yPos)
	{
		int topLeftx = xPos;
		int topLefty = yPos;
		while (topLeftx % 3 != 0)
			topLeftx--;
		while (topLefty % 3 != 0)
			topLefty--;
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
		int index = possibilities[xPos][yPos].indexOf(number + "");

		if (index != -1)
			possibilities[xPos][yPos].replace(index, index + 1, "");

		if (possibilities[xPos][yPos].toString().length() == 1)
		{
			int finalPoss = Integer.parseInt(possibilities[xPos][yPos].toString());
			puzzle[xPos][yPos] = finalPoss;


			possibilities[xPos][yPos] = new StringBuffer();
			amount--;

			if (amount <= 0)
				return;

			checkSpecific(xPos, yPos, finalPoss);

		}

		/*
		 * 		Here there is possible  2-2 pair implementation that could speed up solving
		 */
		//		else if (possibilities[xPos][yPos].toString().length() == 2)
		//		{
		//			for (int j = 0; j < LENGTH; j++)
		//			{
		//				if (puzzle[j][yPos] == 0 && possibilities[xPos][yPos].toString().compareTo(possibilities[j][yPos].toString()) == 0)
		//				{
		//					int number1 = Integer.parseInt(possibilities[xPos][yPos].toString()) / 10;
		//					int number2 = Integer.parseInt(possibilities[xPos][yPos].toString()) % 10;
		//					removeSpecificRow(yPos, number1);
		//					removeSpecificRow(yPos, number2);
		//					
		//					possibilities[xPos][yPos].append(number1).append(number2);
		//					possibilities[j][yPos].append(number1).append(number2);
		//					
		//				
		//					
		//				}
		//			}
		//		}
	}

	private void copyArray(StringBuffer[][] a, StringBuffer[][] b)
	{
		for (int i =0; i < LENGTH; i++)
		{
			for(int j = 0; j < LENGTH; j++)
			{
				if (b[j][i] != null)
				{
					a[j][i] = new StringBuffer(b[j][i]);
				}
			}
		}
	}

	private void copyArray(int[][] a, int[][] b)
	{
		for (int i =0; i < LENGTH; i++)
		{
			for(int j = 0; j < LENGTH; j++)
			{

				a[j][i] = b[j][i];

			}
		}
	}

}