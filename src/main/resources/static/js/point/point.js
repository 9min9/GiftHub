let setPoint = (point) => {
    let pointLi = document.createElement("li");
    pointLi.innerText = "포인트: "

    let showPointSpan = document.createElement("span");
    showPointSpan.id = "show-point-span";
    showPointSpan.innerText = point;

    pointLi.appendChild(showPointSpan);

    let sideHeader = document.querySelector("#side-header");

    sideHeader.prepend(pointLi);
}

let getPoint = () => {
    let xhr = new XMLHttpRequest();

    xhr.open("get", "/api/users/points");

    xhr.setRequestHeader("Authorization", localStorage.getItem("token"));

    xhr.onload = () => {
        if (xhr.status == 200) {
            setPoint(xhr.responseText);
        }
    }

    xhr.send();
}

getPoint();