package com.saltlux.mysite.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saltlux.web.mvc.WebUtil;
import com.saltlux.mysite.dao.Guestbook02Dao;
import com.saltlux.mysite.vo.Guestbook02Vo;

public class GuestbookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("a");

		if("deleteform".equals(action)) {
			String no= request.getParameter("no");
			request.setAttribute("no",no);
			String fail = request.getParameter("fail");
			request.setAttribute("fail", fail);
			
			WebUtil.forward("/WEB-INF/views/guestbook/deleteform.jsp", request, response);

		} else if("add".equals(action)) {
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String contents = request.getParameter("contents");

			Guestbook02Vo vo = new Guestbook02Vo();

			vo.setName(name);
			vo.setPassword(password);
			vo.setContents(contents);

			new Guestbook02Dao().insert(vo);

			WebUtil.redirect(request.getContextPath() + "/guestbook", request, response);
		
		}else if("delete".equals(action)) {
			

			String no = request.getParameter("no");
			String password = request.getParameter("password");
			System.out.println("no:"+no+", password:"+password);
			if(new Guestbook02Dao().delete(no,password))	
				WebUtil.redirect(request.getContextPath() + "/guestbook", request, response);
			else {
				//비밀번호 틀렸을때
				request.setAttribute("authResult", "fail");
				WebUtil.forward("/guestbook?a=deleteform&no=" + no, request, response);
				return;	
				
			}
		}else if("list".equals(action)) {
			List<Guestbook02Vo> list = new Guestbook02Dao().findAll();

			// forwarding = request dispatch = request extension
			request.setAttribute("list", list);
			WebUtil.forward("/WEB-INF/views/guestbook/list.jsp", request, response);			
			
		}else {//guestbook/index.jsp - 방명록
			List<Guestbook02Vo> list = new Guestbook02Dao().findAll();

			// forwarding = request dispatch = request extension
			request.setAttribute("list", list);
			WebUtil.forward("/WEB-INF/views/guestbook/index.jsp", request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
