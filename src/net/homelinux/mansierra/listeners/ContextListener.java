package net.homelinux.mansierra.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.homelinux.mansierra.ddbb.DBManager;

public class ContextListener implements ServletContextListener
{

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent)
	{
		try
		{
			ServletContext ctx = servletContextEvent.getServletContext();
			DBManager dbManager = (DBManager) ctx.getAttribute("DBManager");
			dbManager.closeAndUnRegister();
			
			System.out.println("Database connection Close and unRegistered for Application.");
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent)
	{
		DBManager persistence = null;
		try
		{
			ServletContext ctx = servletContextEvent.getServletContext();
			persistence = new DBManager(ctx);
			ctx.setAttribute("DBManager", persistence);

			System.out.println("Database connection initialized for Application.");

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
