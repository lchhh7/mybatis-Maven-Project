const setUrl = function (u) {
    if (!u.includes(':')) {
        u = 'http://' + u;
    } else {
        let str = u.split(':');
        if (str[1].substring(0, 2) !== '//') {
            u = str[0] + '://' + str[1];
        }
    }
    return u;
}

const downloadHistory = function (id) {
    const form = document.getElementById('download-history-form');
    form.id.value = id;
    form.submit();
};

const openHistoryModal = function (id) {
    $.ajax({
        url: contextPath + 'project/document/history/' + id +'.do',
        method: 'get',
    })
        .done(function (data) {
            let tr = "";

            data.forEach(function (e) {
                tr += '<tr>';
                tr += '<td>' + e.rnum + '</td>';
                tr += '<td>' + e.title + '</td>';
                tr += '<td>' + e.version + '</td>';
                tr += '<td>' + e.kindName + '</td>';
                if (e.kind === fileType) {
                    tr += '<td><a role="button" class="wordbreak" onclick="downloadHistory(\''+e.id+'\')">' + e.originalFileName +'</a></td>'
                } else if (e.kind === linkType) {
                    tr += '<td><a href="' + e.path + '" class="wordbreak" target="_blank">' + e.path +'</a></td>';
                }

                tr += '<td>' + e.changeDt + '(' + e.memberName +')' + '</td>';
                tr += '</tr>';
            });

            document.getElementById('history-cnt').innerText = '문서변경이력(' + data.length + '건)';
            document.getElementById('histories').innerHTML = tr;

            openModal('history-modal');
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });


}

