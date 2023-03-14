const confirmCompany = function (form) {
    const requiredFields = [
        form.companyName, form.companyNo
    ];

    if (confirmRequiredField(requiredFields) === false) return false;

    const companyKind = getRadioValue('companyKind');
    if (companyKind === "") {
        alert("회사유형을 선택해주세요.");
        return false;
    }

    let result = {
        companyName: form.companyName.value,
        companyNo: form.companyNo.value,
        companyKind: companyKind,
        license: attaches2,
        bankbooks: attaches
    };

    return isEditForm(result, form);
};

Dropzone.options.dropzone = {
    url: contextPath + 'admin/company/upload.do',
    autoProcessQueue: false,
    maxFilesize: 10,
    maxFiles: 2,
    parallelUploads: 2,
    addRemoveLinks: true,
    dictRemoveFile: '<img class="eximg pointer" src="' + contextPath + 'common/img/delete.png" alt="삭제">',
    uploadMultiple: true,
    init: dzInit
};

document.getElementById('upload-modal-btn').addEventListener('click', function () {
    if (document.getElementById('files').childElementCount >= 2) {
        alert('최대 2개의 파일까지 업로드 할 수 있습니다.');
        return false;
    }

    openModal('upload-modal');
});