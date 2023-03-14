$('#search-year').val(new Date().getYear() + 1900).prop("selected", true);

const searchType = document.getElementById('search-type');
const searchYear = document.getElementById('search-year');
const requestForm = document.getElementById('request-form');

const searching = function() {
	let url = '';
	url += ('st=' + (searchType != null ? searchType.value : ''));
	url += ('&y=' + (searchYear != null ? searchYear.value : ''));

	$.ajax({
		url: contextPath + 'commuting/searching.do?' + url,
		method: 'get'
	})
		.done(function(data) {
			let tr = '';
			if (data.length > 0) {
				data.forEach(function(e) {
					tr += '<tr class="tbbody" onclick="commuteRequest(' + e.id + ')">';
					tr += '<td>' + e.typeName + '</td>';
					tr += '<td>' + formatDate(new Date(e.requestDt)).replaceAll('-', '.') + '</td>';
					tr += '<td>' + formatDate(new Date(e.crtDt)).replaceAll('-', '.') + '</td>';
					tr += '<td>' + (e.approveName != null ? e.approveName : "-") + '</td>';
					tr += '<td><span>' + e.statusName + '</span></td>';
					tr += '</tr>';
				});

			} else {
				tr += '<tr><td colspan="5">신청한 출퇴근 기록이 없습니다.</td></tr>'
			}

			document.getElementById('apply-tbody').innerHTML = tr;
		})
		.fail(function() {
			alert('신청한 출퇴근 조회 중 오류가 발생했습니다.');
			return false;
		});

	openModal('apply-modal')
};



const commuteRequest = function(id) {
	$.ajax({
		url: contextPath + 'commuting/request/' + id + '.do',
		method: 'get',
		dataType: 'json',
		contentType: 'application/json; charset=utf-8'
	})
		.done(function(e) {
			requestForm.id.value = e.id;
			requestForm.memberName.value = e.memberName;
			requestForm.typeName.value = e.typeName;
			requestForm.requestDt.value = e.requestDt;
			requestForm.requestTm.value = e.requestTm;
			requestForm.content.value = e.content;
			requestForm.approveName.value = e.approveName;
			requestForm.statusName.value = e.statusName;
			requestForm.cancelReason.value = e.cancelReason;
			
			if(e.type == 'O'){
				if(new Date(e.requestDt).getDay() != 6 && new Date(e.requestDt).getDay() !=0) {
					requestForm.startTime2.value = e.startTm;
					requestForm.endTime2.value = e.endTm;
				}else{
					requestForm.startTime1.value = e.startTm;
					requestForm.endTime1.value = e.endTm;
				}
			}
			
			displayEditModal(e);
			openModal('request-modal');
		})
		.fail(function(data) {
			alert('일정 정보 조회 중 오류가 발생했습니다.');
			return false;
		});
};

const deleteRequest = function() {

	if (!confirm('요청을 삭제하시겠습니까?')) return false;

	$.ajax({
		url: contextPath + 'commuting/deleteRequest/' + requestForm.id.value + '.do',
		method: 'delete',
		contentType: 'application/json; charset=utf-8'
	})
		.done(function(data) {
			alert(data);
			location.reload();
		})
		.fail(function(data) {
			alert(data.responseText);
			return false;
		});
};

const editRequest = function() {
	if (!confirm('요청을 수정하시겠습니까?')) return false;

	if(new Date(requestForm.requestDt.value).getDay() != 6 && new Date(requestForm.requestDt.value).getDay() !=0) {
		$.ajax({
			url: contextPath + 'commuting/editRequest/' + requestForm.id.value + '.do',
			method: 'post',
			data: JSON.stringify({
				requestDt: requestForm.requestDt.value,
				requestTm: requestForm.requestTm.value,
				content: requestForm.content.value,
				startTm : requestForm.startTime2.value,
				endTm : requestForm.endTime2.value
			}),
			contentType: 'application/json; charset=utf-8'
		})
			.done(function(data) {
				alert(data);
				location.reload();
	
			})
			.fail(function(data) {
				alert(data.responseText);
				return false;
			});
	}else {
		$.ajax({
			url: contextPath + 'commuting/editRequest/' + requestForm.id.value + '.do',
			method: 'post',
			data: JSON.stringify({
				requestDt: requestForm.requestDt.value,
				requestTm: requestForm.requestTm.value,
				content: requestForm.content.value,
				startTm : requestForm.startTime1.value,
				endTm : requestForm.endTime1.value
			}),
			contentType: 'application/json; charset=utf-8'
		})
			.done(function(data) {
				alert(data);
				location.reload();
	
			})
			.fail(function(data) {
				alert(data.responseText);
				return false;
			});
	}
};

