$(document).ready(function() {
    document.getElementById("descricao").focus();
    var codigoInput = document.getElementById("codigo");
    codigoInput.value = '0';
    var descrInput = document.getElementById("descricao");
    descrInput.value = '';
    codigoInput.addEventListener('keypress', (event) => {
        if (event.keyCode == 13) {
            saveGenero();
        } else {
            var formStatus = document.getElementById("formStatus").getAttribute("status");
            if (formStatus == "normal") {
                adicionar();
            }
        }
    });
    descrInput.addEventListener('keypress', (event) => {
        if (event.keyCode == 13) {
            saveGenero();
        } else {
            var formStatus = document.getElementById("formStatus").getAttribute("status");
            if (formStatus == "normal") {
                adicionar();
            }
        }
    });
});

function deleteGenero(generoID, generoDescr) {
    var formStatus = document.getElementById("formStatus").getAttribute("status");
    if (formStatus == "inserindo") {
        alert("Salve ou Cancele o cadastro do Gênero antes!");
    } else if (formStatus == "editando") {
        alert("Salve ou Cancele a alteração de Gênero antes!");
    } else {
        if (confirm('Deseja realmente excluir o gênero: ' + generoID + ' - ' + generoDescr)) {
            window.open('/delete/generos/' + generoID, "_self");
        }
    }
};

function editGenero(generoID, generoDescr) {
    var formStatus = document.getElementById("formStatus").getAttribute("status");
    if (formStatus == "inserindo") {
        alert("Salve ou Cancele o cadastro do Gênero antes!");
    } else if (formStatus == "editando") {
        alert("Salve ou Cancele a alteração de Gênero antes!");
    } else {
        document.getElementById("formStatus").setAttribute("status", "editando");
        document.getElementById('codigo').value = generoID;
        document.getElementById('descricao').value = generoDescr;
        document.getElementById('btn-adicionar').disabled = true;
        document.getElementById('btn-salvar').disabled = false;
        document.getElementById('btn-cancelar').disabled = false;
    }
};

function adicionar() {
    var formStatus = document.getElementById("formStatus").getAttribute("status");
    if (formStatus == "inserindo") {
        alert("Salve ou Cancele o cadastro do Gênero antes!");
    } else if (formStatus == "editando") {
        alert("Salve ou Cancele a alteração de Gênero antes!");
    } else {
        document.getElementById("formStatus").setAttribute("status", "inserindo");
        document.getElementById('codigo').value = '0';
        document.getElementById('descricao').value = "";
        document.getElementById('btn-adicionar').disabled = true;
        document.getElementById('btn-salvar').disabled = false;
        document.getElementById('btn-cancelar').disabled = false;
    }
};

function saveGenero() {
    var formStatus = document.getElementById("formStatus").getAttribute("status");
    if (formStatus == "editando") {
        document.getElementById("formGenero").submit();
    } else if (formStatus == "inserindo") {
        document.getElementById('codigo').value = '0';
        document.getElementById("formGenero").submit();
    }
    document.getElementById("formStatus").setAttribute("status", "normal");
    document.getElementById('btn-adicionar').disabled = false;
    document.getElementById('btn-salvar').disabled = true;
    document.getElementById('btn-cancelar').disabled = true;
};

function cancelar() {
    var codigoStr = document.getElementById('codigo').value;
    var descricaoStr = document.getElementById('descricao').value;
    if ((codigoStr == "" && descricaoStr == "") || confirm("Deseja cancelar as alterações feitas?")) {
        document.getElementById("formStatus").setAttribute("status", "normal");
        document.getElementById('codigo').value = "";
        document.getElementById('descricao').value = "";
        document.getElementById('btn-adicionar').disabled = false;
        document.getElementById('btn-salvar').disabled = true;
        document.getElementById('btn-cancelar').disabled = true;
    }
};