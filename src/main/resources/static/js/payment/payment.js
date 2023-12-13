let totalAmount = 20000;

let addKakaoPayEvent = (itemName) => {

    document.querySelector("#kakao-pay").addEventListener("click", function() {
        let xhr = new XMLHttpRequest();

        xhr.open("post", "/api/kakao/pay/ready");
        xhr.setRequestHeader("Authorization", localStorage.getItem("token"));

        xhr.onload = () => {
            if (xhr.status !== 200) {
                return;
            }
            let parsedResponse = JSON.parse(xhr.responseText);

            let redirect_url = parsedResponse.next_redirect_pc_url;

            let opened = window.open(redirect_url, "_blank", "width = 450, height = 700");
        }

        let sendDate = {
            itemName,
            totalAmount,
        };

        xhr.setRequestHeader("Content-type", "application/json");

        xhr.send(JSON.stringify(sendDate));
    });

}

// items: 기프티콘 ID 배열
let loadItem = (items) => {
    let xhr = new XMLHttpRequest();

    let url = "/api/checkout?"

    for (let i of items) {
        url += "gifticonIds=" + i + "&";
    }

    xhr.open("get", url);

    xhr.onload = () => {
        let parsedResponse = JSON.parse(xhr.responseText);

        let mainGifticon = document.querySelector("#main-gifticon");

        for (let gifticon of parsedResponse.list) {
            let created = document.createElement("div");

            created.classList.add("o-card")
            created.id = "gifticon-" + gifticon.id;
            created.innerHTML = `
                <div class="o-card__flex">
                    <div class="o-card__img-wrap">
                        <img class="u-img-fluid" src="images/product/men/product8.jpg" alt="">
                    </div>
                    <div class="o-card__info-wrap">
                        <span class="o-card__name">
                            <a href="product-detail.html">${gifticon.productName}</a>
                            <input type="hidden" name="gifticonIds" class="real-gifticonIds" value="${gifticon.id}">
                        </span>
                        <span class="o-card__quantity">Quantity x 1</span>
                        <span class="o-card__price">${gifticon.price}</span></div>
                </div>

                <a class="o-card__del far fa-trash-alt trash" id="trash-${gifticon.id}"></a>
            `;

            mainGifticon.appendChild(created);
        }

        let originalPrice = parsedResponse.originalPrice;
        let price = parsedResponse.price;
        let discountPrice = originalPrice - price;

        document.querySelector("#total-price").innerText = originalPrice + " 원";
        document.querySelector("#discount-price").innerText = discountPrice;
        document.querySelector("#final-price").innerHTML = price;

        payWithPointEvent(price, "기프티콘");

        document.querySelectorAll(".trash").forEach((element) => {
            element.addEventListener("click", function() {
                let gifticonId = element.id.replace("trash-", "");

                window.location = window.location.href.replace("gifticonIds=" + gifticonId, "");
            });
        });
    }

    xhr.send()

}

let payWithPointEvent = (point) => {
    document.querySelector("#pay-with-point").addEventListener("click", function() {
        let xhr = new XMLHttpRequest();

        xhr.open("post", "/api/points/buy");

        xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhr.setRequestHeader("Authorization", localStorage.getItem("token"));

        xhr.onload = () => {
            if (xhr.status !== 200) {
                let errors = JSON.parse(xhr.response);
                alert(errors.message);

                return;
            }

            window.location = "/";
        }

        let sendDate = "point=" + point + "&";
        for (let e of document.querySelectorAll(".real-gifticonIds")) {
            sendDate += "gifticonIds=" + e.value + "&";
        }


        xhr.send(sendDate);
    });
}