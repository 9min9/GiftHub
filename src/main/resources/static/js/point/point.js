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
    if (localStorage.getItem("token")) {
        let xhr = new XMLHttpRequest();

        xhr.open("get", "/api/users/points");

        xhr.setRequestHeader("Authorization", localStorage.getItem("token"));

        xhr.onload = () => {
            if (xhr.status !== 200 || xhr.responseText.indexOf("html") !== -1) {
                return;
            }

            setPoint(xhr.responseText);
        }

        xhr.send();
    }
}

getPoint();