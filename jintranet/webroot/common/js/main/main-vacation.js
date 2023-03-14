$('#search-year').val(new Date().getYear()+1900).prop("selected",true);

const searchType = document.getElementById('search-type');
const searchYear = document.getElementById('search-year');
const editForm = document.getElementById('edit-form');

const mainVacation = function () {
    $.ajax({
        url: contextPath + 'main/schedule.do',
        method: 'get'
    })
        .done(function (data) {
            const schedule = data.schedule;
            let info, status;
            if (schedule === undefined) {
                info = '신청한 휴가가 없습니다.'
                status = '';
            } else {
                // info = schedule.crtDt + '에 신청한 ' + schedule.typeName;
                info = schedule.startDt.replaceAll('-', '.') + ' ' + schedule.typeName;
                status = schedule.statusName;
            }
            document.getElementById('vacation-info').innerText = info;
            document.getElementById('vacation-status').innerText = status;
            document.getElementById('vacation-cnt').innerText = data.cnt + '건';
        })
        .fail(function () {
            alert('신청 휴가 조회 중 오류가 발생했습니다.');
            return false;
        });
};

const searching = function () {
	let url = '';
	url += ('st=' + (searchType != null ? searchType.value : ''));
	url += ('&y=' + (searchYear !=null ? searchYear.value : ''));
	
	 $.ajax({
        url: contextPath + 'main/searching.do?' + url,
        method: 'get'
    })
        .done(function (data) {
            let tr = '';
            if (data.length > 0) {
                data.forEach(function (e) {
					
                    tr += '<tr class="tbbody" onclick="schedule('+ e.id +')">';
                    tr += '<td>' + e.typeName + '</td>';
                    tr += '<td>' + e.startDt.replaceAll('-','.') + '~' + e.endDt.replaceAll('-','.') + '</td>';
                    tr += '<td>' + formatDate(new Date(e.crtDt)).replaceAll('-','.') + '</td>';
                    tr += '<td>' + (e.approveName != null ? e.approveName : "-") + '</td>';
                    tr += '<td><span>' + e.statusName + '</span></td>';
                    tr += '</tr>';
                });

            } else {
                tr += '<tr><td colspan="5">신청한 휴가가 없습니다.</td></tr>'
            }

            document.getElementById('vacation-tbody').innerHTML = tr;
        })
        .fail(function () {
            alert('신청 휴가 조회 중 오류가 발생했습니다.');
            return false;
        });

	openModal('vacation-modal')
}

//schedule-common.js 와 일부 다름
const schedule = function (id) {
	$.ajax({
		url: contextPath + 'schedule/' + id + '.do',
		method: 'get',
		dataType: 'json',
		contentType: 'application/json; charset=utf-8'
	})
		.done(function (e) {
			
			editForm.id.value = e.id;
			editForm.type.value = e.type;
			editForm.memberName.value = e.memberName;
			editForm.typeName.value = e.typeName;
			editForm.startDt.value = e.startDt;
			editForm.startTm.value = e.startTm;
			editForm.endDt.value = e.endDt;
			editForm.endTm.value = e.endTm;
			editForm.title.value = e.title;
			editForm.content.value = e.content;
			editForm.approveName.value = e.approveName;
			editForm.statusName.value = e.statusName;
			editForm.color.jscolor.fromString(e.color);
			editForm.cancelReason.value = e.cancelReason;

			displayEditModal(e);

			openModal('edit-modal');
		})
		.fail(function (data) {
			alert('일정 정보 조회 중 오류가 발생했습니다.');
			return false;
		});
};

