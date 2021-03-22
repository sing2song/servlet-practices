package com.saltlux.mysite.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.saltlux.mysite.dao.BoardDao;
import com.saltlux.mysite.vo.BoardPaging;
import com.saltlux.mysite.vo.BoardVo;
import com.saltlux.mysite.vo.UserVo;
import com.saltlux.web.mvc.WebUtil;

public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		
		String action = request.getParameter("a");
		if("writeform".equals(action)) {
			HttpSession session = request.getSession();	
			UserVo authUser = (UserVo)session.getAttribute("authUser");	
			session.setAttribute("authUser", authUser);//로그인정보
			
			WebUtil.forward("/WEB-INF/views/board/writeform.jsp", request, response);
			
		}else if("write".equals(action)){
			//로그인확인 - Access Control(접근 제어)
			HttpSession session = request.getSession();	
			UserVo authUser = (UserVo)session.getAttribute("authUser");	
			
			String title = request.getParameter("title");
			String contents = request.getParameter("contents");

			BoardVo vo = new BoardVo();

			vo.setTitle(title);
			vo.setWriter(authUser.getName());
			vo.setEmail(authUser.getEmail());
			vo.setPassword(authUser.getPassword());
			vo.setContents(contents);

			System.out.println("[servlet - write]"+vo);
			new BoardDao().insert(vo);

			WebUtil.redirect(request.getContextPath() + "/board?p=1", request, response);
			
		}else if("look".equals(action)){
			//로그인확인 - Access Control(접근 제어)
			HttpSession session = request.getSession();	
			UserVo authUser = (UserVo)session.getAttribute("authUser");	
			
			String no = request.getParameter("no");
			BoardVo vo = new BoardDao().findByNo(no);
			
			//hit늘리기
			new BoardDao().updateHit(no);
			
			if(authUser!=null)
				session.setAttribute("authUser", authUser);//로그인정보
			request.setAttribute("vo", vo);
			request.setAttribute("no", no);
			
			WebUtil.forward("WEB-INF/views/board/look.jsp", request, response);
		
		}else if("updateform".equals(action)){
			
			String no = request.getParameter("no");
			BoardVo vo = new BoardDao().findByNo(no);
			
			request.setAttribute("vo", vo);
			request.setAttribute("no", no);
			
			WebUtil.forward("WEB-INF/views/board/updateform.jsp", request, response);
		
		}else if("update".equals(action)){//글수정
			
			String no = request.getParameter("no");
			String title = request.getParameter("title");
			String contents = request.getParameter("contents");
			
			new BoardDao().update(no,title,contents);
			
			
			WebUtil.redirect(request.getContextPath()+"/board?p=1", request, response);
			
		}else if("deleteform".equals(action)) {
			String no= request.getParameter("no");
			request.setAttribute("no",no);
			String fail = request.getParameter("fail");
			request.setAttribute("fail", fail);
			
			WebUtil.forward("/WEB-INF/views/board/deleteform.jsp", request, response);

		}else if("delete".equals(action)) {			

			String no = request.getParameter("no");
			String password = request.getParameter("password");
			BoardVo originVo = new BoardDao().findByNo(no);
			
			originVo.setNo(Long.parseLong(no));
			originVo.setPassword(password);
			
			if(new BoardDao().delete(originVo))	
				WebUtil.redirect(request.getContextPath() + "/board?p=1", request, response);
			else {
				//비밀번호 틀렸을때
				request.setAttribute("authResult", "fail");
				WebUtil.forward("/board?a=deleteform&no=" + no, request, response);
				return;					
			}
			
		}else if("replyfrom".equals(action)){
			//상위 no
			String no = request.getParameter("no");
			BoardVo vo = new BoardDao().findByNo(no);
			
			HttpSession session = request.getSession();	
			UserVo authUser = (UserVo)session.getAttribute("authUser");	
			session.setAttribute("authUser", authUser);//로그인정보
			request.setAttribute("vo", vo);//상위글정보
			
			WebUtil.forward("/WEB-INF/views/board/replyform.jsp", request, response);
			
		}else if("reply".equals(action)){
			//상위 no
			String g_no = request.getParameter("g_no");
			String o_no = request.getParameter("o_no");
			String depth = request.getParameter("depth");
			
			//로그인확인 - Access Control(접근 제어)
			HttpSession session = request.getSession();	
			UserVo authUser = (UserVo)session.getAttribute("authUser");	
			
			String title = request.getParameter("title");
			String contents = request.getParameter("contents");

			//원글 VO
			BoardVo originVo = new BoardVo();
			
			originVo.setG_no(g_no);
			originVo.setO_no(o_no);
			originVo.setDepth(depth);
			
			//답글 VO
			BoardVo vo = new BoardVo();

			vo.setTitle(title);
			vo.setWriter(authUser.getName());
			vo.setEmail(authUser.getEmail());
			vo.setPassword(authUser.getPassword());
			vo.setContents(contents);
			
			
			new BoardDao().reply(originVo,vo);

			WebUtil.redirect(request.getContextPath() + "/board?p=1", request, response);
			
		}else if("search".equals(action)){
			HttpSession session = request.getSession();	
			UserVo authUser = (UserVo)session.getAttribute("authUser");	
			
			String p="";
			if(request.getParameter("p")==null)
				p = 1+"";
			else
				p = request.getParameter("p");
			
			if(authUser==null) {//비로그인상태
				request.setAttribute("authResult", "fail");				
			}
			
			String search = request.getParameter("search");
			/*
			//검색페이징처리
			int endNum = Integer.parseInt(p) * 5;
			int startNum = endNum-4;
			
			int totalSearchB = new BoardDao().getTotalSearchB(startNum,endNum,search);
			BoardPaging boardPaging = new BoardPaging();
			
			boardPaging.setCurrentPage(pg);
			boardPaging.setPageBlock(3);
			boardPaging.setPageSize(5);
			boardPaging.setTotalB(totalSearchB);		
			boardPaging.makeSearchPagingHTML();
			*/
			//목록처리
			List<BoardVo> list = new BoardDao().findAll(p); 
			
			request.setAttribute("list", list);
			//request.setAttribute("boardPaging", boardPaging);
			session.setAttribute("authUser", authUser);//로그인정보
			
			WebUtil.forward("/WEB-INF/views/board/index.jsp", request, response);
			
		}else {//index들어가기
			System.out.println("action은 캐치했어?");
			//로그인확인 - Access Control(접근 제어)
			HttpSession session = request.getSession();	
			UserVo authUser = (UserVo)session.getAttribute("authUser");	
			String p="";
			if(request.getParameter("p")==null)
				p = 1+"";
			else
				p = request.getParameter("p");
			
			if(authUser==null) {//비로그인상태
				request.setAttribute("authResult", "fail");				
			}
			
			List<BoardVo> list = new BoardDao().findAll(p); 
			System.out.println(list);
			
			//페이징처리
			
			int totalB=new BoardDao().getTotalB();
			BoardPaging boardPaging = new BoardPaging();
			
			boardPaging.setCurrentPage(Integer.parseInt(p));
			boardPaging.setPageBlock(3);
			boardPaging.setPageSize(5);
			boardPaging.setTotalB(totalB);
			
			boardPaging.makePagingHTML();
			
			
			request.setAttribute("list", list);
			//request.setAttribute("p", 1);//디폴트 페이지
			request.setAttribute("boardPaging", boardPaging);
			session.setAttribute("authUser", authUser);//로그인정보
	
			WebUtil.forward("/WEB-INF/views/board/index.jsp", request, response);
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
