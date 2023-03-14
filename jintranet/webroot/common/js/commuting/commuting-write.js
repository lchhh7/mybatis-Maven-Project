const writeCommute = function () {
		const data ={
			type : writeForm.type.value,
			requestDt : writeForm.requestDt.value,
			content :  writeForm.content.value,
			approveId :  writeForm.approveId.value,
			requestTm : writeForm.requestTm.value
		};
		
		//얘가 빨간날 & 휴일
		if(new Date(writeForm.requestDt.value).getDay() != 6 && new Date(writeForm.requestDt.value).getDay() !=0 && redDayCheckFunction(writeForm.requestDt.value) != true) {
			data.startTm = writeForm.startTime2.value;
			data.endTm = writeForm.endTime2.value;
			console.log("22 data.startTm : "+data.startTm + "data.endTm"+ data.endTm);
		}else{ // 얘가 평일 
			data.startTm = writeForm.startTime1.value;
			data.endTm = writeForm.endTime1.value;
			console.log("11 data.startTm : "+data.startTm + "data.endTm"+ data.endTm);
		}
		
	    $.ajax({
	        url: contextPath + 'commuting/writeCommute.do',
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

document.getElementById('write-btn').addEventListener('click', writeCommute, true);

document.getElementById('write-close-btn').addEventListener('click', function () {
    closeModal('write-modal');
}, true);

var redCheck;

const changeCommutingType = function () {
	const val = writeForm.type.value;
	const startTime1 = document.getElementById('startTime1');
	const endTime1 = document.getElementById('endTime1');
	const startTime2 = document.getElementById('startTime2');
	const endTime2 = document.getElementById('endTime2');
	const requestTm = document.getElementById('requestTm');
	startTime1.classList.add('none');
	endTime1.classList.add('none');
	startTime2.classList.add('none');
	endTime2.classList.add('none');
	requestTm.classList.remove('none');
	
	
	if (val === 'O') {
		alreadyDoneCommutingFunction(writeForm.requestDt.value);
		
		if(new Date(writeForm.requestDt.value).getDay() == 6 ||new Date(writeForm.requestDt.value).getDay() ==0 || redDayCheckFunction(writeForm.requestDt.value)) {
		//if(new Date(writeForm.requestDt.value).getDay() == 6 ||new Date(writeForm.requestDt.value).getDay() ==0 || redCheck) {
			startTime1.classList.remove('none');
			endTime1.classList.remove('none');
			startTime1.value ='';
			endTime1.value ='';
			
		}else{
			startTime2.classList.remove('none');
		endTime2.classList.remove('none');
		startTime2.value ='';
		endTime2.value ='';
		}
		
		requestTm.classList.add('none');
		writeForm.requestTm.setAttribute('readonly', true);
		if(startTime1.value != null && endTime1.value != null) {
			writeForm.requestTm.value = "00:00";
		};
	} else {
		startTime1.value ='00:00';
		startTime2.value ='00:00';
		endTime1.value ='00:00';
		endTime2.value ='00:00';
		
		
		startTime1.classList.add('none');
		endTime1.classList.add('none');
		startTime2.classList.add('none');
		endTime2.classList.add('none');
		requestTm.classList.remove('none');
		writeForm.requestTm.removeAttribute('readonly');
		writeForm.requestTm.value = '';
	}
	
	$("#startTime1").timepicker('setTime', '10:00');
	$("#startTime2").timepicker('setTime', '20:00');
};

writeForm.type.addEventListener('change', changeCommutingType, true);

$('.timepicker1').timepicker({
	'minTime': '11:00',
	'maxTime': '18:00',
	'timeFormat': 'H:i',
	'step': 60
});

$('.timepicker2').timepicker({
	'minTime': '21:00',
	'maxTime': '03:00',
	'timeFormat': 'H:i',
	'step': 60
});

const timePickerCustom = function() {
var childList = $('.ui-timepicker-list');
var childSize = childList.children().length;

for(var i=0; i< childSize ; i++){
	childList.find('li:eq('+i+')').text('');
	childList.find('li:eq('+i+')').text(childList.find('li:eq('+i+')').text()+(i+1)+'시간');
};
};

const redDayCheckFunction = function(dt) {
	/*if(dt == '2022-01-01' || dt == '2022-01-31' || dt == '2022-02-01' || dt == '2022-02-02' || dt == '2022-03-01' || dt == '2022-03-09' ||
	dt == '2022-05-05' || dt == '2022-05-08' || dt == '2022-06-01' || dt == '2022-06-06' || dt == '2022-08-15' || dt == '2022-09-09' ||
	dt == '2022-09-10' || dt == '2022-09-11' || dt == '2022-10-03' || dt == '2022-10-09' || dt == '2022-12-25') {
	return true;
	}*/
	
	let arr;
	
		$.ajax({
			url : contextPath + 'redDayCheck.do',
			method : 'POST',
			async : false,
			contentType : 'apllication/json; charset=utf-8'
		})
		.done(function(data){
			arr = data;
		})
		.fail(function(data){
			alert('fail');
		});
	
	if(arr.includes(dt+' 00:00:00.0'))
	return true;
}


const alreadyDoneCommutingFunction = function(dt) {
	
	const data ={
		searchStartDt : dt,
		searchEndDt : dt
	};
	
	$.ajax({
		url :  contextPath + 'commuting/checkCommuting.do',
		method : 'POST', 
		data : JSON.stringify(data),
		contentType: 'application/json; charset=utf-8'
	}).done(function(data) {
		if(data ==false) {
			alert('이미 잔업이 등록된 날짜입니다. 신청근태의 내역확인을 눌러 수정해주세요.');
			closeModal('write-modal');
		}
	}).fail(function(data){
		alert(data.responseText);
	});
};