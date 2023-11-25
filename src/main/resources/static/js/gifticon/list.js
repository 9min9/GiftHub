function getPurchasingGifticon(page, size) {
    page = page || 1;
    size = size || 10;

    $.ajax({
        url: "/api/gifticons",
        type: "get",
        data: {
            page,
            size,
        },
        success: function(result) {
            if (result.content.length != 0) {
                gifticon(result.content);
            }
        }
    })
}

function print(jsonData) {
    for (let j of jsonData) {
        gitfticon(j);
    }
}

function gifticon(jsonData) {
    let gifitconRowDiv = document.getElementById('row-product-div');
    let itemDiv = createItemDiv();
    let productDiv = createProductDiv();
    let imageAndAction = createImageAndActionDiv();
    let brand = createBrand(jsonData.brandName);
    let productName = createProductName(jsonData.productName);
    let due = createDue(jsonData.due);
    let price = createPrice(jsonData.price, "정가");

    productDiv.appendChild(imageAndAction);
    productDiv.appendChild(brand)
    productDiv.appendChild(productName)
    productDiv.appendChild(due)
    productDiv.appendChild(price)
    itemDiv.appendChild(productDiv);

    gifitconRowDiv.appendChild(itemDiv);
}

function createItemDiv() {
    let div = document.createElement('div');
    // div.setAttribute('class', 'col-xl-3 col-lg-4 col-md-6 col-sm-6 u-s-m-b-30 filter__item');
    div.setAttribute('class', 'col-xl-3 col-lg-4 col-md-6 col-sm-6 u-s-m-b-30 filter__item headphone');
    return div;
}
function createProductDiv() {
    let div = document.createElement('div');
    div.setAttribute('class', 'product-o product-o--hover-on product-o--radius');
    return div;
}
function createImageAndActionDiv() {
    let div = document.createElement('div');
    let image = createImageA();
    let action = createActionDiv();

    div.setAttribute('class', 'product-o__wrap');
    div.appendChild(image);
    div.appendChild(action);

    return div;
}
function createImageA() {
    let a = document.createElement('a');
    let img = document.createElement('img');

    a.setAttribute('class', 'aspect aspect--bg-grey aspect--square u-d-block');
    img.setAttribute('class', 'aspect__img');
    // img.setAttribute('src', '');
    // img.setAttribute('alt', '이미지가 없어용');

    a.appendChild(img);

    return a;
}
function createActionDiv() {
    let div = document.createElement('div');
    let ul = document.createElement('ul');
    let quickView = createActionLi("quickView");
    let addToCart = createActionLi("addToCart");

    div.setAttribute('class', 'product-o__action-wrap')
    ul.setAttribute('class', 'product-o__action-list');
    ul.appendChild(quickView);
    ul.appendChild(addToCart);

    div.appendChild(ul)

    return div;
}
function createActionLi(action) {
    let li = document.createElement('li');
    let a = document.createElement('a');
    let i = document.createElement('i');

    a.setAttribute('data-modal', 'modal');
    a.setAttribute('data-tooltip', 'tooltip');
    a.setAttribute('data-placement', 'top');

    if (action == "quickView") {
        a.setAttribute('data-modal-id', '#quick-look');
        a.setAttribute('title', 'Quick View');
        i.setAttribute('class', 'fas fa-search-plus');
    }

    if (action == "addToCart") {
        a.setAttribute('data-modal-id', '#add-to-cart');
        a.setAttribute('title', 'Add to Cart');
        i.setAttribute('class', 'fas fa-plus-circle');
    }

    a.appendChild(i);
    li.appendChild(a);

    return li;
}
function createBrand(brandName) {
    let span = document.createElement('span');
    let a = document.createElement('a');

    span.setAttribute('class', 'product-o__category');
    a.setAttribute('href', '');
    a.append(brandName)
    span.appendChild(a);

    return span;
}
function createProductName(productName) {
    let span = document.createElement('span');
    let a = document.createElement('a');

    span.setAttribute('class', 'product-o__name');
    a.setAttribute('href', '');
    a.append(productName)
    span.appendChild(a);

    return span;
}
function createDue(due) {
    let div = document.createElement('div');
    div.setAttribute('class', 'product-o__rating gl-rating-style');
    div.append(due);

    return div;
}
function createPrice(price, discount) {
    let priceSpan = document.createElement('span');
    let discountSpan = document.createElement('span');

    priceSpan.setAttribute('class', 'product-o__price');
    discountSpan.setAttribute('class', 'product-o__discount');

    priceSpan.append(price);
    discountSpan.append(discount);
    priceSpan.appendChild(discountSpan);

    return priceSpan;
}