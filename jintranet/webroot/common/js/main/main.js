const documentForm = document.getElementById('document-form');

document.addEventListener('DOMContentLoaded', function () {
    mainDocument();
    mainVacation();
});

function projects(department) {
    $.ajax({
        url: contextPath + "main/project/" + department + ".do",
        method: "get",
    })
        .done(function (data) {
            let tr = '';
            if (data.length > 0) {
                data.forEach(function (el) {
                    tr += '<tr class="tbbody" onclick="location.href=\'' + contextPath + 'project/edit.do?id=' + el.id + '\'">';
                    tr += '<td>' + el.rnum + '</td>';
                    tr += '<td>' + el.title + '</td>';
                    tr += '<td>' + el.orderingName + '</td>';
                    tr += '<td>' + el.startDt + ' ~ ' + el.endDt + "</td>";
                })
            }

            document.getElementById("projects").innerHTML = tr;
        })
        .fail(function () {
            alert("정보 조회 중 오류가 발생했습니다. 관리자에게 문의하세요.");
            return false;
        });
}

function commuteInsert(value){
	var data ={
			 attendYn : value
	}
	$.ajax({
		type: "post",
		url: contextPath+"/main/goToWorkButton.do",
		data: JSON.stringify(data),
		contentType: "application/json; charset=utf-8",
	}).done(function (data, textStatus, xhr) {
			window.location.reload();
	}).fail(function (data, textStatus, xhr) {
		alert("정보 조회 중 오류가 발생했습니다. 관리자에게 문의하세요.");
	});
}