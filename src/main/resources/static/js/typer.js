


$(document).ready(function () {
    $("#leagues").change(function () {
        var league = $(this).val();
        $.get("/rest/competitions/" + league + "/matches", function (data) {
            $("#stages").empty();
            data.forEach(function (item, i) {
                var option = "<option value = " + item + ">" + item + "</option>";
                $("#stages").append(option);
            });
            $("#stages").prop('disabled', false);
            var stage = $("#stages").val();
            dodajMeczeButton(league, stage);
        });
    });
});

$(document).ready(function () {
    var league = $("#leagues").val();
    $.get("/rest/competitions/" + league + "/matches", function (data) {
        $("#stages").empty();
        data.forEach(function (item, i) {
            var option = "<option value = " + item + ">" + item + "</option>";
            $("#stages").append(option);
        });
        $("#stages").prop('disabled', false);
        var stage = $("#stages").val();
        dodajMeczeButton(league, stage);
    });
});

$(document).ready(function () {
    $("#stages").change(function () {
        var stage = $(this).val();
        var league = $("#leagues").val();
        dodajMeczeButton(league, stage);
    });
});

function dodajMeczeButton(leagues, stages, ) {
    var id = $("#TCID").text();
    $("#dodajMecze").prop("href", "/competitions/" + leagues + "/matches?stage=" + stages + "&TCID=" + id)
    $("#dodajMecze").prop('disabled', false);
}


