
// URL base para las peticiones AJAX
const url = "http://localhost:8085/api/v1/usuario/";


$(document).ready(function () {

  $('#registrarUsuario').click(function () {
    if (!validarCorreo(document.getElementById('correo').value)) {
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'El correo es inválido',
        timer: 3000
      });
      return;
    }

    $.ajax({
      url: url,
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({
        nombre_completo: document.getElementById('nombre_completo').value,
        direccion: document.getElementById('direccion').value,
        correo: document.getElementById('correo').value,
        tipo_usuario: document.getElementById('tipo_usuario').value,
      }),
      success: function (response) {
        Swal.fire({
          icon: 'success',
          title: 'Éxito',
          text: 'Los datos se guardaron correctamente',
          timer: 3000
        });
        limpiarCampos();
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

  // Listar usuarios
  listarUsuarios();
  buscarUsuarios();
});

function validarCorreo(correo) {
  var expresionRegular = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return expresionRegular.test(correo);
}



function actualizarLibro() {
  $('#actualizarLibro').click(function () {
    $.ajax({
      url: url + document.getElementById('idLibro').innerHTML,
      type: 'PUT',
      contentType: 'application/json',
      data: JSON.stringify({
        titulo: document.getElementById('titulo').value,
        autor: document.getElementById('autor').value,
        isbn: document.getElementById('isbn').value,
        genero: document.getElementById('genero').value

      }),
      success: function (response) {
        document.getElementById('mensaje-resultado').textContent = 'Los datos se guardaron correctamente';
      },
      error: function (xhr, status, error) {
        document.getElementById('mensaje-resultado').textContent = xhr.responseText;
      }
    });
  });
}

function buscarUsuarios() {
  $('#buscarUsuario').click(function () {
    if (document.getElementById('inputSearch').value === '') {
      listarUsuarios();
      return;
    }
    var tablaBody = document.getElementById('cuerpoTablaUsuario');
    tablaBody.innerHTML = '';
    $.ajax({
      url: url + 'busquedafiltro/' + document.getElementById('inputSearch').value,
      type: 'GET',
      contentType: 'application/json',
      success: function (response) {
        $.each(response, function (index, elemento) {
          htmlTablaUsuarios(elemento);
        });
      },
      error: function (xhr, status, error) {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'Error al buscar usuarios: ' + xhr.responseText,
          timer: 3000
        });
      }
    });
  });
}

// Función para eliminar un usuario
function eliminarUsuario(id) {
  $.ajax({
      url: url + id,
      type: 'DELETE',
      contentType: 'application/json',
      success: function (response) {
          Swal.fire({
              icon: 'success',
              title: 'Eliminado',
              text: 'El usuario ha sido eliminado',
              timer: 3000
          });
          listarUsuarios();
      },
      error: function (xhr, status, error) {
          mostrarError('Error al eliminar el usuario: ' + xhr.responseText);
      }
  });
}
// Función para mostrar confirmación antes de eliminar un usuario
function mostrarConfirmacionEliminarUsuario(id) {
  Swal.fire({
      title: '¿Estás seguro?',
      text: "No podrás revertir esto",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí, eliminarlo'
  }).then((result) => {
      if (result.isConfirmed) {
          eliminarUsuario(id);
      }
  });
}
function listarUsuarios() {
  $.ajax({
    url: url,
    type: 'GET',
    contentType: 'application/json',
    success: function (response) {
      var tablaBody = $('#cuerpoTablaUsuario');
      tablaBody.empty();
      $.each(response, function (index, elemento) {
        htmlTablaUsuarios(elemento);
      });
    },
    error: function (xhr, status, error) {
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'Error al listar usuarios: ' + xhr.responseText,
        timer: 3000
      });
    }
  });
}

function htmlTablaUsuarios(elemento) {
  var fila = $('<tr>');

  fila.append('<td>' + elemento.id_usuario + '</td>');
  fila.append('<td>' + elemento.nombre_completo + '</td>');
  fila.append('<td>' + elemento.direccion + '</td>');
  fila.append('<td>' + elemento.correo + '</td>');
  fila.append('<td>' + elemento.tipo_usuario + '</td>');

  var btnEliminar = $('<button>').addClass('btn btn-danger')
    .text('Eliminar')
    .click(function () {
      mostrarConfirmacionEliminarUsuario(elemento.id_usuario);
    });

  var btnActualizar = $('<button>').addClass('btn btn-success')
    .text('Actualizar')
    .click(function () {
      window.location = 'actualizarUsuario.html?id=' + elemento.id_usuario;
    });

  var tdBotonEliminar = $('<td>').append(btnEliminar).append(btnActualizar);
  fila.append(tdBotonEliminar);
  $('#cuerpoTablaUsuario').append(fila);
}

function limpiarCampos() {
  document.getElementById("nombre_completo").value = "";
  document.getElementById("direccion").value = "";
  document.getElementById("correo").value = "";
  document.getElementById("tipo_usuario").value = "";

}