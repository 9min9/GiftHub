let loadCarts = () => {
    let xhr = new XMLHttpRequest();

    xhr.open("get", "/api/carts");
    xhr.setRequestHeader("Authorization", localStorage.getItem("token"));

    xhr.onload = function() {
        if (xhr.status !== 200) {
            alert(xhr.responseText);

            return;
        }

        let carts = JSON.parse(xhr.responseText);

        for (let c of carts) {
            let html = "";

            let element = document.createElement("tr");
            element.classList = "m-cart__get";
            element.id = "tr-" + c.id;

            html +=`
                <!--====== Row ======-->
                    <td>
                        <div class="table-p__box">
                            <div class="table-p__img-wrap">

                                <img class="u-img-fluid" src="images/product/electronic/product3.jpg" alt=""></div>
                            <div class="table-p__info">

                                        <span class="table-p__name">
                                            <a href="product-detail.html">${c.gifticonDto.productName}</a>
                                            <input type="hidden" name="gifticonIds" value="${c.gifticonDto.id}">
                                        </span>
                                            
                            </div>
                        </div>
                    </td>
                    <td>

                        <span class="table-p__price">${c.gifticonDto.price}</span></td>
                    <td>
                        <div class="table-p__input-counter-wrap">

                            <!--====== Input Counter ======-->
                            <div class="input-counter">

                                <span class="input-counter__minus fas fa-minus"></span>

                                <input class="input-counter__text input-counter--text-primary-style" type="text" value="1" data-min="1" data-max="1000">

                                <span class="input-counter__plus fas fa-plus"></span></div>
                            <!--====== End - Input Counter ======-->
                        </div>
                    </td>
                    <td>
                        <div class="table-p__del-wrap">

                            <a class="far fa-trash-alt table-p__delete-link" id="delete-${c.id}"></a></div>
                    </td>
                <!--====== End - Row ======-->
            `;

            element.innerHTML = html;

            document.querySelector("#cart-list").append(element);
        }

        countUpEvent();
        countDownEvent();
        inputEvent();
        deleteEvent();
        removeAllEvent();

        getTotal();

    }

    xhr.send();

}

let countUpEvent = () => {
    $(".input-counter__plus").each(function() {
        $(this).on('click', function() {

            let $input = $(this).parent().find('input');

            let count = parseInt($input.val()) + 1; // Number + Number
            $input.val(count).change();

            getTotal();
        });
    });
}

let countDownEvent = () => {
    $(".input-counter__minus").each(function() {
        $(this).on('click',function () {
            let $input = $(this).parent().find('input');

            let num = parseInt($input.val());
            if (num > 0) {
                let count = num - 1; // Number - Number
                $input.val(count).change();
            }

            getTotal()
        });
    });
}

let inputEvent = () => {
    $(".input-counter__text").each(function() {
        $(this).on('keyup', function() {

            let $input = $(this);

            let count = parseInt($input.val());
            $input.val(count).change();

            getTotal();
        });
    });
}

let getTotal = () => {
    let sum = 0;

    $(".m-cart__get").each(function () {
        let price = parseInt($(this).find(".table-p__price").text());
        let quantity = parseInt($(this).find(".input-counter__text").val());

        sum += price * quantity;
    });

    setPrices(sum);

}

let getTax = (price = 0) => {
    return 0;
}

let setPrices = (sum) => {
    $("#sub-total").text(sum);
    $("#tax").text(getTax(sum));
    $("#grand-total").text(sum - getTax());
}

let deleteEvent = function (){
    $(".table-p__delete-link").each(function() {
        $(this).on("click", function(event) {
            event.preventDefault();

            let id = $(this).attr("id").replace("delete-", "");

            $.ajax({
                url: "/api/carts/delete" + id,
                headers: {
                    Authorization: localStorage.getItem("token"),
                },
                type: "post",
                success: function () {
                    $("#tr-" + id).remove();

                    getTotal();
                },
                error: function () {
                    alert("삭제 실패! 다시 시도해주세요.")
                }
            });
        });
    });

}

let removeAllEvent = () => {
    $(".route-box__link").each(function() {
        $(this).on("click", function(event) {
            event.preventDefault();

            $.ajax({
                url: "/api/carts/delete",
                headers: {
                    Authorization: localStorage.getItem("token"),
                },
                type: "post",
                success: function () {
                    $("tr.m-cart__get").remove();
                },
            })
        })
    });
}

let updateCartEvent = () => {
    $("tr.m-cart__get").remove();

    loadCarts();
}