var url = "http://localhost:8085/api/v1/prestamo/";

$(document).ready(function() {
    // Cargar datos del préstamo y usuarios y libros disponibles
    var prestamoId = getParameterByName('id');
    cargarDatosPrestamo(prestamoId);
    cargarUsuarios();
    cargarLibros();

    $('#actualizarPrestamo').click(function() {
        actualizarPrestamo(prestamoId);
    });
});

function cargarDatosPrestamo(id) {
    $.get("/api/v1/prestamo/" + id, function(data) {
        $('#id_usuario').val(data.usuario.id_usuario);
        $('#id_libro').val(data.libro.id);
        $('#fecha_prestamo').val(data.fecha_prestamo);
        $('#fecha_devolucion').val(data.fecha_devolucion);
        $('#estado_prestamo').val(data.estado_prestamo);
    });
}

function cargarUsuarios() {
    $.get("/api/v1/usuario", function(data) {
        data.forEach(function(usuario) {
            $('#id_usuario').append(new Option(usuario.nombre, usuario.id_usuario));
        });
    });
}

function cargarLibros() {
    $.get("/api/v1/libro", function(data) {
        data.forEach(function(libro) {
            $('#id_libro').append(new Option(libro.nombre, libro.id));
        });
    });
}

function actualizarPrestamo(id) {
    var prestamo = {
        usuario: { id_usuario: $('#id_usuario').val() },
        libro: { id: $('#id_libro').val() },
        fecha_prestamo: $('#fecha_prestamo').val(),
        fecha_devolucion: $('#fecha_devolucion').val(),
        estado_prestamo: $('#estado_prestamo').val()
    };

    $.ajax({
        url: '/api/v1/prestamo/' + id,
        type: 'PUT',
        data: JSON.stringify(prestamo),
        contentType: 'application/json',
        success: function(result) {
            alert('Préstamo actualizado correctamente');
        },
        error: function(xhr, status, error) {
            alert('Error al actualizar el préstamo: ' + xhr.responseText);
        }
    });
}

function getParameterByName(name) {
    var url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}
