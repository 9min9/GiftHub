const urlParams = new URL(location.href).searchParams;

window.onload = function () {
    Kakao.init('578c006810acb509f2ccc52277d13ec9'); //발급받은 키 중 javascript키를 사용해준다.
    // console.log(Kakao.isInitialized()); // sdk초기화여부판단

    //TODO : getJwt가 카카오 실행 후 리다이렉트 시 호출되어야 함
    getJwt();
}

function loginWithKakao() {
    Kakao.Auth.authorize({
        redirectUri: 'http://localhost:8081/login',
    });
}

function getJwt() {
    const code = urlParams.get('code');                 //Kakao 로그인이 성공했을 때 얻는 인가 코드를 URL에서 추출
    let param  = '?code=' + code;                       //Kakao 로그인이 성공했을 때 얻는 인가 코드를 URL에서 추출

    $.ajax({
        type:"post",
        url:"http://localhost:8081/api/kakao/login"+param,      //Kakao의 권고사항에 따라 쿼리스트링으로 전송 code를 전송

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

function logout() {
    $.ajax({
        type:"GET",
        url:"/api/kakao/logout",
        dataType: 'json',

        success: function(jsonData){
            console.log("로그아웃 성공")
        },
        error:function(error) {
            console.log("로그아웃 실패")

        }
    });
}




