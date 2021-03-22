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
System.out.println("여기까지 왔나?");
		try {
			conn = getConnection();
			
			//3. SQL 준비
			String sql = "select R1.* FROM(select no, title, writer, email, password, hit ,date_format(regDate, '%Y-%m-%d %H:%i:%s'),depth,flag from board  order by cast(g_no as unsigned) desc, cast(o_no as unsigned) asc, no asc, cast(depth as unsigned) asc) R1 LIMIT 5 OFFSET ?";
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
				String flag = rs.getString(9);
				
				BoardVo vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setWriter(writer);
				vo.setEmail(email);
				vo.setPassword(password);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setDepth(depth);
				vo.setFlag(flag);
				
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
			String sql="";
			List<BoardVo> list = new BoardDao().findAll("1");
			System.out.println(list);
			//첫글일 때
			if(list.isEmpty())
				sql ="insert into board values(null,?,?,?,?,0,?,now(),1,1,0,0)";
			//첫글이 아닐때	
			else 
				sql = "insert into board values(null,?,?,?,?,0,?,now(),(select max(cast(g_no as unsigned))+1 from board as b),1,0,0)";
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
	
	
	public boolean delete(BoardVo originVo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null,pstmt2=null;
		ResultSet rs = null;
		int count=0;
		try {
			conn = getConnection();
			System.out.println("[삭제 원본글] "+originVo);
			
			//3. SQL 준비
			//답글이있는글인가
			String sql="";
			sql="select * from board where g_no=? and no>?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, originVo.getG_no());
			pstmt.setLong(2, originVo.getNo());
			
			rs = pstmt.executeQuery();
			
			//rs.next()가 있으면 답글이 있다!			
			if(rs.next()) {
				String sql2="update board set flag=1 where no=?";
				pstmt2 = conn.prepareStatement(sql2);
				
				pstmt2.setLong(1, originVo.getNo());
				
			}else {//답글이없다! => 지운다!
			
				String sql2="delete from board where no=?";
				pstmt2 = conn.prepareStatement(sql2);
				
				pstmt2.setLong(1, originVo.getNo());
			}
			
			//5. SQL문 실행
						
			count= pstmt2.executeUpdate();
			
			
			
			//6. 결과
			result = count==1;//하나라도 실행되면 ok
			
		}catch (SQLException  e) {
			System.out.println("error : "+e);
		}finally {
			try {		
				if(rs!=null) {
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

		return result;
	}

	public boolean reply(BoardVo originVo, BoardVo vo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null,pstmt2=null;
		
		try {
			conn = getConnection();
			
			
			//3. SQL 준비		
			
			String sql2 = "insert into board values(null,?,?,?,?,0,?,now(),?,?,?,0)";
			pstmt2 = conn.prepareStatement(sql2);

			String sql1 = "update board set o_no=o_no+1 where g_no=? and o_no>=?";
			pstmt = conn.prepareStatement(sql1);			
			
			//4. 바인딩
			pstmt2.setString(1,vo.getTitle());
			pstmt2.setString(2,vo.getWriter());
			pstmt2.setString(3,vo.getEmail());
			pstmt2.setString(4,vo.getPassword());
			pstmt2.setString(5,vo.getContents());
			pstmt2.setString(6, originVo.getG_no());
			pstmt2.setString(7, (Integer.parseInt(originVo.getO_no())+1)+"");
			pstmt2.setString(8, (Integer.parseInt(originVo.getDepth())+1)+"");
			
			pstmt.setString(1, (Integer.parseInt(originVo.getO_no())+1)+"");
			pstmt.setString(2, originVo.getO_no());
			
			//5. SQL문 실행
			
			int count2 = pstmt2.executeUpdate();
			int count = pstmt.executeUpdate();
			
			
			
			//6. 결과
			result = count==1&count2==1;
			
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

	public int getTotalB() {
		int result=0;
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			conn = getConnection();
			
			//3. SQL 준비
			String sql = "select count(*) from board";
			pstmt = conn.prepareStatement(sql);

			//5. SQL문 실행
			rs = pstmt.executeQuery();

			//6. 데이터 가져오기
			if(rs.next()) {
				result=rs.getInt(1);				
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
		return result;
		
	}
	
	
}
