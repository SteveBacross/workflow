 function myFunction() {
        var elmnt = document.getElementById("forClone");
        var cln = elmnt.cloneNode(true);
        document.body.appendChild(cln);
    }