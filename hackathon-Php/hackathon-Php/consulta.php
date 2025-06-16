<?php
$tipo = $_GET['tipo'] ?? null;
$mensagem = "";
$resultados = [];

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
  if ($tipo === 'inscricao') {
    $inscricao = $_POST['inscricao'] ?? '';

    $usuariosJson = file_get_contents("http://localhost:3000/api/usuarios");
    $usuarios = json_decode($usuariosJson, true);

    foreach ($usuarios as $usuario) {
      if ($usuario['inscricao'] === $inscricao) {
        $mensagem = "Inscrição localizada.";
        $resultados[] = $usuario;
        break;
      }
    }

    if (empty($resultados)) {
      $mensagem = "Nenhum evento encontrado para esse número de inscrição.";
    }

  } elseif ($tipo === 'certificado') {
    $nome = $_POST['nome'] ?? '';
    $cpf = $_POST['cpf'] ?? '';

    $usuariosJson = file_get_contents("http://localhost:3000/api/usuarios");
    $usuarios = json_decode($usuariosJson, true);

    foreach ($usuarios as $usuario) {
      if (strtolower($usuario['nome']) === strtolower($nome) && $usuario['cpf'] === $cpf) {
        $mensagem = "Cadastro localizado.";
        $resultados[] = $usuario;
      }
    }

    if (empty($resultados)) {
      $mensagem = "Nenhum certificado encontrado com os dados informados.";
    }
  }
}
?>

<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Consulta de Inscrição e Certificados</title>
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
      margin-top: 80px;
      margin-bottom: 30px;
    }
  </style>
</head>
<body class="bg-light">

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-custom fixed-top">
  <div class="container">
    <a class="navbar-brand" href="index.php">
      <img src="imagens/logo-unialfa.png" height="40" alt="UniALFA Logo">
    </a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav ms-auto">
        <li class="nav-item">
          <a class="nav-link" href="index.php#inicio">Início</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="index.php#eventos">Eventos</a>
        </li>
        <li class="nav-item">
          <a class="nav-link active" href="consulta.php">Consultar</a>
        </li>
      </ul>
    </div>
  </div>
</nav>

<div class="container mt-5 pt-5">
  <h1 class="mb-4 text-center">Consulta</h1>
  <ul class="nav nav-tabs mb-4 justify-content-center">
    <li class="nav-item">
      <a class="nav-link <?php echo $tipo === 'certificado' ? '' : 'active'; ?>" href="?tipo=inscricao">Consultar por Inscrição</a>
    </li>
    <li class="nav-item">
      <a class="nav-link <?php echo $tipo === 'certificado' ? 'active' : ''; ?>" href="?tipo=certificado">Consultar Certificado</a>
    </li>
  </ul>

  <?php if ($tipo === 'certificado'): ?>
    <form method="POST">
      <div class="mb-3">
        <label for="nome">Nome completo</label>
        <input type="text" class="form-control" name="nome" required>
      </div>
      <div class="mb-3">
        <label for="cpf">CPF</label>
        <input type="text" class="form-control" name="cpf" required>
      </div>
      <button type="submit" class="btn btn-primary">Buscar</button>
    </form>
  <?php else: ?>
    <form method="POST">
      <div class="mb-3">
        <label for="inscricao">Número de Inscrição</label>
        <input type="text" class="form-control" name="inscricao" required>
      </div>
      <button type="submit" class="btn btn-primary">Buscar</button>
    </form>
  <?php endif; ?>

  <?php if (!empty($mensagem)): ?>
    <div class="alert alert-info mt-4"><?php echo $mensagem; ?></div>
  <?php endif; ?>

  <?php if (!empty($resultados)): ?>
    <div class="mt-4">
      <h4>Resultado(s) Encontrado(s)</h4>
      <ul class="list-group">
        <?php foreach ($resultados as $usuario): ?>
          <li class="list-group-item">
            <strong>Nome:</strong> <?php echo $usuario['nome']; ?><br>
            <strong>CPF:</strong> <?php echo $usuario['cpf']; ?><br>
            <strong>Email:</strong> <?php echo $usuario['email']; ?><br>
            <strong>Telefone:</strong> <?php echo $usuario['telefone']; ?><br>
            <strong>Evento ID:</strong> <?php echo $usuario['evento_id']; ?><br>
            <strong>Inscrição:</strong> <?php echo $usuario['inscricao']; ?>
            <?php if ($tipo === 'certificado'): ?>
              <br><a href="certificado.php?inscricao=<?php echo $usuario['inscricao']; ?>" class="btn btn-sm btn-success mt-2">Gerar Certificado</a>
            <?php endif; ?>
          </li>
        <?php endforeach; ?>
      </ul>
    </div>
  <?php endif; ?>
</div>

<footer class="bg-primary text-white text-center py-3 mt-5">
  UniALFA &copy; <?php echo date('Y'); ?> - Sistema de Eventos
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
