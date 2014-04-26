<?php
	try{
        $pdo_options[PDO::ATTR_ERRMODE] = PDO::ERRMODE_EXCEPTION;
        $bdd = new PDO('mysql:host=localhost;dbname=honeycode', 'root', '', $pdo_options);
        $bdd->exec("set names utf8");
    }catch (Exception $e){
        die('Erreur : ' . $e->getMessage());
    }
    if(isset($_POST['login']) and isset($_POST['mdp1']) and isset($_POST['mdp2'])){
    	if($_POST['mdp1'] == $_POST['mdp2']){
			$request = $bdd->prepare('INSERT INTO user(name, url, idUser, description) VALUES(?,?,?,?)');
			$request->execute(array(htmlentities($_POST['name'],ENT_QUOTES), $url, $_SESSION['id'], htmlentities($_POST['desc'],ENT_QUOTES)));
			header("location:connect.php");
		}else{
			header("location:register.php?error=2");
		}
    }else{
    	header("location:register.php?error=1");
    }
?>