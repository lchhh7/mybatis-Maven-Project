const confirmSegment = function (form) {
    let requiredFields = [
        form.kind, form.title
    ];

    if (confirmRequiredField(requiredFields) === false) return false;

    let result = {
        projectId: projectId,
        kind: form.kind.value,
        title: form.title.value,
        ord: form.ord.value
    };

    if (form.getAttribute('id') === 'edit-form') {
        result.id = form.id.value
    }

    return result
};

// 추가
const writeSegment = function () {
    if (!confirm("세그먼트 정보를 추가하시겠습니까?")) return false;

    const data = confirmSegment(writeForm);
    if (data === false) return false;

    $.ajax({
        url: contextPath + 'project/tailoring.do',
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
            return false;
        });
};

document.getElementById('write-btn').addEventListener('click', writeSegment, true);
const openWriteModal = function () {
    const inputs = writeForm.getElementsByTagName('input');
    const select = writeForm.getElementsByTagName('select')[0];

    Array.prototype.forEach.call(inputs, function (input) {
        if (input.type === 'text') input.value = '';
        else if (input.type === 'checkbox') input.checked = false;
    });

    select.value = "";

    openModal('write-modal');
};

document.getElementById('add-btn').addEventListener('click', openWriteModal, true);

// 수정 및 삭제
const openEditModal = function (id) {
    $.ajax({
        url: contextPath + 'project/tailoring/' + id + '.do',
        method: 'get',
    })
        .done(function (data) {
            editForm.id.value = data.id;
            editForm.kind.value = data.kind;
            editForm.title.value = data.title;
            editForm.ord.value = data.ord;
        })
        .fail(function () {
            alert('투입인력 상세정보 조회 중 오류가 발생했습니다.');
            return false;
        });

    openModal('edit-modal');
}

const editSegment = function () {
    if (!confirm("투입인력 정보를 수정하시겠습니까?")) return false;

    const data = confirmSegment(editForm);
    if (data === false) return false;

    $.ajax({
        url: contextPath + 'project/tailoring/' + data.id + '.do',
        method: 'patch',
        data: JSON.stringify(data),
        contentType: 'application/json; charset:UTF-8'
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

document.getElementById('edit-btn').addEventListener('click', editSegment, true);

const deleteSegment = function (id, name) {
    if (!confirm(name + '을 삭제하시겠습니까?')) return false;

    $.ajax({
        url: contextPath + 'project/tailoring.do',
        method: 'delete',
        data: JSON.stringify({id: id}),
        contentType: 'application/json; charset:UTF-8'
    })
        .done(function (data) {
            alert(data);
            location.reload();
        })
        .fail(function (data) {
            alert(data.responseText);
        });
};


document.getElementById('edit-close-btn').addEventListener('click', function () {
    closeModal('edit-modal');
}, true);
document.getElementById('write-close-btn').addEventListener('click', function () {
    closeModal('write-modal');
}, true);

