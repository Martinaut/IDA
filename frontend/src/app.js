let stompClient = null;

function setConnected(connected) {
    $("#btnConnect").prop("disabled", connected);
    $("#btnDisconnect").prop("disabled", !connected);
    $("#btnRecording").prop("disabled", !connected);
    if (connected) {
        $("#results").show();
    } else {
        $("#results").hide();
    }
    $("#results-body").html("No result available yet.");
}

function connect() {
    const socket = new SockJS('http://localhost:8080/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}

function onConnected(frame) {
    setConnected(true);
    console.log('Connected: ' + frame);

    // Subscribe to topic
    stompClient.subscribe('/queue/inga', function (greeting) {
        // showGreeting(JSON.parse(greeting.body).content);
    });
    stompClient.send('/app/start', {}, JSON.stringify({locale: 'en'}))
}

function onError(error) {
    console.log(error);
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
}

$(document).ready(() => {
    $("#btnConnect").click(() => connect());
    $("#btnDisconnect").click(() => disconnect());
});