package net.homelinux.mansierra.log;

import org.json.simple.JSONObject;

public class Out
{
	private static JSONObject obj = new JSONObject();

	public static String error(String text)
	{
		obj.put("type","ERROR");
		obj.put("text",text);
		return obj.toJSONString();
	}
	
	public static String info(String text)
	{
		obj.put("type","INFO");
		obj.put("text",text);
		return obj.toJSONString();
	}
	
	public static String warning(String text)
	{
		obj.put("type","WARNING");
		obj.put("text",text);
		return obj.toJSONString();
	}
}
