"use strict";

var synth = window.speechSynthesis;

function determineVoice() {
  var voices = synth.getVoices();

  for (var i = 0; i < voices.length; i++) {
    if (voices[i].lang === "en-GB") {
      return voices[i];
    }
  }
}

function say(text) {
  var utterance = new SpeechSynthesisUtterance(text);
  utterance.voice = determineVoice();
  utterance.pitch = 1;
  utterance.rate = 1;
  synth.speak(utterance);
}