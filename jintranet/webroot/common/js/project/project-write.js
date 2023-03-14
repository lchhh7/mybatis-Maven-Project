document.addEventListener("DOMContentLoaded", function () {
    changeConYn();
    changeSubYn();
});

const save = function () {
    if (!confirm('신규 프로젝트를 등록하시겠습니까?')) return false;

    const data = confirmProject();
    if (data === false) return false;

    $.ajax({
        url: contextPath + "project/write.do",
        method: 'post',
        data: JSON.stringify(data),
        contentType: 'application/json; charset:UTF-8'
    })
        .done(function (data) {
            alert('프로젝트를 정상적으로 등록했습니다.');
            location.href= contextPath + "project/edit.do?id=" + data;
        })
        .fail(function (data) {
            alert(data.responseText);
        })
}

document.getElementById('save-btn').addEventListener('click', save);