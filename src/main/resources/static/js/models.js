var MODEL_ALERT = 
"<div class='modal custom-modal-dialog' tabindex='-1' role='dialog' style='z-index: 1080;'> " +
    "<div class='modal-dialog' role='document'> " +
        "<div class='modal-content' style='box-shadow: 15px 20px 0px rgba(0,0,0,0.1); border: solid 1.2px;'> " +
            "<div id='modal-header' class='modal-header'> " +
                "<h3 id='modal-title-text' class='modal-title text-center w-100'>Sem título</h3> " +
                "<button type='button' class='close' data-dismiss='modal' aria-label='Close'><span aria-hidden='true'>&times;</span></button>" +
            "</div> " +
            "<div class='modal-body'> " +
                "<p id='modal-body-text'>Sem mensagem de erro.</p> " +
            "</div> " +
        "</div> " +
    "</div> " +
"</div>";

var MODEL_DIALOG_CHANGE_PASSWORD = 
"<div id='change-password-dialog' class='modal' tabindex='-1' role='dialog' style='z-index: 1060;'> " +
"<div class='modal-dialog' role='document'>" +
                "<div class='modal-content' style='box-shadow: 15px 20px 0px rgba(0,0,0,0.1); border: solid 1.2px;'>"+
                    "<div class='modal-header text-center'>"+
                        "<h4 class='modal-title w-100' id='change-password-dialog-title'>Alterar Senha</h4>"+
                        "<button type='button' class='close' data-dismiss='modal' aria-label='Close'>"+
                        "<span aria-hidden='true'>&times;</span>"+
                        "</button>"+
                    "</div>"+
                    "<div class='modal-body'>"+
                        "<fieldset>"+
                            "<div class='form-row'>"+
                                "<label class='sis-required col-md-3 col-12 text-center' for='old-password'>Senha Atual: </label>"+
                                "<input type='password' name='old-password' id='old-password' class='form-control col-md-9  col-12'>"+
                            "</div>"+
                            "<div class='form-row'>"+
                                "<label class='sis-required col-md-3 col-12 text-center' for='new-password'>Nova Senha: </label>"+
                                "<input type='password' name='new-password' id='new-password' class='form-control col-md-9 col-12'>"+
                            "</div>"+
                            "<div class='form-row'>"+
                                "<label class='sis-required col-md-3 col-12 text-center' for='conf-new-password'>Confirmar: </label>"+
                                "<input type='password' name='conf-new-password' id='conf-new-password' class='form-control col-md-9 col-12'>"+
                            "</div>"+
                        "</fieldset>"+
                    "</div>"+
                    "<div class='modal-footer'>"+
                        "<button type='button' class='btn btn-secondary' data-dismiss='modal'>Cancelar</button>"+
                        "<button type='button' class='btn btn-primary' onclick='saveNewPassword();'>Alterar</button>"+
                    "</div>"+
                "</div>"+
            "</div>"+
        "</div>";

var MODEL_LOADING_SCREEN = 
"<div id='custom-loading-screen' class='modal' style='z-index: 1090; background-color: gray;'>"+
    "<div class='modal-dialog' role='document'>" +
        "<div class='loader'></div>" +
    "/<div>" +
"</div>";