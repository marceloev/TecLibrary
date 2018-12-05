    function doDownload() {
        document.getElementById("downloadURL").click();
    };

    function seePDFOnline() {
        var pdfPATH = document.getElementById("downloadURL").href;
        window.open(pdfPATH);
    };

    function clearFeedback() {
        $("#feedback").html("");
    };

    function openBook(ID) {
        window.open('/detalhes-livro/' + ID, "_self");
    };

    function sendReportProblem() {
        var var_ID = document.getElementById("codigo").value;
        var var_resumo = document.getElementById("resumo").value;
        var var_problema = document.getElementById("problema").value;
        var objJson = {
            id: var_ID,
            resumo: var_resumo,
            problema: var_problema
        };

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/send-problem",
            data: JSON.stringify(objJson),
            dataType: 'json',
            cache: false,
            timeout: 600000,
            success: function(data) {
                showSuccessMsg(data.mensagem, "Obrigado por contribuir com nossa comunidade,\n" +
                    "Verificamos sobre seu contato o mais rápido possível.");
            },
            error: function(e) {
                showErrorMsg("Erro ao tentar registrar feedback", e.responseJSON.mensagem);
            }
        });
    };