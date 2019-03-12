// region --- Connection ---
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
    stompClient.subscribe('/user/queue/inga', function (msg) {
        display(JSON.parse(msg.body));
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

// endregion

$(document).ready(() => {
    $("#btnConnect").click(() => connect());
    $("#btnDisconnect").click(() => disconnect());
    $("#btnSend").click(() => send());
});

// region --- Display ---
function display(d) {
    if (d.type === 'ListDisplay') {
        let sayText = d.display.displayMessage + '\r\n';
        let html = '<h4 class="p-3 text-white bg-dark text-center">' + d.display.displayMessage + '</h4>';
        html += '<table class="table table-hover">';

        let i = 1;
        d.display.data.forEach(elem => {
            sayText += 'Option ' + i + ': ' + elem.title + '\r\n';
            html += '<tr><td title="' + elem.details + '">' + elem.title + '</td></tr>'
            i++;
        });

        html += '</table>';

        $("#results-body").html(html);
        say(sayText);
    }
}

// endregion

function send() {
    const content = $("#query").val().trim();
    if (content && stompClient) {
        const message = {
            userInput: content
        };
        stompClient.send('/app/input', {}, JSON.stringify(message));
        $("#query").val('');
    }
}