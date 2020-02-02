<?php
require_once('koneksi.php');

$id = $_GET['id'];

$sql = "delete from siswa where id='$id'";

if(mysqli_query($con, $sql)){
    echo "Data berhasil dihapus";
}else{
    echo "Data gagal dihapus";
}

mysqli_close($con);
?>