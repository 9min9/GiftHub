function createProductSpan(productName, english, active, img) {
    let productSelectors = document.querySelector("#product-selectors");
    let productSelectorContainer = createProductSelectorContainer();
    let productSelector = createProductSelector();
    let productsName = createProductNames(productName);
    let br = createBreak();
    let englishProductName = createEnglishProductNames(english);

    if (active) {
        productSelectorContainer.classList.add("category-active");
        productSelector.classList.add("total-category")
    }

    productSelector.style.backgroundImage = "url(" + img + ")";

    productSelectorContainer.appendChild(productSelector);
    productSelectorContainer.appendChild(br);
    productSelectorContainer.appendChild(productsName);
    productSelectorContainer.appendChild(englishProductName);
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

function createEnglishProductNames(englishProductName) {
    let englishInput = document.createElement("input");
    englishInput.type = "hidden";
    englishInput.value = englishProductName;

    return englishInput

}