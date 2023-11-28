window.onload = function () {
    document.getElementById("authorize-phone").style.display = "none";

    $("#signup-btn").click(function (e) {
        e.preventDefault();
        signup();
    });


    $('#email').on('change', function () {
        emailCheck();
    });

    $('#password').on('change', function () {
        passwordCheck();
    });

    $('#confirm-password').on('change', function () {
        cinfirmPasswordCheck();
    });
}

function emailCheck() {
    let data = {
        email: $("#email").val()
    };

    $.ajax({
        type: "post",
        url: "/signup/email/check",
        data: JSON.stringify(data),
        // data: data,
        contentType: "application/json; charset=utf-8",
        dataType: "json",

        success: function (jsonData) {
            console.log(jsonData);
            checkResult(jsonData);
        },
        error: function (error) {
            console.log(error)
            checkResult(error.responseJSON);
        }
    });
}


function checkResult(result) {
    let label = document.getElementById("result-" + result.target + "-label");
    if (result.status == "success") {
        label.setAttribute('style', 'color: green');
        label.innerText = result.message;
    }

    if (result.status == "error") {
        label.setAttribute('style', 'color: red;');
        label.innerText = result.message;
    }

    label.innerText = result.message;
}


function signup() {
    let year = $("#birth-date").val().substring(0, 4);
    let birthdate = $("#birth-date").val().substring(4);

    let data = {
        email: $("#email").val(),
        password: $("#password").val(),
        name: $("#name").val(),
        nickname: $("#nickname").val(),
        tel: $("#tel").val(),
        gender: $("#gender").val(),
        year: year,
        date: birthdate
    }


    $.ajax({
        type: "post",
        url: "http://localhost:8081/signup/submit",
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",

        success: function (jsonData) {
            console.log(jsonData);
        },

        error: function (error) {
            console.log(error)
        }
    });
}

