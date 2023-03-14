const save = function () {
    if (!confirm('프로젝트 정보를 수정하시겠습니까?')) return false;

    let data = confirmProject();
    if (data === false) return false;

    $.ajax({
        url: contextPath + "project/edit.do",
        method: 'post',
        data: JSON.stringify(data),
        contentType: 'application/json; charset:UTF-8'
    })
        .done(function (data) {
            alert(data);
            location.reload();
        })
        .fail(function (data) {
            alert(data.responseText);
        })
}

document.getElementById('save-btn').addEventListener('click', save);

const del = function () {
    $.ajax({
        url: contextPath + "project.do",
        method: 'delete',
        data: JSON.stringify({id: document.project.id.value}),
        contentType: 'application/json; charset:UTF-8'
    })
        .done(function (data) {
            alert(data);
            location.href = contextPath + 'project.do';
        })
        .fail(function (data) {
            alert(data.responseText);
        })
};

document.getElementById('del-btn').addEventListener('click', del);
