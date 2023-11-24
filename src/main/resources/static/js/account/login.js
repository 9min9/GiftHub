const urlParams = new URL(location.href).searchParams;

window.onload = function () {

    getKakaoJwt()
    getNaverJwt()

    Kakao.init('578c006810acb509f2ccc52277d13ec9'); //발급받은 키 중 javascript키를 사용해준다.
    Kakao.isInitialized()

    //TODO : getJwt가 카카오 실행 후 리다이렉트 시 호출되어야 함
}

function loginWithKakao() {
    Kakao.Auth.authorize({
        redirectUri: 'http://localhost:8081/login',
    });
}



function getKakaoJwt() {
    const code = urlParams.get('code');                 //Kakao 로그인이 성공했을 때 얻는 인가 코드를 URL에서 추출
    let param = '?code=' + code;                       //Kakao 로그인이 성공했을 때 얻는 인가 코드를 URL에서 추출

    $.ajax({
        type: "post",
        url: "http://localhost:8081/api/kakao/login" + param,      //Kakao의 권고사항에 따라 쿼리스트링으로 전송 code를 전송

        success: function (jsonData, status, xhr) {
            alert("로그인 성공")
            console.log(jsonData);

            let authorizationHeader = xhr.getResponseHeader("Authorization");
            let token = authorizationHeader.replace("Bearer ", "");
            localStorage.setItem("token", token);

            let setCookie = () => {
                document.cookie = "Authorization=" + localStorage.getItem("token");
            }

            setCookie();

            window.location.href = "/";
            // window.history.back();

        },
        error: function (error) {
            console.log("로그인실패")
        }
    });
}


    function getNaverJwt() {
        const code = urlParams.get('code');
        let param = '?code=' + code;
        $.ajax({
            type: "post",
            url: "http://localhost:8081/api/naver/login" +param,
            success: function(jsonData, status, xhr){
                alert("로그인 성공")
                console.log(jsonData);

                let authorizationHeader = xhr.getResponseHeader("Authorization");
                let token = authorizationHeader.replace("Bearer ", "");
                localStorage.setItem("token", token);

                window.location.href = "/";
                // window.history.back();
            },
            error:function(error) {
                console.log("로그인실패")
            }
        });

    }

