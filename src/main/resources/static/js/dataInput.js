const flaskUrl = "http://localhost:5000/"
const baseUrl = "http://localhost:8080/"

async function getFlask() {
    console.log("Beginning to gather Letterboxd Information")

    const user = document.getElementById("search-input").value;
    const type = document.getElementById("option-list").value;

    const response = await fetch(flaskUrl + user + "/" + type, );

    if (response.ok) {
        console.log("Response from Web Scraper: " + response.status);
        alert("Films have been added to Noda.")
    } else {
        console.error("Response from Web Scraper: " + response.status);
    }
}