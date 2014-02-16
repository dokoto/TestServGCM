package net.homelinux.mansierra.serv;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.homelinux.mansierra.ddbb.DBManager;
import net.homelinux.mansierra.log.Out;

public class UnRegister extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter out = null;

		try
		{
			DBManager dbManager = (DBManager) getServletContext().getAttribute("DBManager");
			String key = request.getParameter("key");
			String AppName = request.getParameter("AppName");
			String User = request.getParameter("User");

			out = response.getWriter();
			if (key != null && AppName != null && !key.isEmpty() && !AppName.isEmpty() && User != null && !User.isEmpty())
			{
				String ApiKeyRegister = dbManager.query(key, AppName);
				if (ApiKeyRegister != null && !ApiKeyRegister.isEmpty())
				{
					dbManager.delete(key, ApiKeyRegister, AppName);
					out.print(Out.info("UnRegister successful"));

				} else
				{
					out.print(Out.error("Phone not found : " + key + " App : " + AppName + " User : " + User));
				}

			} else
			{
				StringBuilder erroBuff = new StringBuilder();
				if (key == null || key.isEmpty())
					erroBuff.append(" Key not found");
				else if (AppName == null || AppName.isEmpty())
					erroBuff.append(" AppName not found");
				else if (User == null || User.isEmpty())
					erroBuff.append(" User not found");

				out.print(Out.error("There are not enogth datas to send message," + erroBuff.toString()));
			}

		} catch (Exception e)
		{
			e.printStackTrace();
			out.print(Out.error("Server error: " + e.toString()));
		}
	}
}
