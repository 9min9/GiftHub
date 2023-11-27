let setPoint = (point) => {
    let element = document.querySelector("#show-point-span");

    element.innerText = point;
}

let getPoint = () => {
    let xhr = new XMLHttpRequest();

    xhr.open("get", "/api/users/points");

    xhr.setRequestHeader("Authorization", localStorage.getItem("token"));

    xhr.onload = () => {
        setPoint(xhr.responseText);
    }

    xhr.send();
}

getPoint();