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
	console.log(data);
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

const displayEditModal = function (d) {
	
    const approveDiv = document.getElementById('approve-div');
	const checkPassengers = document.getElementById('check-passengers');
    approveDiv.classList.add('none');

	let startDt = editForm.startDt.value;
	
    if (d.type === 'FV' || d.type === 'HV' || d.type === 'OT') {
        approveDiv.classList.remove('none');
    }
	if (d.type === 'OW' || d.type === 'BT') {
		checkPassengers.classList.remove('none');
	}else{
		checkPassengers.classList.add('none');
	}
	
    const reason = document.getElementById('reason');
    reason.classList.add('none');

    const div = document.getElementById('edit-btn-box');
    while (div.firstChild) {
        div.removeChild(div.firstChild);
    }

	

    // 동일인이면 수정,삭제버튼 및 input창 readonly해제
    if (d.memberId == _id) {
        if (d.status === 'R') {
            editForm.startTm.removeAttribute('readonly');
            editForm.endTm.removeAttribute('readonly');
            editForm.title.removeAttribute('readonly');
            editForm.content.removeAttribute('readonly');
			
			
				if (d.type === 'OW' || d.type === 'BT') {
					let now = new Date();
					let year = now.getFullYear();
					let lastMonth = now.getMonth() -1;
					let month = now.getMonth();
					
					//최종기한이 5일까지
					if(new Date(startDt)>=new Date(year,(now.getDate() < 5 ? lastMonth : month) ,1)){
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
					}else {
						alert('이미 추가휴가가 반영되어 관련 데이터를 수정 할 수 없습니다. 관리자에게 문의하세요.');
					}
					
				}else {
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
				}
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

    } else {
        editForm.startTm.setAttribute('readonly', true);
        editForm.endTm.setAttribute('readonly', true);
        editForm.title.setAttribute('readonly', true);
        editForm.content.setAttribute('readonly', true);
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