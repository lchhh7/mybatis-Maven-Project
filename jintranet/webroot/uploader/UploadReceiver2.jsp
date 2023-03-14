<%@page contentType="text/plain" pageEncoding="UTF-8"%><%@ page import="java.io.IOException" %><%@ page import="java.io.File" %><%@ page import="org.apache.commons.fileupload.FileItem" %><%@ page import="org.apache.commons.fileupload.FileUploadException" %><%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory" %><%@ page import="org.apache.commons.fileupload.servlet.FileCleanerCleanup" %><%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload" %><%@ page import="org.apache.commons.io.FileCleaningTracker" %><%@ page import="org.apache.commons.lang3.StringUtils" %><%@ page import="org.apache.commons.io.FileUtils"%><%@ page import="org.apache.commons.io.IOUtils"%><%@ page import="org.slf4j.Logger"%><%@ page import="org.slf4j.LoggerFactory"%><%@ page import="java.io.*"%><%@ page import="java.io.File"%><%@ page import="java.io.IOException"%><%@ page import="java.io.UnsupportedEncodingException"%><%@ page import="java.util.*"%><%@ page import="java.util.regex.Pattern"%><%@ page import="java.net.URLDecoder"%><%@ page import="com.jinjin.jintranet.common.util.DateUtils"%><%!
	public String UPLOAD_FLAG = "";

	/*
	public File UPLOAD_DIR = new File("D:/workspace/sic_event/webroot/uploader/movie/upload");
	File TEMP_UPLOAD_DIR = new File("D:/workspace/sic_event/webroot/uploader/movie/temp");
	*/
	public String UPLOAD_BASE_DOC = File.separator.equals("/") ? "/sportal/pms/u/document" : "D:/workspace/jj_pms/webroot/u/document";
	public String UPLOAD_BASE_APP = File.separator.equals("/") ? "/sportal/pms/u/app" : "D:/workspace/jj_pms/webroot/u/app";
	/*

	public File UPLOAD_DIR = new File("/vod/GameVOD/_upload");
	File TEMP_UPLOAD_DIR = 
	*/
	public File UPLOAD_DIR = null;
	File TEMP_UPLOAD_DIR = new File(File.separator.equals("/") ? "/sportal/pms/u/_temp" : "D:/workspace/jj_pms/webroot/u/_temp");
	

	public String REG_YEAR = DateUtils.getCurrentYear();
	public String REG_MONTH = DateUtils.getCurrentMonth().length()==1 ? ("0" + DateUtils.getCurrentMonth()) : DateUtils.getCurrentMonth();

	public boolean debug = true;
%><%!
	private class MergePartsException extends Exception
	{
	    MergePartsException(String message)
	    {
	        super(message);
	    }
	}
