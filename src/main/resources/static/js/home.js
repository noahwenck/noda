const options = ["Alphabetical", "Date Added (Test)", "Date Modified (Test)"]; //todo rename
const baseUrl = "http://localhost:8080/";
const flaskUrl = "http://localhost:5000/"

// TODO: with Bootstrap - a lot of this may have to be rewritten

document.addEventListener("DOMContentLoaded", function() {
   // addSortSelection();
   addLinksToElements();
});

/**
 * Very Beautifully and well done search functionality
 */
function search() {
    const option = document.getElementById('option-list').value.toLowerCase();

    /*
        The Search Input needs to:
            1. Exist.
            2. Have something actually entered into it.
        to actually use it for navigation.
     */
    let searchParamExists = false;
    let searchParam = null;
    if (document.getElementById('search-param') != null &&
            document.getElementById('search-param').value != null &&
            document.getElementById('search-param').value !== "") {
        searchParam = document.getElementById('search-param').value;
        searchParamExists = true;
    }

    switch (option) {
        case null:
            return; // todo redo bro what is this
        case "director":
            window.location.href = baseUrl + option;
            break;
        case "primary language":
            window.location.href = baseUrl + "film/language/primary";
            break;
        case "spoken language":
            window.location.href = baseUrl + "film/language/spoken";
            break;
        case "genre":
        case "studio":
        case "country":
            window.location.href = baseUrl + "film/" + option;
            break;
        case "year":
            if (searchParamExists) {
                window.location.href = baseUrl + "film/" + option + "/" + searchParam;
            }
            break;
        case "film":
            if (searchParamExists) {
                window.location.href = baseUrl + "film/list/" + searchParam;
            } else {
                window.location.href = baseUrl;
            }
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
//
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
//  * // todo broken by bootstrap
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
        child.children.item(0).setAttribute("href",
            baseUrl + child.children.item(1).dataset.link);
    }
}

async function getFlaskResponse() {
    console.log("Beginning to gather Letterboxd Information")

    const user = document.getElementById("username-input").value;
    const type = document.getElementById("collection-option").value;

    const response = await fetch(flaskUrl + user + "/" + type);

    if (response.ok) {
        console.log("Response from Web Scraper: " + response.status);
        alert("Films have been added to Noda.")
    } else {
        console.error("Response from Web Scraper: " + response.status);
    }
}