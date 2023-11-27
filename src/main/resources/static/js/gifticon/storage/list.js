

getStorageList();



function getStorageList() {
    //todo : page를 urlParam -1 로 변경
    let page = 0;
    let size = 6;

    $.ajax({
        url: "/api/gifticon/storage/list",
        type: "post",
        headers: {
            Authorization: localStorage.getItem("token"),
        },

        data: {page, size},

        success: function(jsonData) {
            for (let i = 0; i < jsonData.size; i++) {
                console.log(jsonData);
                renderGifticon(jsonData[i]);
            }

        },
        error: function (error) {
            console.log("error");
        }
    });

}



