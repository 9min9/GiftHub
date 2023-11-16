let addKakaoPayEvent = (itemName, totalAmount) => {

    document.querySelector("#kakao-pay").addEventListener("click", function() {
        let xhr = new XMLHttpRequest();

        xhr.open("post", "/api/kakao/pay/ready");

        xhr.onload = () => {
            let parsedResponse = JSON.parse(xhr.responseText);

            let redirect_url = parsedResponse.next_redirect_pc_url;

            window.open(redirect_url, "_blank", "width = 450, height = 700");
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

        let totalPrice = 0;
        for (let gifticon of parsedResponse) {
            totalPrice += gifticon.price;

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
                        </span>
                        <span class="o-card__quantity">Quantity x 1</span>
                        <span class="o-card__price">${gifticon.price}</span></div>
                </div>

                <a class="o-card__del far fa-trash-alt trash" id="trash-${gifticon.id}"></a>
            `;

            mainGifticon.appendChild(created);
        }

        document.querySelector("#total-price").innerText = totalPrice + " 원";

        document.querySelectorAll(".trash").forEach((element) => {
            element.addEventListener("click", function() {
                let gifticonId = element.id.replace("trash-", "");

                window.location = window.location.href.replace("gifticonIds=" + gifticonId, "");
            });
        });
    }

    xhr.send()

}