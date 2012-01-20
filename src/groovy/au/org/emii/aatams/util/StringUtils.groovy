package au.org.emii.aatams.util

class StringUtils 
{
	static String removeSurroundingBrackets(String listAsString)
	{
		if (listAsString == "[]")
		{
			return ""
		}
		
		return listAsString[1..listAsString.size() - 2]
	}

	static String removeSurroundingBrackets(List list)
	{
		if (list.isEmpty())
		{
			return ""
		}
		
		return removeSurroundingBrackets(String.valueOf(list))
	}
}
