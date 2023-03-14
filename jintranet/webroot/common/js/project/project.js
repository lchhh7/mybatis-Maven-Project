const projects = function (p) {
    let url = "";
    url += ("&y=" + document.getElementById("searchYear").value);
    url += ("&s=" + document.getElementById("searchStartDt").value);
    url += ("&e=" + document.getElementById("searchEndDt").value);
    url += ("&t=" + document.getElementById("searchTitle").value);
    url += ("&o=" + document.getElementById("searchOrderingName").value);
    url += ("&d=" + document.getElementById("searchDepartment").value);

    $.ajax({
        url: contextPath + "project/search.do?p=" + p + url,
        method: "get",
        dataType: "json",
        contentType: "application/json; charset=utf-8"
    })
        .done(function (data) {
            let tr = "";

            const list = data.list;
            if (list.length > 0) {
                list.forEach(function(el) {
                    tr += '<tr class="tbbody" onclick="location.href=\'' + contextPath + 'project/edit.do?id=' + el.id +'\'">';
                    tr += '<td>' + el.rnum + '</td>';
                    tr += '<td>' + el.title + '</td>';
                    tr += '<td>' + el.orderingName + '</td>';
                    tr += '<td>' + el.departmentName + '</td>';
                    tr += '<td>' + el.startDt + ' ~ ' + el.endDt + '</td>';
                    tr += '<td>' + el.consortiumYN + '</td>';
                    tr += '<td>' + el.subcontractYN + '</td>';
                    tr += '</tr>';
                });
            } else {
                tr += '<tr><td colspan="7">조회된 프로젝트가 없습니다.</td></tr>'
            }

            document.getElementById("projects").innerHTML = tr;
            document.getElementById('page').innerHTML = data.page;
            document.getElementById('total-cnt').innerText = '총 게시물 ' + data.totalCnt + '건';
        })
        .fail(function () {
            alert("정보 조회 중 오류가 발생했습니다.");
            return;
        });
}

document.getElementById('search-btn').addEventListener('click', function () {
    projects(1);
});

document.addEventListener('DOMContentLoaded', function () {
    projects(1);
});