let calendar;
const writeForm = document.getElementById('write-form');
const editForm = document.getElementById('edit-form');
const oneday = 24 * 60 * 60 * 1000;

const commuting = function (info) {
	
    const id = info.event.id;
    if (id === "") return false;
    $.ajax({
        url: contextPath + 'commuting/' + id + '.do',
        method: 'get',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8'
    })
        .done(function (data) {
            const dt = new Date(data.commutingTm);
            const hour = dt.getHours();
            let min = dt.getMinutes();
            min = min < 10 ? '0' + min : min;

            editForm.id.value = data.id;
            editForm.attendYn.value = data.attendYn;
			editForm.requestDt.value = formatDate(info.event.start);
			editForm.type.value = data.attendYn === 'Y' ? '출근시간' :
                                        data.attendYn === 'N' ? '퇴근시간' : '';
            editForm.requestTm.value = hour + ':' + min;
			editForm.approveId.value = '';
			
            openModal('edit-modal');
        })
        .fail(function (data) {
            alert('일정 정보 조회 중 오류가 발생했습니다.');
            return false;
        });
};

const selectCommuting = function (info) {
	startTime1.classList.add('none');
	endTime1.classList.add('none');
	startTime2.classList.add('none');
	endTime2.classList.add('none');
	requestTm.classList.remove('none');
	
	const inputs = writeForm.getElementsByTagName('input');
	
	Array.prototype.forEach.call(inputs, function(e) {
		e.value = '';
	});
	
	const selects = writeForm.getElementsByTagName('select');
	Array.prototype.forEach.call(selects, function(e) {
		e.value = '';
	});
	
	writeForm.requestDt.value = info.startStr;
    writeForm.requestTm.removeAttribute('readonly');
	
	let now = new Date();
	let year = now.getFullYear();
	let lastMonth = now.getMonth() -1;
	let month = now.getMonth();
	
	
	
	//최종기한이 5일까지 
	if(new Date(info.startStr)>=new Date(year, (now.getDate() < 5 ? lastMonth : month) ,1)){
		openModal('write-modal');
	}else {
		alert('이미 추가휴가가 반영되어 관련 데이터를 수정 할 수 없습니다. 관리자에게 문의하세요.');
	}
};

const setCommutings = function (list) {
    let result = [];
    if (list === undefined) return result;

    Array.prototype.forEach.call(list, function (e) {
        let title, tm;
        let attend = e.attendYn;
        if (attend === 'Y') {
            title = '출근: ' + e.tm;
        } else if (attend === 'N') {
            title = '퇴근: ' + e.tm;
        } else if (attend === 'V') {
            title = '퇴근: ' + e.tm + ' (철야)';
        } else {
            alert('올바르지 않은 데이터가 있습니다. 관리자에게 문의하세요.');
            return false;
        }

        result.push({
            id: e.id, title: title, start: e.dt+'T00:10', color: 'blue'
        });
    });
    return result;
}

/*const setOvertimes = function (totalOverTime, overtimes) {
    let result = [];
    let html = '';
    if (totalOverTime === undefined) {
    } else {
        html = totalOverTime;
        Array.prototype.forEach.call(overtimes, function (e) {
            let title = '초과: ';
            title += e.tm;
            result.push({
               title: title, start: e.dt+'T23:00', color: 'gray'
            });
        });
    }

    document.getElementById('total-ot').innerHTML = html;
    return result;
}*/

const setVacations = function (list) {
    let result = [];
    if (list === undefined) return result;

    Array.prototype.forEach.call(list, function (e) {
        result.push({
            start: e.startDt, end: formatDate(new Date(new Date(e.endDt).getTime() + oneday)),
            rendering: 'background', backgroundColor: 'red',
            overlap: true
        });

        result.push({
            start: e.startDt+"T00:01", end: formatDate(new Date(new Date(e.endDt).getTime() + oneday)),
            title: e.title, color: 'red'
        });
    });

    return result;
}

const setOverTimeRequests = function (list) {
    let result = [];
    if (list === undefined) return result;

    Array.prototype.forEach.call(list, function (e) {
        let color = e.status === 'Y' ? '#2DA400' : '#452E86';
        result.push({
            start: e.requestDt, end: e.requestDt,
            rendering: 'background', backgroundColor: color,
            overlap: true
        });
    });
    return result;
}

const setBusinessTrips = function (list) {
	
    let result = [];
    if (list === undefined) return result;
	
    Array.prototype.forEach.call(list, function (e) {
        result.push({
            start: e.startDt, end: formatDate(new Date(new Date(e.endDt).getTime() + oneday)),
            rendering: 'background', backgroundColor: 'green',
            overlap: true
        });

        result.push({
            start: e.startDt, end: e.endDt+"T00:00:02",
            title: e.title, color: 'green'
        });
    });
    return result;
}

const setHolidays = function (list) {
    let result = [];
    if (list === undefined) return result;

    Array.prototype.forEach.call(list, function (e) {
        let dt = formatDate(new Date(e.holidayDt));

        result.push({
            start: dt, end: formatDate(new Date(new Date(dt).getTime() + oneday)),
            rendering: 'background', backgroundColor: '#FF657E',
            overlap: true
        });

        result.push({
            start: dt, end: dt+"T00:00:01", title: e.title, color: '#FF657E'
        });
    });
    return result;
}

