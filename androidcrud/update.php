<?php
require_once('koneksi.php');

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    $id = $_POST['id'];
    $nama = $_POST['nama'];
    $kelas = $_POST['kelas'];
    $jurusan = $_POST['jurusan'];

    $sql = "update siswa set nama='$nama',kelas='$kelas',jurusan='$jurusan' where id='$id'";

    if(mysqli_query($con, $sql)){
        echo "Data berhasil di ubah";
    }else{
        echo "Data gagal di ubah";
    }

    mysqli_close($con);
}
?>