%><%!
	public class MultipartUploadParser
	{
		final Logger log = LoggerFactory.getLogger(MultipartUploadParser.class);
		private Map<String, String> params = new HashMap<String, String>();
	
		private List<FileItem> files = new ArrayList<FileItem>();
	
		// fileItemsFactory is a field (even though it's scoped to the constructor) to prevent the
		// org.apache.commons.fileupload.servlet.FileCleanerCleanup thread from attempting to delete the
		// temp file before while it is still being used.
		//
		// FileCleanerCleanup uses a java.lang.ref.ReferenceQueue to delete the temp file when the FileItemsFactory marker object is GCed
		private DiskFileItemFactory fileItemsFactory;
		
		// 절차상 제일먼저 호출됨
		public MultipartUploadParser(HttpServletRequest request, File repository, ServletContext context) throws Exception
		{
			if (!repository.exists() && !repository.mkdirs())
			{
				throw new IOException("Unable to mkdirs to " + repository.getAbsolutePath());
			}
			if(debug) System.out.println("mkdirs(Temporary) to: " + repository.getAbsolutePath());
	
			fileItemsFactory = setupFileItemFactory(repository, context);
	
	        ServletFileUpload upload = new ServletFileUpload(fileItemsFactory);
	        List<FileItem> formFileItems = upload.parseRequest(request);
	
			parseFormFields(formFileItems);
	
			if (files.isEmpty())
			{
				log.warn("No files were found when processing the requst. Debugging info follows.");
	
				writeDebugInfo(request);
	
				throw new FileUploadException("No files were found when processing the requst.");
			}
			else
			{
				if (log.isDebugEnabled())
				{
					writeDebugInfo(request);
				}
			}
		} // end of constructor
	
		private DiskFileItemFactory setupFileItemFactory(File repository, ServletContext context)
		{
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD);
			factory.setRepository(repository);
	
			FileCleaningTracker pTracker = FileCleanerCleanup.getFileCleaningTracker(context);
			factory.setFileCleaningTracker(pTracker);
	
			return factory;
		}
	
		private void writeDebugInfo(HttpServletRequest request)
		{
			log.debug("-- POST HEADERS --");
			for (String header : Collections.list(request.getHeaderNames()))
			{
				log.debug("{}: {}", header, request.getHeader(header));
			}
	
			log.debug("-- POST PARAMS --");
			for (String key : params.keySet())
			{
				log.debug("{}: {}", key, params.get(key));
			}
		}
	
		private void parseFormFields(List<FileItem> items) throws UnsupportedEncodingException
		{
			for (FileItem item : items)
			{
				if (item.isFormField())
				{
					String key = item.getFieldName();
					String value = item.getString("UTF-8");
					if (StringUtils.isNotBlank(key))
					{
						params.put(key, StringUtils.defaultString(value));
						log.debug("parseFormFields key:{}, value:{}",key, StringUtils.defaultString(value));
					}
				}
				else
				{
					files.add(item);
				}
			}
		}
	
		public Map<String, String> getParams()
		{
			return params;
		}
	
		public List<FileItem> getFiles()
		{
			if (files.isEmpty())
			{
				throw new RuntimeException("No FileItems exist.");
			}
	
			return files;
		}
	
		public FileItem getFirstFile()
		{
			if (files.isEmpty())
			{
				throw new RuntimeException("No FileItems exist.");
			}
	
			return files.iterator().next();
		}
	}
