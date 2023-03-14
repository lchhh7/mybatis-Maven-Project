let month = "";

const lookupByMonth = function(info) {
		if (info.start.getDate() != 1){
			 month = info.start.getMonth() + 1 + 1;
		}else{
			 month = info.start.getMonth() +1;
		};
		
		if(month == 13){
				month = 1;
			}
		};

const excelWriter = function(){	
		$.ajax({
			type: "POST",
			url: contextPath+"individualOvertimeByExcel.do",
			data: JSON.stringify({
				month : month
			}),
			contentType: "application/json; charset=utf-8",
		}).done(function(resp){
				getmFileDownload();
		}).fail(function (error) {
			alert(error.responseText);
		});
	};
	
const currentByExcelWriter = function(selectedYear){	
		$.ajax({
			type: "POST",
			url: contextPath+"currentByExcelWriter.do",
			data: JSON.stringify({
				month : month,
				selectedYear : selectedYear
			}),
			contentType: "application/json; charset=utf-8",
		}).done(function(resp){
				getyFileDownload();
				closeModal('excel-yearchoiceModal');
				
		}).fail(function (error) {
			alert(error.responseText);
		});
	};
	
	
const excelWriterByAdmin = function(){	
		$.ajax({
			type: "POST",
			url: contextPath+"adminOvertimeByExcel.do",
			data: JSON.stringify({
				month : month
			}),
			contentType: "application/json; charset=utf-8",
		}).done(function(resp){
				getFileDownload();
		}).fail(function (error) {
			alert(error.responseText);
		});
	};

const excel_yearchoiceModal = function () {
	openModal('excel-yearchoiceModal');
}

document.getElementById('yearchoice-btn').addEventListener('click', function () {
	yearOption = document.getElementById('selectYear');
	var selectedYear = yearOption.options[yearOption.selectedIndex].value; 
    currentByExcelWriter(selectedYear);
}, true);

document.getElementById('excelWriter').addEventListener('click', function () {
    excelWriter();
}, true);

document.getElementById('excel_yearchoiceModal').addEventListener('click', function () {
    excel_yearchoiceModal();
}, true);

document.getElementById('yearchoice-close-btn').addEventListener('click', function () {
    closeModal('excel-yearchoiceModal');
}, true);

const getmFileDownload = function() {
		window.open(contextPath+"downLoadmFile.do?month="+month);
	};
	
const getyFileDownload = function() {
		window.open(contextPath+"downLoadyFile.do?month="+month);
	}
	

