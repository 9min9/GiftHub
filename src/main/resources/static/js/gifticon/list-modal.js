function getOnSaleProduct(productId) {
    document.querySelector("#price-container").innerHTML = "";

    let xhr = new XMLHttpRequest();

    xhr.open("get", "/api/gifticon/products/" + productId)
    xhr.setRequestHeader("Authorization", localStorage.getItem("token"));

    xhr.onload = () => {
        let parsed = JSON.parse(xhr.responseText);

        let priceContainer = document.querySelector("#price-container");

        for (let p of parsed.content) {
            let priceUnit = document.createElement("tr");

            let priceTd = document.createElement("td");
            let due = document.createElement("td");

            let addToCartTd = document.createElement("td");
            let addToCart = document.createElement("button");
            addToCart.classList.add("btn--e-brand-b-2");
            addToCart.dataset.gifticonId = p.id;

            addToCart.innerText = "장바구니로";

            addToCart.addEventListener("click", function(event) {
                let xhr = new XMLHttpRequest();

                xhr.open("post", "/api/carts");
                xhr.setRequestHeader("Authorization", localStorage.getItem("token"));
                xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

                xhr.onload = () => {
                    // TODO 장바구니 추가
                }

                xhr.send("gifticonId=" + event.target.dataset.gifticonId);
            });

            addToCartTd.appendChild(addToCart);

            priceTd.innerText = p.price;
            due.innerText = p.due;
            
            priceUnit.appendChild(priceTd);
            priceUnit.appendChild(due);
            priceUnit.appendChild(addToCartTd);

            priceContainer.appendChild(priceUnit);
        }
    }

    xhr.send();
}