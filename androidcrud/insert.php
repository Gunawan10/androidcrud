<?php
require_once('koneksi.php');

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $nama = $_POST['nama'];
    $kelas = $_POST['kelas'];
    $jurusan = $_POST['jurusan'];

    $sql = "insert into siswa (nama,kelas,jurusan) values ('$nama','$kelas','$jurusan')";

    if(mysqli_query($con, $sql)){
        echo "Data berhasil di tambahkan";
    }else{
        echo "Data gagal di tambahkan";
    }

    mysqli_close($con);
}
?>