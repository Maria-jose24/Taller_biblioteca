const urlBase = "http://localhost:8085/api/v1/libro/";

$(document).ready(function () {
    // Obtener el ID del libro desde la URL y cargar los datos del libro
    var url = new URL(window.location.href);
    var libroId = url.searchParams.get("id");

    if (libroId) {
        obtenerLibroPorId(libroId);
    }

    // Configurar la función de actualización al hacer clic en el botón
    $('#actualizarLibro').click(function () {
        verificarTituloExistente($('#titulo').val(), actualizarLibro);
    });
});

// Validar solo letras en los campos correspondientes
$('#titulo, #autor, #genero').keypress(function (event) {
    soloLetras(event);
});

// Validar solo números en los campos correspondientes
$('#isbn, #numero_de_ejemplares_disponibles, #numero_de_ejemplares_ocupados').keypress(function (event) {
    soloNumeros(event);
});

// Validar que los números no sean negativos
$('#numero_de_ejemplares_disponibles, #numero_de_ejemplares_ocupados').blur(function (event) {
    validarNumeroNoNegativo(event);
});


// Función para obtener los datos de un libro por su ID y llenar el formulario
function obtenerLibroPorId(id) {
    $.ajax({
        url: urlBase + id,
        type: 'GET',
        contentType: 'application/json',
        success: function (response) {
            $('#idLibroOculto').val(id); // Almacena el ID del libro en el campo oculto
            $('#titulo').val(response.titulo);
            $('#autor').val(response.autor);
            $('#isbn').val(response.isbn);
            $('#genero').val(response.genero);
            $('#numero_de_ejemplares_disponibles').val(response.numero_de_ejemplares_disponibles);
            $('#numero_de_ejemplares_ocupados').val(response.numero_de_ejemplares_ocupados);
        },
        error: function (xhr, status, error) {
            console.error('Error al obtener el libro:', xhr.responseText);
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'No se pudo obtener el libro: ' + xhr.responseText,
                timer: 3000
            });
        }
    });
}


// Función para verificar si un título ya existe
function verificarTituloExistente(titulo, libroId, callback) {
    $.ajax({
        url: urlBase + 'busquedafiltro/' + encodeURIComponent(titulo),
        type: 'GET',
        contentType: 'application/json',
        success: function (response) {
            const existeOtroLibroConMismoTitulo = response.some(libro => libro.id !== libroId);
            if (existeOtroLibroConMismoTitulo) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'El título ya existe. Por favor, elija un título diferente.',
                    timer: 3000
                });
            } else {
                callback();
            }
        },
        error: function (xhr, status, error) {
            console.error('Error al verificar el título:', xhr.responseText);
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'No se pudo verificar el título: ' + xhr.responseText,
                timer: 3000
            });
        }
    });
}

// Función para actualizar un libro por su ID
function actualizarLibro() {
    var idLibro = $('#idLibroOculto').val();
    if (!idLibro) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'ID del libro no encontrado o no válido',
            timer: 3000
        });
        return;
    }
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
        url: urlBase + idLibro,
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
                text: 'Los datos se actualizaron correctamente',
                timer: 3000
            }).then(() => {
                window.location = './ListaLibro.html'; // Redireccionar a la lista de libros después de la actualización
            });
        },
        error: function (xhr, status, error) {
            console.error("Error al actualizar el libro:", error);
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'No se pudo actualizar el libro',
                timer: 3000
            });
        }
    });
}
// Función para permitir solo letras en la entrada
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

// Función para permitir solo números en la entrada
function soloNumeros(event) {
    const numerosPermitidos = [
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    ];
    const key = event.key;

    if (!numerosPermitidos.includes(key)) {
        event.preventDefault();
    }
}

// Función para validar que el número no sea negativo
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