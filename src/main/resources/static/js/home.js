const options = ["Alphabetical", "Date Added (Test)", "Date Modified (Test)"]; //todo rename
const baseUrl = "http://localhost:8080/";

document.addEventListener("DOMContentLoaded", function() {
   addSortSelection();
   addLinksToElements();
});

function search() {
    const option = document.getElementById('option-list').value.toLowerCase();

    if (option === null) {
        return;
    } else if (option === "director") {
        window.location.href = "http://localhost:8080/" + option;
    } else if (option === "spoken language") {
        window.location.href = "http://localhost:8080/film/language/spoken";
    }

    const year = document.getElementById('search-param').value;
    window.location.href = "http://localhost:8080/film/" + option + "/" + year;
}

function updateSearchDiv() {
    const option = document.getElementById('option-list').value;
    if (option === "Year") {
        let yearSearch = document.createElement("input");
        yearSearch.setAttribute("id", "search-param");
        yearSearch.setAttribute("placeholder", "Year");

        document.getElementById("search-div").insertBefore(
            yearSearch,
            document.getElementById("search-button")
        );
    }
}

/**
    Add Options to select method to sort elements of page by.
 */
function addSortSelection() {
    let sort = document.createElement("select");
    options.forEach((option) => {
        let optionTag = document.createElement("option");
        optionTag.setAttribute("value", option)
        optionTag.text = option;
        sort.appendChild(optionTag);
    })

    document.getElementById("sort-div").insertBefore(
        sort,
        document.getElementById("sort-button"));
}

/**
 * Only alphabetically for now
 */
function sort() {
    let ul = document.getElementById("elements-list")
    let tags = ul.cloneNode(false);
    let list = [];

    for (const child of document.getElementById("elements-list").children) {
        list.push(child);
    }

    list.sort(function(node1, node2) {
        return node1.children.item(0).textContent.localeCompare(
            node2.children.item(0).textContent
        );
    });

    for (let i = 0; i < list.length; i++) {
        tags.appendChild(list[i]);
    }
    ul.parentNode.replaceChild(tags, ul);
}

/**
 * Add href values to each element in our list
 * Value determined by the most hacky thymeleaf known to man... pls make better
 */
function addLinksToElements() {
    for (const child of document.getElementById("elements-list").children) {
        child.children.item(0).setAttribute("href",
            baseUrl + child.children.item(1).dataset.link);
    }
}