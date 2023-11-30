function getOnSaleProduct(productId) {
    let xhr = new XMLHttpRequest();

    xhr.open("get", "/api/gifticon/products/" + productId)
    xhr.setRequestHeader("Authorization", localStorage.getItem("token"));

    xhr.onload = () => {
        let parsed = JSON.parse(xhr.responseText);

        let priceContainer = document.querySelector("#price-container");

        for (let p of parsed) {
            let priceUnit = document.createElement("tr");

            let priceTd = document.createElement("td");
            let due = document.createElement("td");

            priceTd.innerText = p.price;
            due.innerText = p.due;

            priceUnit.appendChild(priceTd);
            priceUnit.appendChild(due);

            priceContainer.appendChild(priceUnit);
        }
    }

    xhr.send();
}