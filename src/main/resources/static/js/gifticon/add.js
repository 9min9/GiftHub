


renderGifticon();
// setGifticonAddModal();

function renderGifticon(jsonData) {
    let listContainer = document.getElementById('gifticon-list-div');
    let gifticonRow = document.createElement("div");
    gifticonRow.setAttribute('class', 'w-r u-s-m-b-30');

    let gifticonContainer = document.createElement('div');
    // gifticonContainer.setAttribute('id', jsonData.id);
    gifticonContainer.setAttribute('id', jsonData.id);
    gifticonContainer.setAttribute('class', 'w-r__container');

    let gifticonSection = document.createElement('div');
    gifticonSection.setAttribute('class', 'w-r__wrap-1');

    let image = renderImage(jsonData);
    let info = renderInfo(jsonData);
    let btn = renderGifticonBtn();

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
    image.setAttribute('id', 'gifticon-img-' +jsonData.id);
    image.setAttribute('class', 'w-r__img-wrap u-img-fluid');
    // image.setAttribute('src', 'https://myawsimgbucket.s3.ap-northeast-2.amazonaws.com/5de365d9-09bf-47ce-adad-610e34797e4a..jpg');
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

    section.appendChild(brand);
    section.appendChild(product);
    section.appendChild(barcode);
    section.appendChild(due);

    return section;
}

function renderBrand(jsonData) {
    let div = document.createElement('div');
    let content = document.createElement('span');
    let error = document.createElement('span');
    div.setAttribute('class', 'gl-inline');

    content.setAttribute('id', 'gifticon-brand-'+jsonData.id);
    // content.setAttribute('id', 'gifticon-brand-pk');
    content.setAttribute('class', 'w-r__category')
    content.append(jsonData.brand);
    // content.append("브랜드 이름");

    error.setAttribute('id', 'gifticon-brand-error-' +jsonData.id);
    // error.setAttribute('id', 'gifticon-brand-error-pk');
    error.setAttribute('class', 'u-s-m-x-10');
    error.setAttribute('style', 'color: red');
    error.append("testError");

    div.appendChild(content);
    div.appendChild(error);

    return div;
}

function renderProduct(jsonData) {
    let div = document.createElement('div');
    let content = document.createElement('span');
    let error = document.createElement('span');
    div.setAttribute('class', 'gl-inline');

    content.setAttribute('id', 'gifticon-product-'+jsonData.id);
    // content.setAttribute('id', 'gifticon-product-pk');
    content.setAttribute('class', 'w-r__category')
    content.append(jsonData.productName);
    // content.append("상품 이름");

    // error.setAttribute('id', 'gifticon-product-error-' +jsonData.id);
    error.setAttribute('id', 'gifticon-product-error-pk');
    error.setAttribute('class', 'u-s-m-x-10');
    error.setAttribute('style', 'color: red');
    error.append("testError");

    div.appendChild(content);
    div.appendChild(error);

    return div;
}

function renderBarcode(jsonData) {
    let div = document.createElement('div');
    let content = document.createElement('span');
    let error = document.createElement('span');
    div.setAttribute('class', 'gl-inline');

    content.setAttribute('id', 'gifticon-barcode-'+jsonData.id);
    // content.setAttribute('id', 'gifticon-barcode-pk');
    content.setAttribute('class', 'w-r__category')
    content.append(jsonData.barcode);
    // content.append("바코드 번호");

    error.setAttribute('id', 'gifticon-barcode-error-' +jsonData.id);
    // error.setAttribute('id', 'gifticon-barcode-error-pk');
    error.setAttribute('class', 'u-s-m-x-10');
    error.setAttribute('style', 'color: red');
    error.append("testError");

    div.appendChild(content);
    div.appendChild(error);

    return div;
}


function renderDue(jsonData) {
    let div = document.createElement('div');
    let content = document.createElement('span');
    let error = document.createElement('span');
    div.setAttribute('class', 'gl-inline');

    content.setAttribute('id', 'gifticon-due-'+jsonData.id);
    // content.setAttribute('id', 'gifticon-due-pk');
    content.setAttribute('class', 'w-r__category')
    content.append(jsonData.due);
    // content.append("유효 기간");

    error.setAttribute('id', 'gifticon-due-error-' +jsonData.id);
    error.setAttribute('id', 'gifticon-due-error-pk');
    error.setAttribute('class', 'u-s-m-x-10');
    error.setAttribute('style', 'color: red');
    error.append("testError");

    div.appendChild(content);
    div.appendChild(error);

    return div;
}

function renderGifticonBtn() {
    let div = document.createElement('div');
    let delBtn = document.createElement('a');
    let checkBtn = document.createElement('a');

    div.setAttribute('class', 'w-r-__wrap-2');
    delBtn.setAttribute('class', 'w-r__link btn--e-transparent-platinum-b-2');
    // delBtn.setAttribute('onclick', 'delGifticonStoarge()');
    delBtn.append("삭제");

    checkBtn.setAttribute('class', 'w-r__link btn--e-brand-b-2');
    checkBtn.setAttribute('data-modal', 'modal');
    checkBtn.setAttribute('data-modal-id', '#newsletter-modal');
    checkBtn.setAttribute('onclick', 'setGifticonAddModal(this)');
    checkBtn.append("등록 하기");

    div.appendChild(delBtn);
    div.appendChild(checkBtn);

    return div;
}

function setGifticonAddModal(element) {
    let parentNode = element.parentNode.parentNode;
    let pk = parentNode.id;

    let brandValue = parentNode.querySelector('#gifticon-brand-' +pk).textContent;
    let productValue = parentNode.querySelector('#gifticon-product-' +pk).textContent;
    let barcodeValue = parentNode.querySelector('#gifticon-barcode-' +pk).textContent;
    let dueValue = parentNode.querySelector('#gifticon-due-' +pk).textContent;

    let productName = document.getElementById("product-modal-input");
    let brand = document.getElementById("brand-modal-input");
    let due = document.getElementById("due-modal-input");
    let barcode = document.getElementById("barcode-modal-input");

    productName.setAttribute("value", productValue);
    brand.setAttribute("value", brandValue);
    due.setAttribute("value", dueValue);
    barcode.setAttribute("value", barcodeValue);
}