const editMember = function () {
	if (!confirm('정보를 수정하시겠습니까?')) return false;

	let requiredFields = [
		editForm.name, editForm.position, editForm.mobileNo, editForm.color
	];

	for (let i = 0; i < requiredFields.length; i++) {
		let el = requiredFields[i];
		if (el.value.trim().length === 0) {
			alert("* 표기가 되어있는 항목은 필수 입력 항목입니다.");
			el.focus();
			return false;
		}
	}

	$.ajax({
		url: contextPath + 'member/edit.do',
		method: 'patch',
		data: JSON.stringify({
			name: editForm.name.value,
			position: editForm.position.value,
			department: editForm.department.value,
			phoneNo: editForm.phoneNo.value,
			mobileNo: editForm.mobileNo.value,
			color: editForm.color.value
		}),
		dataType: 'json',
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

document.getElementById('edit-btn').addEventListener('click', editMember, true);

const confirmPassword = function () {
	const inputs = passwordForm.getElementsByTagName('input');
	Array.prototype.forEach.call(inputs, function (input) {
		if (input.value.trim().length === 0) {
			alert('모든 항목을 입력해주세요.');
			input.focus();
			return false;
		}
	});

	return {
		password: passwordForm.password.value,
		newPassword: passwordForm.newPassword.value,
		newPassword2: passwordForm.newPassword2.value
	};
}

const editPassword = function () {
	if (!confirm('비밀번호를 변경하시겠습니까?')) return false;

	const data = confirmPassword();
	if (data === false) return false;

	$.ajax({
		url: contextPath + 'member/edit/p.do',
		method: 'patch',
		data: JSON.stringify(data),
		contentType: 'application/json; charset:UTF-8'
	})
		.done(function (data) {
			alert(data);
			closeModal('password-modal');
		})
		.fail(function (data) {
			alert(data.responseText);
			return false;
		});

};
document.getElementById('password-edit-btn').addEventListener('click', editPassword, true);

document.getElementById('password-close-btn').addEventListener('click', function () {
	closeModal('password-modal')
}, true);

const openPasswordModal = function () {
	const inputs = passwordForm.getElementsByTagName('input');
	Array.prototype.forEach.call(inputs, function (input) {
		input.value = "";
	});

	openModal('password-modal');
};

document.getElementById('password-btn').addEventListener('click', openPasswordModal, true);