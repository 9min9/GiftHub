let loadMiniCart = () => {
    $.ajax({
        url: "/api/carts",
        type: "get",
        success: function(response) {
            $(".total-item-round").each(function() {
                $(this).text(response.length);
            });

            let sum = 0;

            for (let r of response) {
                let miniCart = document.createElement("div");

                miniCart.classList = "card-mini-product";

                sum += parseInt(r.gifticonDto.price);
                let html = `
                    <div class="mini-product">
                        <div class="mini-product__image-wrapper">
                            <a class="mini-product__link" href="product-detail.html">
                                <img class="u-img-fluid" src="images/product/electronic/product3.jpg" alt=""></a></div>
                        <div class="mini-product__info-wrapper">
                            <span class="mini-product__name">
                                <a href="product-detail.html">${r.gifticonDto.productName}</a></span>
                                <input type="hidden" name="gifticonIds" value="${r.gifticonDto.id}">
                            <span class="mini-product__quantity">1</span>
                            <span class="mini-product__price">${r.gifticonDto.price}</span></div>
                    </div>
                    <a class="mini-product__delete-link far fa-trash-alt"></a>`;

                miniCart.innerHTML = html;

                $("#mini-cart-list").append(miniCart);
            }

            $("#subtotal-value").text(sum);

        }
    });
}

loadMiniCart();