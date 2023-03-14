const supplies = function (p) {
    const status1 = document.getElementById('search-status1');
    const status2 = document.getElementById('search-status2');
    const status3 = document.getElementById('search-status3');

    if (!(status1.checked || status2.checked || status3.checked)) {
        alert('승인여부 조건을 최소 하나이상 선택해주세요.');
        return false;
    }

    let url = "";
    url += ("&s1=" + (status1.checked ? status1.value : ""));
    url += ("&s2=" + (status2.checked ? status2.value : ""));
    url += ("&s3=" + (status3.checked ? status3.value : ""));


    const searchBox = document.getElementById('search-select');
    switch (searchBox.value) {
        case '1':
            url += ("&n=" + document.getElementById('search-name').value);
            break;
        case '2':
            url += ("&m=" + document.getElementById('search-member-id').value);
            break;
        default:
            alert('유효하지 않은 접근입니다.');
            return false;
    }
	
    $.ajax({
        url: contextPath + 'supply/search.do?p=' + p + url,
        method: 'get',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8'
    })
        .done(function (data) {
            const list = data.list;
            let tr = "";
            if (list.length > 0) {
                list.forEach(function (e) {
                    tr += '<tr class="tbbody" onclick="location.href=\'' + contextPath + 'supply/view.do?id=' + e.id + '\'">';
                    tr += '<td>' + e.rnum + '</td>';
                    tr += '<td>' + e.name + '</td>';
                    tr += '<td>' + e.amount.toLocaleString('ko-KR') + ' 개</td>';
                    tr += '<td>' + (e.amount * e.price + e.shippingFee).toLocaleString('ko-KR') + ' 원</td>';
                    tr += '<td>' + e.memberName + '</td>';
                    tr += '<td>' + e.approveName + '</td>';
                    tr += '<td>' + e.statusName + '</td>';
                    tr += '</tr>';
                });
            } else {
                tr += "<tr><td colspan='7'>등록된 비품신청 내역이 없습니다.</td></tr>";
            }
            document.getElementById('supply').innerHTML = tr;
            document.getElementById('page').innerHTML = data.page;
            document.getElementById('total-cnt').innerText = "총 게시물 " + data.totalCnt + "건";
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });
        
};

document.getElementById('search-btn').addEventListener('click', function () {
    supplies(1);
}, true);

document.addEventListener('DOMContentLoaded', function () {
    supplies(1);
});

document.getElementById('search-select').addEventListener('change', function () {
    let input = document.getElementById('search-name');
    let box = document.getElementById('search-member-id');
    let v = this.value;
    if (v === "1") {
        input.classList.remove('none');
        box.classList.add('none');
    } else {
        input.classList.add('none');
        box.classList.remove('none');
    }
});