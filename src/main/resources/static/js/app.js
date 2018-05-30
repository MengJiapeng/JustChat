var stompClient = null;
var username = localStorage.getItem("username");
var openedChatWindows = [];

function addGroup(groupName) {
    stompClient.subscribe('/' + groupName, function (greeting) {
        console.log(greeting);
        var content = JSON.parse(greeting.body).content;
        var sender = JSON.parse(greeting.body).senderName;
        var message = "<strong>" + sender + ": </strong>" + content;
        if (sender !== username) {
            displayMessage(groupName, message, true);
        }
    });
}

function connect() {
    var socket = new SockJS('/just-chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/' + username, function (greeting) {
            console.log(greeting);
            console.log(JSON.parse(greeting.body).content);
            var content = JSON.parse(greeting.body).content;
            var sender = JSON.parse(greeting.body).senderName;
            if (JSON.parse(greeting.body).type === 0) {
                displayMessage(sender, content, true);
            } else {
                addGroup(content);
            }
        });

        $.ajax({
            url: "/create-consumer?username=" + username,
            type: "GET"
        });

        if (localStorage.getItem("groupList") !== '') {
            var groupList = localStorage.getItem("groupList").split(",");
            for (var i = 0; i < groupList.length; ++i) {
                addGroup(groupList[i]);
            }
        }
    });
}

function sendMessage(receiver, content) {
    stompClient.send("/app/chat", {}, JSON.stringify({
        'receiverId': receiver,
        'senderName': username,
        'content': content,
        'time': (new Date().toISOString()),
        'type': 0}));
    displayMessage(receiver, content, false);
}

function send(obj) {
    var id = obj.attr("id");
    var receiver = id.split('-')[2];
    var messageInput = $("#to-" + receiver);
    var message = messageInput.val();
    console.log(id);
    console.log(receiver);
    console.log(message);
    sendMessage(receiver, message);
    messageInput.val("");
}

function openChatWindow(username) {
    if (openedChatWindows.indexOf(username) >= 0) {
        console.log("Has username");
        window.location.href = "#" + username;
        return;
    }
    console.log("Doesn't have username");
    openedChatWindows.push(username);
    $("#main-content").append("<div class=\"row chat-window\">\n" +
        "                <div class=\"container\">\n" +
        "                    <div class=\"row\">\n" +
        "                        <div class=\"col-md-12\">\n" +
        "                            <table class=\"table\">\n" +
        "                                <thead class='thead-dark'>\n" +
        "                                    <tr><th>" + username + "</th></tr>\n" +
        "                                </thead>\n" +
        "                                <tbody id=\"" + username + "-chat\">\n" +
        "                                </tbody>\n" +
        "                            </table>\n" +
        "                        </div>\n" +
        "                    </div>\n" +
        "                    <!-- 发送框 -->\n" +
        "                    <div class=\"row\">\n" +
        "                        <div class=\"col-md-10\">\n" +
        "                            <input type=\"text\" id=\"to-" + username + "\" class=\"form-control\"\n" +
        "                                           placeholder=\"\">\n" +
        "                        </div>\n" +
        "                        <div class=\"col-md-2\">\n" +
        "                            <button class=\"btn btn-info btn-block btn-send\" type=\"submit\" onclick='send($(this))' id=\"send-to-" + username + "\">发送</button>\n" +
        "                        </div>\n" +
        "                    </div>\n" +
        "                </div>\n" +
        "            </div>");
}

function displayMessage(username, message, from) {
    console.log(openedChatWindows.indexOf(username));
    if (openedChatWindows.indexOf(username) < 0) {
        openChatWindow(username);
    }

    if (from) {
        $("#" + username + "-chat").append("<tr><td align='left'>" + message + "</td></tr>");
    } else {
        $("#" + username + "-chat").append("<tr><td align='right'>" + message + "</td></tr>");
    }
}

$(function () {
    $("title").text(username + "'s chat");

    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    $("#create-chat").click(function () {
        var receiverName = $("#new-receiver").val();
        if (receiverName !== "") {
            openChatWindow(receiverName);
        }
    });

    $("#create-group").click(function () {
        var groupName = $("#new-group").val();
        if (groupName !== "") {
            var groupMembers = $("#group-members").val().split(',');
            if (groupMembers.indexOf(username) < 0) {
                groupMembers.push(username);
            }
            console.log(groupName);
            console.log(groupMembers);
            $.ajax({
                url: "/temp-group",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify({
                    name: groupName,
                    members: groupMembers
                }),
                success: [
                    function () {
                        openChatWindow(groupName);
                    }
                ]
            });
        }
    });

    $(".btn-send").click(function () {
        var id = $(this).attr("id");
        var receiver = id.split('-')[2];
        var message = $("#to-" + receiver).val();
        console.log(id);
        console.log(receiver);
        console.log(message);
        sendMessage(receiver, message);
    });
    connect();
    window.onbeforeunload = function () {
        if (stompClient !== null) {
            console.log("disconnect");
            stompClient.disconnect();
        }
    }
});