function showErrorMsg(title, content) {
    showMessage("alert-danger", title, content);
};

function showWarnMsg(title, content) {
    showMessage("alert-warning", title, content);
};

function showInfoMsg(title, content) {
    showMessage("alert-info", title, content);
};

function showSuccessMsg(title, content) {
    showMessage("alert-success", title, content);
}

function showMessage(alertType, title, content) {
    $(".custom-modal-dialog").remove();
    $("body").append(MODEL_ALERT);
    $("#modal-header")[0].classList.toggle(alertType);
    $("#modal-title-text")[0].innerText = title;
    $("#modal-body-text")[0].innerText = content;
    $('.custom-modal-dialog').modal();
};

function showChangePasswordDialog() {
    $("#change-password-dialog").remove();
    $("body").append(MODEL_DIALOG_CHANGE_PASSWORD);
    $("#change-password-dialog").modal();
};

function showLoadingScreen() {
    removeLoadingScreen();
    $("body").append(MODEL_LOADING_SCREEN);
};

function removeLoadingScreen() {
    $("#custom-loading-screen").remove();
};

function saveNewPassword() {
    var var_Old_Senha = document.getElementById("old-password").value;
    var var_New_Senha = document.getElementById("new-password").value;
    var var_Conf_Senha = document.getElementById("conf-new-password").value;
    if (var_New_Senha != var_Conf_Senha) {
        showErrorMsg("Erro ao tentar alterar senha", "Senha e Confirmação de senha divergem.")
    } else {
        var objJson = {
            oldSenha: var_Old_Senha,
            newSenha: var_New_Senha,
            confSenha: var_Conf_Senha
        };
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/changePassword",
            data: JSON.stringify(objJson),
            dataType: 'json',
            cache: false,
            timeout: 600000,
            beforeSend: function() {
                showLoadingScreen();
            },
            complete: function() {
                removeLoadingScreen();
            },
            success: function(data) {
                showSuccessMsg("Alteração de Senha", data.mensagem);
            },
            error: function(e) {
                showErrorMsg("Alteração de Senha", e.responseJSON.mensagem);
            }
        });
    }
};

$(document).ready(function() {
    $('.date').mask('00/00/0000');
    $('.time').mask('00:00:00');
    $('.date_time').mask('00/00/0000 00:00:00');
    $('.cep').mask('00000-000');
    $('.var-phone').focusin(function() {
        $('.var-phone').unmask();
    });
    $('.var-phone').focusout(function() {
        var length = $('.var-phone').val().replace(/[^0-9.]/g, "").length;
        if (length < 11) {
            $('.var-phone').mask('(00) 0000-0000');
        } else {
            $('.var-phone').mask('(00) 0-0000-0000');
        }
    });
    $('.phone').mask('(00) 0-0000-0000');
    $('.phone_with_ddd').mask('(00) 0000-0000');
    $('.mixed').mask('AAA 000-S0S');
    $('.cpf').mask('000.000.000-00');
    $('.cnpj').mask('00.000.000/0000-00', {
        reverse: true
    });
    $('.money').mask('000.000.000.000.000,00', {
        reverse: true
    });
    $('.money2').mask("#.##0,00", {
        reverse: true
    });
    $('.ip_address').mask('0ZZ.0ZZ.0ZZ.0ZZ', {
        translation: {
            'Z': {
                pattern: /[0-9]/,
                optional: true
            }
        }
    });
    $('.ip_address').mask('099.099.099.099');
    $('.percent').mask('##0,00%', {
        reverse: true
    });
    $('.clear-if-not-match').mask("00/00/0000", {
        clearIfNotMatch: true
    });
    $('.placeholder').mask("00/00/0000", {
        placeholder: "__/__/____"
    });
    $('.fallback').mask("00r00r0000", {
        translation: {
            'r': {
                pattern: /[\/]/,
                fallback: '/'
            },
            placeholder: "__/__/____"
        }
    });
    $('.selectonfocus').mask("00/00/0000", {
        selectOnFocus: true
    });
});