function addChannel() {
    Kakao.Channel.addChannel({
        // channelPublicId: '_yEhsG',
        channelPublicId: '_ATxoKG',     //Test channel

    });
}

Kakao.Channel.createAddChannelButton({
    container: '#kakao-add-channel-button',
    // channelPublicId: '_yEhsG'
    channelPublicId: '_ATxoKG'
});



function openChat() {
    Kakao.Channel.chat({
        // channelPublicId: '_yEhsG'
        channelPublicId: '_ATxoKG'
    });
}



function responseImage() {

    //todo : 챗봇에서 받은 이미지를 ~~~~ controller에 넘겨줌
}


