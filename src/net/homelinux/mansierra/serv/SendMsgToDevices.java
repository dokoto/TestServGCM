package net.homelinux.mansierra.serv;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.homelinux.mansierra.ddbb.DBManager;
import net.homelinux.mansierra.log.Out;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class SendMsgToDevices extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static final String GOOGLE_SERVER_KEY = "AIzaSyDnyQIvfLYaYjQgF6Y712nzYBJYr-FdQZc";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter out = null;
		Result result = null;

		try
		{
			DBManager dbManager = (DBManager) getServletContext().getAttribute("DBManager");
			String key = request.getParameter("key");
			String typeMsg = request.getParameter("typeMsg");
			String data = request.getParameter("data");
			String User = request.getParameter("user");
			String AppName = request.getParameter("appName");

			out = response.getWriter();
			if (key != null && typeMsg != null && !key.isEmpty() && !typeMsg.isEmpty() && User != null && !User.isEmpty())
			{
				String ApiKeyRegister = dbManager.query(key, AppName, User);
				if (ApiKeyRegister != null && !ApiKeyRegister.isEmpty())
				{
					Sender sender = new Sender(GOOGLE_SERVER_KEY);
					Message message = new Message.Builder().timeToLive(30).delayWhileIdle(true).addData("m", data).build();
					result = sender.send(message, ApiKeyRegister, 1);
					request.setAttribute("pushStatus", result.toString());
					String error = result.getErrorCodeName();
					if (error != null && !error.isEmpty())
					{
						out.print(Out.error(error));
					} else
					{
						out.print(Out.info("Message sent successful"));
					}
				} else
				{
					out.print(Out.error("No registed phone found, for : " + key + " App : " + AppName));
				}

			} else
			{
				StringBuilder erroBuff = new StringBuilder();
				if (key.isEmpty())
					erroBuff.append(" Key not found");
				else if (AppName.isEmpty())
					erroBuff.append(" AppName not found");
				else if (data.isEmpty())
					erroBuff.append(" data not found");
				else if (User.isEmpty())
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