// schedule-edit.js 의 CRUD와 완전히 동일함
const cancelSchedule = function () {
    if (!confirm('일정을 취소요청하시겠습니까?')) return false;

    $.ajax({
        url: contextPath + 'schedule/cancel/' + editForm.id.value + '.do',
        method: 'patch',
        data: JSON.stringify({
            id: editForm.id.value,
            cancelReason: editForm.cancelReason.value
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

const deleteSchedule = function () {
    if (!confirm('일정을 삭제하시겠습니까?')) return false;

    $.ajax({
        url: contextPath + 'schedule/' + editForm.id.value + '.do',
        method: 'delete',
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

const editSchedule = function () {
    if (!confirm('일정을 수정하시겠습니까?')) return false;
	
	const data = confirmSchedule(editForm);
    if (data === false) return false;
	
    $.ajax({
        url: contextPath + 'schedule/' + data.id + '.do',
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

// schedule-edit.js 의 displayEditModal 과 일부 다름
const displayEditModal = function (d) {
    const approveDiv = document.getElementById('approve-div');
    approveDiv.classList.add('none');

    if (d.type === 'FV' || d.type === 'HV' || d.type === 'OT') {
        approveDiv.classList.remove('none');
    }

    const reason = document.getElementById('reason');
    reason.classList.add('none');

    const div = document.getElementById('edit-btn-box');
    while (div.firstChild) {
        div.removeChild(div.firstChild);
    }


    // 동일인이면 수정,삭제버튼 및 input창 readonly해제
        if (d.status === 'R') {
            editForm.startTm.removeAttribute('readonly');
            editForm.endTm.removeAttribute('readonly');
            editForm.title.removeAttribute('readonly');
            editForm.content.removeAttribute('readonly');

            // 수정, 삭제
            let editBtn = document.createElement('a');
            editBtn.setAttribute('role', 'button');
            editBtn.classList.add('btn');
            editBtn.classList.add('jjblue');
            editBtn.classList.add('mr5');
            editBtn.innerText = '수정';
            editBtn.addEventListener('click', editSchedule, true);
            div.appendChild(editBtn);

            let delBtn = document.createElement('a');
            delBtn.setAttribute('role', 'button');
            delBtn.classList.add('btn');
            delBtn.classList.add('jjred');
            delBtn.classList.add('mr5');
            delBtn.innerText = '삭제';
            delBtn.addEventListener('click', deleteSchedule, true);
            div.appendChild(delBtn);
        } else if (d.status === 'N') {
            let delBtn = document.createElement('a');
            delBtn.setAttribute('role', 'button');
            delBtn.classList.add('btn');
            delBtn.classList.add('jjred');
            delBtn.classList.add('mr5');
            delBtn.innerText = '삭제';
            delBtn.addEventListener('click', deleteSchedule, true);
            div.appendChild(delBtn);
        } else if (d.status !== 'C') {
            reason.classList.remove('none');

            // 삭제 요청
            let cancelBtn = document.createElement('a');
            cancelBtn.setAttribute('role', 'button');
            cancelBtn.classList.add('btn');
            cancelBtn.classList.add('jjred');
            cancelBtn.classList.add('mr5');
            cancelBtn.innerText = '삭제요청';
            cancelBtn.addEventListener('click', cancelSchedule, true);
            div.appendChild(cancelBtn);
        }

    let closeBtn = document.createElement('a');
    closeBtn.setAttribute('role', 'button');
    closeBtn.classList.add('btn');
    closeBtn.classList.add('jjblue');
    closeBtn.innerText = '닫기'
    closeBtn.addEventListener('click', function () {
        closeModal('edit-modal');
    }, true);

    div.appendChild(closeBtn);
}

//schedule-common.js 의 confirmSchedule 와 완전 동일함
const confirmSchedule = function (form) {
	const requiredFields = [
		form.type, form.startDt, form.endDt,
		form.startTm, form.endTm,
		form.title, form.content, form.color
	];

	if (confirmRequiredField(requiredFields) === false) return false;

	let data = {
		type: form.type.value,
		startDt: form.startDt.value,
		endDt: form.endDt.value,
		startTm: form.startTm.value,
		endTm: form.endTm.value,
		title: form.title.value,
		content: form.content.value,
		color: form.color.value
	};

	if (form.getAttribute('id') === 'write-form') {
		if (form.type.value === 'VA') {
			if (form.vacationType.value === '') {
				alert('휴가 종류를 선택해주세요.');
				return false;
			}

			if (form.approveId.value === '') {
				alert('결제자를 선택해주세요.');
				return false;
			}

			data.vacationType = form.vacationType.value;
			data.approveId = form.approveId.value;
		} else if (form.type.value === 'OT') {
			if (form.overtimeApproveId.value === '') {
				alert('결재자를 선택해주세요.');
				return false;
			}
			data.approveId = form.overtimeApproveId.value;
		}
	}

	return isEditForm(data, form);
};

document.getElementById('vacation-btn').addEventListener('click', searching, true);
document.getElementById('vacation-close-btn').addEventListener('click', function () {
    closeModal('vacation-modal');
});