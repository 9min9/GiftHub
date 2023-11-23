let loadGifticon= () => {

    let page = 0;
    let size = 12;

    $.ajax({
        url: "/gifticon/list",
        type: "get",
        data: {
            page,
            size,
        },
        success: function (result) {

            let html = "";

            for (let r of result) {
                html += `
                    <div class="col-lg-3 col-md-6 u-s-m-b-30">
                        <div class="product-o product-o--radius product-o--hover-off u-h-100">
                            <div class="product-o__wrap">
                                <a class="aspect aspect--bg-grey aspect--square u-d-block" href="product-detail.html">
                                    <img class="aspect__img" src="" alt="이미지가 없어용"></a>
                                <div class="product-o__action-wrap">
                                    <ul class="product-o__action-list">
                                        <li>
                                            <a data-modal="modal" data-modal-id="#quick-look" data-tooltip="tooltip" data-placement="top" title="Quick View"><i class="fas fa-search-plus"></i></a></li>
                                        <li>

                                            <a data-modal="modal" data-modal-id="#add-to-cart" data-tooltip="tooltip" data-placement="top" title="Add to Cart" id="gifticon-${r.id}"><i class="fas fa-plus-circle"></i></a></li>
                                        <li>

                                            <a href="account/login.html" data-tooltip="tooltip" data-placement="top" title="Add to Wishlist"><i class="fas fa-heart"></i></a></li>
                                    </ul>
                                </div>
                            </div>


                            <span class="product-o__category">
                                    <a href="shop-side-version-2.html">${r.brandName}</a></span>
                            <span class="product-o__name">
                                    <a href="product-detail.html">${r.productName}</a>
                                </span>

                            <div class="product-o__rating gl-rating-style">
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <span class="product-o__review">(2)</span>
                            </div>
                            <span class="product-o__price">${r.price}<span class="product-o__discount"></span></span>
                        </div>
                    </div>
                `;
            }
        },
    });


};


let addToCartEvent = () => {
    $("[data-modal-id='#add-to-cart']").each(function() {
        $(this).on("click", function() {
            let gifticonId = $(this).id.replace("gifticon-", "");
            $.ajax({
                url: "/api/carts",
                type: "post",
                data: {
                    gifticonId,
                },
                success: function () {
                    loadMiniCart();
                }
            });
        });
    });
};