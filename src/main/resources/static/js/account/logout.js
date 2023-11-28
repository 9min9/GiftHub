    Kakao.init('578c006810acb509f2ccc52277d13ec9');
    Kakao.isInitialized();

    //admin key
    function kakaoLogout() {
        $.ajax({
            type:"post",
            url:"/api/kakao/logout",

            success: function(jsonData, status, xhr){
                alert("로그아웃 성공")
                console.log(jsonData);
                localStorage.removeItem("token");

                document.cookie = "Authorization=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

                window.location.href = "/";
            },

            error:function(error) {
                console.log("로그아웃 실패")
            }
        });
    }

//     function kakaoLogout() {
//         let accessToken = Kakao.Auth.getAccessToken();
//         console.log(accessToken);
//
//
//     Kakao.Auth.logout()
//         .then(function() {
//             let accessToken = Kakao.Auth.getAccessToken();
//             console.log(accessToken);
//
//
//
//             alert('logout ok\naccess token -> ' + Kakao.Auth.getAccessToken());
//             localStorage.removeItem("token");
//         })
//         .catch(function() {
//             alert('Not logged in');
//         });
// }
