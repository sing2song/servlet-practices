package com.saltlux.mysite.vo;

public class BoardPaging {

	private int currentPage;//현재페이지
	private int pageBlock;//[이전][1][2][3][다음] => 5블럭
	private int pageSize;//1페이지당 5개씩
	private int totalB;//총글수(totalBrticle)
	private StringBuffer pagingHTML;//StringBuffer는 편집이 된다	
	
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageBlock() {
		return pageBlock;
	}

	public void setPageBlock(int pageBlock) {
		this.pageBlock = pageBlock;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalB() {
		return totalB;
	}

	public void setTotalB(int totalB) {
		this.totalB = totalB;
	}

	public StringBuffer getPagingHTML() {
		return pagingHTML;
	}

	public void setPagingHTML(StringBuffer pagingHTML) {
		this.pagingHTML = pagingHTML;
	}	
	
	@Override
	public String toString() {
		return "BoardPaging [currentPage=" + currentPage + ", pageBlock=" + pageBlock + ", pageSize=" + pageSize
				+ ", totalB=" + totalB + ", pagingHTML=" + pagingHTML + "]";
	}

	
	public void makePagingHTML() {
		
		pagingHTML=new StringBuffer();
		
		int totalP = (totalB+pageSize-1)/pageSize;//총 페이지 수
		int startPage = (currentPage-1)/pageBlock*pageBlock+1;
		int endPage = startPage+pageBlock-1;
		
		if(endPage>totalP) endPage=totalP;//총페이지보다 endPage가 크면 totalP를 덮어씌운다
		
		if(startPage>pageBlock)
			pagingHTML.append("<li><a href='/mysite02/board?p="+(startPage-1)+"'>◀</a></li>");
		
		for(int i=startPage; i<=endPage; i++) {
			if(i==currentPage)
				pagingHTML.append("<li class='selected'><a href='/mysite02/board?p="+i+"'>"+i+"</a></li>");
			else 
				pagingHTML.append("<li>				<a href='/mysite02/board?p="+i+"'>"+i+"</a></li>");
		}
		
		if(endPage<totalP)
			pagingHTML.append("<li><a href='/mysite02/board?p="+(endPage+1)+"'>▶</a></li>");
		
	}
	
	public void makeSearchPagingHTML() {
		pagingHTML=new StringBuffer();
		
		int totalP = (totalB+pageSize-1)/pageSize;//총 페이지 수
		int startPage = (currentPage-1)/pageBlock*pageBlock+1;
		int endPage = startPage+pageBlock-1;
		
		if(endPage>totalP) endPage=totalP;//총페이지보다 endPage가 크면 totalP를 덮어씌운다
		
		if(startPage>pageBlock)
			pagingHTML.append("[<span id='paging' onclick='boardSearchPaging("+(startPage-1)+")'>이전</span>]");
		
		for(int i=startPage; i<=endPage; i++) {
			if(i==currentPage)
				pagingHTML.append("[ <span id='currentPaging'	onclick='boardSearchPaging("+i+")'>"+i+"</span> ]");
			else 
				pagingHTML.append("[ <span id='paging'			onclick='boardSearchPaging("+i+")'>"+i+"</span> ]");
		}
		
		if(endPage<totalP)
			pagingHTML.append("[<span id='paging' onclick='boardSearchPaging("+(endPage+1)+")'>다음</span>]");
	}
	
	
}
