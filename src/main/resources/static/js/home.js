function openDetails(bookId) {
    var isManager = document.getElementById("book-list").getAttribute("value");
    if (isManager == "true") {
        window.open('/livro/' + bookId, "_self");
    } else {
        window.open('/detalhes-livro/' + bookId, "_self");
    }
}
$(document).ready(function() {
    $('#sidebarCollapse').on('click', function() {
        $('#sidebar').toggleClass('active');
        $('#logo').toggleClass('active');
        $('.book-right').toggleClass('active');
        $('.book-left').toggleClass('active');
        $(this).toggleClass('active');
    });
});