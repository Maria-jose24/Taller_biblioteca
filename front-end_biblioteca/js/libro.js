// URL base para las peticiones AJAX
const url = "http://localhost:8085/api/v1/libro/";

document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("titulo").addEventListener("keypress", soloLetras);
    document.getElementById("autor").addEventListener("keypress", soloLetras);
    document.getElementById("isbn").addEventListener("keypress", soloNumeros);
    document.getElementById("genero").addEventListener("keypress", soloLetras);
    document.getElementById("numero_de_ejemplares_disponibles").addEventListener("keypress", soloNumeros);
    document.getElementById("numero_de_ejemplares_ocupados").addEventListener("keypress", soloNumeros);

    // Validar longitud del ISBN en el blur event
    document.getElementById("isbn").addEventListener("blur", validarISBN);

    // Validar que los números no sean negativos
    document.getElementById("numero_de_ejemplares_disponibles").addEventListener("blur", validarNumeroNoNegativo);
    document.getElementById("numero_de_ejemplares_ocupados").addEventListener("blur", validarNumeroNoNegativo);

    // Validar que los ejemplares ocupados no sean mayores que los disponibles
    document.getElementById("numero_de_ejemplares_disponibles").addEventListener("blur", validarEjemplares);
    document.getElementById("numero_de_ejemplares_ocupados").addEventListener("blur", validarEjemplares);


    function soloLetras(event) {

        const letrasPermitidas = [
            'A', 'Á', 'B', 'C', 'D', 'E', 'É', 'F', 'G', 'H', 'I', 'Í', 'J', 'K', 'L', 'M',
            'N', 'Ñ', 'O', 'Ó', 'P', 'Q', 'R', 'S', 'T', 'U', 'Ú', 'Ü', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'á', 'b', 'c', 'd', 'e', 'é', 'f', 'g', 'h', 'i', 'í', 'j', 'k', 'l', 'm',
            'n', 'ñ', 'o', 'ó', 'p', 'q', 'r', 's', 't', 'u', 'ú', 'ü', 'v', 'w', 'x', 'y', 'z', ' '
        ];
        const key = event.key;

        if (!letrasPermitidas.includes(key)) {
            event.preventDefault();
        }
    }

    function soloNumeros(event) {
        const numerosPermitidos = [
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
        ];
        const key = event.key;

        if (!numerosPermitidos.includes(key)) {
            event.preventDefault();
        }
    }

    function validarNumeroNoNegativo(event) {
        const value = event.target.value;
        if (value < 0) {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'El valor no puede ser negativo',
                timer: 3000
            });
            event.target.value = '';
        }
    }

    function validarEjemplares() {
        const disponibles = parseInt(document.getElementById("numero_de_ejemplares_disponibles").value);
        const ocupados = parseInt(document.getElementById("numero_de_ejemplares_ocupados").value);

        if (ocupados > disponibles) {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Los ejemplares ocupados no pueden ser mayores a los ejemplares disponibles',
                timer: 3000
            });
            document.getElementById("numero_de_ejemplares_ocupados").value = '';
        }
    }

    // Función para registrar un libro
    $('#registrarLibro').click(function () {
        const isbn = $('#isbn').val();
        const titulo = $('#titulo').val();

        // Verificar si el ISBN ya está registrado
        $.ajax({
            url: url + 'buscarISBN/' + isbn,
            type: 'GET',
            contentType: 'application/json',
            success: function (response) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Ya existe un libro registrado con este ISBN',
                    timer: 3000
                });
            },
            error: function (xhr, status, error) {
                if (xhr.status === 404) {
                    // Verificar si el título ya está registrado
                    $.ajax({
                        url: url + 'busquedafiltro/' + titulo,
                        type: 'GET',
                        contentType: 'application/json',
                        success: function (response) {
                            if (response.length > 0) {
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Error',
                                    text: 'Ya existe un libro registrado con este título',
                                    timer: 3000
                                });
                            } else {
                                registrarLibro();
                            }
                        },
                        error: function (xhr, status, error) {
                            Swal.fire({
                                icon: 'error',
                                title: 'Error',
                                text: 'Error al verificar el título: ' + xhr.responseText,
                                timer: 3000
                            });
                        }
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: 'Error al verificar el ISBN: ' + xhr.responseText,
                        timer: 3000
                    });
                }
            }
        });
    });




    // Función para listar libros
    listarLibros();

    // Función para buscar libros
    $('#buscarLibro').click(function () {
        if ($('#inputSearch').val() === '') {
            listarLibros();
            return;
        }
        var tablaBody = $('#cuerpoTablaLibros');
        tablaBody.empty();
        $.ajax({
            url: url + 'busquedafiltro/' + $('#inputSearch').val(),
            type: 'GET',
            contentType: 'application/json',
            success: function (response) {
                $.each(response, function (index, elemento) {
                    htmlTablaLibros(elemento);
                });
            },
            error: function (xhr, status, error) {
                console.log(xhr.responseText);
            }
        });
    });

    // Función para eliminar un libro
    $(document).on('click', '.eliminar-libro', function () {
        var libroId = $(this).data('id');
        mostrarConfirmacionEliminar(libroId);
    });

    // Función para actualizar un libro
    $('#actualizarLibro').click(function () {
        const disponibles = parseInt($('#numero_de_ejemplares_disponibles').val());
        const ocupados = parseInt($('#numero_de_ejemplares_ocupados').val());

        if (disponibles < ocupados) {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Los ejemplares disponibles no pueden ser menores a los ejemplares ocupados',
                timer: 3000
            });
            return;
        }
        $.ajax({
            url: url + $('#idLibro').html(),
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({
                titulo: $('#titulo').val(),
                autor: $('#autor').val(),
                isbn: $('#isbn').val(),
                genero: $('#genero').val(),
                numero_de_ejemplares_disponibles: $('#numero_de_ejemplares_disponibles').val(),
                numero_de_ejemplares_ocupados: $('#numero_de_ejemplares_ocupados').val(),
            }),
            success: function (response) {
                Swal.fire({
                    icon: 'success',
                    title: 'Éxito',
                    text: 'Los datos se guardaron correctamente',
                    timer: 3000
                });
                listarLibros(); // Refrescar la lista de libros
            },
            error: function (xhr, status, error) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: xhr.responseText,
                    timer: 3000
                });
            }
        });
    });

});

