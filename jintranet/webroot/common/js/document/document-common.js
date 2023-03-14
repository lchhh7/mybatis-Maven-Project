const searchYear = document.getElementById('searchYear');
const searchStartDt = document.getElementById('searchStartDt');
const searchEndDt = document.getElementById('searchEndDt');
const searchDocumentName = document.getElementById('searchDocumentName');
const searchProjectName = document.getElementById('searchProjectName');



const documents = function (p) {
    let url = "";
    url += ("&sy=" +(searchYear != null ? searchYear.value : ''));
    url += ("&sdt=" + (searchStartDt != null ? searchStartDt.value : ''));
    url += ("&edt=" + (searchEndDt != null ? searchEndDt.value : ''));
    url += ("&sn=" + (searchDocumentName != null ? searchDocumentName.value : ''));
    url += ("&spn=" + (searchProjectName != null ? searchProjectName.value : ''));
	
    $.ajax({
        url: contextPath + "document/search.do?p=" + p + url,
        method: "get",
        dataType: "json",
        contentType: "application/json; charset=utf-8"
    })
        .done(function (data) {
			
            let tr = "";
			
			const list = data.list;
            if (list.length > 0) {
                list.forEach(function(el) {
					console.log(el);
                    tr += '<tr class="tbbody" onclick="location.href=\'' + contextPath + 'document/edit.do?id=' + el.id +'\'">';
                    tr += '<td>' + el.rnum + '</td>';
                    tr += '<td>' + el.title + '</td>';
                    tr += '<td>' + el.documentNo + '</td>';
                    tr += '<td>' + (el.projectName == null ? '-' : el.projectName)+ '</td>';
					tr += '<td>' + el.memberName + '</td>';
                    tr += '<td>' + el.documentDt + '</td>';
                    tr += '</tr>';
                });
            } else {
                tr += '<tr><td colspan="7">조회된 문서가 없습니다.</td></tr>'
            }

            document.getElementById("documents").innerHTML = tr;
            document.getElementById('page').innerHTML = data.page;
            document.getElementById('total-cnt').innerText = '총 게시물 ' + data.totalCnt + '건';
        })
        .fail(function () {
            alert("정보 조회 중 오류가 발생했습니다.");
            return;
        });
}

document.getElementById('search-btn').addEventListener('click', function () {
    documents(1);
});

document.addEventListener('DOMContentLoaded', function () {
    documents(1);
});