%><%!
	public class RequestParser
	{
		final Logger log = LoggerFactory.getLogger(MultipartUploadParser.class);
	
	    private static final String FILENAME_PARAM = "qqfile";
	    private static final String PART_INDEX_PARAM = "qqpartindex";
	    private static final String FILE_SIZE_PARAM = "qqtotalfilesize";
	    private static final String TOTAL_PARTS_PARAM = "qqtotalparts";
	    private static final String UUID_PARAM = "qquuid";
	    private static final String PART_FILENAME_PARAM = "qqfilename";
	    private static final String METHOD_PARAM = "_method";
	
	    private static final String GENERATE_ERROR_PARAM = "generateError";
	
	    private String filename;
	    private FileItem uploadItem;
	    private boolean generateError;
	
	    private int partIndex = -1;
	    private long totalFileSize;
	    private int totalParts;
	    private String uuid;
	    private String originalFilename;
	    private String method;
		
			// Modified by CWYOO at 2019.02.13
			// 구문 에러 발생
			// weblogic.servlet.jsp.CompilationException: Failed to compile JSP /uploader/UploadReceiver2.jsp
			// UploadReceiver2.jsp:181:59: Syntax error on token "<", ? expected after this token
	    // private Map<String, String> customParams = new HashMap<>();    
			private Map<String, String> customParams = new HashMap<String, String>();
	    
	    //HDS
	    //private String projectFullPath;
	
	    private RequestParser()
	    {
	    }
	
	    //HDS
	    //public String getProjectFullPath()
	    //{
	    //    return projectFullPath;
	    //}
	
	    public String getFilename()
	    {
	        return originalFilename != null ? originalFilename : filename;
	    }
	
	    //only non-null for MPFRs
	    public FileItem getUploadItem()
	    {
	        return uploadItem;
	    }
	
	    public boolean generateError()
	    {
	        return generateError;
	    }
	
	    public int getPartIndex()
	    {
	        return partIndex;
	    }
	
	    public long getTotalFileSize()
	    {
	        return totalFileSize;
	    }
	
	    public int getTotalParts()
	    {
	        return totalParts;
	    }
	
	    public String getUuid()
	    {
	        return uuid;
	    }
	
	    public String getOriginalFilename()
	    {
	        return originalFilename;
	    }
	
	    public String getMethod()
	    {
	        return method;
	    }
	
	    public Map<String, String> getCustomParams()
	    {
	        return customParams;
	    }
	
	    private void parseRequestBodyParams(RequestParser requestParser, MultipartUploadParser multipartUploadParser) throws Exception
	    {
	        if (multipartUploadParser.getParams().get(GENERATE_ERROR_PARAM) != null)
	        {
	            requestParser.generateError = Boolean.parseBoolean(multipartUploadParser.getParams().get(GENERATE_ERROR_PARAM));
	        }
	
	        String partNumStr = multipartUploadParser.getParams().get(PART_INDEX_PARAM);
	        if (partNumStr != null)
	        {
	            requestParser.partIndex = Integer.parseInt(partNumStr);
	
	            requestParser.totalFileSize = Long.parseLong(multipartUploadParser.getParams().get(FILE_SIZE_PARAM));
	            requestParser.totalParts = Integer.parseInt(multipartUploadParser.getParams().get(TOTAL_PARTS_PARAM));
	        }
	
	        for (Map.Entry<String, String> paramEntry : multipartUploadParser.getParams().entrySet())
	        {
	            requestParser.customParams.put(paramEntry.getKey(), paramEntry.getValue());
	        }
	
	        if (requestParser.uuid == null)
	        {
	            //HDS 
	        	requestParser.uuid = multipartUploadParser.getParams().get(UUID_PARAM);
	        }
	
	        if (requestParser.originalFilename == null)
	        {
	            requestParser.originalFilename = multipartUploadParser.getParams().get(PART_FILENAME_PARAM);
	        }
	    }
	
	    private void parseQueryStringParams(RequestParser requestParser, HttpServletRequest req)
	    {
	        if (req.getParameter(GENERATE_ERROR_PARAM) != null)
	        {
	            requestParser.generateError = Boolean.parseBoolean(req.getParameter(GENERATE_ERROR_PARAM));
	        }
	
	        String partNumStr = req.getParameter(PART_INDEX_PARAM);
	        if (partNumStr != null)
	        {
	            requestParser.partIndex = Integer.parseInt(partNumStr);
	            requestParser.totalFileSize = Long.parseLong(req.getParameter(FILE_SIZE_PARAM));
	            requestParser.totalParts = Integer.parseInt(req.getParameter(TOTAL_PARTS_PARAM));
	        }
	
	        Enumeration<String> paramNames = req.getParameterNames();
	        while (paramNames.hasMoreElements())
	        {
	            String paramName = paramNames.nextElement();
	            requestParser.customParams.put(paramName, req.getParameter(paramName));
	        }
	
	        if (requestParser.uuid == null)
	        {
	        	//HDS
	            requestParser.uuid = req.getParameter(UUID_PARAM);
	        }
	
	        if (requestParser.method == null)
	        {
	            requestParser.method = req.getParameter(METHOD_PARAM);
	        }
	
	        if (requestParser.originalFilename == null)
	        {
	            requestParser.originalFilename = req.getParameter(PART_FILENAME_PARAM);
	        }
	    }
	
	    private void removeQqParams(Map<String, String> customParams)
	    {
	        Iterator<Map.Entry<String, String>> paramIterator = customParams.entrySet().iterator();
	
	        while (paramIterator.hasNext())
	        {
	            Map.Entry<String, String> paramEntry = paramIterator.next();
	            if (paramEntry.getKey().startsWith("qq"))
	            {
	                paramIterator.remove();
	            }
	        }
	    }
	
	    private void parseXdrPostParams(HttpServletRequest request, RequestParser requestParser) throws Exception
	    {
	        String queryString = getQueryStringFromRequestBody(request);
	        String[] queryParams = queryString.split("&");
	
	        for (String queryParam : queryParams)
	        {
	            String[] keyAndVal = queryParam.split("=");
	            String key = URLDecoder.decode(keyAndVal[0], "UTF-8");
	            String value = URLDecoder.decode(keyAndVal[1], "UTF-8");
	
	            if (key.equals(UUID_PARAM))
	            {
	            	//HDS
	                requestParser.uuid = value;
	            }
	            else if (key.equals(METHOD_PARAM))
	            {
	                requestParser.method = value;
	            }
	            else
	            {
	                requestParser.customParams.put(key, value);
	            }
	        }
	    }
	
	    private String getQueryStringFromRequestBody(HttpServletRequest request) throws Exception
	    {
	        StringBuilder content = new StringBuilder();
	        BufferedReader reader = null;
	
	        try
	        {
	            reader = request.getReader();
	            char[] chars = new char[128];
	            int bytesRead;
	            while ( (bytesRead = reader.read(chars)) != -1 )
	            {
	                content.append(chars, 0, bytesRead);
	            }
	        }
	        finally
	        {
	            if (reader != null)
	            {
	                reader.close();
	            }
	        }
	
	        return content.toString();
	    }
	}
