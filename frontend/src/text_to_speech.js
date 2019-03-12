const synth = window.speechSynthesis;

function determineVoice() {
    const voices = synth.getVoices();
    for (let i = 0; i < voices.length; i++) {
        if (voices[i].lang === "en-GB") {
            return voices[i];
        }
    }
}

function say(text){
    const utterance = new SpeechSynthesisUtterance(text);
    utterance.voice = determineVoice();
    utterance.pitch = 1;
    utterance.rate = 1;
    synth.speak(utterance);
}

function cancelSay(){
    synth.cancel();
}