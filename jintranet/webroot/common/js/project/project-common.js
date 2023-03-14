const resetCompany = function (divId) {
    const div = document.getElementById(divId);
    const elCnt = div.childElementCount;

    for (let i = 0; i < elCnt; i++) {
        let el = div.firstElementChild;
        if (el.tagName === "INPUT") {
            el.remove();
        }
    }
};

const saveCompany = function (list, divId, btnId, modalId) {
    const btn = document.getElementById(btnId);
    const div = document.getElementById(divId);

    resetCompany(divId);

    const length = list.length - 1;

    list.forEach(function (e, i) {
        console.log(i);
        let hidden = document.createElement("input");
        hidden.setAttribute('type', 'hidden');
        hidden.setAttribute('name', divId + '[' + i + ']');
        hidden.value = list[i].id;

        div.insertBefore(hidden, btn);

        let input = document.createElement("input");
        input.setAttribute("type", "text");
        input.classList.add("inputbox");
        input.classList.add("width60");

        if (i < length) {
            input.classList.add("mb5");
        } else if (i === length) {
            input.classList.add("mr5");
        }

        input.setAttribute('readonly', true);
        input.setAttribute('disabled', true);
        input.value = list[i].companyName;

        div.insertBefore(input, btn);
    });

    document.getElementById(modalId).classList.remove('show-modal');
}

const resetCompanyYn = function (tr) {
    Array.prototype.forEach.call(tr, function (el) {
        el.getElementsByClassName('check')[0].checked = false;
    });
}

const checkCompanyYn = function (tr, list) {
    Array.prototype.forEach.call(tr, function (el) {
        let checkbox = el.getElementsByClassName('check')[0];
        let companyId = el.getElementsByClassName('companyId')[0].innerText;

        list.forEach(function (e) {
            if (e === companyId) {
                checkbox.checked = true;
            }
        })
    });
}

// 컨소시움 시작
const openConModal = function () {
    const modal = document.getElementById('consortium-modal');
    const tbody = document.getElementById('consortium-company');
    const tr = tbody.getElementsByTagName('tr');

    let consortiums = getCompany('consortiums');
    resetCompanyYn(tr);
    checkCompanyYn(tr, consortiums);

    modal.classList.add('show-modal');
}

const closeConModal = function () {
    if (!confirm('저장하지 않고 창을 종료하시겠습니까?')) return false;
    document.getElementById('consortium-modal').classList.remove('show-modal');
}


const saveConModal = function () {
    const tbody = document.getElementById('consortium-company');
    const tr = tbody.getElementsByTagName('tr');

    let consortiums = [];
    Array.prototype.forEach.call(tr, function (el) {
        let checkbox = el.getElementsByClassName('check')[0];

        if (checkbox.checked) {
            consortiums.push({
                id: el.getElementsByClassName('companyId')[0].innerText,
                companyName: el.getElementsByClassName('companyName')[0].innerText
            });
        }
    })

    if (consortiums.length === 0) {
        if (!confirm("선택된 기업이 없습니다. 저장하시겠습니까?")) return false;
        document.getElementById("consortium-yn").checked = false;
        changeConYn();
        resetCompany('consortiums');
        document.getElementById('consortium-modal').classList.remove('show-modal');
        return false;
    }

    if (!confirm("컨소시엄 업체 참여여부를 저장하시겠습니까?")) return false;

    saveCompany(consortiums, 'consortiums', 'add-consortium', 'consortium-modal');
}

const warningConModal = function () {
    if (!confirm('"컨소시움으로 구성"에 체크한 뒤 업체를 선택 할 수 있습니다.\n"컨소시엄으로 구성"에 체크하시겠습니까?')) return false;

    document.getElementById('consortium-yn').checked = true;
    changeConYn();
    openConModal();
};

const changeConYn = function () {
    const checkbox = document.getElementById('consortium-yn');
    const addBtn = document.getElementById('add-consortium');

    checkbox.checked ? (
        addBtn.removeEventListener('click', warningConModal, true),
            addBtn.addEventListener('click', openConModal, true)
    ) : (
        addBtn.addEventListener('click', warningConModal, true),
            addBtn.removeEventListener('click', openConModal, true)
    );
}

// 하도급 시작

const openSubModal = function () {
    // 선택된 애들 체크하기
    const modal = document.getElementById('subcontract-modal');
    const tbody = document.getElementById('subcontract-company');
    const tr = tbody.getElementsByTagName('tr');

    let subcontract = getCompany('subcontracts');
    resetCompanyYn(tr);
    checkCompanyYn(tr, subcontract);

    modal.classList.add('show-modal');
}

