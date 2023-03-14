<%-- 
    https://docs.fineuploader.com/branch/master/quickstart/01-getting-started.html 참고
    2018-12-03
--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
	//String pclassCd = request.getParameter("pclassCd");
	//String eventYear = request.getParameter("eventYear");
	String flag = request.getParameter("flag");
	
	if(flag==null || "".equals(flag)) {
		out.println("<html><head></head><body>");
		out.println("<script>parent.callbackFail('업로드 구분이 누락되었습니다.');</script>");
		out.println("</body></html>");
		return;
	}
    /*
	if(eventYear==null || "".equals(eventYear)) {
		out.println("<html><head></head><body>");
		out.println("<script>parent.callbackFail('대회년도가 누락되었습니다.');</script>");
		out.println("</body></html>");
		return;
	}
	if(eventCd==null || "".equals(eventCd)) {
		out.println("<html><head></head><body>");
		out.println("<script>parent.callbackFail('대회코드가 누락되었습니다.');</script>");
		out.println("</body></html>");
		return;
	}
    */
%>
<!DOCTYPE html>
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
 	<meta name="viewport" content="width=device-width, initial-scale=1">
 	
 	<!-- jQuery
    ====================================================================== -->
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
 	
	<!-- Fine Uploader New/Modern CSS file
    ====================================================================== -->
    <link href="css/fine-uploader/fine-uploader-new.css" rel="stylesheet">
    
	<!-- alertify CSS file
    ====================================================================== -->
	<link rel="stylesheet" href="css/alertify/themes/alertify.core.css" />
	<link rel="stylesheet" href="css/alertify/themes/alertify.default.css" id="toggleCSS" />    

    <!-- Fine Uploader JS file
    ====================================================================== -->
    <script src="js/fine-uploader/jquery.fine-uploader.js"></script>

    <!-- alertify JS file
    ====================================================================== -->
    <script src="js/alertify/alertify.min.js"></script>

    <!-- Fine Uploader Thumbnails template w/ customization
    ====================================================================== -->
    <script type="text/template" id="qq-template-manual-trigger">
        <div class="qq-uploader-selector qq-uploader" qq-drop-area-text="여기에 파일을 드래그해 주세요">
						
						<!--
            <div class="qq-total-progress-bar-container-selector qq-total-progress-bar-container">
                <div role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" class="qq-total-progress-bar-selector qq-progress-bar qq-total-progress-bar"></div>
            </div>
						-->
						
            <div class="qq-upload-drop-area-selector qq-upload-drop-area" qq-hide-dropzone>
                <span class="qq-upload-drop-area-text-selector"></span>
            </div>

            <div class="buttons">
                <!--
                <div class="qq-upload-button-selector qq-upload-button">
                    <div>파일 선택하기</div>
                </div>

                <button type="button" id="trigger-upload" class="btn btn-primary">
                    <i class="icon-upload icon-white"></i> 파일 올리기
                </button>
                -->

                <!--
                <button type="button" class="qq-upload-button-selector qq-upload-button" style="width:110px;display:inline-block;">
                    <i class="icon-upload icon-white"></i> 파일 선택하기
                </button>
                -->
                <div class="qq-upload-button-selector qq-upload-button">
                    <div>파일 선택하기</div>
                </div>
                <button type="button" id="trigger-upload" class="qq-upload-button-selector qq-upload-button" style="width:110px;display:inline-block;">
                    <i class="icon-upload icon-white"></i> 파일 올리기
                </button>
            </div>

            <span class="qq-drop-processing-selector qq-drop-processing">
                <span>드롭하신 파일을 처리하고 있습니다...</span>
                <span class="qq-drop-processing-spinner-selector qq-drop-processing-spinner"></span>
            </span>

            <ul class="qq-upload-list-selector qq-upload-list" aria-live="polite" aria-relevant="additions removals">
                <li>
                    <div class="qq-progress-bar-container-selector">
                        <div role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" class="qq-progress-bar-selector qq-progress-bar"></div>
                    </div>
                    <span class="qq-upload-spinner-selector qq-upload-spinner"></span>
                    <!--<img class="qq-thumbnail-selector" qq-max-size="100" qq-server-scale> -->
                    <span class="qq-upload-file-selector qq-upload-file"></span>
                    <span class="qq-edit-filename-icon-selector qq-edit-filename-icon" aria-label="Edit filename"></span>
                    <input class="qq-edit-filename-selector qq-edit-filename" tabindex="0" type="text">
                    <span class="qq-upload-size-selector qq-upload-size"></span>
                    <button type="button" class="qq-btn qq-upload-cancel-selector qq-upload-cancel">업로드 취소</button>
                    <button type="button" class="qq-btn qq-upload-retry-selector qq-upload-retry">재시도</button>
                    <button type="button" class="qq-btn qq-upload-delete-selector qq-upload-delete">삭제</button>
					<!-- 커스텀 추가 예 -->
					<button type="button" id="view-btn" class="view-btn qq-hide btn">저장위치 보기</button>

                    <span role="status" class="qq-upload-status-text-selector qq-upload-status-text"></span>
                </li>
            </ul>

        </div>

    </script>

    <style>
        #trigger-upload {
            color: white;
            background-color: #00ABC7;
            font-size: 13px;
            font-family:"dotum";
            /*padding: 7px 20px;*/
            background-image: none;
        }

        #fine-uploader-manual-trigger .qq-upload-button {
            margin-right: 15px;
            color: white;
            background-color: #00ABC7;
            font-size: 13px;
            font-family:"dotum";
            /*padding: 7px 20px;*/
            background-image: none;
        }

        #fine-uploader-manual-trigger .buttons {
            width: 40%;
        }

        #fine-uploader-manual-trigger .qq-uploader .qq-total-progress-bar-container {
            width: 60%;
        }
    </style>

    <title>파일 업로드</title> 	
 	
