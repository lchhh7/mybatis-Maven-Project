const amountInput = document.getElementById('amount');
const priceInput = document.getElementById('price');
const shippingFee = document.getElementById('shippingFee');
const totalInput = document.getElementById('total');

const calculateTotal = function () {
    totalInput.value = (parseInt(priceInput.value * amountInput.value) +parseInt(shippingFee.value)).toLocaleString('ko-KR');
};

amountInput.addEventListener('keyup', calculateTotal, true);
priceInput.addEventListener('keyup', calculateTotal, true);
shippingFee.addEventListener('keyup', calculateTotal, true);

const confirmSupply = function (form) {
	const urlLength = $("[name=urlLink]").parent('td').children('input').length;
	
	
    const requiredFields = [
        form.name, form.amount, form.price, form.approveId, form.payment , form.billYN
    ];

    if (confirmRequiredField(requiredFields) === false) return false;

    let data = {
        name: form.name.value,
        amount: form.amount.value,
        price: form.price.value,
		payment: form.payment.value,
		shippingFee : form.shippingFee.value,
		billYN: form.billYN.value,
        url: form.url.value,
        attaches: attaches,
        approveId: form.approveId.value
    };

	if(urlLength >= 2) {
		data.url1 = form.url1.value
	}
	if(urlLength >= 3) {
		data.url2 = form.url2.value
	}
	if(urlLength >= 4) {
		data.url3 = form.url3.value
	}
	if(urlLength == 5) {
		data.url4 = form.url4.value
	}
	

    return isEditForm(data, form);
};

Dropzone.options.dropzone = {
    url: contextPath + 'supply/upload.do',
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

$(document).ready(function() {
	var i = $("[name=urlLink]").parent('td').children('input').length;
  $("[name=urlLink]").click(function() {
	if(i <= 4){
    	$("[name=urlLink]").parent('td').append('<input class="inputbox width70" type="text" name="url'+i+'">');
    	i++; 
	}else{
		alert('URL은 최대 5개까지만 등록 가능합니다.');
	}
  });
});