const notices = function (p) { 
    const searchType = document.getElementById('search-type').value;
    const searchContent = document.getElementById('search-content').value;
    let url = ""

    if (searchType === "searchTitle") {
        url = "&t=" + searchContent;

    } else if (searchType === "searchCrtId") {
        url = "&m=" + searchContent;

    } else {
        alert('유효하지 않은 접근입니다.');
        return false;
    }

    $.ajax({
        url: contextPath + 'notice/search.do?p=' + p + url,
        method: 'get',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
    })
        .done(function (data) {
	console.log(data);
            let tr = "";

            const list = data.list;
            if (list.length > 0) {
                list.forEach(function (e) {
                    tr += '<tr class="tbbody" onclick="location.href=\'' + contextPath + 'notice/view.do?id=' + e.id + '\'">';
                    tr += '<td>' + e.rnum + '</td>';
                    tr += '<td>' + e.title + '</td>';
                    tr += '<td>' + e.memberName + '</td>';
                    tr += '<td>';
                    if (e.attachYN === "Y") {
                        tr += '<img src="' + contextPath +'common/img/file.png" alt="첨부파일">';
                    }
                    tr += '</td>';
                    tr += '<td>' + e.crtDt + '</td>';
                    tr += '</tr>';
                });
            } else {
                tr += "<tr><td colspan='5'>등록된 공지사항이 없습니다.</td></tr>";
            }

            document.getElementById('notice').innerHTML = tr;
            document.getElementById('page').innerHTML = data.page;
            document.getElementById('total-cnt').innerText = "총 게시물 " + data.totalCnt + "건";
        })
        .fail(function () {
            alert("정보 조회 중 오류가 발생했습니다.");
            return false;
        });
};

document.getElementById('search-btn').addEventListener('click', function () {
    notices(1);
}, true);

document.addEventListener('DOMContentLoaded', function () {
   notices(1);
});
