const editStatus = function (status) {
    const statusName = status === 'Y' ? '승인' : status === 'N' ? '비승인' : '승인 대기';

    if (!confirm('비품요청 내역을 ' + statusName + ' 상태로 변경하시겠습니까?')) return false;

    $.ajax({
        url: contextPath + 'supply/edit/status.do',
        method: 'patch',
        data: JSON.stringify({
            id: viewForm.id.value,
            status: status
        }),
        contentType: 'application/json; charset=utf-8'
    })
        .done(function (data) {
            alert(data);
            location.reload();
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });
};

document.getElementById('approve-btn').addEventListener('click', function() {
    editStatus('Y')
}, true);
document.getElementById('refuse-btn').addEventListener('click', function() {
    editStatus('N')
}, true);
document.getElementById('ready-btn').addEventListener('click', function() {
    editStatus('R')
}, true);

