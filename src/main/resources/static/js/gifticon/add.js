document.onload = function () {
    $('#product-modal-input').on('change', function () {
        validateProduct();
    });
}

function renderGifticon(jsonData) {
    let listContainer = document.getElementById('gifticon-list-div');
    let gifticonRow = document.createElement("div");
    gifticonRow.setAttribute('class', 'w-r u-s-m-b-30');

    let gifticonContainer = document.createElement('div');
    gifticonContainer.setAttribute('id', jsonData.gifticonStorageId);
    gifticonContainer.setAttribute('class', 'w-r__container');

    let gifticonSection = document.createElement('div');
    gifticonSection.setAttribute('class', 'w-r__wrap-1');

    let image = renderImage(jsonData);
    let info = renderInfo(jsonData);
    let btn = renderGifticonBtn(jsonData);

    gifticonSection.appendChild(image);
    gifticonSection.appendChild(info);

    gifticonContainer.appendChild(gifticonSection);
    gifticonContainer.appendChild(btn);

    gifticonRow.appendChild(gifticonContainer);
    listContainer.appendChild(gifticonRow);
}


function renderImage(jsonData) {
    let div = document.createElement('div');
    let image = document.createElement('img');

    div.setAttribute('class', 'w-r__img-wrap');
    image.setAttribute('id', 'gifticon-img-' + jsonData.gifticonStorageId);
    image.setAttribute('class', 'w-r__img-wrap u-img-fluid');
    image.setAttribute('src', jsonData.imageUrl);
    image.setAttribute('alt', '이미지가 없어용');

    div.appendChild(image);

    return div;
}

function renderInfo(jsonData) {
    let section = document.createElement('div');
    section.setAttribute('class', 'w-r__info');

    let brand = renderBrand(jsonData);
    let product = renderProduct(jsonData);
    let barcode = renderBarcode(jsonData);
    let due = renderDue(jsonData);
    let status = renderStatus(jsonData);

    section.appendChild(brand);
    section.appendChild(product);
    section.appendChild(barcode);
    section.appendChild(due);
    section.appendChild(status);

    return section;
}

function renderBrand(jsonData) {
    let div = document.createElement('div');
    let content = document.createElement('span');
    let error = document.createElement('span');
    div.setAttribute('class', 'gl-inline');

    content.setAttribute('id', 'gifticon-brand-' + jsonData.gifticonStorageId);
    content.setAttribute('class', 'w-r__category')

    if (jsonData.brand == null || jsonData.brand == "") {
        jsonData.brand = "브랜드 이름이 존재하지 않습니다."

        error.setAttribute('id', 'gifticon-brand-error-' + jsonData.gifticonStorageId);
        error.setAttribute('class', 'u-s-m-x-10');
        error.setAttribute('style', 'color: red');
        error.append(jsonData.brand);

    } else {
        content.append(jsonData.brand);
    }

    div.appendChild(content);
    div.appendChild(error);

    return div;
}

function renderProduct(jsonData) {
    let div = document.createElement('div');
    let content = document.createElement('span');
    let error = document.createElement('span');
    div.setAttribute('class', 'gl-inline');

    content.setAttribute('id', 'gifticon-product-' + jsonData.gifticonStorageId);
    content.setAttribute('class', 'w-r__category')

    if (jsonData.productName == null || jsonData.productName == "") {
        jsonData.productName = "상품 이름이 존재하지 않습니다."

        error.setAttribute('id', 'gifticon-product-error-' + jsonData.gifticonStorageId);
        error.setAttribute('class', 'u-s-m-x-10');
        error.setAttribute('style', 'color: red');
        error.append(jsonData.productName);

    } else {
        content.append(jsonData.productName);
    }

    div.appendChild(content);
    div.appendChild(error);

    return div;
}

