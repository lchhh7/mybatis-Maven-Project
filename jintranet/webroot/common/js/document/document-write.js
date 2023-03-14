
document.addEventListener("DOMContentLoaded", function () {
   // changeConYn();
    //changeSubYn();
});

const save = function () {
    if (!confirm('문서발급을 진행 하시겠습니까?')) return false;

    $.ajax({
        url: contextPath + "document/write.do",
        method: 'post',
        data: JSON.stringify({
			title : documentWriteForm.title.value,
			documentNo : documentWriteForm.documentNo.value,
			documentDt : documentWriteForm.documentDt.value,
			projectId  : documentWriteForm.searchProject.value
		}),
        contentType: 'application/json; charset:UTF-8'
    })
        .done(function (data) {
            alert('문서를 정상적으로 발급했습니다.');
            location.href= contextPath + "document.do";
        })
        .fail(function (data) {
            alert(data.responseText);
        })
}

const documentSetting = function (dt) {
	
    $.ajax({
        url: contextPath + '/document/info.do?dt=' + dt,
        method: 'get'
    })
        .done(function (data) {
            documentWriteForm.documentDt.value = dt;
            documentWriteForm.documentNo.value = data.documentNo;

        })
        .fail(function () {
            alert('문서번호 조회 중 오류가 발생했습니다.');
            return false;
        });
}
documentWriteForm.documentDt.addEventListener('change', function () {
    documentSetting(this.value);
}, true);

document.getElementById('save-btn').addEventListener('click', save);