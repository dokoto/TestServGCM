package net.homelinux.mansierra.serv;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class SendMsgToDevices extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static final String GOOGLE_SERVER_KEY = "AIzaSyDnyQIvfLYaYjQgF6Y712nzYBJYr-FdQZc";

	public SendMsgToDevices()
	{
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String regId = null;
		Result result = null;
		PrintWriter out = response.getWriter();
		try
		{
			regId = Register.getRegID();
			if (regId != null && !regId.isEmpty())
			{
				String userMessage = request.getParameter("message");
				Sender sender = new Sender(GOOGLE_SERVER_KEY);
				Message message = new Message.Builder().timeToLive(30).delayWhileIdle(true).addData("m", userMessage).build();
				result = sender.send(message, regId, 1);
				request.setAttribute("pushStatus", result.toString());
				String error = result.getErrorCodeName();

				out.println(error);
			}

		} catch (IOException ioe)
		{
			ioe.printStackTrace();
			out.println("RegId required: " + ioe.toString());
		} catch (Exception e)
		{
			e.printStackTrace();
			out.println(e.toString());
		}
	}
}
