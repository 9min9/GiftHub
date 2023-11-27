const TOKEN_KEY = 'token';

// 페이지 로드 이벤트에 함수 추가
document.addEventListener('DOMContentLoaded', onPageLoad);

// 페이지 로드 시 실행되는 함수
function onPageLoad() {
    // 저장된 토큰 가져오기
    const token = localStorage.getItem(TOKEN_KEY);

    // 토큰이 있을 경우에는 로그인 정보 업데이트
    if (token) {
        updateNavbarOnLogin();
    } else {
        updateNavbarOnLogout();
    }
}

// 로그인 후 Navbar 업데이트 함수
function updateNavbarOnLogin() {
    // const loginInfoElement = document.getElementById('login-info');
    // const logoutButton = document.createElement('button');

    // 토큰에서 사용자 정보를 추출하여 활용
    // const userInfo = decodeToken(token); // 토큰에서 정보 추출하는 함수 필요
    // document.getElementById("myElement").style.visibility = "visible";
    // 숨기기
    // document.getElementById("myElement").style.visibility = "hidden";

    // document.getElementById("nav-point-li").style.visibility = "visible";
    // document.getElementById("nav-mypage-li").style.visibility = "visible";
    // document.getElementById("nav-logout-li").style.visibility = "visible";
    // document.getElementById("nav-signup-li").style.visibility = "hidden";
    // document.getElementById("nav-login-li").style.visibility = "hidden";


    document.getElementById("nav-attendance-li").style.display = "inline-block";
    document.getElementById("nav-point-li").style.display = "inline-block";
    document.getElementById("nav-mypage-li").style.display = "inline-block";
    document.getElementById("nav-logout-li").style.display = "inline-block";
    document.getElementById("nav-signup-li").style.display = "none";
    document.getElementById("nav-login-li").style.display = "none";


    // const navbar = document.getElementById('nav-list');
    // navbar.appendChild(navPoint());
}

function updateNavbarOnLogout() {
    document.getElementById("nav-attendance-li").style.display = "none";
    document.getElementById("nav-point-li").style.display = "none";
    document.getElementById("nav-mypage-li").style.display = "none";
    document.getElementById("nav-logout-li").style.display = "none";
    document.getElementById("nav-signup-li").style.display = "inline-block";
    document.getElementById("nav-login-li").style.display = "inline-block";
}


function navPoint() {
    let li = document.createElement("li");
    let pointAnchor = document.createElement("a");
    let pointIcon = document.createElement("svg");
    let path1 = document.createElement("path");
    let path2 = document.createElement("path");
    let span = document.createElement("span");

    pointAnchor.setAttribute("href", "/pay/kakao");
    pointIcon.setAttribute("xmlns", "http://www.w3.org/2000/svg");
    pointIcon.setAttribute("width", "16");
    pointIcon.setAttribute("height", "16");
    pointIcon.setAttribute("fill", "currentColor");
    pointIcon.setAttribute("className", "bi bi-wallet-fill");
    pointIcon.setAttribute("viewBox", "0 0 16 16");
    path1.setAttribute("d", "M1.5 2A1.5 1.5 0 0 0 0 3.5v2h6a.5.5 0 0 1 .5.5c0 .253.08.644.306.958.207.288.557.542 1.194.542.637 0 .987-.254 1.194-.542.226-.314.306-.705.306-.958a.5.5 0 0 1 .5-.5h6v-2A1.5 1.5 0 0 0 14.5 2h-13z");
    path1.setAttribute("d", "M16 6.5h-5.551a2.678 2.678 0 0 1-.443 1.042C9.613 8.088 8.963 8.5 8 8.5c-.963 0-1.613-.412-2.006-.958A2.679 2.679 0 0 1 5.551 6.5H0v6A1.5 1.5 0 0 0 1.5 14h13a1.5 1.5 0 0 0 1.5-1.5v-6z")
    span.innerText = "포인트 충전";

    pointIcon.appendChild(path1);
    pointIcon.appendChild(path2);
    pointAnchor.appendChild(pointIcon);
    pointAnchor.appendChild(span);
    li.appendChild(pointAnchor);

    return li;
}

function navMyinfo() {

}

function NavSignup() {

}

function navLogin() {

}

function navLogout() {
    //todo : 소셜 로그인에 따라 해당 소셜 로그아웃도 같이 진행되어야 함
    localStorage.removeItem(TOKEN_KEY);

    // Navbar 초기화
    const loginInfoElement = document.getElementById('login-info');
    loginInfoElement.textContent = '로그인 전';

    // 로그아웃 버튼 제거
    const logoutButton = document.querySelector('button');
    if (logoutButton) {
        logoutButton.remove();
    }

}


