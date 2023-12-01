let page = 0;

function getPurchasingGifticon(page, size, type = "전체") {
    page = page || 1;
    size = size || 10;

    $.ajax({
        url: "/api/gifticons/" + type,
        type: "get",
        data: {
            page,
            size,
        },
        success: function (result) {
            if (result.content.length != 0) {
                print(result.content);
            }
        }
    });
}

function print(jsonData) {
    for (let j of jsonData) {
        gifticon(j);
    }

    document.querySelectorAll(".add-to-cart").forEach((elem) => {
        elem.addEventListener("click", function (event) {
            let gifticonId = event.target.dataset.gifticonId;

            let xhr = new XMLHttpRequest();

            xhr.open("post", "/api/carts");

            xhr.onload = () => {
                if (xhr.status != 200) {
                    return;
                }

                document.querySelector("#total-item-round").innerText =
                    parseInt(document.querySelector("#total-item-round").innerText) + 1;

                // TODO 미니카트에 기프트콘 넣어야함
            };

            let sendDate = {
                gifticonId,
            }

            xhr.send();
        });
    });
}

function setProductSelectorEvent() {
    document.querySelectorAll(".product-selector").forEach((elem) => {
        elem.addEventListener("click", function (event) {
            page = 0;
            setJsCheckedToTotal();
            document.querySelectorAll(".product-selector-container").forEach(elem => {
                elem.classList.remove("category-active");
            });

            event.target.parentNode.classList.add("category-active");

            document.querySelectorAll(".filter__item").forEach(function (elem) {
                elem.remove();
            });

            clearBrand();
            clearProducts();
            setBrand(event.target.parentNode.querySelector("input[type='hidden']").value.replaceAll("/", "-"));
            getProductByCategoryAndBrand(event.target.parentNode.querySelector(".product-name").innerText);

            scrollTo({top: document.querySelector("#show-product-div").offsetTop, behavior: "smooth"});

        });
    })
}

function clearJsChecked() {
    document.querySelectorAll(".brand-filter").forEach(element => {
        element.classList.remove("js-checked");
    })
}

function totalFilterEvent() {
    document.querySelector(".total-filter").addEventListener("click", function (event) {
        let categoryName = document.querySelector(".category-active").querySelector(".product-name").innerText;

        clearProducts();
        getProductByCategoryAndBrand(categoryName);
    });
}

function setJsCheckedToTotal() {
    clearJsChecked();
    document.querySelector(".total-filter").classList.add("js-checked");
}

