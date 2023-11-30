

function openFile(imageFile){

    console.log(imageFile.files[0]);

    const formData = new FormData();
    formData.append("imageFile", imageFile.files[0])

    $.ajax({
        url: "/api/gifticon/file/add",
        type: "post",
        headers: {
            Authorization: localStorage.getItem("token"),
        },
        data : formData,
        dataType : 'json',
        contentType : false,
        processData : false,
        enctype: "multipart/form-data",

        success : function (jsonData){
            alert("등록 성공");
            $('#add-to-cart').modal('hide');
        },

        error : function (error){
            alert("등록에 실패했습니다.")
        },

        complete: function () {
            window.location.href = "/gifticon/add";
        }
    })
}