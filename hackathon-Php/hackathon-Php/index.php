<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>UniALFA Eventos</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body {
      scroll-behavior: smooth;
    }
    .navbar-custom {
      background-color: #fff;
      box-shadow: 0 2px 6px rgba(0,0,0,0.1);
    }
    .navbar-custom .nav-link {
      color: #1a1a1a;
      font-weight: 500;
    }
    .navbar-custom .nav-link:hover {
      color: #0d6efd;
    }
    .section-title {
      margin-top: 60px;
      margin-bottom: 30px;
    }
    .card-evento:hover {
      transform: scale(1.02);
      transition: 0.3s;
      box-shadow: 0 0 15px rgba(0,0,0,0.1);
    }
    .carousel-item img {
      object-fit: cover;
      height: 100vh;
      width: 100%;
    }
    .carousel-caption {
      background-color: rgba(0,0,0,0.5);
      padding: 20px;
      border-radius: 8px;
    }
  </style>
</head>
<body class="bg-light">

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-custom fixed-top">
  <div class="container">
    <a class="navbar-brand" href="#inicio">
      <img src="imagens/logo-unialfa.png" height="40" alt="UniALFA Logo">
    </a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav ms-auto">
        <li class="nav-item">
          <a class="nav-link" href="#inicio">Início</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="#eventos">Eventos</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="consulta.php">Consultar</a>
        </li>
      </ul>
    </div>
  </div>
</nav>

<section id="inicio">
  <div id="carouselEventos" class="carousel slide" data-bs-ride="carousel">
    <div class="carousel-inner">
      <?php
        $eventosJson = file_get_contents("http://localhost:3000/api/eventos");
        $eventos = json_decode($eventosJson, true);
        foreach ($eventos as $index => $evento):
      ?>
      <div class="carousel-item <?php echo $index === 0 ? 'active' : ''; ?>">
        <img src="imagens/<?php echo $evento['fotos']; ?>" class="d-block w-100" alt="<?php echo $evento['nomeEvento']; ?>">
        <div class="carousel-caption d-none d-md-block">
          <h5><?php echo $evento['nomeEvento']; ?></h5>
          <a href="#eventos" class="btn btn-warning mt-2">Ver Eventos</a>
        </div>
      </div>
      <?php endforeach; ?>
    </div>
    <button class="carousel-control-prev" type="button" data-bs-target="#carouselEventos" data-bs-slide="prev">
      <span class="carousel-control-prev-icon" aria-hidden="true"></span>
    </button>
    <button class="carousel-control-next" type="button" data-bs-target="#carouselEventos" data-bs-slide="next">
      <span class="carousel-control-next-icon" aria-hidden="true"></span>
    </button>
  </div>
</section>

<!-- Eventos -->
<section id="eventos" class="container">
  <h2 class="text-center section-title">Eventos Disponíveis</h2>
  <div class="row">
    <?php foreach ($eventos as $evento): ?>
    <div class="col-md-4 mb-4">
      <div class="card card-evento">
        <img src="imagens/<?php echo $evento['fotos']; ?>" class="card-img-top" alt="<?php echo $evento['nomeEvento']; ?>">
        <div class="card-body">
          <h5 class="card-title"><?php echo $evento['nomeEvento']; ?></h5>
          <p class="card-text"><?php echo substr($evento['descricao'], 0, 100); ?>...</p>
          <a href="evento.php?evento=<?php echo $evento['id']; ?>" class="btn btn-primary">Ver mais</a>
        </div>
      </div>
    </div>
    <?php endforeach; ?>
  </div>
</section>

<!-- Footer -->
<footer class="bg-primary text-white text-center py-3 mt-5">
  UniALFA &copy; <?php echo date('Y'); ?> - Sistema de Eventos
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