%><%!
    //2nd param is null unless a MPFR
    public RequestParser RequestParser_getInstance(HttpServletRequest request, MultipartUploadParser multipartUploadParser) throws Exception
    {
	    final String FILENAME_PARAM = "qqfile";
      RequestParser requestParser = new RequestParser();

      if (multipartUploadParser == null)
      {
          if (request.getMethod().equals("POST") && request.getContentType() == null)
          {
              requestParser.parseXdrPostParams(request, requestParser);
          }
          else
          {
              requestParser.filename = request.getParameter(FILENAME_PARAM);
              requestParser.parseQueryStringParams(requestParser, request);
          }
      }
      else 
      {
          requestParser.uploadItem = multipartUploadParser.getFirstFile();
          requestParser.filename = multipartUploadParser.getFirstFile().getName();

          //params could be in body or query string, depending on Fine Uploader request option properties
          requestParser.parseRequestBodyParams(requestParser, multipartUploadParser);
          requestParser.parseQueryStringParams(requestParser, request);
      }

      requestParser.removeQqParams(requestParser.customParams);
      
      //HDS
      //D:\Jinjin\MovieService\bin\apache-tomcat-8.0.38\appBase\FineUploader3
      //requestParser.projectFullPath = request.getServletContext().getRealPath(File.separator);  

      return requestParser;
    }
%><%!
	private void writeFileForMultipartRequest(RequestParser requestParser, Logger log) throws Exception
	{
	    File tempDir = new File(UPLOAD_DIR, requestParser.getUuid());
	    File targetDir = new File(UPLOAD_DIR.getAbsolutePath());
	    if(debug) System.out.println("## TARGET DIR : " + UPLOAD_DIR.getAbsolutePath());
	    
	    try{
	    	if(!targetDir.exists()) {
		    	boolean ret = targetDir.mkdirs();
		    	if (ret) {
					if(debug) System.out.println("mkdirs success");	    		
		    	}else {	    		
					if(debug) System.out.println("mkdirs failure....");
		    	}
		    } else {
		    	if(debug) System.out.println("## TARGET DIR ALREADY EXIST : " + UPLOAD_DIR.getAbsolutePath());
		    }
	    } catch(Exception e){
			//e.printStackTrace();
			log.warn("mkdirs Error : {}", new Object[] { e.getMessage()});
	    }
	
	    if (requestParser.getPartIndex() >= 0) { //청크 형식으로 보낼때...
	    	tempDir.mkdirs();
	 
	        writeFile(requestParser.getUploadItem(), new File(tempDir, requestParser.getUuid() + "_" + String.format("%05d", requestParser.getPartIndex())), null);
	
	        if (requestParser.getTotalParts()-1 == requestParser.getPartIndex()){
	            File[] parts = getPartitionFiles(tempDir, requestParser.getUuid());
	        	// aa(uuid).mp4  형식으로 저장한다....
	            int index = requestParser.getOriginalFilename().lastIndexOf(".");
	            
	            // Modified by CWYOO at 2019.02.14
	            // 실제 최종 파일이 UUID로 저장되도록 변경
	            //File outputFile = new File(targetDir, requestParser.getOriginalFilename().substring(0, index) +"("+requestParser.getUuid()+")."+requestParser.getOriginalFilename().substring(index+1));
	            File outputFile = new File(targetDir, requestParser.getUuid() + "." + requestParser.getOriginalFilename().substring(index+1));
	            
	            for (File part : parts)
	            {
	                mergeFiles(outputFile, part);
	            }
	
	            assertCombinedFileIsVaid(requestParser.getTotalFileSize(), outputFile, requestParser.getUuid());
	/*
	            for (File part : parts)// dschae delete tamporary files
	            {
					log.debug("deleting {}", part.getName());
	                FileUtils.deleteQuietly(part);
	            }
	*/
	            FileUtils.deleteDirectory(tempDir);
	        }
	    }
	    else // 단일파일로 보낼때...(청크타입 아니고...)
	    {
	    	// aa(uuid).mp4  형식으로 저장한다....
	        int index = requestParser.getFilename().lastIndexOf(".");
			//old code--        writeFile(requestParser.getUploadItem().getInputStream(), new File(targetDir, requestParser.getFilename().substring(0, index) +"("+requestParser.getUuid()+")."+requestParser.getFilename().substring(index+1)), null);
	        // Modified by CWYOO at 2019.09.25
	        // 원본파일(uuid).확장자 형태로 저장됨
	        // 그냥 uuid.확장자 형태로 저장되도록 변경
	        //writeFile(requestParser.getUploadItem(), new File(targetDir, requestParser.getFilename().substring(0, index) +"("+requestParser.getUuid()+")."+requestParser.getFilename().substring(index+1)), null);
	        writeFile(requestParser.getUploadItem(), new File(targetDir, requestParser.getUuid()+"."+requestParser.getFilename().substring(index+1)), null);
	    }
	
		deleteTempFile(requestParser.getUploadItem());
	}
