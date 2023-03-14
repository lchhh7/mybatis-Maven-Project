const writeSchedule = function () {
    if (!confirm('일정을 등록하시겠습니까?')) return false;
	
    const data = confirmSchedule(writeForm);
    if (data === false) return false;

    $.ajax({
        url: contextPath + 'schedule.do',
        method: 'post',
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

document.getElementById('write-btn').addEventListener('click', writeSchedule, true);

const changeVacationType = function () {
    const value = writeForm.vacationType.value;
    const startTm = writeForm.startTm;
    const endTm = writeForm.endTm;
    const title = writeForm.title;
    const content = writeForm.content;

    startTm.setAttribute('readonly', true);
    endTm.setAttribute('readonly', true);

    if (value === '1') {
        startTm.value = '10:00';
        endTm.value = '19:00';
        title.value = _name + ' 휴가';
        content.value = _name + ' 휴가';
    } else if(value === '2') {
        startTm.value = '10:00';
        endTm.value = '15:00';
        title.value = _name + ' 반차';
        content.value = _name + ' 반차';
    } else if (value === '3') {
        startTm.value = '15:00';
        endTm.value = '19:00';
        title.value = _name + ' 반차';
        content.value = _name + ' 반차';
    }
};

writeForm.vacationType.addEventListener('change', changeVacationType, true);

const changeType = function () {
	
    writeForm.title.value = '';
    writeForm.content.value = '';
	let startDt = writeForm.startDt.value;

    const vacationDiv = document.getElementById('vacation');
    const overtimeDiv = document.getElementById('overtime');
	const passengers = document.getElementById('add-passengers');
    vacationDiv.classList.add('none');
    overtimeDiv.classList.add('none');
	
	if (writeForm.type.value === 'OW' || writeForm.type.value === 'BT') {
		passengers.classList.remove('none');
		
		let now = new Date();
		let year = now.getFullYear();
		let lastMonth = now.getMonth() -1;
		let month = now.getMonth();
		
		//최종기한이 5일까지
		if(new Date(startDt)>=new Date(year, (now.getDate() < 5 ? lastMonth : month) ,1)){
		}else {
			alert('이미 추가휴가가 반영되어 관련 데이터를 추가 할 수 없습니다. 관리자에게 문의하세요.');
			closeModal('write-modal');
		}
	}else{
		passengers.classList.add('none');
	}
	
    if (writeForm.type.value === 'VA') {
        vacationDiv.classList.remove('none');
    } else {
        writeForm.startTm.removeAttribute('readonly');
        writeForm.endTm.removeAttribute('readonly');

        if (writeForm.type.value === 'OT') {
            overtimeDiv.classList.remove('none');
            writeForm.startTm.value = '19:00';
            writeForm.endTm.value = '19:00';
            writeForm.title.value = _name + ' 잔업';
            writeForm. content.value = _name + ' 잔업';
        } else {
            writeForm.startTm.value = '10:00';
            writeForm.endTm.value = '19:00';
        }
    }
};

writeForm.type.addEventListener('change', changeType, true);

const writeModal = function (info) {
    const inputs = writeForm.getElementsByTagName('input');
    const boxes = writeForm.getElementsByTagName('select');
	
	
    Array.prototype.forEach.call(inputs, function (e) {
        e.value = '';
    });
    Array.prototype.forEach.call(boxes, function (e) {
        e.value = '';
    });

    writeForm.startDt.value = info.startStr;
    writeForm.endDt.value = formatDate(new Date(info.end-1));
    writeForm.color.jscolor.fromString(_color);
    openModal('write-modal'); 
};

document.getElementById('write-close-btn').addEventListener('click', function () {
    closeModal('write-modal');
}, true);

