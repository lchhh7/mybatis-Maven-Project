<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.util.UUID"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.io.File"%>
<%@page import="org.joda.time.LocalDate"%>
<%!
	final static String PHOTO_UPLOAD_ROOT_TEST = "D:/eclipse-workspace/jintranet/webroot/common/files/notice/";

	public static String getUploadRoot() {
		return PHOTO_UPLOAD_ROOT_TEST;
	}

%>
<%
	String CONTEXT_PATH = request.getContextPath();
	LocalDate now = LocalDate.now();
	String detailPath = "/" + now.getMonthOfYear() + "/" + now.getDayOfMonth() + "/";

	//파일정보
	String sFileInfo = "";
	//파일명을 받는다 - 일반 원본파일명
	String filename = request.getHeader("file-name");
	//파일 확장자
	String filename_ext = filename.substring(filename.lastIndexOf(".")+1);
	//확장자를소문자로 변경
	filename_ext = filename_ext.toLowerCase();
	 
	//이미지 검증 배열변수
	String[] allow_file = {"jpg","png","bmp","gif"};

	//돌리면서 확장자가 이미지인지 
	int cnt = 0;
	for(int i=0; i<allow_file.length; i++) {
		if(filename_ext.equals(allow_file[i])){
			cnt++;
		}
	}

	//이미지가 아님
	if(cnt == 0) {
		out.println("NOTALLOW_"+filename);
	} else {
		String filePath = getUploadRoot() + detailPath;

		File file = new File(filePath);
		if(!file.exists()) {
			file.mkdirs();
		}

		String realFileNm = "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String today= formatter.format(new java.util.Date());
		realFileNm = today+UUID.randomUUID().toString() + filename.substring(filename.lastIndexOf("."));
		String rlFileNm = filePath + realFileNm;
		///////////////// 서버에 파일쓰기 ///////////////// 
		InputStream is = request.getInputStream();
		OutputStream os=new FileOutputStream(rlFileNm);
		int numRead;
		byte b[] = new byte[Integer.parseInt(request.getHeader("file-size"))];
		while((numRead = is.read(b,0,b.length)) != -1){
		 os.write(b,0,numRead);
		}
		if(is != null) {
			is.close();
		}
		os.flush();
		os.close();
		///////////////// 서버에 파일쓰기 /////////////////

		// 정보 출력
		sFileInfo += "&bNewLine=true";
		sFileInfo += "&sFileName="+ filename;;
		sFileInfo += "&sFileURL="+ CONTEXT_PATH + "/common/files/notice" + detailPath + realFileNm;
		out.println(sFileInfo);
	}
%>