function renderBarcode(jsonData) {
    let div = document.createElement('div');
    let content = document.createElement('span');
    let error = document.createElement('span');
    div.setAttribute('class', 'gl-inline');

    content.setAttribute('id', 'gifticon-barcode-' + jsonData.gifticonStorageId);
    content.setAttribute('class', 'w-r__category')

    if(jsonData.barcode == null || jsonData.barcode == "") {
        jsonData.barcode = "바코드 번호가 존재하지 않습니다.";

        error.setAttribute('id', 'gifticon-barcode-error-' + jsonData.gifticonStorageId);
        error.setAttribute('class', 'u-s-m-x-10');
        error.setAttribute('style', 'color: red');
        error.append(jsonData.barcode);
    } else {
        content.append(jsonData.barcode);
    }
    div.appendChild(content);
    div.appendChild(error);

    return div;
}


function renderDue(jsonData) {
    let div = document.createElement('div');
    let content = document.createElement('span');
    let error = document.createElement('span');
    div.setAttribute('class', 'gl-inline');

    content.setAttribute('id', 'gifticon-due-' + jsonData.gifticonStorageId);
    content.setAttribute('class', 'w-r__category')

    if (jsonData.due == null || jsonData.due == "") {
        jsonData.due = "유효기간이 존재하지 않습니다.";
        error.setAttribute('id', 'gifticon-due-error-' + jsonData.gifticonStorageId);
        error.setAttribute('class', 'u-s-m-x-10');
        error.setAttribute('style', 'color: red');
        error.append(jsonData.due);

    } else {
        content.append(jsonData.due);
    }

    div.appendChild(content);
    div.appendChild(error);

    return div;
}

function renderStatus(jsonData) {
    let div = document.createElement('div');
    let flag = document.createElement('span');
    let status = document.createElement('span');

    flag.setAttribute('id', 'gifticon-flag-' + jsonData.gifticonStorageId);
    flag.setAttribute('value', jsonData.flagInDb);

    status.setAttribute('id', 'gifticon-status-' + jsonData.gifticonStorageId);
    status.setAttribute('value', jsonData.status);

    div.appendChild(flag);
    div.appendChild(status);

    return div
}


function renderGifticonBtn(jsonData) {
    let div = document.createElement('div');
    let delBtn = document.createElement('a');

    div.setAttribute('class', 'w-r-__wrap-2');
    delBtn.setAttribute('class', 'w-r__link btn--e-transparent-platinum-b-2');
    delBtn.append("삭제");

    delBtn.addEventListener('click', function () {
        delStorage(jsonData.gifticonStorageId)
        window.location.reload();
    });

    if (jsonData.status == "ADMIN_APPROVAL") {
        let span = document.createElement('span');
        span.innerText = "등록 신청중 입니다."

        div.appendChild(span);
        div.appendChild(delBtn);
    } else {
        div.appendChild(delBtn);

        let checkBtn = document.createElement('a');

        checkBtn.setAttribute('class', 'w-r__link btn--e-brand-b-2');
        checkBtn.setAttribute('data-modal', 'modal');
        checkBtn.setAttribute('data-modal-id', '#newsletter-modal');

        checkBtn.addEventListener('click', function () {
            setGifticonAddModal(this);
            openModal(this);
        });
        checkBtn.append("등록 하기");
        div.appendChild(checkBtn);
    }
    return div;
}

