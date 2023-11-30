function addChannel() {
    Kakao.Channel.addChannel({
        // channelPublicId: '_yEhsG',
        channelPublicId: '_ATxoKG',     //Test channel

    });
}

// Kakao.Channel.createAddChannelButton({
//     container: '#kakao-add-channel-button',
//     channelPublicId: '_yEhsG'
//     // channelPublicId: '_ATxoKG'
// });


function openChat() {
    contextInit();

    Kakao.Channel.chat({
        // channelPublicId: '_yEhsG'
        channelPublicId: '_ATxoKG'
    });
}


function contextInit() {
    $.ajax({
        url: "/api/jwt/context/init",
        type: "post",
        // contentType: "application/json; charset=utf-8",
        dataType: "json",

        headers: {
            Authorization: localStorage.getItem("token"),
        },

        success: function (result) {
            console.log(result);
        }, error: function (error) {

        }
    });
}