const editDocument = function () {
    if (!confirm('문서를 수정하시겠습니까?')) return false;

    const data = confirmDocument(editForm);
    if (data === false) return false;

    $.ajax({
        url: contextPath + 'project/document/' + data.id +'.do',
        method: 'patch',
        data: JSON.stringify(data),
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

document.getElementById('edit-btn').addEventListener('click', editDocument, true);

const openEditModal = function (id) {
    $.ajax({
        url: contextPath + 'project/document/' + id + '.do',
        method: 'get',
    })
        .done(function (data) {
            editForm.documentId.value = data.id;
            editForm.version.value = data.version;
            editForm.segment.value = data.segment;
            changeSegment(editForm, data.segment, data.task);
            editForm.title.value = data.title;
            editForm.kind.value = data.kind;
            changeKind(editForm, data.kind);

            if (data.kind === fileType) {
                let div = '<div class="filelist mt5 mb10 clearfix">';
                div += '<a role="button" class="file floatleft" onclick="downloadAttach(\''+data.id+'\')">' + data.originalFileName +'</a>';
                div += '<img class="ml15 eximg pointer" src="'+ contextPath + '/common/img/delete.png" alt="삭제" onclick="deleteAttach(this, \''+ data.id +'\')">';
                div += '</div>';

                document.getElementById('files2').innerHTML = div;
            } else if (data.kind === linkType) {
                editForm.link.value = data.path;
            }

            openModal('edit-modal');
        })
        .fail(function () {
            alert("문서 정보 조회 중 오류가 발생했습니다.");
        });

}

const deleteDocument = function (id, name) {
    if (!confirm(name + " 문서를 삭제하시겠습니까?")) return false;

    $.ajax({
        url: contextPath + 'project/document.do',
        method: 'delete',
        data: JSON.stringify({id: id}),
        contentType: 'application/json; charset:UTF-8'
    })
        .done(function (data) {
            alert(data);
            location.reload();
        })
        .fail(function (data) {
            alert(data.responseText);
        });
}

const confirmDocument = function (form) {
    let requiredFields = [
        form.segment, form.kind, form.title
    ];

    if (confirmRequiredField(requiredFields) === false) return false;

    let result = {
        projectId: projectId,
        segment: form.segment.value,
        task: form.task.value,
        kind: form.kind.value,
        title: form.title.value
    };

    if (form.kind.value === fileType) {
        let attach;

        if (form.getAttribute('id') === 'edit-form') {
            attach = attaches2;
        } else {
            attach = attaches;
        }

        if (attach.length <= 0) {
            alert('파일을 업로드해주세요.');
            return false;
        }

        attach = attach[0];

        result.path = attach.path;
        result.originalFileName = attach.originalFileName;
        result.fileSize = attach.fileSize;
        result.storedFileName = attach.storedFileName;

    } else if (form.kind.value === linkType) {
        result.path = setUrl(form.link.value);

        if (result.path.trim().length === 0) {
            alert("* 표기가 되어있는 항목은 필수 입력 항목입니다.");
            form.link.focus();
            return false;
        }
    }

    if (form.getAttribute('id') === 'edit-form') {
        result.id = form.documentId.value;
        result.version = form.version.value;
    }

    return result;
}

const writeDocument = function () {
    if (!confirm('문서를 등록하시겠습니까?')) return false;

    const data = confirmDocument(writeForm);
    if (data === false) return false;

    $.ajax({
        url: contextPath + 'project/document.do',
        method: 'post',
        data: JSON.stringify(data),
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

document.getElementById('write-btn').addEventListener('click',writeDocument, true);

const changeKind = function (form, kind) {
    let pathDiv, linkDiv;

    if (form.id === "write-form") {
        pathDiv = document.getElementsByName('path-div')[0];
        linkDiv = document.getElementsByName('link-div')[0];
    } else if (form.id === "edit-form") {
        pathDiv = document.getElementsByName('path-div')[1];
        linkDiv = document.getElementsByName('link-div')[1];
    }

    if (kind === fileType) {
        pathDiv.classList.remove('none');
        linkDiv.classList.add('none');
    } else if (kind === linkType) {
        pathDiv.classList.add('none');
        linkDiv.classList.remove('none');
    } else {
        pathDiv.classList.add('none');
        linkDiv.classList.add('none');
    }
};

editForm.kind.addEventListener('change', function () {
    changeKind(editForm, this.value);
}, true);
writeForm.kind.addEventListener('change', function () {
    changeKind(writeForm, this.value);
}, true);

const openWriteModal = function () {
    const inputs = writeForm.getElementsByTagName('input');
    const boxes = writeForm.getElementsByTagName('select');

    Array.prototype.forEach.call(inputs, function (input) {
        input.value = "";
    });
    Array.prototype.forEach.call(boxes, function (box) {
        box.value = "";
    });

    openModal('write-modal');
};

document.getElementById('add-btn').addEventListener('click', openWriteModal, true);


document.getElementById('history-close-btn').addEventListener('click', function () {
    closeModal('history-modal');
}, true);
document.getElementById('edit-close-btn').addEventListener('click', function () {
    closeModal('edit-modal');
}, true);
document.getElementById('write-close-btn').addEventListener('click', function () {
    closeModal('write-modal');
}, true);

const changeSegment = function (form, value, task) {
    $.ajax({
        url: contextPath + 'project/document/task.do',
        method: 'post',
        data: JSON.stringify({codeFg: value}),
        contentType: 'application/json; charset:UTF-8'
    })
        .done(function (data) {
            let option = "";
            if (data.length > 0) {
                option += '<option value="">선택</option>';

                data.forEach(function (e) {
                    option += '<option value="' + e.minorCd +'">' + e.codeName + '</option>';
                });
            } else {
                option = '<option value="">타스크가 없습니다</option>';
            }

            form.task.innerHTML = option;

            if (task !== "" && task !== null) {
                form.task.value = task;
            }
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });

}

writeForm.segment.addEventListener('change', function () {
    changeSegment(writeForm, this.value, '');
}, true);
editForm.segment.addEventListener('change', function () {
    changeSegment(editForm, this.value, '');
}, true);

//////////////
Dropzone.options.dropzone = {
    url: contextPath + 'project/document/upload.do',
    autoProcessQueue: false,
    maxFilesize: 10,
    maxFiles: 1,
    parallelUploads: 2,
    addRemoveLinks: true,
    acceptedFiles: ".jpeg,.jpg,.png,.gif,.JPEG,.JPG,.PNG,.GIF,.pdf,.PDF,.zip,.ZIP",
    dictRemoveFile: '<img class="eximg pointer" src="' + contextPath + 'common/img/delete.png" alt="삭제">',
    uploadMultiple: true,
    init: dzInit
};

document.getElementById('upload-modal-btn').addEventListener('click', function () {
    if (document.getElementById('files').childElementCount >= 1) {
        alert('최대 1개의 파일까지 업로드 할 수 있습니다.');
        return false;
    }

    openModal('upload-modal');
});

const deleteAttach = function (el, id) {
    const div = el.parentNode;
    div.parentNode.removeChild(div);
}