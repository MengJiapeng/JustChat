var stompClient = null;
var username = localStorage.getItem("username");
var openedChatWindows = [];

function addGroup(groupName) {
    stompClient.subscribe('/' + groupName, function (greeting) {
        console.log(greeting);
        $("#greetings").append("<tr><td><strong>" + JSON.parse(greeting.body).content + "</strong></td></tr>");
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

        if (localStorage.getItem("groupList") !== '') {
            var groupList = localStorage.getItem("groupList").split(",");
            for (var i = 0; i < groupList.length; ++i) {
                addGroup(groupList[i]);
            }
        }
    });
}

function sendMessage() {
    var contentElement = $("#content");
    var content = contentElement.val();
    var receiver = $("#receiver").val();
    stompClient.send("/app/chat", {}, JSON.stringify({
        'receiverId': receiver,
        'senderName': username,
        'content': content,
        'time': (new Date().toISOString()),
        'type': 0}));
    displayMessage(receiver, content, false);
    contentElement.val("");
}

function openChatWindow(username) {
    openedChatWindows.push(username);
    $("#chat-window-container").append("<table id=\"conversation\" class=\"table\" style='margin-top: 50px'>\n" +
        "                <thead class='thead-dark'>\n" +
        "                <tr>\n" +
        "                    <th>" + username + "</th>\n" +
        "                </tr>\n" +
        "                </thead>\n" +
        "                <tbody id=\"" + username + "-chat\">\n" +
        "                </tbody>\n" +
        "            </table>");
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
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#send" ).click(function() { sendMessage(); });
    connect();
    window.onbeforeunload = function () {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
    }
});