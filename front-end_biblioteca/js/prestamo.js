var url = "http://localhost:8085/api/v1/prestamo/";


$(document).ready(function() {
    // Cargar la lista de préstamos al cargar la página
    listarPrestamos();

    // Evento para el botón de registrar nuevo préstamo
    $('#registrarPrestamo').click(function() {
        registrarPrestamo();
    });

    // Evento para el botón de buscar préstamo
    $('#buscarPrestamo').click(function() {
        buscarPrestamo();
    });
});

function listarPrestamos() {
    $.get("/api/v1/prestamo/", function(data) {
        var tabla = htmlTablaPrestamos(data);
        $('#tablaPrestamos').html(tabla);
    });
}

function registrarPrestamo() {
    var prestamo = {
        usuario: { id_usuario: $('#id_usuario').val() },
        libro: { id: $('#id').val() },
        fecha_prestamo: $('#fecha_prestamo').val(),
        fecha_devolucion: $('#fecha_devolucion').val(),
        estado_prestamo: $('#estado_prestamo').val()
    };

    $.ajax({
        url: '/api/v1/prestamo/',
        type: 'POST',
        data: JSON.stringify(prestamo),
        contentType: 'application/json',
        success: function(result) {
            alert('Préstamo registrado correctamente');
            listarPrestamos();
            limpiarCampos();
        },
        error: function(xhr, status, error) {
            alert('Error al registrar el préstamo: ' + xhr.responseText);
        }
    });
}

function buscarPrestamo() {
    var id_prestamo = $('#id_prestamo_buscar').val();

    $.get("/api/v1/prestamo/" + id_prestamo, function(data) {
        if (data) {
            $('#id_usuario').val(data.usuario.id_usuario);
            $('#id').val(data.libro.id);
            $('#fecha_prestamo').val(data.fecha_prestamo);
            $('#fecha_devolucion').val(data.fecha_devolucion);
            $('#estado_prestamo').val(data.estado_prestamo);
        } else {
            alert('Préstamo no encontrado');
        }
    }).fail(function() {
        alert('Préstamo no encontrado');
    });
}

function eliminarPrestamo(id_prestamo) {
    $.ajax({
        url: '/api/v1/prestamo/' + id_prestamo,
        type: 'DELETE',
        success: function(result) {
            alert('Préstamo eliminado correctamente');
            listarPrestamos();
        },
        error: function(xhr, status, error) {
            alert('Error al eliminar el préstamo: ' + xhr.responseText);
        }
    });
}

function htmlTablaPrestamos(prestamos) {
    var tabla = '<table class="table">';
    tabla += '<thead><tr><th>ID</th><th>Usuario</th><th>Libro</th><th>Fecha Préstamo</th><th>Fecha Devolución</th><th>Estado</th><th>Acciones</th></tr></thead>';
    tabla += '<tbody>';
    prestamos.forEach(function(prestamo) {
        tabla += '<tr>';
        tabla += '<td>' + prestamo.id_prestamo + '</td>';
        tabla += '<td>' + prestamo.usuario.nombre + '</td>';
        tabla += '<td>' + prestamo.libro.nombre + '</td>';
        tabla += '<td>' + prestamo.fecha_prestamo + '</td>';
        tabla += '<td>' + prestamo.fecha_devolucion + '</td>';
        tabla += '<td>' + prestamo.estado_prestamo + '</td>';
        tabla += '<td>';
        tabla += '<button class="btn btn-primary" onclick="cargarDatosPrestamo(\'' + prestamo.id_prestamo + '\')">Editar</button> ';
        tabla += '<button class="btn btn-danger" onclick="eliminarPrestamo(\'' + prestamo.id_prestamo + '\')">Eliminar</button>';
        tabla += '</td>';
        tabla += '</tr>';
    });
    tabla += '</tbody></table>';
    return tabla;
}

function limpiarCampos() {
    $('#id_usuario').val('');
    $('#id').val('');
    $('#fecha_prestamo').val('');
    $('#fecha_devolucion').val('');
    $('#estado_prestamo').val('');
}
