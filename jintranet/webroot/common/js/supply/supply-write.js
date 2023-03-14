const writeForm = document.getElementById('write-form');

const writeSupply = function () {
    if (!confirm('비품을 신청하시겠습니까?')) return false;

    const data = confirmSupply(writeForm);
    if (data === false) return false;

    $.ajax({
        url: contextPath + 'supply/write.do',
        method: 'post',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=utf-8'
    })
        .done(function (data) {
            alert('비품을 정상적으로 신청했습니다.');
            location.href = contextPath + "supply/view.do?id=" + data;
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });
};

document.getElementById('write-btn').addEventListener('click', writeSupply, true);