function clearBrand() {
    document.querySelectorAll(".filter__btn").forEach(element => {
        if (!element.classList.contains("total-filter")) {
            element.remove();
        }
    });
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

function createHiddenInput(gifticonId) {
    let input = document.createElement("input");

    input.type = "hidden";
    input.value = gifticonId;
    input.classList.add("gifticonId")
    input.name = "gifticonIds";

    return input;
}

function createItemDiv() {
    let div = document.createElement('div');
    // div.setAttribute('class', 'col-xl-3 col-lg-4 col-md-6 col-sm-6 u-s-m-b-30 filter__item');
    div.setAttribute('class', 'col-xl-3 col-lg-4 col-md-6 col-sm-6 u-s-m-b-30 filter__item headphone');
    return div;
}

function createProductDiv() {
    let div = document.createElement('div');
    div.setAttribute('class', 'product-o product-o--hover-on product-o--radius gifticon-container');
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

function createActionLi(action, gifticonId) {
    let li = document.createElement('li');
    let a = document.createElement('a');
    let i = document.createElement('i');

    a.classList.add("add-to-cart");

    a.setAttribute('data-modal', 'modal');
    a.setAttribute('data-tooltip', 'tooltip');
    a.setAttribute('data-placement', 'top');

    if (action == "quickView") {
        a.setAttribute('data-modal-id', '#quick-look');
        a.setAttribute("data-gifticonId", gifticonId);
        a.setAttribute('title', 'Quick View');
        i.setAttribute('class', 'fas fa-search-plus');
    }

    if (action == "addToCart") {
        a.setAttribute('data-modal-id', '#add-to-cart');
        a.setAttribute("data-gifticonId", gifticonId);
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


let getBrandButton = (product, brand) => {
    let filter = document.createElement("div");
    filter.classList.add("filter__category-wrapper");

    let filterButton = document.createElement("button");
    filterButton.classList = "btn filter__btn filter__btn--style-1";
    filterButton.classList.add("brand-filter");
    filterButton.dataset.filter = "." + product;
    filterButton.innerText = brand;

    filter.appendChild(filterButton);

    return filter;
}

let setBrand = (category) => {
    let xhr = new XMLHttpRequest();

    xhr.open("get", "/api/product/" + category + "/brands");

    // xhr.setRequestHeader("Authorization", localStorage.getItem("token"));

    xhr.onload = () => {
        let parsed = JSON.parse(xhr.responseText);

        let buttons = [];

        for (let p of parsed) {
            buttons.push(getBrandButton(category, p));
        }

        let container = document.querySelector("#filter-category-container");
        for (let b of buttons) {
            container.appendChild(b);
        }

        brandFilterEvent();
        totalFilterEvent();
    }

    xhr.send()
}

function clearProducts() {
    document.querySelectorAll(".product-wrapper").forEach(element => {
        element.remove()
    });
}

function setProduct(parsed) {
    let productDiv = document.querySelector("#row-product-div");

    for (let p of parsed) {
        let outer = createDivWithClass("col-lg-3 col-md-6 u-s-m-b-30");
        outer.classList.add("product-wrapper")
        let productBox = createDivWithClass("product-o product-o--radius product-o--hover-off u-h-100");
        let product = createDivWithClass("product-o__wrap");
        let imgA = createAWithClass("aspect aspect--bg-grey aspect--square u-d-block");
        let img = createImgWithClass("aspect__img");
        let input = createHiddenInput(p.id);

        if(p.category=="생활/가전/엔터") {
            img = createImgWithClass("aspect__img", "/images/noproductimage/no-image-machine.png");
        }
        if(p.category=="건강/리빙/식품관") {
            img = createImgWithClass("aspect__img", "/images/noproductimage/no-image-healthy.png");
        }

        if(p.category=="뷰티/헤어/바디") {
            img = createImgWithClass("aspect__img", "/images/noproductimage/no-image-beauty.png");
        }

        if(p.category=="베이커리/도넛") {
            img = createImgWithClass("aspect__img", "/images/noproductimage/no-image-bakery.png");
        }

        if(p.category=="아이스크림") {
            img = createImgWithClass("aspect__img", "/images/noproductimage/no-image-icecream.png");
        }

        if(p.category=="버거/피자") {
            img = createImgWithClass("aspect__img", "/images/noproductimage/no-image-fastfood.png");
        }

        if(p.category=="치킨") {
            img = createImgWithClass("aspect__img", "/images/noproductimage/no-image-chicken.png");
        }

        if(p.category=="구이/족발") {
            img = createImgWithClass("aspect__img", "/images/noproductimage/no-image-fried.png");
        }

        if(p.category=="편의점") {
            img = createImgWithClass("aspect__img", "/images/noproductimage/no-image-drugstore.png");
        }

        if(p.category=="백화점/마트") {
            img = createImgWithClass("aspect__img", "/images/noproductimage/no-image-departmentstore.png");
        }
        if(p.category=="레스토랑/뷔페") {
            img = createImgWithClass("aspect__img", "/images/noproductimage/no-image-restaurant.png");
        }

        if(p.category=="주유권") {
            img = createImgWithClass("aspect__img", "/images/noproductimage/no-image-oil.png");
        }

        if(p.category=="영화/음악/도서") {
            img = createImgWithClass("aspect__img", "/images/noproductimage/no-image-culture.png");
        }

        if(p.category=="외식/분식/배달") {
            img = createImgWithClass("aspect__img", "/images/noproductimage/no-image-bunsik.png");
        }


        if(p.category=="카페") {
            img = createImgWithClass("aspect__img", "/images/noproductimage/no-image-cafe.png");
        }

        if(p.category=="기타") {
            img = createImgWithClass("aspect__img", "/images/noproductimage/no-image-etc.png");
        }


        let productBrandName = createSpanWithClass("product-o__category");
        productBrandName.innerText = p.brandName;
        let productName = createSpanWithClass("product-o__name");
        productName.innerText = p.name;

        imgA.appendChild(img);
        product.appendChild(imgA);

        productBox.appendChild(product);
        productBox.appendChild(productBrandName);
        productBox.appendChild(productName);
        productBox.appendChild(input);

        outer.appendChild(productBox);

        productDiv.appendChild(outer);

        product.dataset.modal = "modal";
        product.dataset.modalId = "#newsletter-modal";

        product.addEventListener("click", function(event) {
            let productName = event.target.parentNode.querySelector(".product-o__name").innerText;
            let productId = event.target.parentNode.querySelector("input.gifticonId").value;

            getOnSaleProduct(productId);

            document.querySelector("#modal-product-name").innerText = productName;

            document.querySelector("#gifticon-modal-img").src = event.target.querySelector("img").src;

            $(event.target.dataset.modalId).modal();
        });
    }
}

function brandFilterEvent() {
    document.querySelectorAll(".brand-filter").forEach(element => {
        element.addEventListener("click", function (event) {
            page = 0;

            clearProducts();
            clearJsChecked();

            event.target.classList.add("js-checked");

            let brand = event.target.innerText;

            getProductByCategoryAndBrand(null, brand);

            scrollTo({top: document.querySelector("#show-product-div").offsetTop, behavior: "smooth"});
        });
    });
}

function createDivWithClass(clazz) {
    let div = document.createElement("div");

    div.classList = clazz;

    return div;
}

function createAWithClass(clazz) {
    let a = document.createElement("a");

    a.classList = clazz;

    return a;
}

function createImgWithClass(clazz, img) {
    let imgTag = document.createElement("img");

    imgTag.classList = clazz;

    imgTag.src = img;

    return imgTag;
}

function createSpanWithClass(clazz) {
    let span = document.createElement("span");

    span.classList = clazz;

    return span;
}

let totalCategoryEvent = () => {
    document.querySelector(".total-category").addEventListener("click", function () {
        clearProducts();

        getProductByCategoryAndBrand();
    });
}

function scrollEvent(element) {
    const io = new IntersectionObserver(entries => {
        entries.forEach(entry => {
                if (entry.intersectionRatio > 0) {
                    io.unobserve(element);

                    page++;
                    if (page === 0) {
                        return;
                    }

                    getProductByCategoryAndBrand(
                        document.querySelector(".product-selector-container.category-active")
                            .querySelector(".product-name").innerText,
                        document.querySelector(".brand-filter.js-checked").innerText,
                    );

                    getProductByCategoryAndBrand.onload = () => {
                        io.observe(document.querySelector(".product-wrapper:last-child"));
                    }
                }
            }
        )
    });

    io.observe(element);
}

function getProductByCategoryAndBrand(category = "전체", brand = "전체") {
    let xhr = new XMLHttpRequest();

    let cat = "";
    if (category) {
        cat = category.replaceAll("/", "-");
    } else {
        cat = "공백";
    }

    xhr.open("get", "/api/product/page/" + cat + "/" + brand + "?page=" + page + "&size=12");

    // xhr.setRequestHeader("Authorization", localStorage.getItem("token"));

    xhr.onload = () => {
        const parsed = JSON.parse(xhr.responseText);

        setProduct(parsed.content);

        if (parsed.length !== 0) {
            scrollEvent(document.querySelector(".product-wrapper:last-child"), page);
        }
    }

    xhr.send();
}