let drawCalendar = (checkList) => {
    let today = new Date();

    let firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
    let lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);


    let dayCounter = 0;
    let row = document.createElement("div");
    row.classList = "row";
    for (let i = 0; i < firstDay.getDay(); i++) {
        dayCounter++;
        row.innerHTML += makeUnit();
    }

    for (let i = 1; i <= lastDay.getDate(); i++) {
        dayCounter++;
        row.innerHTML += makeUnit(i, checkList);

        if (dayCounter == 7) {
            dayCounter = 0;
            row = document.createElement("div");
            row.classList = "row";
        }

        let calendarContainer = document.querySelector("#calendar-container");

        calendarContainer.append(row);
    }

    for (let i = dayCounter; i < 7; i++) {
        row.innerHTML += makeUnit();
    }
}

let makeUnit = (num = "", checkList = [], today = new Date()) => {
    if (!num) {
        return `<div class='unit-box'>${num}</div>`;
    }

    if (checkList.includes(num)) {
        return `<div class='unit-box attendance'>${num}</div>`;
    } else if(today.getDate() > num) {
        return `<div class='unit-box unattendance'>${num}</div>`;
    } else {
        if (today.getDate() == num) {
            return `<div class='unit-box today'>${num}</div>`;
        } else {
            return `<div class='unit-box'>${num}</div>`;
        }
    }
}

$(function () {
    $.ajax({
        url: "/api/attendances",
        type: "get",
        headers: {
            token: localStorage.getItem("token"),
        },
        success: function (attendanceList) {
            let checkList = [];

            for (let attendance of attendanceList) {
                checkList.push(new Date(attendance.createDate).getDate());
            }

            drawCalendar(checkList);

            $(".today").on("click", function () {
                $.ajax({
                    url: "/api/attendances",
                    type: "post",
                    headers: {
                        token: localStorage.getItem("token"),
                    },
                    success: function () {
                        document.querySelector(".today").classList.replace("today", "attendance");
                    }
                })
            });
        },
        error: function (errorMessage) {
            alert(errorMessage);
        }
    });
});