
$.ajax({
    type: "post",
    url: "/api/users/myinfo",
    headers: {
        Authorization: localStorage.getItem("token"),
    },
    // contentType: "application/json; charset=utf-8",
    dataType: "json",

    success: function (jsonData) {
        setInfo(jsonData);

        console.log(jsonData);
    },
    error: function (error) {
        console.log(error)
    }
});


function setInfo(jsonData) {
    document.getElementById('name').innerText = jsonData.name;
    document.getElementById('email').innerText = jsonData.email;
    document.getElementById('nickname').innerText = jsonData.nickname;
    document.getElementById('tel').innerText = formatTel(jsonData.tel)
    document.getElementById('birthDay').innerText = formatBirthDate(jsonData.year, jsonData.date);
    document.getElementById('gender').innerText = formatGender(jsonData.gender);
    document.getElementById('point').innerText = jsonData.point;
    document.getElementById('login-type').innerText = jsonData.loginType;
    document.getElementById('user-type').innerText = jsonData.userType;


}

function formatBirthDate(year, date) {
    if(date.length === 4) {
        date = date.slice(0, 2) + '-' + date.slice(2, 4);
    }
    return year + '-' + date;
}

function formatTel(tel) {
    if(tel.startsWith('+82')) {
        tel = '010' + tel.slice(6);
    }
    if(tel.length === 11) {
        tel = tel.slice(0, 3) + '-' + tel.slice(3, 7) + '-' + tel.slice(7, 11);
    }
    return tel;
}

function formatGender(gender) {
    var lowerCaseGender = gender.toLowerCase();
    if(lowerCaseGender === 'male' || lowerCaseGender === 'm') {
        return '남자';
    } else if(lowerCaseGender === 'female' || lowerCaseGender === 'f' || lowerCaseGender === 'femal') {
        return '여자';
    } else {
        return '입력값 오류';
    }
}