

//Confirm delete Modal
$('#deleteModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget) // Button that triggered the modal
    var link = button.data('link')
    // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
    // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
    var modal = $(this)
    console.log(link);
    modal.find('#deleteLink').attr('href', link)
    modal.find('#contentModal').text('Czy na pewno chcesz usunąć ten element?')
})
//Filter table
$(document).ready(function () {
    $("#mySearch").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#searchTable tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
});


//Filter select list -not implemented yet
// $(document).ready(function () {
//     $('select').selectize({
//         sortField: 'text'
//     });
// });

$(document).ready(function () {
    $(".slice").slice(0, 2).show();
    $("#loadMore").on('click', function (e) {
        e.preventDefault();
        $("#loadLess").show('slow');
        $(".slice:hidden").slice(0, 2).slideDown();
        if ($(".slice:hidden").length == 0) {
            $("#loadMore").fadeOut('slow');
        }
        $('html,body').animate({
            scrollTop: $(this).offset().top
        }, 1500);
    });

    $("#loadLess").on('click', function (e) {
        e.preventDefault();
        $("#loadMore").show('slow');
        $(".slice:visible").slice(-2).slideUp();
        if ($(".slice:visible").length == 4) {
            $("#loadLess").fadeOut('slow');
        }

        $('html,body').animate({
            scrollTop: $(this).offset().top
        }, 1500);
    });
});
let btn = $('#back-top');


$(window).scroll(function () {
    if ($(window).scrollTop() > 650) {
        btn.addClass('show');
    } else {
        btn.removeClass('show');
    }
});

btn.on('click', function (e) {
    e.preventDefault();
    $('html, body').animate({scrollTop: 0}, '300');
});


$(document).ready(function () {
    $(".add-match-button").click(function () {
        var button = $(this);
        var TCID = $("#TCID").text();
        var matchId = button.attr("value");
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $.ajax({
            url: "http://localhost:8080/typer/manager/rest/competitions",
            method: "post",
            data: JSON.stringify({
                "typerCompetitionId": TCID,
                "matchId": matchId,
            }),
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        })
            .done(function (response) {
                button.prop("disabled", true);
                button.text("Mecz dodany do ligi: " + response.name);
            })
            .fail(function () {
                console.log("Wystąpił błąd");
            })
    });
});