%><%!
	private void assertCombinedFileIsVaid(long totalFileSize, File outputFile, String uuid) throws MergePartsException
	{
	    if (totalFileSize != outputFile.length())
	    {
	        deletePartitionFiles(UPLOAD_DIR, uuid);
	        outputFile.delete();
	        throw new MergePartsException("Incorrect combined file size!");
	    }
	}
%><%!
	private static class PartitionFilesFilter implements FilenameFilter
	{
	    private String filename;
	    PartitionFilesFilter(String filename)
	    {
	        this.filename = filename;
	    }
	
	    public boolean accept(File file, String s)
	    {
	        return s.matches(Pattern.quote(filename) + "_\\d+");  // uuid_숫자 형식 
	        // 정규표현식 설명 참고 : http://highcode.tistory.com/6
	    }
	}
%><%!
  private static File[] getPartitionFiles(File directory, String filename)
  {
      File[] files = directory.listFiles(new PartitionFilesFilter(filename));
      Arrays.sort(files);
      return files;
  }
%>
<%!
	private static void deletePartitionFiles(File directory, String filename)
  {
      File[] partFiles = getPartitionFiles(directory, filename);
      for (File partFile : partFiles)
      {
          partFile.delete();
      }
  }
%><%!
  private File mergeFiles(File outputFile, File partFile) throws IOException
  {
      FileOutputStream fos = new FileOutputStream(outputFile, true);

      try
      {
          FileInputStream fis = new FileInputStream(partFile);

          try
          {
              IOUtils.copy(fis, fos);  // fos의 파일끝에 추가로 붙여서 복사한다
          }
          finally
          {
			IOUtils.closeQuietly(fis);
          }
      }
      finally
      {
          IOUtils.closeQuietly(fos);
      }

      return outputFile;
  }
%><%!
	private void deleteTempFile(FileItem fitem)
	{
		fitem.delete();
	}
%><%!
  private File writeFile(FileItem fitem, File out, Long expectedFileSize) throws IOException
  {
	InputStream in = fitem.getInputStream();
      FileOutputStream fos = null;

      try
      {
          fos = new FileOutputStream(out);
          IOUtils.copy(in, fos);

          if (expectedFileSize != null)
          {
              Long bytesWrittenToDisk = out.length();
              if (!expectedFileSize.equals(bytesWrittenToDisk))
              {
                  out.delete();
                  throw new IOException(String.format("Unexpected file size mismatch. Actual bytes %s. Expected bytes %s.", bytesWrittenToDisk, expectedFileSize));
              }
          }

          return out;
      }
      catch (Exception e)
      {
          throw new IOException(e);
      }
      finally
      {
		in.close();
          IOUtils.closeQuietly(fos);
      }
  }
