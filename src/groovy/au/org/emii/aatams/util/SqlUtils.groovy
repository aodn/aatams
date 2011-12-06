package au.org.emii.aatams.util

class SqlUtils 
{
	public static void appendIntegerParams(buff, params, paramNames)
	{
		paramNames.each
		{
			buff.append(params[it])
			buff.append(",")
		}
	}
	
	public static  void appendStringParams(buff, params, paramNames)
	{
		paramNames.each
		{
			if (params[it] != null)
			{
				buff.append("'")
				buff.append(params[it])
				buff.append("'")
			}
			else
			{
				buff.append(params[it])
			}
			buff.append(",")
		}
	}
	
	public static  void removeTrailingCommaAndAddBracket(buff)
	{
		buff.deleteCharAt(buff.length() - 1)
		buff.append(')')
	}
}
