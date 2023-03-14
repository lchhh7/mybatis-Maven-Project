let calendar;
const writeForm = document.getElementById('write-form');
const editForm = document.getElementById('edit-form');
const searchTypeSC = document.getElementById('search-typeSC');
const searchTypeVA = document.getElementById('search-typeVA');
const searchTypeOW = document.getElementById('search-typeOW');
const searchTypeBT = document.getElementById('search-typeBT');
const oneday = 24 * 60 * 60 * 1000;

const schedule = function(info) {
	$.ajax({
		url: contextPath + 'schedule/' + info.event.id + '.do',
		method: 'get',
		dataType: 'json',
		contentType: 'application/json; charset=utf-8'
	})
		.done(function(data) {
			editForm.id.value = data.id;
			editForm.type.value = data.type;
			editForm.memberName.value = data.memberName;
			editForm.typeName.value = data.typeName;
			editForm.startDt.value = data.startDt;
			editForm.startTm.value = data.startTm;
			editForm.endDt.value = data.endDt;
			editForm.endTm.value = data.endTm;
			editForm.title.value = data.title;
			editForm.content.value = data.content;
			editForm.approveName.value = data.approveName;
			editForm.statusName.value = data.statusName;
			editForm.color.jscolor.fromString(data.color);
			editForm.cancelReason.value = data.cancelReason;
			//레이어모달에 동석자 정보 가져가놓기
			passengerList(data.passengers);
			
			displayEditModal(data);
			openModal('edit-modal');
			
		})
		.fail(function(data) {
			alert('일정 정보 조회 중 오류가 발생했습니다.');
			return false;
		});
};

const schedules = function(info, successCallback) {
	const searchMemberId = getRadioValue('calendar-type');
	
	let url = 'm=' + searchMemberId;
	url += ('&sc=' + (searchTypeSC.checked ? searchTypeSC.value : ''));
	url += ('&va=' + (searchTypeVA.checked ? searchTypeVA.value : ''));
	url += ('&ow=' + (searchTypeOW.checked ? searchTypeOW.value : ''));
	url += ('&bt=' + (searchTypeBT.checked ? searchTypeBT.value : ''));
	url += ('&sd=' + info.startStr.substr(0, 10));
	url += ('&ed=' + info.endStr.substr(0, 10));

	$.ajax({
		url: contextPath + 'schedule/search.do?' + url,
		method: 'get',
		dataType: 'json',
		contentType: 'application/json; charset=utf-8'
	})
		.done(function(data) {
			let events = [];
			const list = data.list;
			Array.prototype.forEach.call(list, function(e) {
				if ((e.type === 'FV' || e.type === 'HV') && e.status === 'R') {
					e.title += '(승인대기)';
					events.push({
						id: e.id, title: e.title, start: e.startDt,
						end: e.endDt+"T00:00:02", color: e.color
					});
				} else {
					events.push({
						id: e.id, title: e.title, start: e.startDt + 'T' + e.startTm,
						end: e.endDt + 'T' + e.endTm, color: e.color
					});
				}
			});

			const holidays = data.holidays;
			Array.prototype.forEach.call(holidays, function(e) {

				let dt = formatDate(new Date(e.holidayDt));

				events.push({
					start: dt, end: formatDate(new Date(new Date(dt).getTime() + oneday)),
					rendering: 'background', backgroundColor: 'red',
					overlap: true
				});

				events.push({
					start: dt, title: e.title, color: 'red'
				});
			});

			successCallback(events);
		})
		.fail(function(data) {
			alert('일정 정보 조회 중 오류가 발생했습니다.');
			return false;
		});
}

