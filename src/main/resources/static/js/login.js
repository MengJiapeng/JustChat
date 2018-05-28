localStorage.clear();

$("#submit").on("click", function () {
    var username = $("#username").val();
    var password = $("#password").val();
    console.log(username);
    console.log(password);
    $.ajax({
        url: "/login",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            "username": username,
            "password": password
        }),
        success: [
            function (data) {
                console.log(data);
                localStorage.setItem("username", username);
                localStorage.setItem("groupList", data);
                window.location.href = "/chat.html"
            }
        ]
    });
});