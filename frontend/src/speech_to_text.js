const SpeechRecognition = SpeechRecognition || webkitSpeechRecognition;

const recognition = new SpeechRecognition();
recognition.lang = "en-GB";
recognition.interimResults = true;
recognition.maxAlternatives = 1;

recognition.onresult = (evt) => {
    $('#query').val(evt.results[evt.results.length - 1][0].transcript);
};
recognition.onspeechend = () => {
    recognition.stop();
};
recognition.onerror = (evt) => {
    alert("Error occurred in recognition: " + evt.error)
};

$(document).ready(() => {
    $('#btnRecording').click(() => recognition.start());
});