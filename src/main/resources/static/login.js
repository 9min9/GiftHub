window.onload = function () {

}

function login() {
    $.ajax({
        type:"post",
        url:"/api/kakao/login",
        dataType: 'json',


        success: function(jsonData){
            console.log("로그인성공")

            let nickname = jsonData.nickname;
            let email = jsonData.email;


            let restNickname = document.getElementById("rest-nickname");
            let restEmail = document.getElementById("rest-email");


            restNickname.innerText = nickname;
            restEmail.innerText = email;


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




