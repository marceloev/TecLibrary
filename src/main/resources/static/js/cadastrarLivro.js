    $(document).ready(function() {
        $(document).on('change', '.btn-file :file', function() {
            var input = $(this),
                label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
            input.trigger('fileselect', [label]);
        });
        $('.btn-file :file').on('fileselect', function(event, label) {

            var input = $(this).parents('.input-group').find(':text'),
                log = label;

            if (input.length) {
                input.val(log);
            }

        });

        function readURL(input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();

                reader.onload = function(e) {
                    $('#img-upload').attr('src', e.target.result);
                }
                reader.readAsDataURL(input.files[0]);
            }
        }
        $("#imgInp").change(function() {
            readURL(this);
        });
    });

    function deleteLivro(livroID, livroDescr) {
        if (confirm('Deseja realmente excluir o livro: ' + livroID + ' - ' + livroDescr)) {
            window.open('/delete/livros/' + livroID, "_self");
        }
    };

    function resetForm() {
        document.getElementById("descricao").value = "";
        document.getElementById("dataPublicacao").value = "";
        document.getElementById("codigoBarra").value = "";
        document.getElementById("resenha").value = "";
        document.getElementById("sinopse").value = "";
        document.getElementById("imgInp").value = "";
        $('#img-upload').removeAttr('src');
    };

    function expand() {
        var expandable = document.getElementById("expandable").innerHTML;
        if (expandable == "▲") {
            document.getElementById("expandable").innerHTML = "▼";
        } else {
            document.getElementById("expandable").innerHTML = "▲";
            document.getElementById("generosDIV").classList.remove("show");
        }
    };

    function booleanFunction() {
        return "checked";
    };

    function detalhesLivro(livroID) {
        window.open('/detalhes-livro/' + livroID, "_self");
    };