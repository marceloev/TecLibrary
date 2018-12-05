function loadTableData(tableID) {
    $(tableID).DataTable({
        "pagingType": "full_numbers",
        "processing": true,
        "language": {
            "sEmptyTable": "Nenhum registro encontrado",
            "sInfo": "Exibindo de _START_ até _END_ no total de _TOTAL_ registros",
            "sInfoEmpty": "Mostrando 0 até 0 de 0 registros",
            "sInfoFiltered": "(Filtrados de _MAX_ registros)",
            "sInfoPostFix": "",
            "sInfoThousands": ".",
            "sLengthMenu": "Exibir: _MENU_ itens por página",
            "sLoadingRecords": "Carregando...",
            "sProcessing": "Processando...",
            "sZeroRecords": "Nenhum registro encontrado",
            "sSearch": "Pesquisar:",
            "oPaginate": {
                "sNext": "Próximo",
                "sPrevious": "Anterior",
                "sFirst": "Primeiro",
                "sLast": "Último"
            },
            "oAria": {
                "sSortAscending": ": Ordenar colunas de forma ascendente",
                "sSortDescending": ": Ordenar colunas de forma descendente"
            }
        },
        "iDisplayLength": 50
    });
    $('div.dataTables_filter input').focus();
};