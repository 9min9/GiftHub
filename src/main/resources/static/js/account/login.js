const urlParams = new URL(location.href).searchParams;

window.onload = function () {
    Kakao.init('578c006810acb509f2ccc52277d13ec9'); //발급받은 키 중 javascript키를 사용해준다.
    Kakao.isInitialized();

    if (urlParams.get("state") != null) {
        getNaverJwt();
    } else if (urlParams.get("code") != null){
        getKakaoJwt();
    }

    $("#login-btn").click(function (){
        getLocalJwt();
    })

    $("#")
}


function getLocalJwt(){
    let email = $("#login-email").val();
    let password = $("#login-password").val();

    let data= {
        email:email,
        password : password
    };

    $.ajax({
        type:"post",
        url:"/signup/login",
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",


        success: function (jsonData, status, xhr) {
            alert("로그인 성공")
            console.log(jsonData);

            let authorizationHeader = xhr.getResponseHeader("Authorization");
            let token = authorizationHeader.replace("Bearer ", "");
            localStorage.setItem("token", token);
            setCookie();

            // window.location.href = "/";
            // window.history.back();

        },
        error: function (error) {
            console.log(error)
            console.log("로그인실패")

        },
        complete: function (xhr, status) {
            console.log("서버 응답", xhr.responseText);
        }
    });
}

function getKakaoJwt() {
    const code = urlParams.get('code');                 //Kakao 로그인이 성공했을 때 얻는 인가 코드를 URL에서 추출
    let param = '?code=' + code;                       //Kakao 로그인이 성공했을 때 얻는 인가 코드를 URL에서 추출

    $.ajax({
        type: "post",
        url: "/api/kakao/login" + param,      //Kakao의 권고사항에 따라 쿼리스트링으로 전송 code를 전송

        success: function (jsonData, status, xhr) {
            alert("로그인 성공")
            console.log(jsonData);

            let authorizationHeader = xhr.getResponseHeader("Authorization");
            let token = authorizationHeader.replace("Bearer ", "");
            localStorage.setItem("token", token);
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
        url: "/api/naver/login" + param,
        success: function (jsonData, status, xhr) {
            alert("로그인 성공")
            console.log(jsonData);

            let authorizationHeader = xhr.getResponseHeader("Authorization");
            let token = authorizationHeader.replace("Bearer ", "");
            localStorage.setItem("token", token);
            setCookie();

            window.location.href = "/";
            // window.history.back();
        },
        error: function (error) {
            console.log("로그인실패")
        }
    });



}

function setCookie() {
    document.cookie = "Authorization=" + localStorage.getItem("token");
}