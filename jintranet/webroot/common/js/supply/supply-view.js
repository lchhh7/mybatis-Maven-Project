const moveEditPage = function () {
    if (!confirm('신청내역을 수정페이지로 이동하시겠습니까?')) return false;
    location.href =  contextPath + 'supply/edit.do?id=' + viewForm.id.value;
};

const editPay = function () {
    if (!confirm('결제방법, 계산서 발행 여부를 저장하시겠습니까?\n신청내역 수정은 승인대기 상태에서만 가능합니다.')) return false;

    const payment = viewForm.payment.value;
    const billYN = viewForm.billYN.value;

    if (status !== "Y") {
        alert('승인상태가 아닌경우 저장할 수 없습니다.');
        return false;
    }

    if (payment.trim().length === 0 || !(billYN === "Y" || billYN === "N")) {
        alert('결제방법과 계산서발행 여부 모두 입력해주세요.');
        return false;
    }

    $.ajax({
        url: contextPath + 'supply/edit/pay.do',
        method: 'patch',
        data: JSON.stringify({
            id: viewForm.id.value,
            payment: viewForm.payment.value,
            billYN: viewForm.billYN.value
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

const deleteSupply = function () {
    if (!confirm('비품 신청내역을 삭제하시겠습니까?')) return false;

    $.ajax({
        url: contextPath + 'supply/edit.do',
        method: 'delete',
        data: JSON.stringify({id: viewForm.id.value}),
        contentType: 'application/json; charset:utf-8'
    })
        .done(function (data) {
            alert(data);
            location.href = contextPath + 'supply.do';
        })
        .fail(function (data) {
            alert(data.responseText);
            return false;
        });
};

document.addEventListener('DOMContentLoaded', function () {
    if (status === 'R') {
        editBtn.innerText = "수정";
        editBtn.addEventListener('click', moveEditPage, true);

        document.getElementById('delete-btn').addEventListener('click', deleteSupply, true);
    } else if (status === 'Y') {
        editBtn.innerText = "저장";
        editBtn.addEventListener('click', editPay, true);
    }
});
