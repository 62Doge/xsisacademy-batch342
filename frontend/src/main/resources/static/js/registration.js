let roleId;
let emailUser = "";
let passwordUser = "";
let confirmPasswordUser = "";
let fullNameUser = "";
let mobilePhoneUser = "";

$(document).ready(function () {

})


$('#buttonRegister').on('click', function(e) {
    $.ajax({
        type: "get",
        url: "/registration/emailForm",
        contentType: "html",
        success: function (addForm) {
            $('#baseModal').modal('show');
            $('#baseModalTitle').html(`<strong>Daftar</strong>`);
            $('#baseModalBody').html(addForm);
            $('#baseModalFooter').html(`
                <button onclick="sendOtpForEmailRegistration()" type="button" class="btn btn-primary">Kirim OTP</button>
            `);
        }
    })
})

function sendOtpForEmailRegistration() {
    const newEmail = $('#email').val();
    emailUser = newEmail.trim();

    $('#loadingSpinner').show();
    $.ajax({
        url: `http://localhost:9001/api/registration/send-otp`,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ email: emailUser }),
        success: function(response) {
            alert("OTP telah dikirim ke email Anda. Silakan periksa email Anda.");


            $('#emailForm').hide();
            $('#baseModalTitle').html(`<strong>Konfirmasi OTP</strong>`);
            $('#modal-description').text("Harap konfirmasi kode otp")
            $('#otpForm').show();

            $('#baseModalFooter').html(`
                <button onclick="verifyOtpAndEmail('${emailUser}')" type="button" class="btn btn-primary">Verifikasi OTP</button>
            `);
        },
        error: function(xhr) {
            const response = xhr.responseJSON;
            if (xhr.status === 400) {
                if (response && response.message === "Email sudah terdaftar") {
                    $('#emailError').text("Email sudah terdaftar, gunakan e-mail yang berbeda.").show();
                } else if (response && response.message === "Validation error"){
                    $('#emailError').text("Pastikan e-mail sudah valid.").show();
                }
            } else if (xhr.status === 429){
                $('#emailError').text(response.message).show();
            }
            else {
                alert("An error occurred. Please try again later.");
            }
        },
        complete: function() {
            $('#loadingSpinner').hide();
        }
    });
}

function verifyOtpAndEmail() {
    const otp = $('#otp').val();

    $.ajax({
        url: `http://localhost:9001/api/registration/verify-otp`,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ email: emailUser, otp: otp }),
        success: function(response) {
            alert("OTP valid! Silahkan lanjut ke tahap berikutnya.");
            $('#email').val(emailUser);
            $('#otpForm').hide();

            $('#passwordForm').show();
            $('#confirmPasswordForm').show();
            $('#baseModalTitle').html(`<strong>Masukkan Password</strong>`);
            $('#modal-description').html(`<strong>Masukkan password dan konfirmasi password</strong>`);

            $('#baseModalFooter').html(`
                <button onclick="confirmationPassword()" type="button" class="btn btn-primary">Submit Password</button>
            `);
            // $('#baseModal').modal('hide');
            // location.reload();
        },
        error: function(xhr) {
            if (xhr.status === 400 && xhr.responseJSON && xhr.responseJSON.message) {
                $('#otpError').text(xhr.responseJSON.message).show();
            } else if (xhr.status ===  404) {
                $('#otpError').text("Pastikan OTP sudah valid.").show();
            } else {
                alert("Terjadi kesalahan saat memverifikasi OTP. Silakan coba lagi.");
            }
        }
    });
}

function confirmationPassword() {
    const password = $('#password').val();
    const confirmPassword = $('#confirmPassword').val();

    passwordUser = password.trim();
    confirmPasswordUser = confirmPassword.trim();

    if (password !== confirmPassword) {
        $('#confirmPasswordError').text("Konfirmasi password tidak cocok.").show();
        return;
    } else {
        $('#confirmPasswordError').hide();
    }

    $.ajax({
        url: `http://localhost:9001/api/registration/verify-password`,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            password: passwordUser,
            confirmPassword: confirmPasswordUser
        }),
        success: function(response) {
            $('#passwordForm').hide();
            $('#confirmPasswordForm').hide();

            $('#fullNameForm').show();
            $('#mobilePhoneForm').show();
            $('#roleForm').show();
            loadRoles();

            $('#baseModalTitle').html(`<strong>Masukkan Data Diri</strong>`);
            $('#modal-description').html(`<strong>Masukkan data diri beserta nomor handphone</strong>`);

            $('#baseModalFooter').html(`
                <button onclick="confirmRegist()" type="button" class="btn btn-primary">Submit Data Diri</button>
            `);
        },
        error: function(xhr) {
            if (xhr.status === 400) {
                const response = xhr.responseJSON;
                $('#passwordError').text(response.data.password).show();
                $('#confirmNewPasswordError').text(response.data.password).show();
            } else {
                alert("Terjadi kesalahan saat memverifikasi password.");
            }
            $('#baseModal').modal('show');
            setTimeout(() => {
            }, 1000)
        },
        complete: function (){
            setTimeout(() => {
                $('#passwordError').hide();
                $('#confirmPasswordError').hide();
            }, 2000)
        }

    });
}

function confirmRegist() {
    const newFullName = $('#fullName').val();
    const newMobilePhone = $('#mobilePhone').val();
    const newRole = $('#role').val();

    $.ajax({
        url: `http://localhost:9001/api/registration`,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            roleId: newRole,
            email: emailUser,
            password: passwordUser,
            confirmPassword: confirmPasswordUser,
            fullName: newFullName,
            mobilePhone: newMobilePhone,
        }),
        success: function(response) {
            alert("Berhasil registrasi akun");

            $('#baseModal').modal('hide');

        },
        error: function(xhr) {
            const response = xhr.responseJSON;
            if (xhr.status === 400 && xhr.responseJSON && xhr.responseJSON.message) {
                $('#mobilePhoneError').text(response.data.mobilePhone).show();
            } else if (xhr.status ===  404) {
                $('#otpError').text("Pastikan OTP sudah valid.").show();
            } else {
                alert("Terjadi kesalahan saat memverifikasi OTP. Silakan coba lagi.");
            }
        }
    });
}

function loadRoles() {
    $.ajax({
        type: "GET",
        url: "http://localhost:9001/api/admin/role",
        contentType: "application/json",
        success: function (response) {
            console.log(response);
            let roles = response.data.content;
            let roleOptions = `<option value="" hidden selected disabled>Select Role</option>`;

            roles.forEach(role => {
                roleOptions += `<option value="${role.id}">${role.name}</option>`;
            });

            $('#role').append(roleOptions);

        },
        error: function (error) {
            console.error("Error loading categories:", error);
        }
    });
}