document.addEventListener('DOMContentLoaded', function() {
	let calendarEl = document.getElementById('calendar');
	calendar = new FullCalendar.Calendar(calendarEl, {
		plugins: ['interaction', 'dayGrid', 'resourceTimeline'],
		eventTimeFormat: {
			hour: '2-digit',
			minute: '2-digit',
			hour12: false
		},
		businessHours: true,
		locale: 'ko',
		header: {
			left: 'title',
			center: '',
			right: 'prev,next'
		},
		events: function(info, successCallback) {
			schedules(info, successCallback);
		},
		eventClick: function(info) {
			console.log(info);
			schedule(info);
		},
		selectable: true,
		select: function(info) {
			writeModal(info);
		}
	});

	calendar.render();

	searchTypeSC.addEventListener('click', function() {
		calendar.refetchEvents();
	}, true);
	searchTypeVA.addEventListener('click', function() {
		calendar.refetchEvents();
	}, true);
	searchTypeOW.addEventListener('click', function() {
		calendar.refetchEvents();
	}, true);
	searchTypeBT.addEventListener('click', function() {
		calendar.refetchEvents();
	}, true);

	const radios = document.getElementsByName('calendar-type');
	radios.forEach(function(e) {
		e.addEventListener('change', function() {
			calendar.refetchEvents();
		});
	});
});


const confirmSchedule = function(form) {
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
		color: form.color.value,
		passengers: passengerChoice()
	};

	if (form.getAttribute('id') === 'write-form') {
		if (form.type.value === 'VA') {
			if (form.vacationType.value === '') {
				alert('휴가 종류를 선택해주세요.');
				return false;
			}

			if (form.approveId.value === '') {
				alert('결재자를 선택해주세요.');
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

const colenWrite = function(_this) {
	_this.value = _this.value.replace(/[^0-9:]/g, "");
	if (_this.value.length == 2) {
		_this.value = _this.value + ":";
	}
};

const passengerCheckList = function () {
	$.ajax({
		url : contextPath + 'schedule/checkedPassengers.do',
		method : 'post',
		dataType : 'json',
		contentType : 'apllication/json; charset=utf-8'
	})
};

const passengerList = function(str) {
		
	arr = str.slice(0);
	
	$.ajax({
		url: contextPath + 'schedule/passengers.do',
		method: 'get',
		contentType: 'application/json; charset=utf-8'
	})
		.done(function(data) {
			let chkBox = "";
			let count = 0;
			if (data.length > 0) {
				data.forEach(function(el) {
					
					if (el.rnum % 4 == 1) {
						chkBox += '<tr>';
					}
						for(var j=0;j<arr.length ;j++){
							count = 0;
							if(arr[j] == el.id) {
								chkBox += '<td><input type="checkbox" name="passengers" id ="passenger' + el.id + '" value ="' + el.id + '" checked>' + el.name + '</td>';
								count ++;
								break;
							}
						}
						if(count == 0) {
							if(_id == el.id){
								chkBox += '<td><input type="checkbox" name="passengers" id ="passenger' + el.id + '" value ="' + el.id + '" disabled>' + el.name + '</td>';
							}else {
							chkBox += '<td><input type="checkbox" name="passengers" id ="passenger' + el.id + '" value ="' + el.id + '">' + el.name + '</td>';
							}
						}
					if (el.rnum % 4 == 0) {
						chkBox += '</tr>';
					}
				});
			}
			document.getElementById('passengers-info').innerHTML = chkBox;
		})
		.fail(function(data) {
			alert(data.responseText);
			return false;
		});
}

const passengerChoice = function() {
	let passengers = [];
	$("input[name=passengers]:checked").each(function() {
		passengers.push($(this).val());
	});
	console.log(passengers);
	return passengers;
}

document.getElementById('passengers-save-btn').addEventListener('click', function() {
	passengerChoice();

	closeModal('passengers-modal');
});

document.getElementById('add-passengers').addEventListener('click', function() {
	passengerList(passengerChoice());
	
	openModal('passengers-modal');
}, true);

document.getElementById('check-passengers').addEventListener('click', function() {
	passengerList(passengerChoice());
	openModal('passengers-modal');
}, true);


document.getElementById('passengers-close-btn').addEventListener('click', function() {
	closeModal('passengers-modal');
}, true);
