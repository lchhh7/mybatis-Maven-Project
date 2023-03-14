index.jsp : 업로드 화면입니다.
UploadReceriver2.jsp : index.jspㅇ[서 올리면 청크로 받는 jsp 입니다.
js\ - java script 들어있습니다.
=====
파일 올라가는 폴더와 파일명 정해주는 부분은 UploadReseiver2.jsp를 보면 됩니다.

동작 방식은
UPLOAD_TEMP_DIR에 일단 올려서, UPLOAD_DIR/temp밑으로 복사했다가
UPLOAD_DIR에 저장하고, UPLOAD_DIR/temp 폴더를 지웁니다.
temp 폴더를 만들 때에 UUID를 사용하므로, 폴더 중복에 의한 문제는 발생하지 않습니다.
이 내용은 README.txt에도 들어갑니다.