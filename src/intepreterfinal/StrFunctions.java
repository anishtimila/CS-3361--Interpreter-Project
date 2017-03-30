package intepreterfinal;

/**
 *
 * @author Anish
 */
public final class StrFunctions
{
	//	This method allows replacing a single character in a string with another.
	
	public static String CharChange(String srcStr, int charIndex, char changeChar)
	{
		return (charIndex > 0 ? srcStr.substring(0, charIndex) : "")
			+ Character.toString(changeChar) + (charIndex < srcStr.length() - 1 ? srcStr.substring(charIndex + 1) : "");
	}
}