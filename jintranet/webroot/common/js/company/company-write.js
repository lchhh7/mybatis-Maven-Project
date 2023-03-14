const writeForm = document.getElementById('write-form');

const writeCompany = function () {
    if (!confirm('업체를 등록하시겠습니까?')) return false;

    const data = confirmCompany(writeForm);
    if (data === false) return false;

    $.ajax({
        url: contextPath + 'admin/company/write.do',
        method: 'post',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=utf-8'
    })
        .done(function (data) {
            alert('업체를 정상적으로 등록했습니다.');
            location.href = contextPath + "admin/company/edit.do?id=" + data;
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });
};

document.getElementById('write-btn').addEventListener('click', writeCompany, true);