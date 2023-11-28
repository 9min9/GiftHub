let getBrandButton = (product, brand) => {
    let filter = document.createElement("div");
    filter.classList.add("filter__category-wrapper");

    let filterButton = document.createElement("button");
    filterButton.classList = "btn filter__btn filter__btn--style-1";
    filterButton.dataset.filter = "." + product
    filterButton.innerText = brand;

    filter.appendChild(filterButton);

    return filter;
}

let setBrand = (category) => {
    // if (localStorage.getItem("token")) {
        let xhr = new XMLHttpRequest();

        xhr.open("get", "/api/gifticons/" + category + "/brands");

        xhr.setRequestHeader("Authorization", localStorage.getItem("token"));

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
        }

        xhr.send()
    // }
}