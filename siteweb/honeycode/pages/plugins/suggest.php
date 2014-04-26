<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>HoneyCode</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width">

        <link rel="stylesheet" href="../../css/normalize.min.css">
        <link rel="stylesheet" href="../../css/main.css">

        <script src="../../js/vendor/modernizr-2.6.2.min.js"></script>

        <style>
            .name{
                width: 170px;
                font-weight: bold;
            }
            .desc{
                width: 700px;
                color:dimgray;
            }
            .user{
                width: 130px;
            }
            table td{
                width: 500px;
            }
            table tr:hover{
                background-color: #DEEDFF;
                transition: background-color 0.5s ease;
            }
            table tr{
                background-color: white;
                transition: background-color 0.5s ease;
            }
        </style>
    </head>
    <body>
        <!--[if lt IE 7]>
            <p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
        <![endif]-->
        <div id="top">
            <div id="logo">
                <div id="img">
                    <img class="logoImg" src="../../img/logo.png">
                </div>
                <div id="title">
                    <h1>HoneyCode</h1>
                </div>
            </div>
            <div id="menu">
                <div class="cat">
                    <div class="btn">
                    <a href="/honeycode/"><img class="catImg" src="../../img/home.png"><br>
                        Home</a>
                    </div>
                </div>
                <div class="cat">
                    <div class="btn">
                    <a href="../../pages/download"><img class="catImg" src="../../img/download.png"><br>
                        Download</a>
                    </div>
                </div>
                <div class="cat">
                    <div class="here">
                    <a href="../../pages/plugins"><img class="catImg" src="../../img/plugins.png"><br>
                        Plugins</a>
                    </div>
                </div>
                <div class="cat">
                    <div class="btn">
                    <a href="../../pages/help"><img class="catImg" src="../../img/help.png"><br>
                        help</a>
                    </div>
                </div>
            </div>
        </div>
        <?php
        $user = $_POST['user'];
        $passwd = $_POST['mdp'];
        try{
            $pdo_options[PDO::ATTR_ERRMODE] = PDO::ERRMODE_EXCEPTION;
            $bdd = new PDO('mysql:host=localhost;dbname=honeycode', 'root', '', $pdo_options);
            $bdd->exec("set names utf8");
        }catch (Exception $e){
            die('Erreur : ' . $e->getMessage());
        }
        $requete=$bdd->prepare('SELECT * FROM user WHERE login = ? AND password = ?');
        $requete->execute(array($user, $passwd));
        $result = $requete->fetch();
        $requete->closeCursor();
        if($result){
        session_start();
        $_SESSION['id'] = $result['id'];
        $_SESSION['login'] = $user;
        ?>
        <div id="content" style="margin-left:200px;">
            <p>Bonjour <?php echo $result['login'] ?></p>
        	<form action="upload.php" method="post"  enctype="multipart/form-data">
            <div style="text-align: center;">Proposez votre Plugin</div><br>
            <input type="text" name="name" placeholder="Nom du Plugin"><br><br>
            <TEXTAREA rows="5" cols="50" name="desc">Description du plugin</TEXTAREA><br><br>
            <input type="hidden" name="MAX_FILE_SIZE" value="7340032">
			<input type="file" name="plugin"><br><br>
			<input type="submit">
			</form>
        </div>
        <?php } ?>
        <div id="foot">
        </div>

        <!--<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="js/vendor/jquery-1.10.1.min.js"><\/script>')</script>-->

        <script src="../../js/plugins.js"></script>
        <script src="../../js/main.js"></script>

        <!--<script>
            var _gaq=[['_setAccount','UA-XXXXX-X'],['_trackPageview']];
            (function(d,t){var g=d.createElement(t),s=d.getElementsByTagName(t)[0];
            g.src='//www.google-analytics.com/ga.js';
            s.parentNode.insertBefore(g,s)}(document,'script'));
        </script>-->
    </body>
</html>
