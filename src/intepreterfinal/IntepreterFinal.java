package intepreterfinal;

import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author Anish
 */
public class IntepreterFinal {

    public static Stack BoolExpression = new Stack();

	 /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
	{
            String Ans;
            do{
		boolean isValid = false;

		//Prompt user to enter a valid expression
                System.out.print('\n');
		while (!isValid)
		{
			int position = 0;
			String Exp = getExpression();
			Exp = cleanWhiteSpace(Exp);
			if (!Exp.equals("False"))
			{
			RefObject<Integer> tempRef_position = new RefObject<Integer>(position);
				isValid = B(Exp, tempRef_position);
				position = tempRef_position.argValue;
			}
			if (isValid)
			{
				System.out.print("The Solved Result of ");
				System.out.print(Exp);
				System.out.print(" is ");
				System.out.print(BoolExpression.peek());
				System.out.print("\n");
				BoolExpression.pop();
			}
		}
                //Prompts User if they want to continue or quit.
		System.out.println("Do You Want To Run Interpreter Again?? (Y/N)");
                Ans = new Scanner(System.in).nextLine();
            }while ("Y".equals(Ans));
	}
        
	//Gets the boolean expression from User
	public static String getExpression()
	{
		String Expression = "";
		System.out.print("Enter a boolean expression:");
		System.out.print("\n");
		System.out.print("T = True, F = False, ~ = Not, V = Or, ^ = And, -> = Imply");
		System.out.print("\n");
		Expression = new Scanner(System.in).nextLine();

		return Expression;
	}
        
//Cleans all the white spaces.
	public static String cleanWhiteSpace(String Ex)
	{
		int ExLength = Ex.length();
		//Checks if there is a white space between an implication or not.
		int found = Ex.indexOf("- >");
		if (found != -1)
		{
			ErrReport(Ex, ++found, " ['- >' is an invalid boolean opperator!!]");
			Ex = "False";
			return Ex;
		}
		else
		{
                    //Removes all the white spaces.
			Ex = Ex.replaceAll("\\s+","");
			return Ex;
		}
	}
        
//Error Report based on the incorrect expression occured.
	public static void ErrReport(String Ex, int position, String Function)
	{
		char a = (char) Ex.codePointAt(position-1);
		System.out.print("[Invalid Expression at character] ");
		System.out.print(": " + position  + " ==>> ");
		System.out.print("'" + a + "'");
		System.out.print("\n");
                System.out.print("ERROR: " + Function);
                System.out.print("\n");
                System.out.print("\n");
	}

//Checks if IT is true
	public static boolean B(String Ex, RefObject<Integer> position)
	{
		if (Ex.charAt(Ex.length() - 1) != '.')
		{
			ErrReport(Ex, Ex.length(), " [The Expression must end in a period (.)]");
			return false;
		}
		else if (IT(Ex, position))
		{
			++position.argValue;
			return true;
		}
		else
		{
			return false;
		}
	}

//Checks if OT is true sends the expression to check IT Tail or Imply Terms
	public static boolean IT(String Ex, RefObject<Integer> position)
	{
		if (OT(Ex, position))
		{
			if (IT_Tail(Ex, position))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}

//Checls for AT if true sends expression to OT Tail for or terms
	public static boolean OT(String Ex, RefObject<Integer> position)
	{
		if (AT(Ex, position))
		{
			if (OT_Tail(Ex, position))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}

	}

//Checks for L if true sends the expression to AT Tail for And Terms
	public static boolean AT(String Ex, RefObject<Integer> position)
	{
		if (L(Ex, position))
		{
			if (AT_Tail(Ex, position))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
//Check for the expression
	public static boolean IT_Tail(String Ex, RefObject<Integer> position)
	{
		if (Ex.charAt(position.argValue) == '-' && Ex.charAt(position.argValue + 1) == '>')
		{

			position.argValue += 2;
			if (OT(Ex, position))
			{
				if (IT_Tail(Ex, position))
				{
					//Evaluate the imply expression
					char rightEx = (char) BoolExpression.peek();
					BoolExpression.pop();
					char leftEx = (char) BoolExpression.peek();
					BoolExpression.pop();
					if (leftEx == 'F' || (leftEx == 'T' && rightEx == 'T'))
					{
						BoolExpression.push('T');
					}
					else
					{
						BoolExpression.push('F');
					}
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else if (Ex.charAt(position.argValue) == '.' || Ex.charAt(position.argValue) == ')')
		{
			return true;
		}
		else
		{
			return false;
		}
	}

//Checks for the expression
	public static boolean OT_Tail(String Ex, RefObject<Integer> position)
	{
		if (Ex.charAt(position.argValue) == 'V')
		{
			++position.argValue;

			if (AT(Ex, position))
			{
				if (OT_Tail(Ex, position))
				{
					//Evaluate or expression
					char rightEx = (char) BoolExpression.peek();
					BoolExpression.pop();
					char leftEx = (char) BoolExpression.peek();
					BoolExpression.pop();
					if (leftEx == 'F' && rightEx == 'F')
					{
						BoolExpression.push('F');
					}
					else
					{
						BoolExpression.push('T');
					}
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else if ((Ex.charAt(position.argValue) == '-' && Ex.charAt(position.argValue + 1) == '>') || Ex.charAt(position.argValue) == '.' || Ex.charAt(position.argValue) == ')')
		{
			return true;
		}
		else
		{
			return false;
		}
	}

//Checks for the expression
	public static boolean AT_Tail(String Ex, RefObject<Integer> position)
	{
		if (Ex.charAt(position.argValue) == '^')
		{
			++position.argValue;
			if (L(Ex, position))
			{
				if (AT_Tail(Ex, position))
				{
					//Evaluate and term
					char rightEx = (char) BoolExpression.peek();
					BoolExpression.pop();
					char leftEx = (char) BoolExpression.peek();
					BoolExpression.pop();
					if (leftEx == 'T' && rightEx == 'T')
					{
						BoolExpression.push('T');
					}
					else
					{
						BoolExpression.push('F');
					}
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		//check for seperators
		else if ((Ex.charAt(position.argValue) == '-' && Ex.charAt(position.argValue + 1) == '>') || Ex.charAt(position.argValue) == '.' || Ex.charAt(position.argValue) == ')' || Ex.charAt(position.argValue) == 'V')
		{
			return true;
		}
		else
		{
			ErrReport(Ex, ++position.argValue, "[This Character is not a valid boolean opperation]");
			return false;
		}
	}

//Checks for Literals in the expression
	public static boolean L(String Ex, RefObject<Integer> position)
	{
                if (A(Ex, position))
		{
			return true;
		}
                
                else if (Ex.charAt(position.argValue) == '~')
		{
			//Parsing through NOT of the Literals.
			++position.argValue;
			if (Ex.charAt(position.argValue) == 'T')
			{
				Ex = StrFunctions.CharChange(Ex, position.argValue, 'F');
			}
			else
			{
				Ex = StrFunctions.CharChange(Ex, position.argValue, 'T');
			}
			L(Ex, position);
			return true;
		}                
		else
		{
			return false;
		}
	}

//Checks if expression is an atom or not.
	public static boolean A(String Ex, RefObject<Integer> position)
	{
		if (Ex.charAt(position.argValue) == 'T' || Ex.charAt(position.argValue) == 'F')
		{
			BoolExpression.push(Ex.charAt(position.argValue));
			++position.argValue;
			return true;
		}
		else if (Ex.charAt(position.argValue) == '(')
		{
                        ++position.argValue;
			IT(Ex, position);
			return true;
		}
		else
		{
			ErrReport(Ex, ++position.argValue, " [This Character is not a valid Atom.]");
			return false;
		}
	}
}
