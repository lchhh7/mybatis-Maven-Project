const editForm = document.getElementById('edit-form');

const editCompany = function () {
    if (!confirm('업체정보를 수정하시겠습니까?')) return false;

    const data = confirmCompany(editForm);
    if (data === false) return false;

    $.ajax({
        url: contextPath + 'admin/company/edit.do',
        method: 'patch',
        data: JSON.stringify(data),
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

document.getElementById('edit-btn').addEventListener('click', editCompany, true);

const deleteCompany = function () {
    if (!confirm('업체정보를 삭제하시겠습니까?')) return false;

    $.ajax({
        url: contextPath + 'admin/company/edit.do',
        method: 'delete',
        data: JSON.stringify({id: editForm.id.value}),
        contentType: 'application/json; charset:utf-8'
    })
        .done(function (data) {
            alert(data);
            location.href = contextPath + 'admin/company.do';
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });
};

document.getElementById('delete-btn').addEventListener('click', deleteCompany, true);

const deleteAttach = function (el, id) {
    if(!confirm('기존에 업로드 되었던 첨부파일을 삭제하면 복구할 수 없습니다.\n첨부파일을 삭제하시겠습니까?')) return false;

    $.ajax({
        url: contextPath + 'admin/company/attach.do?id=' + id,
        method: 'delete',
    })
        .done(function () {
            const div = el.parentNode;
            div.parentNode.removeChild(div);
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });
}