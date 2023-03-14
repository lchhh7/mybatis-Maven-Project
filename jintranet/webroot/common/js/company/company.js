const company = function (p) {
    const kind1 = document.getElementById('search-kind1');
    const kind2 = document.getElementById('search-kind2');

    if (!(kind1.checked || kind2.checked)) {
        alert('회사유형을 최소 하나이상 선택해주세요.');
        return false;
    }

    let url = "";
    url += ("&n=" + document.getElementById('search-name').value);
    url += ("&k1=" + (kind1.checked ? kind1.value : ""));
    url += ("&k2=" + (kind2.checked ? kind2.value : ""));

    $.ajax({
        url: contextPath + 'admin/company/search.do?p=' + p + url,
        method: 'get',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8'
    })
        .done(function (data) {
            let tr = "";

            const list = data.list;
            if (list.length > 0) {
                list.forEach(function (e) {
                    tr += '<tr class="tbbody" onclick="location.href=\'' + contextPath + 'admin/company/edit.do?id=' + e.id + '\'">';
                    tr += '<td>' + e.rnum + '</td>';
                    tr += '<td>' + e.companyNo + '</td>';
                    tr += '<td>' + e.companyName + '</td>';
                    tr += '<td>' + e.companyKindName + '</td>';
                    tr += '<td>' + e.licenseYN + '</td>';
                    tr += '<td>' + e.bankbookYN + '</td>';
                    tr += '</tr>';
                });
            } else {
                tr += "<tr><td colspan='5'>등록된 업체정보가 없습니다.</td></tr>";
            }

            document.getElementById('company').innerHTML = tr;
            document.getElementById('page').innerHTML = data.page;
            document.getElementById('total-cnt').innerText = '총 게시물 ' + data.totalCnt + '건';
        })
        .fail(function (data) {
            alert("정보 조회 중 오류가 발생했습니다.");
            return false;
        });
};

document.getElementById('search-btn').addEventListener('click', function () {
    company(1);
}, true);

document.addEventListener('DOMContentLoaded', function () {
    company(1);
}, true);