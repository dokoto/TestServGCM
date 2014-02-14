package net.homelinux.mansierra.serv;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Register extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static String RegID = null;

	public Register()
	{
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RegID = request.getParameter("shareRegId");
		PrintWriter out = response.getWriter();
		if (RegID != null && !RegID.isEmpty())
		{
			out.println("sharedRegId has registred");
		} else
		{
			out.println("sharedRegId is empty");
		}
	}

	public static synchronized String getRegID()
	{
		return RegID;
	}

}
