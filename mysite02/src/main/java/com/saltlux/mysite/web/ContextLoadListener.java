package com.saltlux.mysite.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoadListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent)  {
    	ServletContext context = servletContextEvent.getServletContext();
    	String contextConfigLoacation = context.getInitParameter("contextConfigLoacation");
    	
    	System.out.println("Application Starts...." + contextConfigLoacation);
    }

    public void contextDestroyed(ServletContextEvent arg0)  { 
    }

	
}
