$('[data-click]').on('click', function (e) {
    $( $(this).data('click') ).trigger('click');
});