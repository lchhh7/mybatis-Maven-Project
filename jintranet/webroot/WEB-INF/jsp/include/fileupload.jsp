<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<div class="modal" id="upload-modal">
    <div class="modal_wrap mw_big">
        <div class="title_bar clearfix">
            <h3>첨부파일 선택</h3>
        </div>
        <div class="modal_content">
            <div id="dropzone" class="dropzone">
                <div class="dz-default dz-message file-dropzone text-center well col-sm-12">
                    <span class="glyphicon glyphicon-paperclip"></span>
                    <span>파일을 드래그&드랍하거나 <em class="s_blue">여기</em>를 눌러주세요</span>
                </div>
            </div>
            <div class="mbtnbox">
                <a role="button" id="upload-btn" class="btn jjblue" >업로드</a>
                <a role="button" id="upload-close-btn" class="btn jjblue">닫기</a>
            </div>
        </div>
    </div>
</div>

<link rel="stylesheet" href='<c:url value="/common/fileuploader/dropzone.css"/>'>
<script src='<c:url value="/common/fileuploader/dropzone.js"/>'></script>
<script src='<c:url value="/common/fileuploader/dropzone-common.js"/>'></script>