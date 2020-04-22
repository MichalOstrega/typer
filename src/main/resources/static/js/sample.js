
$('#deleteModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget) // Button that triggered the modal
    var id = button.data('id')
    // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
    // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
    var modal = $(this)
    modal.find('#deleteLink').attr('href','/typer/manager/' + id + '/delete')
    modal.find('#contentModal').text('Czy na pewno chcesz usunąć Ligę o id=' + id + '?')
})
