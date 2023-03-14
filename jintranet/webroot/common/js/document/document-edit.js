const save = function () {
    if (!confirm('발급된 문서를 수정하시겠습니까?')) return false;

    $.ajax({
        url: contextPath + "document/edit.do",
        method: 'post',
        data: JSON.stringify({
			id : documentEditForm.editId.value,
			title : documentEditForm.title.value,
			documentNo : documentEditForm.documentNo.value,
			documentDt : documentEditForm.documentDt.value,
			projectId  : documentEditForm.searchProject.value
		}),
        contentType: 'application/json; charset:UTF-8'
    })
        .done(function (data) {
            alert(data);
            location.reload();
        })
        .fail(function (data) {
            alert(data.responseText);
        })
}

document.getElementById('save-btn').addEventListener('click', save);

const del = function () {
	 if (!confirm('발급된 문서를 삭제하시겠습니까?')) return false;
    $.ajax({
        url: contextPath + "document/delete.do",
        method: 'post',
        data: JSON.stringify({
			id : documentEditForm.editId.value	
		}),
        contentType: 'application/json; charset:UTF-8'
    })
        .done(function (data) {
            alert(data);
            location.href = contextPath + 'document.do';
        })
        .fail(function (data) {
            alert(data.responseText);
        })
};

const documentSetting = function (dt) {
	
    $.ajax({
        url: contextPath + '/document/info.do?dt=' + dt,
        method: 'get'
    })
        .done(function (data) {
            documentEditForm.documentDt.value = dt;
           // documentEditForm.documentNo.value = data.documentNo;

        })
        .fail(function () {
            alert('문서번호 조회 중 오류가 발생했습니다.');
            return false;
        });
}
documentEditForm.documentDt.addEventListener('change', function () {
    //documentSetting(this.value);
}, true);

document.getElementById('del-btn').addEventListener('click', del);