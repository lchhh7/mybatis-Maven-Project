<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<div class="modal" id="upload-modal2">
    <div class="modal_wrap mw_big">
        <div class="title_bar clearfix">
            <h3>첨부파일 선택</h3>
        </div>
        <div class="modal_content">
            <div id="dropzone2" class="dropzone">
                <div class="dz-default dz-message file-dropzone text-center well col-sm-12">
                    <span class="glyphicon glyphicon-paperclip"></span>
                    <span>파일을 드래그&드랍하거나 <em class="s_blue">여기</em>를 눌러주세요</span>
                </div>
            </div>
            <div class="mbtnbox">
                <a role="button" id="upload-btn2" class="btn jjblue" >업로드</a>
                <a role="button" id="upload-close-btn2" class="btn jjblue">닫기</a>
            </div>
        </div>
    </div>
</div>

<script>
    let attaches2 = [];

    Dropzone.options.dropzone2 = {
        url: contextPath + 'project/document/upload.do',
        autoProcessQueue: false,
        maxFilesize: 10,
        maxFiles: 1,
        parallelUploads: 2,
        addRemoveLinks: true,
        acceptedFiles: "image/*, application/zip, application/x-7z-compressed, application/x-tar, application/x-rar-compressed, application/x-bzip, application/x-bzip2",
        dictRemoveFile: '<img class="eximg pointer" src="' + contextPath + 'common/img/delete.png" alt="삭제">',
        uploadMultiple: true,
        init: dzInit2
    };

    document.getElementById('upload-modal-btn2').addEventListener('click', function () {
        if (document.getElementById('files2').childElementCount >= 1) {
            alert('최대 1개의 파일까지 업로드 할 수 있습니다.');
            return false;
        }

        openModal('upload-modal2');
    });

    document.getElementById('upload-close-btn2').addEventListener('click', function () {
        const wrap = document.getElementById('dropzone2');
        const divs = wrap.getElementsByClassName('dz-preview').length;
        const completeDivs = wrap.getElementsByClassName('dz-complete').length;

        if (divs !== completeDivs) {
            if (!confirm("업로드 하지 않은 파일이 있습니다. 창을 닫으시겠습니까?")) return false;
        }

        closeModal('upload-modal2');
    });

    document.addEventListener('DOMContentLoaded', function () {
        document.getElementById('dropzone2').addEventListener('drop', function () {
            alert_yn = true;
        }, true);
    });

    const uploadSuccess2 = function (files, response) {
        let div = "";

        let e = response[0];
        let uuid = files[0].upload.uuid;

        div += '<div class="filelist mt5 mb10 clearfix">';
        div += '<a class="file floatleft" data-id="' + uuid + '" data-path="' + e.path + e.storedFileName + '">';
        div += e.originalFileName;
        div += '</a>';
        div += '<img class="ml15 eximg pointer" src="' + contextPath + 'common/img/delete.png" alt="삭제" onclick="cancelAttach(this, attaches);">';
        div += '</div>';

        attaches2.push(e);
        document.getElementById('files2').innerHTML += div;
    }
</script>