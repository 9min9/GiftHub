function createProductSpan(productName, active) {
    let productSelectors = document.querySelector("#product-selectors");
    let productSelectorContainer = createProductSelectorContainer();
    let productSelector = createProductSelector();
    let productsName = createProductNames(productName);
    let br = createBreak();

    if (active) {
        productSelectorContainer.classList.add("category-active");
    }

    productSelectorContainer.appendChild(productSelector);
    productSelectorContainer.appendChild(br);
    productSelectorContainer.appendChild(productsName);
    productSelectors.appendChild(productSelectorContainer);
};

function createProductSelectorContainer() {
    let span = document.createElement("span");
    span.classList = "product-selector-container";

    return span;
}

function createProductSelector() {
    let span = document.createElement("span");
    span.classList = "product-selector";

    return span;
}

function createProductNames(productName) {
    let span = document.createElement("span");
    span.classList = "product-name";
    span.innerText = productName;

    return span;
}

function createBreak() {
    return document.createElement("br");
}