const closeSubModal = function () {
    if (!confirm('저장하지 않고 창을 종료하시겠습니까?')) return false;
    document.getElementById('subcontract-modal').classList.remove('show-modal');
}


const saveSubModal = function () {
    const tbody = document.getElementById('subcontract-company');
    const tr = tbody.getElementsByTagName('tr');

    let subcontracts = [];
    Array.prototype.forEach.call(tr, function (el) {
        let checkbox = el.getElementsByClassName('check')[0];

        if (checkbox.checked) {
            subcontracts.push({
                id: el.getElementsByClassName('companyId')[0].innerText,
                companyName: el.getElementsByClassName('companyName')[0].innerText
            });
        }
    })

    if (subcontracts.length === 0) {
        if (!confirm("선택된 기업이 없습니다. 저장하시겠습니까?")) return false;
        document.getElementById("subcontract-yn").checked = false;
        changeSubYn();
        resetCompany('subcontracts');
        document.getElementById('subcontract-modal').classList.remove('show-modal');
        return false;
    }

    if (!confirm("하도급 업체 참여여부를 저장하시겠습니까?")) return false;

    saveCompany(subcontracts, 'subcontracts', 'add-subcontract', 'subcontract-modal');
}

const warningSubModal = function () {
    if (!confirm('"하도급 있음"에 체크한 뒤 업체를 선택 할 수 있습니다.\n"하도급 있음"에 체크하시겠습니까?')) return false;

    document.getElementById('subcontract-yn').checked = true;
    changeSubYn();
    openSubModal();
};

const changeSubYn = function () {
    const checkbox = document.getElementById('subcontract-yn');
    const addBtn = document.getElementById('add-subcontract');

    checkbox.checked ? (
        addBtn.removeEventListener('click', warningSubModal, true),
            addBtn.addEventListener('click', openSubModal, true)
    ) : (
        addBtn.addEventListener('click', warningSubModal, true),
            addBtn.removeEventListener('click', openSubModal, true)
    );
}

const getCompany = function (divId) {
    const div = document.getElementById(divId);
    let list = [];

    Array.prototype.forEach.call(div.children, function (el) {
        if (el.type === 'hidden' && el.tagName === 'INPUT') {
            list.push(el.value);
        }
    })
    return list;
};

const confirmProject = function () {
    const form = document.project;

    const requiredFields = [
        form.title, form.orderingName, form.contractNo, form.businessField,
        form.startDt, form.endDt, form.launchDt, form.amountSurtaxInclude,
        form.amountSurtaxExclude, form.contractDepositRate, form.contractDeposit,
        form.defectDepositRate, form.defectDeposit, form.department,
        form.projectManagerName
    ];

    if (confirmRequiredField(requiredFields) === false) return false;

    const registYN = getRadioValue('performanceRegistYN');
    if (registYN === "") {
        alert("사업실적 등록여부를 선택해주세요.");
        return false;
    }

    let result = {
        title: form.title.value,
        orderingName: form.orderingName.value,
        ppsYN: form.ppsYN.checked ? 'Y' : 'N',
        contractNo: form.contractNo.value,
        businessField: form.businessField.value,
        startDt: form.startDt.value,
        endDt: form.endDt.value,
        launchDt: form.launchDt.value,
        amountSurtaxInclude: form.amountSurtaxInclude.value.replaceAll(',', ''),
        amountSurtaxExclude: form.amountSurtaxExclude.value.replaceAll(',', ''),
        contractDepositRate: form.contractDepositRate.value,
        contractDeposit: form.contractDeposit.value.replaceAll(',', ''),
        defectDepositRate: form.defectDepositRate.value,
        defectDeposit: form.defectDeposit.value.replaceAll(',', ''),
        consortiumYN: form.consortiumYN.checked ? 'Y' : 'N',
        consortiums: getCompany('consortiums'),
        subcontractYN: form.subcontractYN.checked ? 'Y' : 'N',
        subcontracts: getCompany('subcontracts'),
        department: form.department.value,
        projectManagerName: form.projectManagerName.value,
        performanceRegistYN: registYN,
        implementDt: form.implementDt.value,
    };

    return isEditForm(result, form);
}

document.getElementById("consortium-yn").addEventListener("change", changeConYn, true);
document.getElementById('consortium-close').addEventListener('click', closeConModal, true);
document.getElementById('consortium-save').addEventListener('click', saveConModal, true);

document.getElementById("subcontract-yn").addEventListener("change", changeSubYn, true);
document.getElementById('subcontract-close').addEventListener('click', closeSubModal, true);
document.getElementById('subcontract-save').addEventListener('click', saveSubModal, true);