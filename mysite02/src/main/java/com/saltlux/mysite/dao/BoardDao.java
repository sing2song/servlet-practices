package com.saltlux.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.saltlux.mysite.vo.BoardVo;

public class BoardDao {
	private Connection getConnection() throws SQLException {
		//이 함수를 받는 곳에서 sqlException을 처리함으로 throw시킴
		Connection conn = null;
		
		try {
			//1. JDBC Driver 로딩
			Class.forName("com.mysql.cj.jdbc.Driver");

			//2. 연결하기
			String url ="jdbc:mysql://localhost:3306/webdb?characterEncoding=utf8&serverTimezone=UTC";
			conn =DriverManager.getConnection(url,"webdb","webdb");
			
		} catch (ClassNotFoundException e) {
			System.out.println("error-"+e);
		}

		return conn;
	}
	
	public List<BoardVo> findAll(String p){
		List<BoardVo> list = new ArrayList<>();
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;

		try {
			conn = getConnection();
			
			//3. SQL 준비
			String sql = "select R1.* FROM(select no, title, writer, email, password, hit ,date_format(regDate, '%Y-%m-%d %H:%i:%s'),depth from board  order by g_no desc, depth asc) R1 LIMIT 5 OFFSET ?";
			pstmt = conn.prepareStatement(sql);
			
			//4. 바인딩
			pstmt.setInt(1, 5*(Integer.parseInt(p)-1));
			
			//5. SQL문 실행
			rs = pstmt.executeQuery();

			//6. 데이터 가져오기
			while(rs.next()) {
				Long no = rs.getLong(1);
				String title = rs.getString(2);
				String writer = rs.getString(3);
				String email = rs.getString(4);
				String password = rs.getString(5);
				String hit = rs.getString(6);
				String regDate = rs.getString(7);
				String depth = rs.getString(8);

				BoardVo vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setWriter(writer);
				vo.setEmail(email);
				vo.setPassword(password);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setDepth(depth);
				
				list.add(vo);
			}

		}catch (SQLException  e) {
			System.out.println("error : "+e);
		}finally {
			try {
				if(rs==null) {
					rs.close();
				}
				if(pstmt!=null) {
					pstmt.close();//없어도 되지만 명시적으로 등록
				}
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		

		return list;
	}

	public BoardVo findByNo(String no) {
		BoardVo vo=null;
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			conn = getConnection();
			
			//3. SQL 준비
			String sql = "select title, email, contents, g_no, o_no, depth from board where no=?";
			pstmt = conn.prepareStatement(sql);

			//4. 바인딩
			pstmt.setString(1,no);
			
			//5. SQL문 실행
			rs = pstmt.executeQuery();

			//6. 데이터 가져오기
			if(rs.next()) {
				String title = rs.getString(1);
				String email = rs.getString(2);
				String contents = rs.getString(3);
				String g_no = rs.getString(4);
				String o_no = rs.getString(5);
				String depth = rs.getString(6);
				
				vo = new BoardVo();
				vo.setTitle(title);
				vo.setEmail(email);
				vo.setContents(contents);
				vo.setG_no(g_no);
				vo.setO_no(o_no);
				vo.setDepth(depth);
			}

		}catch (SQLException  e) {
			System.out.println("error : "+e);
		}finally {
			try {
				if(rs==null) {
					rs.close();
				}
				if(pstmt!=null) {
					pstmt.close();//없어도 되지만 명시적으로 등록
				}
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return vo;
	}

	//조회수 늘리기
	public boolean updateHit(String no) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			//3. SQL 준비
			String sql = "update board set hit=hit+1 where no=?";
			pstmt = conn.prepareStatement(sql);

			//4. 바인딩
			pstmt.setString(1,no);
			
			//5. SQL문 실행
			int count = pstmt.executeUpdate();

			//6. 결과
			result = count==1;//맞으면 true 아니면 false
			
		}catch (SQLException  e) {
			System.out.println("error : "+e);
		}finally {
			try {				
				if(pstmt!=null) {
					pstmt.close();//없어도 되지만 명시적으로 등록
				}
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		

		return result;
	}

	public boolean update(String no, String title, String contents) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			//3. SQL 준비
			String sql = "update board set title=?, contents=? where no=?";
			pstmt = conn.prepareStatement(sql);

			//4. 바인딩
			pstmt.setString(1,title);
			pstmt.setString(2, contents);
			pstmt.setString(3, no);
			
			//5. SQL문 실행
			int count = pstmt.executeUpdate();

			//6. 결과
			result = count==1;//맞으면 true 아니면 false
			
		}catch (SQLException  e) {
			System.out.println("error : "+e);
		}finally {
			try {				
				if(pstmt!=null) {
					pstmt.close();//없어도 되지만 명시적으로 등록
				}
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		

		return result;
		
	}

	public boolean insert(BoardVo vo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			//3. SQL 준비		
			String sql = "insert into board values(null,?,?,?,?,0,?,now(),(select max(g_no)+1 from board as b),1,0)";
			pstmt = conn.prepareStatement(sql);

			//4. 바인딩
			pstmt.setString(1,vo.getTitle());
			pstmt.setString(2,vo.getWriter());
			pstmt.setString(3,vo.getEmail());
			pstmt.setString(4,vo.getPassword());
			pstmt.setString(5,vo.getContents());
			
			//5. SQL문 실행
			int count = pstmt.executeUpdate();

			//6. 결과
			result = count==1;//맞으면 true 아니면 false
			
		}catch (SQLException  e) {
			System.out.println("error : "+e);
		}finally {
			try {				
				if(pstmt!=null) {
					pstmt.close();//없어도 되지만 명시적으로 등록
				}
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		

		return result;
	}
	
	
	public boolean delete(String no, String password, String g_no,String o_no,String depth) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null,pstmt2=null,pstmt3=null,pstmt4=null;
		int count=0,count2=0,count3=0,count4=0;
		try {
			conn = getConnection();
			
			//3. SQL 준비
			String sql="";
			if(Integer.parseInt(o_no)>1) {//답글이 있는 글
				sql = "update board set o_no=o_no-1 where g_no=?";
				pstmt = conn.prepareStatement(sql);
				
				//4. 바인딩
				pstmt.setString(1,g_no);
				
				if(Integer.parseInt(depth)==0) {
					System.out.println("depth는? "+depth);
					String sql2="update board set title='삭제' where no=?";
					pstmt2 = conn.prepareStatement(sql2);
					
					pstmt2.setString(1,no);
				}
				if(pstmt2==null) {//첫글이지워지면 depth변동X
					String sql3="update board set depth=depth-1 where g_no=? and depth>?";
					pstmt3=conn.prepareStatement(sql3);
					
					pstmt3.setString(1, g_no);
					pstmt3.setString(2, depth);
				}
			}
			
			
			sql = "delete from board where no=? and password=?";
			pstmt4 = conn.prepareStatement(sql);
			
			//4. 바인딩
			pstmt4.setString(1,no);
			pstmt4.setString(2, password);
			
			
			//5. SQL문 실행
			if(pstmt!=null)
				count = pstmt.executeUpdate();
			
			if(pstmt3!=null)
				count3 = pstmt3.executeUpdate();
			
			if(pstmt2!=null)
				count2= pstmt2.executeUpdate();
			else
				count4 = pstmt4.executeUpdate();
			
			System.out.println("count="+count+", count2="+count2+", count3="+count3+", count4="+count4);
			System.out.println(count|count2|count3);
			//6. 결과
			result = (count|count2|count3|count4)>=1;//하나라도 실행되면 ok
			
		}catch (SQLException  e) {
			System.out.println("error : "+e);
		}finally {
			try {				
				if(pstmt!=null) {
					pstmt.close();//없어도 되지만 명시적으로 등록
				}
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		

		return result;
	}

	public boolean reply(BoardVo originVo, BoardVo vo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null,pstmt2=null;
		
		try {
			conn = getConnection();
			
			//3. SQL 준비		
			String sql1 = "update board set o_no=o_no+1 where g_no=?";
			pstmt = conn.prepareStatement(sql1);
			
			pstmt.setString(1, originVo.getG_no());
			
			String sql2 = "insert into board values(null,?,?,?,?,0,?,now(),?,?,?)";
			pstmt2 = conn.prepareStatement(sql2);

			//4. 바인딩
			pstmt2.setString(1,vo.getTitle());
			pstmt2.setString(2,vo.getWriter());
			pstmt2.setString(3,vo.getEmail());
			pstmt2.setString(4,vo.getPassword());
			pstmt2.setString(5,vo.getContents());
			pstmt2.setString(6, originVo.getG_no());
			pstmt2.setString(7, (Integer.parseInt(originVo.getO_no())+1)+"");
			pstmt2.setString(8, (Integer.parseInt(originVo.getDepth())+1)+"");
			
			//5. SQL문 실행
			int count = pstmt.executeUpdate();
			int count2 = pstmt2.executeUpdate();
			
			//6. 결과
			result = count==1 && count2==1;//맞으면 true 아니면 false
			
		}catch (SQLException  e) {
			System.out.println("error : "+e);
		}finally {
			try {				
				if(pstmt!=null) {
					pstmt.close();//없어도 되지만 명시적으로 등록
				}
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		

		return result;
	}
	
	
}
