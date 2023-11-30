window.onload = function () {
    getConfirmStorageList();

    $("input[name='isConfirm']").change(function () {
        let isConfirm = $("input:radio[name='isConfirm']:checked").val();

        if(isConfirm) {
            setCategory();
        }
    });

}

function getConfirmStorageList() {
    $.ajax({
        type: "post",
        url: "/api/admin/gifticon/confirm/list",
        headers: {
            Authorization: localStorage.getItem("token"),
        },
        // data: JSON.stringify(data),
        // data: data,
        // contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (jsonData) {
            console.log("!!!")
            console.log(jsonData)
            console.log(jsonData.content)
            console.log(jsonData.totalElements)
            createTableBody(jsonData);
        },
        error: function (error) {
            console.log(error)
        }
    });

}

function createTableBody(jsonData) {
    let tbody = document.getElementById('tbody');

    for (let i = 0; i < jsonData.content.length; i++) {
        let tr = createTable(jsonData.content[i]);
        tbody.appendChild(tr);
    }
}

function createTable(content) {
    let tr = document.createElement('tr');
    let product = document.createElement('td');
    let brandName = document.createElement('td');
    let userName = document.createElement('td');
    let modified = document.createElement('td');
    let btnTd = document.createElement('td');
    let btn = document.createElement('a');
    let div = document.createElement('div');
    let url = document.createElement('span');
    let barcode = document.createElement('span');
    let due = document.createElement('span');

    url.setAttribute('value', content.imageUrl);
    url.setAttribute('id', "imageUrl-" +content.id);

    barcode.setAttribute('id', "barcode-" +content.id);
    barcode.setAttribute('value', content.barcode);

    due.setAttribute('id', "due-" +content.id);
    due.setAttribute('value', content.due);

    tr.setAttribute('id', content.id);

    product.setAttribute('id', 'productName-' + content.id);
    product.append(content.productName);

    brandName.setAttribute('id', 'brandName-' + content.id)
    brandName.append(content.brandName);

    userName.setAttribute('id', 'userName-' +  content.id)
    userName.append(content.userName);

    modified.setAttribute('id', 'modified-' +  content.id)
    modified.append(content.modifiedDate);

    btn.setAttribute('data-modal', 'modal');
    btn.setAttribute('data-modal-id', '#newsletter-modal')
    btn.append("확인")

    btn.addEventListener('click', function () {
        console.log(this)
        setGifticonAddModal(this);
        openModal(this);
    });

    div.setAttribute('class', 'dash__link dash__link--brand');
    div.appendChild(btn);
    btnTd.append(div);

    div.append(url)
    div.append(barcode)
    div.append(due)
    tr.append(product)
    tr.append(brandName)
    tr.append(userName)
    tr.append(modified)
    tr.append(btnTd)
    return tr;
}


function setGifticonAddModal(element) {
    let parentNode = element.parentNode.parentNode.parentNode;
    let pk = parentNode.id;

    let imageUrlValue = parentNode.querySelector('#imageUrl-' + pk).getAttribute('value');
    let brandValue = parentNode.querySelector('#brandName-' + pk).textContent;
    let productValue = parentNode.querySelector('#productName-' + pk).textContent;
    let barcodeValue = parentNode.querySelector('#barcode-' + pk).getAttribute('value');
    let dueValue = parentNode.querySelector('#due-' + pk).getAttribute('value');

    let image = document.getElementById("gifticon-modal-img");
    let productName = document.getElementById("product-modal-input");
    let brand = document.getElementById("brand-modal-input");
    let due = document.getElementById("due-modal-input");
    let barcode = document.getElementById("barcode-modal-input");
    let addBtn = document.getElementById('gifticon-confirm-modal-btn');

    addBtn.setAttribute('onclick', "addProductAndGifticon("+pk+")");

    image.setAttribute('src', imageUrlValue);
    productName.setAttribute("value", productValue);
    brand.setAttribute("value", brandValue);
    due.setAttribute("value", dueValue);
    barcode.setAttribute("value", barcodeValue);
}

function openModal(element) {
    let getElemId = $(element).data('modal-id');
    $(getElemId).modal({
        backdrop: 'static',
        keyboard: false,
        show: true
    });
}


function setCategory() {
    $.ajax({
        type: "post",
        url: "/api/product/get/category",
        contentType: "application/json; charset=utf-8",
        // dataType: "json",
        headers: {
            Authorization: localStorage.getItem("token"),
        },

        success: function (jsonData) {
            let categorySelector = document.getElementById('category-select');

            for (const [key, value] of Object.entries(jsonData)) {
                const optionElement = document.createElement("option");
                optionElement.value = key;
                optionElement.textContent = value;
                categorySelector.appendChild(optionElement);
            }
        },

        error: function (error) {
            alert("카테고리 정보를 불러오는데 실패했습니다.");
        }

    });
}

function addProductAndGifticon(pk) {

    let form = document.getElementById("product-add-form");
    let formData = new FormData(form);
    let data = {};

    formData.forEach(function(value, key){
        data[key] = value;
    });

    data['storageId'] = pk;

    $.ajax({
        type: "post",
        url: "/api/admin/gifticon/confirm/register",
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        headers: {
            Authorization: localStorage.getItem("token"),
        },

        success: function (jsonData) {
            alert("기프티콘 검수가 완료되었습니다");
            window.location.href = "/admin/index";
        },
        error: function (xhr, status, error) {
            alert("기프티콘 검수에 실패했습니다");
        }
    });
}