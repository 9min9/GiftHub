const urlParams = new URL(location.href).searchParams;





const naverLogin = new naver.LoginWithNaverId(
    {
        clientId: "iw7Eqm8dtb9NpQ22vmxe",
        callbackUrl: "http://localhost:8081/api/naver/login",
        loginButton: {color: "green", type: 2, height: 40}
    }
);
naverLogin.init(); // 로그인 설정

function getCode() {
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