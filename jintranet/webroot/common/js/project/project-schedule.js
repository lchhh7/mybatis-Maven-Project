const deleteSchedule = function () {
    if (!confirm(editForm.title.value + " 일정을 삭제하시겠습니까?")) return false;

    $.ajax({
        url: contextPath + 'project/schedule.do',
        method: 'delete',
        data: JSON.stringify({id: editForm.id.value}),
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

document.getElementById('delete-btn').addEventListener('click', deleteSchedule, true);

const editSchedule = function () {
    if (!confirm("일정 정보를 수정하시겠습니까?")) return false;

    const data = confirmSchedule(editForm);
    if (data === false) return false;

    $.ajax({
        url: contextPath + 'project/schedule/' + data.id + '.do',
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

document.getElementById('edit-btn').addEventListener('click', editSchedule, true);

const openEditModal = function (id) {
    $.ajax({
        url: contextPath + 'project/schedule/' + id + '.do',
        method: 'get',
    })
        .done(function (data) {
            editForm.id.value = data.id;
            editForm.kind.value = data.kind;
            editForm.title.value = data.title;
            editForm.scheduleStartDt.value = data.scheduleStartDt;
            editForm.scheduleStartTm.value = data.scheduleStartTm;
            editForm.scheduleEndDt.value = data.scheduleEndDt;
            editForm.scheduleEndTm.value = data.scheduleEndTm;
            editForm.place.value = data.place;
            editForm.attendee.value = data.attendee;
            editForm.remark.value = data.remark;

            openModal('edit-modal');
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });

}

const confirmSchedule = function (form) {
    let requiredFields = [
        form.kind, form.title, form.scheduleStartDt, form.scheduleEndDt
    ];

    if (confirmRequiredField(requiredFields) === false) return false;

    if (form.scheduleStartDt.value > form.scheduleEndDt.value) {
        alert("일정 시작일은 종료일보다 늦을 수 없습니다.");
        return false;
    }

    let result = {
        projectId: projectId,
        kind: form.kind.value,
        title: form.title.value,
        scheduleStartDt: form.scheduleStartDt.value,
        scheduleStartTm: form.scheduleStartTm.value,
        scheduleEndDt: form.scheduleEndDt.value,
        scheduleEndTm: form.scheduleEndTm.value,
        place: form.place.value,
        attendee: form.attendee.value,
        remark: form.remark.value
    };

    if (form.getAttribute('id') === 'edit-form') {
        result.id = form.id.value
    }

    return result
};

const writeSchedule = function () {
    if (!confirm('일정 정보를 등록하시겠습니까?')) return false;

    const data = confirmSchedule(writeForm);
    if (data === false) return false;

    $.ajax({
        url: contextPath + 'project/schedule.do',
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

document.getElementById('write-btn').addEventListener('click', writeSchedule, true);

const openWriteModal = function () {
    const inputs = writeForm.getElementsByTagName('input');
    Array.prototype.forEach.call(inputs, function (input) {
        input.value = "";
    });

    const boxes = writeForm.getElementsByTagName('select');
    Array.prototype.forEach.call(boxes, function (box) {
        box.value = "";
    });

    openModal('write-modal');
};

document.getElementById('add-btn').addEventListener('click', openWriteModal, true);

document.getElementById('edit-close-btn').addEventListener('click', function () {
    closeModal('edit-modal');
}, true);
document.getElementById('write-close-btn').addEventListener('click', function () {
    closeModal('write-modal');
}, true);


const schedules = function () {
    const searchKind1 = document.getElementById('search-kind1');
    const searchKind2 = document.getElementById('search-kind2');
    const searchKind3 = document.getElementById('search-kind3');
    $.ajax({
        url: contextPath + 'project/schedules.do',
        method: 'post',
        data: JSON.stringify({
            id: projectId,
            searchYear: targetYear,
            searchKind1: searchKind1.checked ? searchKind1.value : "",
            searchKind2: searchKind2.checked ? searchKind2.value : "",
            searchKind3: searchKind3.checked ? searchKind3.value : ""
        }),
        dataType: 'json',
        contentType: 'application/json; charset=utf-8'
    })
        .done(function (data) {
            const table = document.getElementById('schedule-table');
            const tds = table.getElementsByTagName('td');
            Array.prototype.forEach.call(tds, function (td) {
                td.innerHTML = "";
            });

            let kind1Cnt = 0, kind2Cnt = 0, kind3Cnt = 0;

            data.forEach(function (e) {
                let html = "";
                let id = (new Date(e.scheduleStartDt).getMonth() + 1) + '-' + getWeekOfMonth(new Date(e.scheduleStartDt));
                let el = document.getElementById(id);

                let className = "";
                if (e.kind === searchKind1.value) {
                    className = 'schyellow';
                    kind1Cnt++;
                } else if (e.kind === searchKind2.value) {
                    className = 'schpurple';
                    kind2Cnt++;
                } else if (e.kind === searchKind3.value) {
                    className = 'schgreen';
                    kind3Cnt++;
                }

                html += '<div class="' + className + '" onclick="openEditModal(' + e.id +')">';
                html += '<p class="schtb_tit">' + e.title + '</p>';
                html += '<p class="schtb_time">' + e.scheduleStartDt + '(' + getDayOfWeek(e.scheduleStartDt) +') ' + e.scheduleStartTm + '</p>';
                html += '<p class="schtb_place">' + e.place + '</p>';
                html += '</div>';

                el.innerHTML += html;
            });
            document.getElementById('kind1-cnt').innerText = '프로젝트관리(' + kind1Cnt + '건)';
            document.getElementById('kind2-cnt').innerText = '회의(' + kind2Cnt + '건)';
            document.getElementById('kind3-cnt').innerText = '설명회/교육(' + kind3Cnt + '건)';
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });
};

document.getElementById('search-kind1').addEventListener('change', schedules, true);
document.getElementById('search-kind2').addEventListener('change', schedules, true);
document.getElementById('search-kind3').addEventListener('change', schedules, true);

document.addEventListener('DOMContentLoaded', function () {
    schedules();
    targetYearDiv.innerText = targetYear + "년";
});

document.getElementById('year-minus').addEventListener('click', function () {
    targetYear--;
    targetYearDiv.innerHTML = targetYear + "년";
    schedules();
}, true);

document.getElementById('year-plus').addEventListener('click', function () {
    targetYear++;
    targetYearDiv.innerHTML = targetYear + "년";
    schedules();
}, true);