// Función para mostrar confirmación antes de eliminar un libro
function mostrarConfirmacionEliminar(id) {
    Swal.fire({
        title: '¿Estás seguro?',
        text: "No podrás revertir esto",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminarlo',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            eliminarLibro(id);
        }
    });
}

// Función para eliminar un libro
function eliminarLibro(id) {
    $.ajax({
        url: url + id,
        type: 'DELETE',
        success: function (response) {
            Swal.fire({
                icon: 'success',
                title: 'Eliminado',
                text: 'El libro ha sido eliminado',
                timer: 3000
            });
            listarLibros(); // Refrescar la lista de libros
        },
        error: function (xhr, status, error) {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Error al eliminar el libro',
                timer: 3000
            });
        }
    });
}

// Función para generar filas de la tabla de libros
function htmlTablaLibros(elemento) {
    var fila = $('<tr>');

    fila.append('<td>' + elemento.id + '</td>');
    fila.append('<td>' + elemento.titulo + '</td>');
    fila.append('<td>' + elemento.autor + '</td>');
    fila.append('<td>' + elemento.isbn + '</td>');
    fila.append('<td>' + elemento.genero + '</td>');
    fila.append('<td>' + elemento.numero_de_ejemplares_disponibles + '</td>');
    fila.append('<td>' + elemento.numero_de_ejemplares_ocupados + '</td>');

    var btnEliminar = $('<button>').addClass('btn btn-danger eliminar-libro')
        .text('Eliminar')
        .data('id', elemento.id);

    var btnActualizar = $('<button>').addClass('btn btn-success')
        .text('Actualizar')
        .click(function () {
            // Redirigir a la página de actualización con el ID del libro
            window.location = 'actualizarLibro.html?id=' + elemento.id;
        });

    var tdBotonEliminar = $('<td>').append(btnEliminar).append(btnActualizar);
    fila.append(tdBotonEliminar);

    $('#cuerpoTablaLibros').append(fila);
}

// Función para registrar un libro
function registrarLibro() {
    // Recopilar datos del formulario
    const titulo = $('#titulo').val();
    const autor = $('#autor').val();
    const isbn = $('#isbn').val();
    const genero = $('#genero').val();
    const numero_de_ejemplares_disponibles = $('#numero_de_ejemplares_disponibles').val();
    const numero_de_ejemplares_ocupados = $('#numero_de_ejemplares_ocupados').val();

    // Crear objeto libro
    const libro = {
        titulo: titulo,
        autor: autor,
        isbn: isbn,
        genero: genero,
        numero_de_ejemplares_disponibles: numero_de_ejemplares_disponibles,
        numero_de_ejemplares_ocupados: numero_de_ejemplares_ocupados
    };

    // Enviar petición POST para registrar el libro
    $.ajax({
        url: url,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(libro),
        success: function (response) {
            // Mostrar mensaje de éxito
            Swal.fire({
                icon: 'success',
                title: 'Éxito',
                text: 'El libro ha sido registrado correctamente',
                timer: 3000
            });
            limpiarCampos(); // Limpiar campos del formulario después de registrar
            listarLibros();

        },
        error: function (xhr, status, error) {
            // Mostrar mensaje de error
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'No se pudo registrar el libro: ' + xhr.responseText,
                timer: 3000
            });
        }
    });
}

// Función para listar libros
function listarLibros() {
    $.ajax({
        url: url,
        type: 'GET',
        contentType: 'application/json',
        success: function (response) {
            var tablaBody = $('#cuerpoTablaLibros');
            tablaBody.empty();
            $.each(response, function (index, elemento) {
                htmlTablaLibros(elemento);
            });
        },
        error: function (xhr, status, error) {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: xhr.responseText,
                timer: 3000
            });
        }
    });
}

// Función para limpiar campos del formulario
function limpiarCampos() {
    $('#titulo').val('');
    $('#autor').val('');
    $('#isbn').val('');
    $('#genero').val('');
    $('#numero_de_ejemplares_disponibles').val('');
    $('#numero_de_ejemplares_ocupados').val('');
}

// Función para validar el formato del ISBN
function validarISBN() {
    const isbn = $('#isbn').val();
    if (!/^\d{10}(\d{3})?$/.test(isbn)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Formato de ISBN incorrecto, debe ser 10 o 13 dígitos numéricos',
            timer: 3000
        });
        $('#isbn').val('');
    }
}