const setProposalPeriods = function (totalOverTime,list) {
    let result = [];
	let html = '';
    if (list === undefined) return result;
	
 	if (totalOverTime === undefined) {
		html = '';
    } else {
	html = totalOverTime;
    Array.prototype.forEach.call(list, function (e) {
            title = '신청시간 : ';
			title += e.pureWorkTm +"시간";
        result.push({
			 title: title, start: e.requestDt.split(" ")[0]+'T23:00', color: 'gray'
        });
    });
 	document.getElementById('total-ot').innerHTML = html;
	}
    return result;
}

const commutings = function (info, successCallback) {
    $.ajax({
        url: contextPath + 'commuting/search.do?' + ('&sd=' + info.startStr.substr(0, 10)) + (''),
        method: 'get',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8'
    })
        .done(function (data) {
			console.log(data);
            let events = [];
            const commutings = setCommutings(data.list);
            //const overtimes = setOvertimes(data.totalOverTime, data.overtimes);
            const vacations = setVacations(data.vacations);
            const holidays = setHolidays(data.holidays);
			const overtimeRequests = setOverTimeRequests(data.overtimeRequests);
			const businessTrips = setBusinessTrips(data.businessTrips);
			const proposalPeriods = setProposalPeriods(data.totalOverTime, data.proposalPeriods);
			
            Array.prototype.push.apply(events, commutings);
            //Array.prototype.push.apply(events, overtimes);
            Array.prototype.push.apply(events, vacations);
			Array.prototype.push.apply(events, overtimeRequests);
			Array.prototype.push.apply(events, businessTrips);
            Array.prototype.push.apply(events, holidays);
			Array.prototype.push.apply(events, proposalPeriods);
            successCallback(events);

			const r = data.nearRequest;
            document.getElementById('request-title').innerText = r.content;
            document.getElementById('request-content').innerText = r.typeName;
            document.getElementById('request-status').innerText = r.statusName;
		
        })
        .fail(function () {
            alert('일정 정보 조회 중 오류가 발생했습니다.');
            return false;
        });
}

document.addEventListener('DOMContentLoaded', function () {
    let calendarEl = document.getElementById('calendar');
    calendar = new FullCalendar.Calendar(calendarEl, {
        plugins: ['interaction', 'dayGrid', 'resourceTimeline'],
        locale: 'ko',
        businessHours: true,
      displayEventTime: false,
        header: {
            left: 'title',
            center: '',
            right: 'legend prev,next'
        },
      customButtons: {
         legend : {
            text: 'displaynone'
         }
      },
        events: function (info, successCallback) {
            commutings(info, successCallback);
         lookupByMonth(info);
        },
        eventClick: function (info) {
            commuting(info);
        },
        selectable: true,
        select: function (info) {
           selectCommuting(info);
        }
    });
    calendar.render();
});


const confirmCommuting = function (form) {
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


    if (form.type.value === '1') {
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
    }

    return isEditForm(data, form);
};

const addBreakCount = function () {
	const availableCnt = document.getElementById("selectedMonth");
    $.ajax({
        url: contextPath + 'commuting/addBreak/search.do?m='+ availableCnt.value,
        method: 'get'
    })
        .done(function (resp) {
			document.getElementById("additionalBreak").innerHTML = resp;
        })
        .fail(function (error) {
           document.getElementById("additionalBreak").innerHTML = 0;
        });
};

const writeAddBreak = function () {
	const data ={
		requestDt : formatDate(new Date(new Date().getFullYear(),document.getElementById("selectedMonth").value -1 ,1)),
		content : $("#additionalBreak").text(),
		approveId: $("#addApproveId").val()
	};
    $.ajax({
        url: contextPath + 'commuting/writeAddBreak.do',
        method: 'post',
		data: JSON.stringify(data),
		contentType: 'application/json; charset=utf-8'
    })
        .done(function (data) {
			alert(data);
	 		window.location.reload();
        })
        .fail(function (data) {
            alert(data.responseText);
        });
};


const editCommute = function () {
    $.ajax({
        url: contextPath + 'commuting/editCommute.do',
        method: 'post',
		data: JSON.stringify({
            type: editForm.attendYn.value,
            requestDt: editForm.requestDt.value,
            requestTm: editForm.requestTm.value,
            content: editForm.content.value,
            approveId: editForm.approveId.value
        }),
		contentType: 'application/json; charset=utf-8'
    })
        .done(function (data) {
			alert(data);
	 		window.location.reload();
        })
        .fail(function (data) {
            alert(data.responseText);
        });
};

const colenWrite = function (_this) {
	_this.value = _this.value.replace(/[^0-9:]/g,"");
	if(_this.value.length == 2) {
		_this.value = _this.value+":";
	}
};

const timePickerColenWrite = function (_this) {
	_this.value = _this.value.replace(/[^0-9:]/g,"");
	if(_this.value.length == 2) {
		_this.value = "";
	}
};

document.getElementById('edit-close-btn').addEventListener('click', function () {
    closeModal('edit-modal');
}, true);