const displayEditModal = function(d) {
	const approveDiv = document.getElementById('approve-div');
	approveDiv.classList.remove('none');

	const reason = document.getElementById('reason');
	reason.classList.add('none');

	const div = document.getElementById('request-btn-box');
	while (div.firstChild) {
		div.removeChild(div.firstChild);
	}


	if (d.status === 'R') {
		requestForm.requestDt.removeAttribute('readonly');
		requestForm.requestTm.removeAttribute('readonly');
		requestForm.content.removeAttribute('readonly');
		requestForm.cancelReason.removeAttribute('readonly');

		let editBtn = document.createElement('a');
		editBtn.setAttribute('role', 'button');
		editBtn.classList.add('btn');
		editBtn.classList.add('jjblue');
		editBtn.classList.add('mr5');
		editBtn.innerText = '수정';
		editBtn.addEventListener('click', editRequest, true);
		div.appendChild(editBtn);

		let delBtn = document.createElement('a');
		delBtn.setAttribute('role', 'button');
		delBtn.classList.add('btn');
		delBtn.classList.add('jjred');
		delBtn.classList.add('mr5');
		delBtn.innerText = '삭제';
		delBtn.addEventListener('click', deleteRequest, true);
		div.appendChild(delBtn);
	} else if (d.status !== 'R') {
		requestForm.requestDt.setAttribute('readonly', 'readonly');
		requestForm.requestTm.setAttribute('readonly', 'readonly');
		requestForm.content.setAttribute('readonly', 'readonly');
		requestForm.cancelReason.setAttribute('readonly', 'readonly');
	}
	//12-09 내역보기에서 시간 추가
	if (d.type == 'O') {
		requestForm.startTime1.classList.add('none');
			requestForm.endTime1.classList.add('none');
			requestForm.startTime2.classList.add('none');
			requestForm.endTime2.classList.add('none');
		
		if (new Date(requestForm.requestDt.value).getDay() == 6 || new Date(requestForm.requestDt.value).getDay() == 0) {
			requestForm.startTime1.classList.remove('none');
			requestForm.endTime1.classList.remove('none');
			requestForm.startTime2.value = '';
			requestForm.endTime2.value = '';
		} else {
			requestForm.startTime2.classList.remove('none');
			requestForm.endTime2.classList.remove('none');
			requestForm.startTime1.value = '';
			requestForm.endTime1.value = '';
		}
		requestForm.requestTm.setAttribute('readonly', true);
		if ((requestForm.startTime1.value != '' && requestForm.endTime1.value != '')||
			requestForm.startTime2.value != '' && requestForm.endTime2.value != '') {
			requestForm.requestTm.value = d.pureWorkTm.split(':')[0]+"시간";
		};
	} else {
		requestForm.requestTm.classList.remove('none');
		
		requestForm.startTime1.value = '00:00';
		requestForm.startTime2.value = '00:00';
		requestForm.endTime1.value = '00:00';
		requestForm.endTime2.value = '00:00';

		requestForm.startTime1.classList.add('none');
		requestForm.endTime1.classList.add('none');
		requestForm.startTime2.classList.add('none');
		requestForm.endTime2.classList.add('none');
	}

let closeBtn = document.createElement('a');
closeBtn.setAttribute('role', 'button');
closeBtn.classList.add('btn');
closeBtn.classList.add('jjblue');
closeBtn.innerText = '닫기'
closeBtn.addEventListener('click', function() {
	closeModal('request-modal');
}, true);

div.appendChild(closeBtn);
}

const confirmSchedule = function(form) {
	const requiredFields = [
		form.memberName, form.typeName, form.requestDt,
		form.requestTm, form.content,
		form.approveName, form.statusName, form.cancelReason
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
document.getElementById('searching').addEventListener('click', function() {
	searching();
});


document.getElementById('apply-detail-btn').addEventListener('click', searching, true);
document.getElementById('apply-close-btn').addEventListener('click', function() {
	closeModal('apply-modal');
});