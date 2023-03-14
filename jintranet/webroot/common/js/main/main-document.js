const mainDocument = function () {
    $.ajax({
        url: contextPath + 'main/document.do?dt=' + formatDate(new Date()),
        method: 'get'
    })
        .done(function (data) {
            document.getElementById('document-no').innerText = data.documentNo;
            document.getElementById('document-cnt').innerText = data.documentCnt + '건';
        })
        .fail(function () {
            alert('문서번호 조회 중 오류가 발생했습니다.');
            return false;
        });
};

const documents = function (dt) {
    $.ajax({
        url: contextPath + 'main/document.do?dt=' + dt,
        method: 'get'
    })
        .done(function (data) {
            documentForm.documentDt.value = dt;
            documentForm.documentNo.value = data.documentNo;
            documentForm.title.value = '';

            let tr = '';
            if (data.documentCnt > 0) {
                data.documents.forEach(function (e) {
                    tr += '<tr onclick="editDocument(\'' + e.id + '\')">';
                    tr += '<td>' + e.rnum + '</td>';
                    tr += '<td>' + e.title + '</td>';
                    tr += '<td>' + e.memberName + '</td>';
                    tr += '<td onclick="event.cancelBubble=true"><a role="button" class="btn jjred" onclick="deleteDocument(\'' + e.id + '\')">삭제</a></td>';
                    tr += '</tr>';
                });
            } else {
                tr += '<tr><td colspan="4">발급된 문서가 없습니다.</td></tr>';
            }

            document.getElementById('document-tbody').innerHTML = tr;
        })
        .fail(function () {
            alert('문서번호 조회 중 오류가 발생했습니다.');
            return false;
        });
}

const pList = function () {
    $.ajax({
        url: contextPath + 'main/pList.do',
        method: 'get'
    })
        .done(function (data) {
            let tr = '';
			tr += '<option class="dpList" value="" selected>없음</option>';
            if (data.length > 0) {
                data.forEach(function (e) {
                    tr += '<option class="dpList" value= '+ e.id +'>' + e.title + '</option>';
                });
            } 
            document.getElementById('search-project').innerHTML = tr;
        })
        .fail(function () {
            alert('프로젝트 조회 중 오류가 발생했습니다.');
            return false;
        });
}

const writeDocument = function () {
    if(!confirm('문서번호를 발급하시겠습니까?')) return false;

    $.ajax({
        url: contextPath + 'main/document.do',
        method: 'post',
        data: JSON.stringify({
            title : documentForm.title.value,
            documentDt : documentForm.documentDt.value,
			projectId : documentForm.searchProject.value 
        }),
        contentType: 'application/json; charset=utf-8'
    })
        .done(function (data) {
            alert(data);
            mainDocument();
            documents(documentForm.documentDt.value);
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });
};
/*
const writeDocument = function () {
    if(!confirm('문서번호를 발급 페이지로 이동합니다.')) return false;
	
	dt = documentForm.documentDt.value;
	no = documentForm.documentNo.value;
	
	location.href = contextPath + "document/mainToWritePage.do?dt="+dt;
};*/

const editDocument = function (id) {
    if(!confirm('문서수정 페이지로 이동합니다.')) return false;
	location.href = contextPath + "document/edit.do?id="+id;
};

const deleteDocument = function (id) {
    if(!confirm('해당 문서를 삭제하시겠습니까?')) return false;

    $.ajax({
        url: contextPath + 'main/document/' + id +'.do',
        method: 'delete'
    })
        .done(function (data) {
            alert(data);
            mainDocument();
            documents(documentForm.documentDt.value);
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });

};

document.getElementById('document-write-btn').addEventListener('click', writeDocument, true);
documentForm.documentDt.addEventListener('change', function () {
    documents(this.value);
}, true);
document.getElementById('document-close-btn').addEventListener('click', function () {
    closeModal('document-modal');
}, true);
document.getElementById('document-btn').addEventListener('click', function() {
    documents(formatDate(new Date()));
	pList();
    openModal('document-modal');
}, true);