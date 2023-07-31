$(document).ready(function () {
    $('${tableId}').DataTable({
        lengthMenu: [[5, 10, 25, 50, -1], [5, 10, 25, 50, "Tout"]],
        language: {
            "lengthMenu": "Afficher _MENU_ enregistrement(s) par page",
            "zeroRecords": "Aucune correspondance",
            "info": "Afficher la page _PAGE_ of _PAGES_",
            "infoEmpty": "Aucun enregistrement disponible",
            "infoFiltered": "(filtré à partir de _MAX_ enregistrements totaux)",
            "search": "Rechercher",
            "emptyTable":     "Aucune donnee n'est disponible dans la table.",
            "loadingRecords": "Chargement...",
            "processing":     "Traitement en cours...",
            "thousands":      ".",
            "paginate": {
                "first":    "Premier",
                "last":     "Dernier",
                // "next":     '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-arrow-right"><line x1="5" y1="12" x2="19" y2="12"></line><polyline points="12 5 19 12 12 19"></polyline></svg>',
                // "previous": '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-arrow-left"><line x1="19" y1="12" x2="5" y2="12"></line><polyline points="12 19 5 12 12 5"></polyline></svg>',
                "previous": '<i class="fa fa-long-arrow-left"></i>',
                "next": '<i class="fa fa-long-arrow-right"></i>',
            },
        },
        destroy: true,
    });
});