%><%!
	/*
  private void writeResponse(PrintWriter writer, String failureReason, boolean isIframe, boolean restartChunking, RequestParser requestParser)
  {
      if (failureReason == null){
//            if (isIframe)
//            {
//                writer.print("{\"success\": true, \"uuid\": \"" + requestParser.getUuid() + "\"}<script src=\"http://192.168.130.118:8080/client/js/iframe.xss.response.js\"></script>");
//            }
//            else
//            {
              int index = requestParser.getFilename().lastIndexOf(".");
              String savedFilePath = UPLOAD_DIR.getAbsolutePath() + "\\" + requestParser.getFilename().substring(0, index) +"("+requestParser.getUuid()+")."+requestParser.getFilename().substring(index+1);
              savedFilePath = savedFilePath.replace("\\", "\\\\");
      		  
              //log.warn("writeResponse JSON => " + "{\"success\": true, \"filePath\": \"" + savedFilePath + "\"}");
              writer.print("{\"success\": true, \"filePath\": \"" + savedFilePath + "\"}");
      		  
//            }
      }
      else
      {
          if (restartChunking)
          {
              writer.print("{\"error\": \"" + failureReason + "\", \"reset\": true}");
          }
          else
          {
//                if (isIframe)
//                {
//                    writer.print("{\"error\": \"" + failureReason + "\", \"uuid\": \"" + requestParser.getUuid() + "\"}<script src=\"http://192.168.130.118:8080/client/js/iframe.xss.response.js\"></script>");
//                }
//                else
//                {

                  writer.print("{\"error\": \"" + failureReason + "\"}");
//                }
          }
      }
  }
  */
  private void writeResponse(HttpServletResponse response, 
  		String failureReason, 
  		boolean isIframe, 
  		boolean restartChunking, 
  		RequestParser requestParser) throws IOException
  {
      if (failureReason == null){
				//if (isIframe)
				//{
				//	writer.print("{\"success\": true, \"uuid\": \"" + requestParser.getUuid() + "\"}<script src=\"http://192.168.130.118:8080/client/js/iframe.xss.response.js\"></script>");
				//}
				//else
				//{
						// 파일저장 시 uuid.확장자로 저장하고
						// 실제 파일명은 사용자에게 리턴해서 DB에 저장하도록 한다.
            int index = requestParser.getFilename().lastIndexOf(".");
            //String savedFilePath = UPLOAD_DIR.getAbsolutePath() + "\\" + requestParser.getFilename().substring(0, index) +"("+requestParser.getUuid()+")."+requestParser.getFilename().substring(index+1);
            
            // 사용자가 PC에서 선택한 원본파일이름
            String realFileName = requestParser.getFilename();
            
            // 서버에 저장될 실제 파일 이름 (uuid.확장자)
            String saveFileName = requestParser.getUuid() + "." + requestParser.getFilename().substring(index+1);
            
            // 서버에 저장될 전체 파일 경로 
            String savedFilePath = UPLOAD_DIR.getAbsolutePath() + "/" + saveFileName;
            //String savedFilePath = UPLOAD_DIR.getAbsolutePath() + "/" + requestParser.getUuid() + "." + requestParser.getFilename().substring(index+1);
            //savedFilePath = savedFilePath.replace("\\", "\\\\");
            
            // Windows 파일구분자를 /로 변경
            savedFilePath = savedFilePath.replace("\\", "/");
            
            /*
            System.out.println("#------------------------------------");
            System.out.println("# REAL_FILE_NAME : " + realFileName);
            System.out.println("# SAVE_FILE_NAME : " + saveFileName);
            System.out.println("# SAVE_FILE_PATH : " + savedFilePath);
            */
    		  	
            //log.warn("writeResponse JSON => " + "{\"success\": true, \"filePath\": \"" + savedFilePath + "\"}");
            //writer.print("{\"success\": true, \"filePath\": \"" + savedFilePath + "\"}");
            // Modified by CWYOO at 2019.02.14
            // 파일명 추가
            //response.getOutputStream().print("{\"success\": true, \"filePath\": \"" + savedFilePath + "\"}");
            StringBuffer buf = new StringBuffer();
            buf.append("{\"success\": true,");
            buf.append("\"filePath\": \"" + savedFilePath + "\",");
            buf.append("\"saveFileName\": \"" + saveFileName + "\",");
            buf.append("\"realFileName\": \"" + realFileName + "\",");
            buf.append("\"regYear\": \"" + REG_YEAR + "\",");
            buf.append("\"regMonth\": \"" + REG_MONTH + "\"");
            buf.append("}");
            //response.getOutputStream().print("{\"success\": true, \"filePath\": \"" + savedFilePath + "\", \"fileName\": \"" + (requestParser.getFilename().substring(0, index) +"("+requestParser.getUuid()+")."+requestParser.getFilename().substring(index+1)) + "\"}");
            response.getOutputStream().print(buf.toString());
				//}
      }
      else
      {
          if (restartChunking)
          {
              //writer.print("{\"error\": \"" + failureReason + "\", \"reset\": true}");
              response.getOutputStream().print("{\"error\": \"" + failureReason + "\", \"reset\": true}");
          }
          else
          {
//                if (isIframe)
//                {
//                    writer.print("{\"error\": \"" + failureReason + "\", \"uuid\": \"" + requestParser.getUuid() + "\"}<script src=\"http://192.168.130.118:8080/client/js/iframe.xss.response.js\"></script>");
//                }
//                else
//                {
										//writer.print("{\"error\": \"" + failureReason + "\"}");
										response.getOutputStream().print("{\"error\": \"" + failureReason + "\"}");
//                }
          }
      }
  }
