"use strict";

var SpeechRecognition = SpeechRecognition || webkitSpeechRecognition;
var recognition = new SpeechRecognition();
recognition.lang = "en-GB";
recognition.interimResults = true;
recognition.maxAlternatives = 1;

recognition.onresult = function (evt) {
  $('#query').val(evt.results[evt.results.length - 1][0].transcript);
};

recognition.onspeechend = function () {
  recognition.stop();
};

recognition.onerror = function (evt) {
  alert("Error occurred in recognition: " + evt.error);
};

$(document).ready(function () {
  $('#btnRecording').click(function () {
    return recognition.start();
  });
});