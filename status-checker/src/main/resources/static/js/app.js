const pathArray = window.location.pathname.split('/');
const filename = pathArray[1];
const evtSource = new EventSource("http://localhost:8085/sse/" + filename.toString());

evtSource.onmessage = function (event) {
    const mainElement = document.getElementById("imageArea");
    mainElement.innerHTML = "<img src=\x22../images/" + filename + "\x22 th:src=\x22@{/images/" + filename + "}\x22" + " alt=\x22output\x22 />";
    evtSource.close();
}

evtSource.onerror = function (err) {
    console.error("EventSource failed:", err);
};