</head>
<body>

<!-- Fine Uploader DOM Element Start
    ====================================================================== -->
    <div id="fine-uploader-manual-trigger"></div>
    
<!-- Fine Uploader DOM Element End
    ====================================================================== -->

	<div class="buttons">
			<%--
      <button type="button" id="test-func1" class="btn btn-primary">
      	<i class="icon-upload icon-white"></i> FuncTest1
      </button>
      <button type="button" id="test-func2" class="btn btn-primary">
      	<i class="icon-upload icon-white"></i> FuncTest2
      </button>
      --%>
    </div>	
    
    <script>
    
    	// 옵션 설정....
        $('#fine-uploader-manual-trigger').fineUploader({
			//템플릿 스크립트 id 지정
        	template: 'qq-template-manual-trigger',
        	
           
        	// 서버 서블릿 클래스 지정
        	request: {
                endpoint: '${pageContext.request.contextPath}/uploader/UploadReceiver2.jsp?flag=<%=flag%>'  // 서블릿 클래스 지정
            },
            
            // 이어붙이기 등을 지원하기 위해 청크형식 업로드 옵션을 활성화한다 
            chunking: {
                enabled: true,
                partSize: 5*1024*1024,// default : 5 Mb 단위  
            },
            
            // 청크형식 업로드중 여러가지 사유로 멈추었을때 재시도를 할수 있게 활성화 
            resume: {
                enabled: true
            },    
            
            //네트워크 문제로 업로드중 자동으로 재시도 할지 여부 지정...
            retry: {
            	enableAuto: false,
            	showButton: true,  // 문제가 발생한 업로드 건에 대해 재시도를 할 수 있는 버튼 보여주기 설정 
            },
            
            // 업로드 완료후 나타나는 삭제버튼을 클릭시 관련 옵션을 여기서 지정한다
            //  현재는 사용하지 않는다 
            deleteFile: {   
                enabled: false,  // 사용안함...
                method: 'POST', 
                endpoint: '${pageContext.request.contextPath}/uploader/UploadReceiver2.jsp?flag=<%=flag%>',
                params: {
                    "aaa": "bbb",
                },  
	            forceConfirm: true,
	            confirmMessage: "정말 삭제하시겠습니까? => {filename}?",
	            deletingStatusText: "삭제중...",
	            deletingFailedText: "삭제 작업이 실패했습니다."
            },
            
            // 현재는 사용하지 않는다 
            thumbnails: {
                placeholders: {
                    waitingPath: 'img/fine-uploader/placeholders/waiting-generic.png',
                    notAvailablePath: 'img/fine-uploader/placeholders/not_available-generic.png'
                }
            },
            
            
            validation: {
                allowedExtensions: ['zip', 'hwp', 'pdf', 'doc', 'docx', 'ppt', 'pptx', 'xls', 'xlsx', 'jpg', 'gif', 'bmp', 'jpeg', 'png'], // 업로드 가능한 확장자 
                itemLimit: 1,  // 한번에 업로드 할 수 있는  파일리스트에서 최대 파일수 
                sizeLimit: 6000 * 1024 * 1024  // 5K, 5M, 5G    업로드 파일 크기 제한  
            },
            
            // 각종 상황에서의 메시지 지정
            messages: {
                typeError: "{file} 파일의 확장자는 지원하지 않습니다. 지원하는 확장자는 <{extensions}> 입니다.",
                sizeError: "{file} 파일의 크기가 너무 큽니다.최대 업로드 파일 크기는 {sizeLimit} 입니다.",
                minSizeError: "{file} 파일의 크기가 너무 작습니다. 최소 업로드 파일 크기는 {minSizeLimit} 입니다.",
                emptyError: "{file} 파일이 이상합니다. 이 파일을 제외하고 다시 선택 해주세요.",
                noFilesError: "업로드 할 파일이 없습니다.",
                tooManyItemsError: "업로드 할 파일이 너무 많습니다.(요청 파일 수 : {netItems}), 최대 업로드 가능 파일수 는 {itemLimit} 입니다.",
                maxHeightImageError: "이미지 파일의 세로 크기가 너무 큽니다.",
                maxWidthImageError: "이미지 파일의 가로 크기가 너무 큽니다.",
                minHeightImageError: "이미지 파일의 세로 크기가 너무 작습니다.",
                minWidthImageError: "이미지 파일의 가로 크기가 너무 작습니다.",
                retryFailTooManyItems: "재시도가 실패 했습니다. 재시도 횟수가 한계에 도달 했습니다.",
                onLeave: "현재 파일이 업로드 중입니다. 지금 닫으시면 업로드 작업이 취소 됩니다.",
                unsupportedBrowserIos8Safari: "심각한 에러 발생 - 이 브라우저는 iOS8 Safari 의 버그로 인해 파일 업로드 기능을 제공하지 않습니다.  애플사가 수정 할 때까지는 iOS8 Chrome 을 사용하세요."
            },
            
	    	// alertify.js 를 이용한 커스텀 다이얼로그 
				showMessage: function(message) {
	    			return alertify.alert(message);
	    	},
	    	
	    	showPrompt: function(message, defaultValue) {
	    		var promise;
	    		promise = new qq.Promise();
	    		alertify.prompt(message, function(result, inStr) {
		    			if (result) {
		    		 		return promise.success(inStr);
		    		 	} else {
		    		 		return promise.failure(inStr);
		    		 	}
		    		}, defaultValue);
	    		return promise;
	    	},
	    		
	    	showConfirm: function(message) {
	    		var promise;
	    		promise = new qq.Promise();
	    		alertify.confirm(message, function(result) {
	    			if (result) {
	    		 		return promise.success(result);
	    		 	} else {
	    		 		return promise.failure();
	    		 	}
	    		});
	    		
	    		return promise;
	    	},

            callbacks: {
            	// 파일 1개가 업로드가 완료되었을때 해당 파일의  목록 UI 변화를 준다.
                onComplete: function(id, name, responseJSON, maybeXhr) {
		            if(responseJSON.success==true) {
		                  configureFineUploaderUI();
		                  //console.log(maybeXhr);
		                  
		                  //커스텀 버튼 보여주기.... 업로드 파일 패스 보기용 버튼 활성화 
 						  var serverPathToFile = responseJSON.filePath,
                          fileItem = this.getItemByFileId(id);
                      
                          //console.log(responseJSON);
                          // Added by CWYOO at 2019.02.14
                          var tmpSaveFileName = responseJSON.saveFileName;
                          var tmpRealFileName = responseJSON.realFileName;
                          var tmpRegYear = responseJSON.regYear;
                          var tmpRegMonth = responseJSON.regMonth;
                          //alert("SAVE : " + tmpSaveFileName + "\nREAL : " + tmpRealFileName);
                          if(parent) {
                            <% if("REPAIR".equals(flag)) { %>
                            parent.callbackRepairSuccess(tmpSaveFileName, tmpRealFileName, tmpRegYear, tmpRegMonth);
                            <% } else if("APP".equals(flag)) { %>
                            parent.callbackAttachSuccess(tmpSaveFileName, tmpRealFileName, tmpRegYear, tmpRegMonth);
                            <% } else { %>
                            parent.callbackSuccess(tmpSaveFileName, tmpRealFileName, tmpRegYear, tmpRegMonth);
                            <% } %>
                          }
                          	
                          	

		                  // https://docs.fineuploader.com/branch/master/api/qq.html
		                  // qq() api 참조 
                          var viewBtn = qq(fileItem).getByClass("view-btn")[0];
                          viewBtn.setAttribute("href", serverPathToFile);
                          qq(viewBtn).removeClass("qq-hide");
           		                  
		              }
		        },
				// 지정한 리스트 파일들이 모두 업로드 되었을때 호출됨
		        onAllComplete: function(successful, failed) {
	                  //alertify.alert("업로드 작업이 완료되었습니다.");
		        },
		        
		        onError: function(id, name, errorReason, xhrOrXdr) {
		        	alertify.alert(qq.format("Error on file number {} - {}.  Reason: {}", id, name, errorReason));
		        },		        
		        
		        // 삭제 버튼 클릭시 작업이 완료되었을때 호출됨 : 현재 사용안함
		        onDeleteComplete: function(id, xhrOrXdr, isError) {
	                  alertify.alert("삭제 작업이 완료되었습니다.");
	        	},
            	onSubmitted: function (id, name) {
                },
                onProgress: function (id, fileName, loaded, total) {
            	},
            	
		        onCancel: function(id, name) {
		        },
		        onError: function(id, name, reason, maybeXhrOrXdr) {
		        },
		        onSubmitDelete: function(id) {
		        },
		        onDelete: function(id) {
		        },
		        
            },
            
            autoUpload: false,  // 업로드 하고자 하는 파일을 선택하면 자동으로 업로드가 시작되도록 하려면 true 로 설정함
                        
            debug: false,  // debug 모드로 세팅하고자 할때  브라우저 콘솔창에 메시지 많이 나온다  
            
           // disableCancelForFormUploads: false,
            
        });

    	// 파일 올리기 버튼 클릭시 아래 함수가 호출되며 업로드가 시작된다 
        $('#trigger-upload').click(function() {
            $('#fine-uploader-manual-trigger').fineUploader('uploadStoredFiles');
        });    
    	
    	
        //예제.. 서버에 저장된 파일 패스 보기...
        $("ul.qq-upload-list-selector").on("click","li button", function(){
        	alertify.alert($(this).attr("href"));
         });        
        
        
        //업로드 완료된 파일 리스트 UI 변경...
        function configureFineUploaderUI()
        {
            $('.qq-upload-success').css('background-color','white');
            $('.qq-upload-file').css('color','black');
        }
        
        
        // UI 임의 변경 테스트.....
        $('#test-func1').click(function() {
            $('.qq-total-progress-bar-container-selector').removeClass("qq-hide");
            $('.qq-total-progress-bar-selector').css('width','100%');
            $('.qq-total-progress-bar-selector').attr('aria-valuenow','100');
        });    
        
        $('#test-func2').click(function() {
            $('.qq-total-progress-bar-selector').css('width','0%');
            $('.qq-total-progress-bar-selector').attr('aria-valuenow','0');
        });
    </script>
</body> 
</html>