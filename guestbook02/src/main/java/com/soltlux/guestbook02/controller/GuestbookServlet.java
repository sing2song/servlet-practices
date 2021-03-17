package com.soltlux.guestbook02.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.soltlux.guestbook02.dao.Guestbook02Dao;
import com.soltlux.guestbook02.vo.Guestbook02Vo;
import com.soltlux.web.mvc.WebUtil;


public class GuestbookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("a");

		if("deleteform".equals(action)) {
			String no= request.getParameter("no");
			request.setAttribute("no",no);
			
			WebUtil.forward("/WEB-INF/views/deleteform.jsp", request, response);

		} else if("add".equals(action)) {
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String contents = request.getParameter("contents");

			Guestbook02Vo vo = new Guestbook02Vo();

			vo.setName(name);
			vo.setPassword(password);
			vo.setContents(contents);

			new Guestbook02Dao().insert(vo);

			WebUtil.redirect(request.getContextPath() + "/GuestbookServlet", request, response);
		}else if("delete".equals(action)) {
			request.setCharacterEncoding("utf-8");

			String no = request.getParameter("no");
			String password = request.getParameter("password");

			if(new Guestbook02Dao().delete(no,password))	
				WebUtil.redirect(request.getContextPath() + "/GuestbookServlet", request, response);
			else
				response.sendRedirect("/GuestbookServlet/deleteform.jsp?no="+no);
		}else {//index.jsp
			List<Guestbook02Vo> list = new Guestbook02Dao().findAll();

			// forwarding = request dispatch = request extension
			request.setAttribute("list", list);
			WebUtil.forward("/WEB-INF/views/index.jsp", request, response);		
			
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
