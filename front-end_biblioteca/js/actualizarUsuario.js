const url = "http://localhost:8085/api/v1/usuario/";

$(document).ready(function () {
    var urlParams = new URLSearchParams(window.location.search);
    var id = urlParams.get('id');
    if (id) {
        obtenerUsuarioPorId(id);
    }

    actualizarUsuario();
});

function validarCorreo(correo) {
    var expresionRegular = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return expresionRegular.test(correo);
}

function actualizarUsuario() {
    $('#actualizarUsuario').click(function () {
        if (!validarCorreo(document.getElementById('correo').value)) {
            Swal.fire({
                title: 'Error',
                text: 'El correo es inválido',
                icon: 'error',
                confirmButtonColor: '#3085d6',
                timer: 3000,
                timerProgressBar: true,
                showConfirmButton: false
            });
            return;
        }

        Swal.fire({
            title: '¿Estás seguro?',
            text: "Esta acción actualizará los datos del usuario.",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Sí, actualizarlo',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: url + document.getElementById('idUsuario').value,
                    type: 'PUT',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        nombre_completo: document.getElementById('nombre_completo').value,
                        direccion: document.getElementById('direccion').value,
                        correo: document.getElementById('correo').value,
                        tipo_usuario: document.getElementById('tipo_usuario').value,
                    }),
                    success: function (response) {
                        Swal.fire({
                            title: 'Éxito',
                            text: 'Los datos se guardaron correctamente',
                            icon: 'success',
                            confirmButtonColor: '#3085d6',
                            timer: 3000,
                            timerProgressBar: true,
                            showConfirmButton: false
                        }).then(() => {
                            // Redirigir a la página de listar usuarios
                            window.location.href = 'ListaUsuario.html';
                        });
                    },
                    error: function (xhr, status, error) {
                        Swal.fire({
                            title: 'Error',
                            text: xhr.responseText,
                            icon: 'error',
                            confirmButtonColor: '#3085d6',
                            timer: 3000,
                            timerProgressBar: true,
                            showConfirmButton: false
                        });
                    }
                });
            }
        });
    });
}

function obtenerUsuarioPorId(id) {
    $.ajax({
        url: url + id,
        type: 'GET',
        contentType: 'application/json',
        success: function (response) {
            document.getElementById('idUsuario').value = response.id_usuario;
            document.getElementById('nombre_completo').value = response.nombre_completo;
            document.getElementById('direccion').value = response.direccion;
            document.getElementById('correo').value = response.correo;
            document.getElementById('tipo_usuario').value = response.tipo_usuario;
        },
        error: function (xhr, status, error) {
            Swal.fire({
                title: 'Error',
                text: xhr.responseText,
                icon: 'error',
                confirmButtonColor: '#3085d6',
                timer: 3000,
                timerProgressBar: true,
                showConfirmButton: false
            });
        }
    });
}