<?php
// Conexão com a API
$eventosJson = file_get_contents("http://localhost:3000/api/eventos");
$eventos = json_decode($eventosJson, true);
?>

<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Eventos</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    .custom-header {
      background-color: #0d6efd;
    }
    .card-evento:hover {
      box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
      transform: scale(1.01);
      transition: 0.3s;
    }
    .carousel-item img {
      object-fit: cover;
      height: 400px;
    }
  </style>
</head>
<body class="bg-light">

<!-- Cabeçalho com logo -->
<header class="navbar navbar-expand-lg fixed-top custom-header">
  <div class="container d-flex justify-content-between align-items-center">
    <span class="navbar-brand text-white">Minha Logo</span>
  </div>
</header>

<div class="pt-5 mt-4"></div>

<!-- Carrossel de eventos -->
<div class="container mt-3">
  <div id="carouselEventos" class="carousel slide" data-bs-ride="carousel">
    <div class="carousel-inner">
      <?php foreach ($eventos as $index => $evento): ?>
        <div class="carousel-item <?php echo $index === 0 ? 'active' : ''; ?>">
          <img src="imagens/<?php echo $evento['fotos']; ?>" class="d-block w-100" alt="<?php echo $evento['nomeEvento']; ?>">
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
</div>

<!-- Lista de eventos -->
<div class="container mt-5">
  <h1 class="mb-4">Eventos Disponíveis</h1>
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
</div>

<!-- Rodapé -->
<footer class="bg-primary text-white text-center py-3">
  desenvolvido no Hackathon &copy; 2025
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
