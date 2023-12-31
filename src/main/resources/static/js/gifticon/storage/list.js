
window.onload = function () {
    getStorageList();
}

function getStorageList() {
    //todo : page를 urlParam -1 로 변경
    let page = 0;
    let size = 10;

    $.ajax({
        url: "/api/storage/list",
        type: "post",
        headers: {
            Authorization: localStorage.getItem("token"),
        },
        data: {page, size},

        success: function(jsonData) {
            console.log(jsonData)

            for (let i = 0; i < jsonData.content.length; i++) {
                renderGifticon(jsonData.content[i]);
            }

        },
        error: function (error) {
            console.log(error);
        }
    });

}
