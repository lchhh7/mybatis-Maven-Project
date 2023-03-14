// 추가
const confirmMember = function (form) {
    let requiredFields = [
        form.role, form.action, form.manStartDt, form.manEndDt
    ];

    if (confirmRequiredField(requiredFields) === false) return false;

    if (form.manStartDt.value > form.manEndDt.value) {
        alert("투입 시작일은 종료일보다 늦을 수 없습니다.");
        return false;
    }

    let result = {
        projectId: projectId,
        companyId: form.companyId.value,
        memberName: form.memberName.value,
        role: form.role.value,
        action: form.action.value,
        departmentName: form.departmentName.value,
        manMonth: form.manMonth.value,
        manStartDt: form.manStartDt.value,
        manEndDt: form.manEndDt.value
    };

    if (form.getAttribute('id') === 'edit-form') {
        result.id = form.id.value
    }

    return result
};
const writeMember = function () {
    if (!confirm("투입인력 정보를 저장하시겠습니까?")) return false;

    const data = confirmMember(writeForm);
    if (data === false) return false;

    $.ajax({
        url: contextPath + 'project/member.do',
        method: 'post',
        data: JSON.stringify(data),
        contentType: 'application/json; charset:utf-8'
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

document.getElementById('write-btn').addEventListener('click', writeMember, true);

writeForm.allDay.addEventListener('click', function () {
    if (this.checked) {
        writeForm.manStartDt.value = formatProjectDate(startDt);
        writeForm.manEndDt.value = formatProjectDate(endDt);
    }
});
writeForm.manStartDt.addEventListener('change', function () {
    if (this.value !== formatProjectDate(startDt)) {
        writeForm.allDay.checked = false;
    } else {
        if (writeForm.manEndDt.value === formatProjectDate(endDt)) {
            writeForm.allDay.checked = true;
        }
    }
});
writeForm.manEndDt.addEventListener('change', function () {
    if (this.value !== formatProjectDate(endDt)) {
        writeForm.allDay.checked = false;
    } else {
        if (writeForm.manStartDt.value === formatProjectDate(startDt)) {
            writeForm.allDay.checked = true;
        }
    }
});
const openWriteModal = function () {
    const inputs = writeForm.getElementsByTagName('input');
    const selectboxes = writeForm.getElementsByTagName('select');

    Array.prototype.forEach.call(inputs, function (input) {
        if (input.type === 'text') input.value = '';
        else if (input.type === 'checkbox') input.checked = false;
    });

    Array.prototype.forEach.call(selectboxes, function (selectbox) {
        selectbox.value = '';
    });

    openModal('write-modal');
};
document.getElementById('add-btn').addEventListener('click', openWriteModal, true);
// 수정 및 삭제
const openEditModal = function (id) {
    $.ajax({
        url: contextPath + 'project/member/' + id + '.do',
        method: 'get',
    })
        .done(function (data) {
            editForm.id.value = data.id;
            editForm.companyId.value = data.companyId;
            editForm.memberName.value = data.memberName;
            editForm.role.value = data.role;
            editForm.action.value = data.action;
            editForm.departmentName.value = data.departmentName;
            editForm.manMonth.value = data.manMonth;
            editForm.manStartDt.value = data.manStartDt;
            editForm.manEndDt.value = data.manEndDt;

            if (data.manStartDt === data.startDt && data.manEndDt === data.endDt)
                editForm.allDay.checked = true;
        })
        .fail(function () {
            alert('투입인력 상세정보 조회 중 오류가 발생했습니다.');
            return false;
        });

    openModal('edit-modal');
}


const editMember = function () {
    if (!confirm("투입인력 정보를 수정하시겠습니까?")) return false;

    const data = confirmMember(editForm);
    if (data === false) return false;

    $.ajax({
        url: contextPath + 'project/member/' + data.id + '.do',
        method: 'patch',
        data: JSON.stringify(data),
        contentType: 'application/json; charset:utf-8'
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

document.getElementById('edit-btn').addEventListener('click', editMember, true);

const deleteMember = function (id, name) {
    if (!confirm(name + '의 투입내역을 삭제하시겠습니까?')) return false;

    $.ajax({
        url: contextPath + 'project/member.do',
        method: 'delete',
        data: JSON.stringify({id: id}),
        contentType: 'application/json; charset:utf-8'
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

const formatProjectDate = function (date) {
    const arr = [
        date.substring(0, 4), date.substring(4, 6), date.substring(6, 8)
    ]
    return arr[0] + "-" + arr[1] + "-" + arr[2];
}

editForm.allDay.addEventListener('click', function () {
    if (this.checked) {
        editForm.manStartDt.value = formatProjectDate(startDt);
        editForm.manEndDt.value = formatProjectDate(endDt);
    }
});
editForm.manStartDt.addEventListener('change', function () {
    if (this.value !== formatProjectDate(startDt)) {
        editForm.allDay.checked = false;
    } else {
        if (editForm.manEndDt.value === formatProjectDate(endDt)) {
            editForm.allDay.checked = true;
        }
    }
});
editForm.manEndDt.addEventListener('change', function () {
    if (this.value !== formatProjectDate(endDt)) {
        editForm.allDay.checked = false;
    } else {
        if (editForm.manStartDt.value === formatProjectDate(startDt)) {
            editForm.allDay.checked = true;
        }
    }
});