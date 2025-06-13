<?php
$evento_id = $_GET['evento'] ?? null;

if (empty($evento_id)) {
  die("<h3 style='text-align: center;'>Evento não especificado. Use ?evento=1 na URL.</h3>");
}

$eventoJson = file_get_contents("http://localhost:3000/api/eventos/$evento_id");
$evento = json_decode($eventoJson, true);

$mensagem = $_GET['inscrito'] ?? "";

if ($_SERVER["REQUEST_METHOD"] === "POST") {
  $dados = [
    "nome" => $_POST["nome"],
    "cpf" => $_POST["cpf"],
    "email" => $_POST["email"],
    "telefone" => $_POST["telefone"],
    "evento_id" => $_POST["evento_id"]
  ];

  $json = json_encode($dados);
  $ch = curl_init("http://localhost:3000/api/usuarios");

  curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
  curl_setopt($ch, CURLOPT_POST, true);
  curl_setopt($ch, CURLOPT_HTTPHEADER, ["Content-Type: application/json"]);
  curl_setopt($ch, CURLOPT_POSTFIELDS, $json);

  $resposta = curl_exec($ch);
  $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
  curl_close($ch);

  if ($httpCode === 201) {
    $usuario = json_decode($resposta, true);
    $mensagem = $usuario && isset($usuario['inscricao'])
      ? "Inscrição realizada com sucesso! Número: {$usuario['inscricao']}"
      : "Inscrição realizada, mas não foi possível recuperar o número.";

    header("Location: evento.php?evento=$evento_id&inscrito=" . urlencode($mensagem));
    exit;
  } elseif ($httpCode === 400) {
    $usuariosJson = file_get_contents("http://localhost:3000/api/usuarios");
    $usuarios = json_decode($usuariosJson, true);

    foreach ($usuarios as $usuario) {
      if ($usuario["cpf"] === $_POST["cpf"] && $usuario["evento_id"] == $_POST["evento_id"]) {
        $mensagem = "Este CPF já está inscrito neste evento. Número: {$usuario['inscricao']}";
        break;
      }
    }
    header("Location: evento.php?evento=$evento_id&inscrito=" . urlencode($mensagem));
    exit;
  } else {
    $mensagem = "Erro ao se inscrever. Código: $httpCode";
    header("Location: evento.php?evento=$evento_id&inscrito=" . urlencode($mensagem));
    exit;
  }
}
?>

<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title><?php echo htmlspecialchars($evento['nomeEvento']); ?></title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body class="bg-light">

<header class="navbar navbar-expand-lg fixed-top bg-primary">
  <div class="container">
    <span class="navbar-brand text-white">UniALFA Eventos</span>
  </div>
</header>

<div class="mt-5 pt-4"></div>

<div class="container my-4">
  <img src="imagens/<?php echo htmlspecialchars($evento['fotos']); ?>" class="img-fluid rounded mb-3" alt="Imagem do Evento" />
  <h1><?php
  if ($evento['nomeEvento'] == ""){
    header("Location: index.php");
  }else{
    echo htmlspecialchars($evento['nomeEvento']);
  } ?></h1>
  <p><strong>Data:</strong> <?php echo date("d/m/Y H:i", strtotime($evento['data_hora'])); ?></p>
  <p><strong>Local:</strong> <?php echo htmlspecialchars($evento['local']); ?></p>
  <p><?php echo htmlspecialchars($evento['descricao']); ?></p>

  <h2 class="mt-5">Inscreva-se</h2>
  <form method="POST" action="">
    <input type="hidden" name="evento_id" value="<?php echo htmlspecialchars($evento_id); ?>" />

    <div class="mb-3">
      <label>Nome</label>
      <input type="text" name="nome" class="form-control" required />
    </div>
    <div class="mb-3">
      <label>CPF</label>
      <input type="text" name="cpf" class="form-control" required />
    </div>
    <div class="mb-3">
      <label>Email</label>
      <input type="email" name="email" class="form-control" required />
    </div>
    <div class="mb-3">
      <label>Telefone</label>
      <input type="text" name="telefone" class="form-control" required />
    </div>
    <button type="submit" class="btn btn-primary">Enviar Inscrição</button>
  </form>
</div>

<div class="modal fade" id="mensagemModal" tabindex="-1" aria-labelledby="mensagemModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="mensagemModalLabel">Mensagem</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Fechar"></button>
      </div>
      <div class="modal-body" id="mensagemConteudo">
      </div>
      <div class="modal-footer">
        <button type="button" id="botaoVoltar" class="btn btn-primary">Voltar</button>
      </div>
    </div>
  </div>
</div>

<footer class="bg-primary text-white text-center py-3">
  desenvolvido no Hackathon &copy; 2025
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
  document.addEventListener("DOMContentLoaded", function () {
    const mensagem = <?php echo json_encode($mensagem); ?>;
    const eventoId = <?php echo json_encode($evento_id); ?>;

    if (mensagem && mensagem.trim() !== "") {
      document.getElementById("mensagemConteudo").textContent = mensagem;

      document.getElementById("botaoVoltar").addEventListener("click", function () {
        window.location.href = "http://localhost/hackathon-Php/";
      });

      const modal = new bootstrap.Modal(document.getElementById("mensagemModal"));
      modal.show();

      const url = new URL(window.location);
      url.searchParams.delete("inscrito");
      window.history.replaceState(null, "", url.toString());
    }
  });
</script>
</body>
</html>