function setGifticonAddModal(element) {
    let parentNode = element.parentNode.parentNode;
    let pk = parentNode.id;

    let flagValue = parentNode.querySelector('#gifticon-flag-' + pk).getAttribute('value');
    let imageUrlValue = parentNode.querySelector('#gifticon-img-' + pk).getAttribute('src');
    let brandValue = parentNode.querySelector('#gifticon-brand-' + pk).textContent;
    let productValue = parentNode.querySelector('#gifticon-product-' + pk).textContent;
    let barcodeValue = parentNode.querySelector('#gifticon-barcode-' + pk).textContent;
    let dueValue = parentNode.querySelector('#gifticon-due-' + pk).textContent;

    let status = document.getElementById('product-valid');
    let productValidResult = document.getElementById('product-valid-error');
    let image = document.getElementById("gifticon-modal-img");
    let productName = document.getElementById("product-modal-input");
    let brand = document.getElementById("brand-modal-input");
    let due = document.getElementById("due-modal-input");
    let barcode = document.getElementById("barcode-modal-input");
    let addBtn = document.getElementById('gifticon-add-modal-btn');

    status.setAttribute('value', flagValue);
    image.setAttribute('src', imageUrlValue);
    productName.setAttribute("value", productValue);
    brand.setAttribute("value", brandValue);
    due.setAttribute("value", dueValue);
    barcode.setAttribute("value", barcodeValue);

    if(flagValue == "true") {
        productValidResult.innerText = "";
        addBtn.innerText = "등록"
        addBtn.setAttribute('onclick', "addGifticon("+pk+")");

    } else {
        document.createElement('div');

        productValidResult.innerText = "상품 정보가 없어 관리자의 확인이 필요합니다.";
        addBtn.innerText = "등록 요청"
        addBtn.setAttribute('onclick', "sendToAdmin("+pk+")");
    }
}

function openModal(element) {
    let getElemId = $(element).data('modal-id');
    $(getElemId).modal({
        backdrop: 'static',
        keyboard: false,
        show: true
    });
}

function addGifticon(pk) {

    let form = document.getElementById("gifticon-add-form");
    let formData = new FormData(form);
    let data = {};

    formData.forEach(function(value, key){
        data[key] = value;
    });

    data['storageId'] = pk;

    $.ajax({
        type: "post",
        url: "/api/gifticon/register",
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        headers: {
            Authorization: localStorage.getItem("token"),
        },

        success: function (jsonData) {
            alert("기프티콘 등록이 완료되었습니다");

            console.log(jsonData);

            document.location.href = "/gifticon/add";
        },
        error: function (error) {
            console.log(error.responseJSON);
            checkError(error.responseJSON);
        }
    });
}

function sendToAdmin(pk) {
    //todo form을 받아 submit 처리하고 gifticonStorage를 update 상태를 검수 상태로 변경
    let form = document.getElementById("gifticon-add-form");

    let formData = new FormData(form);
    let data = {};

    formData.forEach(function(value, key){
        data[key] = value;
    });
    data['id'] = pk;

    $.ajax({
        type: "post",
        url: "/api/admin/gifticon/register",
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        headers: {
            Authorization: localStorage.getItem("token"),
        },

        success: function (jsonData) {
            alert("등록 요청이 완료되었습니다")
        },

        error: function (error) {
            validateForm(error.responseJSON);
            alert("등록 요청에 실패했습니다.");
        }
    });
}

function validateProduct() {
    let target = document.getElementById('product-modal-input');

}

function validateForm(error) {
    let target = document.getElementById("error-" +error.field+ "-modal-label");
    target.innerText = error.message;
}

function validateProduct() {
    let data = {
        product: $("#product-modal-input").val()
    };

    $.ajax({
        type: "post",
        url: "/api/gifticon/add/validate/product",
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        headers: {
            Authorization: localStorage.getItem("token"),
        },

        success: function (jsonData) {
            let label = document.getElementById("result-" + result.target + "-label");
            label.innerText = "";

        },
        error: function (error) {
            checkError(error.responseJSON);
        }
    });
}

function checkError(result) {
    let label = document.getElementById("error-" + result.field + "-modal-label");

    if (result.status == "error") {
        label.setAttribute('style', 'color: red;');
        label.innerText = result.message;

    }

}

function delStorage(storageId) {
    $.ajax({
        type: "post",
        url: "/api/storage/delete/"+storageId,
        headers: {
            Authorization: localStorage.getItem("token"),
        },

        success: function (jsonData) {
            alert("삭제되었습니다.");
        },
        error: function (error) {
            alert("삭제에 실패했습니다");
        }
    });

}