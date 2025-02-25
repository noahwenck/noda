const sortOptions = ["Alphabetical", "Date Added (Test)", "Date Modified (Test)"];
const baseUrl = "http://localhost:8080/";
const flaskUrl = "http://localhost:5000/";
const filmUrl = "http://localhost:8080/film/";

document.addEventListener("DOMContentLoaded", function() {
   addLinksToElements();
});

/**
 * Very Beautifully and well done search functionality
 */
function search() {
    const searchOption = document.getElementById('option-list').value.toLowerCase();

    /*
        The Search Input needs to:
            1. Exist.
            2. Have something actually entered into it.
        to actually use it for navigation.
     */
    let searchParam = null;
    if (document.getElementById('search-param') != null &&
            document.getElementById('search-param').value != null &&
            document.getElementById('search-param').value !== "") {
        searchParam = document.getElementById('search-param').value;

        if (searchOption === "film") {
            window.location.href = filmUrl + searchParam;
        } else if (searchOption === "year") {
            window.location.href = filmUrl + searchOption + "/" + searchParam;
        } else {
            console.error("Search Param exists for a searchOption that it should not, searchOption=" + searchOption);
        }
        return;
    }

    switch (searchOption) {
        case null:
            break;
        case "director":
        case "genre":
        case "studio":
        case "country":
        case "list":
            window.location.href = filmUrl + searchOption;
            break;
        case "primary language":
            window.location.href = filmUrl + "language/primary";
            break;
        case "spoken language":
            window.location.href = filmUrl + "language/spoken";
            break;
        default: // includes "film" case
            window.location.href = baseUrl;
            break;
    }
}

/**
 * Updates the search div with whatever extra functionality that I want to add mid-use.
 *
 * For example - If the user has selected to search by year, add an input to let them enter the year.
 */
function updateSearchDiv() {
    const option = document.getElementById('option-list').value;
    if (option === "Year" || option === "Film") {
        if (document.getElementById('search-param') != null) {
            const placeholderValue = document.getElementById('search-param').getAttribute("placeholder");
            if (placeholderValue === option) {
                return;
            } else { // Don't want duplicate search inputs, so remove the existing one.
                document.getElementById('search-form').removeChild(
                    document.getElementById('search-param')
                );
            }
        }

        let searchInput = document.createElement("input");
        searchInput.setAttribute("id", "search-param");
        searchInput.setAttribute("placeholder", option)

        document.getElementById("search-form").insertBefore(
            searchInput,
            document.getElementById("search-button")
        );
    } else { // remove search input for options that don't need it
        if (document.getElementById('search-param') != null) {
            document.getElementById('search-form').removeChild(
                    document.getElementById('search-param')
                );
            }
    }
}
//  todo: readd sort functionality
// /**
//     Add Options to select method to sort elements of page by.
//  */
// function addSortSelection() {
//     let sort = document.createElement("select");
//     options.forEach((option) => {
//         let optionTag = document.createElement("option");
//         optionTag.setAttribute("value", option)
//         optionTag.text = option;
//         sort.appendChild(optionTag);
//     })
//
//     document.getElementById("sort-div").insertBefore(
//         sort,
//         document.getElementById("sort-button"));
// }
//
// /**
//  * Only alphabetically for now - need to add stuff on backend to actually sort by
//  *
//  */
// function sort() {
//     let ul = document.getElementById("elements-list")
//     let tags = ul.cloneNode(false);
//     let list = [];
//
//     for (const child of document.getElementById("elements-list").children) {
//         list.push(child);
//     }
//
//     list.sort(function(node1, node2) {
//         return node1.children.item(0).textContent.localeCompare(
//             node2.children.item(0).textContent
//         );
//     });
//
//     for (let i = 0; i < list.length; i++) {
//         tags.appendChild(list[i]);
//     }
//     ul.parentNode.replaceChild(tags, ul);
// }

/**
 * Add href values to each element in our list
 */
function addLinksToElements() {
    for (const child of document.getElementById("elements-rows").children) {
        child.children.item(0).children.item(0).setAttribute("href",
            baseUrl + child.children.item(1).dataset.link);
    }
}

/**
 * Parses the Letterboxd username and which types of films we wish to collect, and passes that information along to scraper
 */
async function requestFilmsFromScraper() {
    console.log("Beginning to import Letterboxd User information.");

    const potentialTypes = ["films", "likes", "watchlist"];
    let types = [];
    for (const type of potentialTypes) {
        if (document.getElementById(type).checked) {
            types.push(type);
        }
    }

    const user = document.getElementById("username-input").value;

    for (const type of types) {
        console.log("Importing data for user: " + user + ", of type: " + type);

        const urlExtension = user + "/" + type;
        await handleDataTransfer(urlExtension, "films");
    }
}

/**
 * Parses the Letterboxd username and list we wish to collect, and passes that information along to scraper
 */
async function requestListFromScraper() {
    console.log("Beginning to import Letterboxd List information.");

    const hostVerificationLiteral = "https://letterboxd.com/";

    let listUrl = document.getElementById("list-input").value;
    if (!listUrl.startsWith(hostVerificationLiteral)) {
        console.error("Not a valid Letterboxd List URL. url:" + listUrl);
        return;
    }
    let username = listUrl.replace(hostVerificationLiteral, "");
    username = username.substring(0, username.indexOf("/"));
    const listPath = listUrl.replace(hostVerificationLiteral + username + "/list/", "").replace("/", "");

    console.log("Importing list: " + listPath + ", from user: " + username);

    const urlExtension = "list/" + username + "/" + listPath;
    await handleDataTransfer(urlExtension, "list");
}

async function handleDataTransfer(urlExtension, typeOfData) {
    await fetch(flaskUrl + urlExtension)
        .then(response => {
            if (!response.ok) {
                throw new Error('Response from shinoda was not ok ' + response.statusText);
            } else {
                console.log("Successfully imported data from shinoda.")
            }
            return response.json();
        }).then(data => {
            fetch(baseUrl + "input/" + typeOfData,
                {method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify(data)})
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Response form noda was not ok ' + response.statusText);
                    } else {
                        console.log("Successfully exported data to noda.")
                    }
                });
        });
}