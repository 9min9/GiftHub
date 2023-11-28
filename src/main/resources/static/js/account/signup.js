window.onload =function (){

    document.getElementById("authorize-phone").style.display="none";
}

  $(document).ready(function(){
      $("#signup-btn").click(function (e){
          e.preventDefault();
          signup();
      });
  })
function emailcheck(){
    let data = {email: $("#email").val}
    $.ajax({
        type:"post",
        url:"http://localhost:8081/local/emailcheck",
        data:data

    })
}






function signup() {
    let year= $("#birth-date").val().substring(0,4);
    let birthdate= $("#birth-date").val().substring(4);

        let data= {
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
        type:"post",
        url:"http://localhost:8081/local/signup",
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).done(function(resp){


    }).fail(function(error){
        console.log("signup\n" + JSON.stringify(error));
    });




}

