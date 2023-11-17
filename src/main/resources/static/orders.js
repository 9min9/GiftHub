let loadOrders = (page, size) => {
    page = page || 0;
    size = size || 10;


    let xhr = new XMLHttpRequest();

    xhr.open("get", "/api/payments?page=" + page + "&size=" + size);

    xhr.onload = function() {
        if (xhr.status !== 200) {
            alert("불러오기 에러");
        }

        let orders = JSON.parse(xhr.responseText);

        for (let o of orders) {
            let html = "";

            let element = document.createElement("div");
            element.classList = "m-order__get";

            html +=`
                <div class="manage-o__header u-s-m-b-30">
                    <div class="dash-l-r">
                        <div>
                            <div class="manage-o__text-2 u-c-secondary">Order
                                #${o.id}
                            </div>
                            <div class="manage-o__text u-c-silver">Placed on ${o.createDate}
                            </div>
                        </div>
                        <div>
                            <div class="dash__link dash__link--brand">
        
                                <a href="dash-manage-order.html">Details</a></div>
                        </div>
                    </div>
                </div>
                <div class="manage-o__description">
                    <div class="description__container">
                        <div class="description__img-wrap">
        
                            <img class="u-img-fluid"
                                 src="images/product/electronic/product3.jpg" alt="">
                        </div>
                        <div class="description-title">${o}</div>
                    </div>
                    <div class="description__info-wrap">
                        <div>
        
                            <span class="manage-o__badge badge--processing">${o.saleStatus}</span>
                        </div>
                        <div>
        
                                <span class="manage-o__text-2 u-c-silver">Quantity:
        
                                    <span class="manage-o__text-2 u-c-secondary">1</span></span>
                        </div>
                        <div>
        
                                <span class="manage-o__text-2 u-c-silver">Total:
        
                                    <span class="manage-o__text-2 u-c-secondary">${o.price}</span></span>
                        </div>
                    </div>
                </div>
            `;

            element.innerHTML = html;

            document.querySelector("#order-list").append(element);
        }

    }

    xhr.send();

}