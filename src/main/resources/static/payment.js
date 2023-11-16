let addKakaoPayEvent = (itemName, totalAmount) => {

    document.querySelector("#kakao-pay").addEventListener("click", function() {
        let xhr = new XMLHttpRequest();

        xhr.open("post", "/api/kakao/pay/ready");

        xhr.onload = () => {
            let parsedResponse = JSON.parse(xhr.responseText);

            let redirect_url = parsedResponse.next_redirect_pc_url;

            window.open(redirect_url, "_blank", "width = 450, height = 700");
        }

        let sendDate = {
            itemName,
            totalAmount,
        };

        xhr.setRequestHeader("Content-type", "application/json");

        xhr.send(JSON.stringify(sendDate));
    });

}