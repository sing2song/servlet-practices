package com.saltlux.guestbook.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.saltlux.guestbook.vo.Guestbook02Vo;

public class Guestbook02Dao {
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
	
	public List<Guestbook02Vo> findAll(){
		List<Guestbook02Vo> list = new ArrayList<>();
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;

		try {
			conn = getConnection();
			
			//3. SQL 준비
			String sql = "select no, name, contents,date_format(reg_date, '%Y년 %m월 %d일 %H시%i분%s초') from guestbook order by no desc";
			pstmt = conn.prepareStatement(sql);

			//4. 바인딩
			//여기선 지금 필요없음!!!

			//5. SQL문 실행
			rs = pstmt.executeQuery();

			//6. 데이터 가져오기
			while(rs.next()) {
				Long no = rs.getLong(1);
				String name = rs.getString(2);
				String contents = rs.getString(3);
				String reg_date = rs.getString(4);

				Guestbook02Vo vo = new Guestbook02Vo();
				vo.setNo(no);
				vo.setName(name);
				vo.setContents(contents);
				vo.setReg_date(reg_date);
				
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
	
	public boolean insert(Guestbook02Vo vo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		//test
		System.out.println(vo);
		try {
			conn = getConnection();
			
			//3. SQL 준비
			String sql = "insert into guestbook values (null,?,?,?,now())";
			pstmt = conn.prepareStatement(sql);

			//4. 바인딩
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getContents());
			
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

	
	public boolean delete(String no, String password) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = getConnection();
			
			//3. SQL 준비
			String sql = "delete from guestbook where no=? and password=?";
			pstmt = conn.prepareStatement(sql);

			//4. 바인딩
			pstmt.setString(1,no);
			pstmt.setString(2, password);
			
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
	
}
