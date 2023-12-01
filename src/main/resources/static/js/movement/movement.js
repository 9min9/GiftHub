let page = 0;

function getMovementByUserIdContain(page = 0, size = 10) {
    let xhr = new XMLHttpRequest();

    xhr.open("get", "/api/movements?page=" + page + "&size=" + size);
    xhr.setRequestHeader("Authorization", localStorage.getItem("token"));

    xhr.onload = () => {
        let parsed = JSON.parse(xhr.responseText);

        print(parsed);
    }

    xhr.send();
}

function print(data) {
    for (let m of data.content) {
        let html = "";

        let element = document.createElement("div");
        element.classList = "m-order__get";

        html +=`
                <div class="manage-o__header u-s-m-b-30">
                    <div class="dash-l-r">
                        <div>
                            <div class="manage-o__text-2 u-c-secondary">
                                Exchange # ${m.id}
                            </div>
                        </div>
                    </div>
                </div>
                <div class="manage-o__description">
                    <div class="description__container">
                        <div class="description__img-wrap">
        
                            <img class="u-img-fluid"
                                 src="images/product/electronic/product3.jpg" alt="">
                        </div>
                        <div class="description-title">${m.gifticon.price} Point <br> ${m.gifticon.productName}</div>
                    </div>
                    <div class="description__info-wrap">
                        <div>
        
                            <span class="manage-o__badge badge--processing">${m.movementStatus}</span>
                        </div>
                        <div>
        
                                <span class="manage-o__text-2 u-c-silver">판매:
        
                                    <span class="manage-o__text-2 u-c-secondary">${m.fromUser.nickname}</span></span>
                        </div>
                        <div>
        
                                <span class="manage-o__text-2 u-c-silver">구매: 
        
                                    <span class="manage-o__text-2 u-c-secondary">${m.toUser.nickname}</span></span>
                        </div>
                    </div>
                </div>
            `;

        element.innerHTML = html;

        document.querySelector("#order-list").append(element);
    }
}

function loadMovement() {
    print(getMovementByUserIdContain(page));
}