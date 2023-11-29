
window.onload = function () {
    getStorageList();

    // $(document).on('.w-r__link btn--e-brand-b-2','click', function(){
    //     alert("클릭됨");
    // });

}



function getStorageList() {
    //todo : page를 urlParam -1 로 변경t
    let page = 0;
    let size = 10;

    $.ajax({
        url: "/api/gifticon/storage/list",
        type: "post",
        headers: {
            Authorization: localStorage.getItem("token"),
        },
        data: {page, size},

        success: function(jsonData) {
            for (let i = 0; i < jsonData.content.length; i++) {
                renderGifticon(jsonData.content[i]);
            }

        },
        error: function (error) {
            console.log("error");
        }
    });

}
