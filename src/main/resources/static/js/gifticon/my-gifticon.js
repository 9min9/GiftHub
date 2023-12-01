let getMyGifticons = (page = 0) => {
    page = page || 0;

    let xhr = new XMLHttpRequest();

    xhr.open("get", "/api/gifticons?page=" + page + "&size=5");
    xhr.setRequestHeader("Authorization", localStorage.getItem("token"));


    xhr.onload = () => {
        if (xhr.status !== 200) {
            return;
        }

        let parsed = JSON.parse(xhr.responseText);

        print(parsed);

        let elem = document.querySelector(".gifticon-div:last-child");

        let observer = scrollEventObserver(elem);

        if (parsed.content.length) {
            observer.observe(elem);
        }

        setEvent();
    }

    xhr.send();
}

let print = (gifticons) => {
    let list = document.querySelector("#gifticon-list-div");

    let html = "";
    for (let gifticon of gifticons.content) {
        html += `
                  <div class="w-r u-s-m-b-30 gifticon-div">
                    <div class="w-r__container">
                      <div class="w-r__wrap-1">
                        <div class="w-r__img-wrap">
                          <img
                            class="u-img-fluid"
                            src="images/product/electronic/product3.jpg"
                            alt=""
                          />
                        </div>
                        <div class="w-r__info">
                          <span class="w-r__name">
                            <a href="product-detail.html"
                              >${gifticon.productName}</a
                            ></span
                          >

                          <span class="w-r__category">
                            <a href="shop-side-version-2.html"
                              >${gifticon.brandName}</a
                            ></span
                          >

                          <span class="w-r__price"
                            >${gifticon.price}

                            <span class="w-r__discount"></span></span
                          >
                        </div>
                      </div>`;

        if (gifticon.gifticonStatus === "ONSALE") {
            html += `
                <div class="w-r__wrap-2 gifticon-button-area" id="gifticon-button-area-${gifticon.id}">    
                    판매중
                </div>
            `;
        } else {
            html +=
                    `<div class="w-r__wrap-2 gifticon-button-area" id="gifticon-button-area-${gifticon.id}">
                            <button
                              class="w-r__link btn--e-transparent-platinum-b-2 gifticon-remove-button gifticon-buttons"
                              data-gifticon-id="${gifticon.id}"
                              data-modal="modal"
                              data-modal-id="#add-to-cart"
                              >제거</button
                            >
    
                            <button
                              class="w-r__link btn--e-transparent-platinum-b-2 for-sale-btn gifticon-buttons"
                              href="product-detail.html"
                              data-gifticon-id="${gifticon.id}"
                              >판매</button
                            >
    
                            <a
                              class="w-r__link btn--e-brand-b-2 for-use-btn gifticon-buttons"
                              href="/api/barcode/${gifticon.barcode}"
                              target="_blank"
                              >사용</a
                            >
                          </div>
            `;

        }

        html += `
                        </div>
                      </div>
            `;
    }

    list.innerHTML += html;
}

let pagination = (pageableJson) => {
    $("#pagination-place").twbsPagination({
        totalPages: pageableJson.totalPages,
        visiblePages: 4,
        onPageClick: function (event, page) {
            $(".gifticon-div").each(function () {
                $(this).remove();
            });

            window.scrollTo({top: 0, left: 0, behavior: "smooth"});

            getMyGifticons(page - 1);

            setEvent();
        },
    });
};

function deleteGifticon(gifticonId) {
    let xhr = new XMLHttpRequest();

    xhr.open("post", "/api/gifticon/delete/" + gifticonId);
    xhr.setRequestHeader("Authorization", localStorage.getItem("token"));

    xhr.onload = () => {
        document.querySelector(".gifticon-remove-button[data-gifticon-id='" + gifticonId + "']")
            .parentElement.parentElement.remove();

        let elem = document.querySelector(".gifticon-div:last-child");

        let observer = scrollEventObserver(elem);

        observer.observe(elem);
    }

    xhr.send();
}

function removeAllGifticonElement() {
    document.querySelectorAll(".gifticon-div").forEach(element => {
        element.remove();
    });

}

function setEvent() {
    document.querySelectorAll(".gifticon-remove-button").forEach(element => {
        element.addEventListener("click", function(event) {
            let gifticonId = event.target.dataset.gifticonId;

            deleteGifticon(gifticonId);
        });
    });

    forSaleEvent();
}

let page = 0;
function scrollEventObserver(element) {
    const observer = new IntersectionObserver(entries => {
        entries.forEach(entry => {
                if (entry.intersectionRatio > 0) {
                    observer.unobserve(element);

                    page++;
                    if (page === 0) {
                        return;
                    }

                    getMyGifticons(page);

                    getMyGifticons.onload = () => {
                        io.observe(document.querySelector(".gifticon-div:last-child"));
                    }
                }
            }
        )

        return observer;
    });

    return observer;
}

function forSaleEvent() {
    document.querySelectorAll(".for-sale-btn").forEach(element => {
        element.addEventListener("click", function(event) {
            let gifticonId = event.target.dataset.gifticonId;

            setForSale(gifticonId);
        });
    });
}

function setForSale(gifticonId) {
    let xhr = new XMLHttpRequest();
    xhr.open("post", "/api/gifticon/forSale/" + gifticonId);
    xhr.setRequestHeader("Authorization", localStorage.getItem("token"));

    xhr.onload = () => {
        document.querySelectorAll("#gifticon-button-area-" + gifticonId + " gifticon-buttons").forEach(element => {
            element.remove();
        });

        document.querySelector("#gifticon-button-area-" + gifticonId).innerText = "판매중";
    }

    xhr.send();
}