%><%
//----------------------------------------------------
//실제 JSP 소스
//----------------------------------------------------
/*
// 참고 
// https://thinkwarelab.wordpress.com/2016/11/18/java%EC%97%90%EC%84%9C-logback%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EB%A1%9C%EA%B9%85logging-%EC%82%AC%EC%9A%A9%EB%B2%95/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
*/
	//UPLOAD_DIR/aa(uuid).mp4 형식으로 저장된다
	boolean isIframe = false;
	RequestParser requestParser = null;	
  	HttpServletRequest req = request;
	HttpServletResponse resp = response;
	
	//final Logger log = LoggerFactory.getLogger(MultipartUploadParser.class);
	final Logger log = LoggerFactory.getLogger(this.getClass());
	
	if(debug) System.out.println("## Start....");
	
	/* try 안으로 이동
	resp.setContentType("text/plain");
  resp.setStatus(200);//SUCCESS_RESPONSE_CODE);// 200
  resp.setCharacterEncoding("utf-8");
  */

  try
  {
  	resp.setContentType("text/plain");
    resp.setStatus(200);//SUCCESS_RESPONSE_CODE);// 200
    resp.setCharacterEncoding("utf-8");
    if(debug) System.out.println("## 1");
    
    // 원래 주석이었음 
		//resp.setContentType(isIframe ? "text/html" : "text/plain");
		//resp.addHeader("Access-Control-Allow-Origin", "http://192.168.130.118:8080");
		//resp.addHeader("Access-Control-Allow-Credentials", "true");
		//resp.addHeader("Access-Control-Allow-Origin", "*");

		if (ServletFileUpload.isMultipartContent(req))  // 업로드 요청시...
		{
			if(debug) System.out.println("## 2");
			// Modified by CWYOO at 2019.02.13
			//MultipartUploadParser multipartUploadParser = new MultipartUploadParser(req, TEMP_UPLOAD_DIR, getServletContext());
			MultipartUploadParser multipartUploadParser = new MultipartUploadParser(req, TEMP_UPLOAD_DIR, req.getServletContext());
			
			if(debug) System.out.println("## 3");
			
			requestParser = RequestParser_getInstance(req, multipartUploadParser);
			
		  	isIframe = req.getHeader("X-Requested-With") == null || !req.getHeader("X-Requested-With").equals("XMLHttpRequest");
			if(debug) System.out.println("## 4");
			
			//------------------------------------------
			UPLOAD_FLAG = request.getParameter("flag");
			if(debug) System.out.println("## FLAG : " + UPLOAD_FLAG);
			if(UPLOAD_FLAG==null || "".equals(UPLOAD_FLAG)) {
				writeResponse(resp, "업로드 구분이 누락되었습니다.", isIframe, true, requestParser);
				return;
			}
			/*
			if(eventYear==null || "".equals(eventYear)) {
				writeResponse(resp, "대회년도가 누락되었습니다.", isIframe, true, requestParser);
				return;
			}
			if(eventCd==null || "".equals(eventCd)) {
				writeResponse(resp, "대회코드가 누락되었습니다.", isIframe, true, requestParser);
				return;
			}
			*/

			/*
			public String UPLOAD_BASE_DOC = "/vod/GameVOD";
			public File UPLOAD_DIR = new File("/vod/GameVOD/_upload");
			File TEMP_UPLOAD_DIR = new File("/vod/GameVOD/_temp");
			*/
			//UPLOAD_DIR = new File(UPLOAD_BASE_DOC + "/" + pclassCd + "/" + eventYear + "/" + eventCd + "/_upload");
			if("DOC".equals(UPLOAD_FLAG)) {
				UPLOAD_DIR = new File(UPLOAD_BASE_DOC + "/" + REG_YEAR + "/" + REG_MONTH);
			} else if("APP".equals(UPLOAD_FLAG) || "REPAIR".equals(UPLOAD_FLAG)) {
				UPLOAD_DIR = new File(UPLOAD_BASE_APP + "/" + REG_YEAR + "/" + REG_MONTH);
			}
			//TEMP_UPLOAD_DIR = new File(UPLOAD_BASE_DOC + "/" + pclassCd + "/" + eventYear + "/" + eventCd + "/_temp");
			//------------------------------------------
			
			// 이 변수는 사용하는 곳이 없음 
		  	//final String CONTENT_LENGTH = "Content-Length";
			
			//업로드 폴더 위치 지정...
			// TEMP_UPLOAD_DIR 에 업로드된후 UPLOAD_DIR 에 복사되는 형식이다.
			// TEMP_UPLOAD_DIR 에 업로드시의 임시 파일들이 그대로 남아 있다... 주기적으로 강제로 삭제해 주어야 할 둣하다...
			// UPLOAD_DIR 에 UUID 형식의 폴더가 있으면 업로드중 취소되어 그대로 남아 있는 경우이다...주기적으로 삭제해야 한다.
			
			if(debug) System.out.println("## 5");
			
			// Modified bu CWYOO at 2019.02.13
			// 실제 디렉토리가 없을 경우에만 생성하도록 수정
			if(!UPLOAD_DIR.exists()) {
				UPLOAD_DIR.mkdirs();
			}

			if(!TEMP_UPLOAD_DIR.exists()) {
				TEMP_UPLOAD_DIR.mkdirs();
			}
			
			if(debug) System.out.println("## 6");
			
			writeFileForMultipartRequest(requestParser, log);
			if(debug) System.out.println("## 7");
			
			// 이 로직을 사용 시 에러 발생
			// java.lang.IllegalStateException: strict servlet API: cannot call getWriter() after getOutputStream()
			// 이전에 어디서 getOutputStream()이 호출되는 것으로 추정
			// 아니면 WebLogic12의 버그일 수도...
			//writeResponse(resp.getWriter(), requestParser.generateError() ? "Generated error" : null, isIframe, false, requestParser);
			writeResponse(resp, requestParser.generateError() ? "Generated error" : null, isIframe, false, requestParser);
			//writeResponse(pw, requestParser.generateError() ? "Generated error" : null, isIframe, false, requestParser);
			if(debug) System.out.println("## 8");
		}
		else  
		{
			// 주로 삭제 요청시... 사용하지 말자...
		}
  }
  catch (Exception e)
  {
		e.printStackTrace();
		//log.error("Problem handling upload request", e);
		if(e instanceof MergePartsException)
		{
			if(debug) System.out.println("## 9");
			//writeResponse(resp.getWriter(), e.getMessage(), isIframe, true, requestParser);
			writeResponse(resp, e.getMessage(), isIframe, true, requestParser);
			//writeResponse(pw, e.getMessage(), isIframe, true, requestParser);
			if(debug) System.out.println("## 10");
		}
		else
		{
			if(debug) System.out.println("## 11");
			//writeResponse(resp.getWriter(), e.getMessage(), isIframe, false, requestParser);
			writeResponse(resp, e.getMessage(), isIframe, false, requestParser);
			//writeResponse(pw, e.getMessage(), isIframe, false, requestParser);
			if(debug) System.out.println("## 12");
		}
  }
%>