package com.saltlux.mysite.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.saltlux.mysite.dao.UserDao;
import com.saltlux.mysite.vo.UserVo;
import com.saltlux.web.mvc.WebUtil;


public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		
		String action = request.getParameter("a");
		
		if("joinform".equals(action)) {
			WebUtil.forward("/WEB-INF/views/user/joinform.jsp", request, response);
		
		}else if("joinsuccess".equals(action)) {			
			WebUtil.forward("/WEB-INF/views/user/joinsuccess.jsp", request, response);
			
		}else if("loginform".equals(action)) {
			WebUtil.forward("/WEB-INF/views/user/loginform.jsp", request, response);
		
		}else if("updateform".equals(action)) {
			//로그인확인 - Access Control(접근 제어)
			HttpSession session = request.getSession();			
			if(session==null) {
				WebUtil.redirect(request.getContextPath(), request, response);
				return;
			}
			
			UserVo authUser = (UserVo)session.getAttribute("authUser");			
			if(authUser==null) {
				WebUtil.redirect(request.getContextPath(), request, response);
				return;
			}
			
			Long no = authUser.getNo();
			UserVo userVo = new UserDao().findByNo(no);
			//UserVo userVo = new UserVo();
			
			request.setAttribute("userVo", userVo);
			
			WebUtil.forward("/WEB-INF/views/user/updateform.jsp", request, response);
		
		}else if("update".equals(action)){
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			
			HttpSession session = request.getSession();		
			UserVo authUser = (UserVo)session.getAttribute("authUser");	
			Long no = authUser.getNo();
			
			
			UserVo userVo = new UserVo();
			userVo.setNo(no);
			userVo.setName(name);
			userVo.setEmail(email);
			userVo.setPassword(password);
			userVo.setGender(gender);
			
			
			System.out.println("[update-servlet] "+userVo);
			
			new UserDao().update(userVo);
			authUser = new UserDao().findByEmailAndPassword(userVo);
			session.setAttribute("authUser", authUser);//바뀐 정보 헤더에 띄우기 위해서
			
			WebUtil.redirect(request.getContextPath(), request, response);
		
		}else if("logout".equals(action)) {
			HttpSession session = request.getSession();
			//로그인확인
			if(session==null) {
				WebUtil.redirect(request.getContextPath(), request, response);
				return;
			}
			
			UserVo authUser = (UserVo)session.getAttribute("authUser");			
			if(authUser==null) {
				WebUtil.redirect(request.getContextPath(), request, response);
				return;
			}
			//////////////////////////////////
			//로그아웃처리
			if(session!=null && session.getAttribute("authUser")!=null) {
				session.removeAttribute("authUser");
				session.invalidate();//세션아이디 바꾸기
			}
			WebUtil.redirect(request.getContextPath(), request, response);
		
		}else if("login".equals(action)) {
		
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			System.out.println("로그인한 정보"+email+":"+password);
			UserVo vo = new UserVo();
			vo.setEmail(email);
			vo.setPassword(password);
			
			UserVo authUser = new UserDao().findByEmailAndPassword(vo);
			
			
			if(authUser==null) {//로그인실패
				request.setAttribute("authResult", "fail");
				//WebUtil.redirect(request.getContextPath()+"/user?a=loginform", request, response);
				WebUtil.forward("/WEB-INF/views/user/loginform.jsp", request, response);
				return;	
			}
			
			authUser.setEmail(email);
			authUser.setPassword(password);
			
			//인증처리
			HttpSession session = request.getSession(true);
			session.setAttribute("authUser", authUser);
			
			//응답
			WebUtil.redirect(request.getContextPath(), request, response);
			
		}else if("join".equals(action)){
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			
			UserVo userVo = new UserVo();
			userVo.setName(name);
			userVo.setEmail(email);
			userVo.setPassword(password);
			userVo.setGender(gender);
			
			
			System.out.println(userVo);
			
			new UserDao().insert(userVo);
			
			WebUtil.redirect(request.getContextPath()+"/user?a=joinsuccess", request, response);
		
		}else {
			WebUtil.redirect(request.getContextPath(), request, response);
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
