let loadMiniCart = () => {
    $(".card-mini-product").remove();

    if (localStorage.getItem("token")) {
        $.ajax({
            url: "/api/carts",
            headers: {
                Authorization: localStorage.getItem("token"),
            },
            type: "get",
            success: function (response) {
                $(".total-item-round").each(function () {
                    $(this).text(response.length);
                });

                let sum = 0;

                for (let r of response) {
                    let miniCart = document.createElement("div");

                    miniCart.classList = "card-mini-product";
                    miniCart.id = `div-mini-cart-${r.id}`;

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
                    <a class="mini-product__delete-link far fa-trash-alt mini-delete" id="mini-delete-${r.id}"></a>`;

                    miniCart.innerHTML = html;

                    $("#mini-cart-list").append(miniCart);
                }

                $("#subtotal-value").text(sum);

                miniDeleteEvent();

            }
        });
    }
}

let miniDeleteEvent = function (){
    $(".mini-delete").each(function() {
        $(this).on("click", function(event) {
            event.preventDefault();

            let id = $(this).attr("id").replace("mini-delete-", "");

            if (localStorage.getItem("token")) {
                $.ajax({
                    url: "/api/carts/delete" + id,
                    type: "post",
                    headers: {
                        Authorization: localStorage.getItem("token"),
                    },
                    success: function () {
                        $("#div-mini-cart-" + id).remove();

                        $(".total-item-round").each(function () {
                            $(this).text(parseInt($(this).text()) - 1);
                        });
                    },
                    error: function () {
                        alert("삭제 실패! 다시 시도해주세요.")
                    }
                });
            }
        });
    });

}

loadMiniCart();