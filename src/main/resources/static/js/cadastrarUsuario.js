function adicionar() {
    window.open('/cadastro/usuarios/new', "_self");
};

function editar(userID) {
    window.open('/cadastro/usuarios/' + userID, "_self");
};

function deletar(userID, userName) {
    if (confirm('Deseja realmente excluir o usuário: ' + userID + ' - ' + userName)) {
        window.open('/delete/usuarios/' + userID, "_self");
    }
};

function cancelar() {
    var codigoStr = document.getElementById('codigo').value;
    if (codigoStr == "0") {
        adicionar();
    } else {
        editar(codigoStr);
    }
};