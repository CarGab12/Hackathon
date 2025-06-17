<?php
$inscricao = $_GET['inscricao'] ?? null;

if (!$inscricao) {
    die("Número de inscrição não informado.");
}

$apiUrl = "http://localhost:3000/api/certificado/" . urlencode($inscricao);

$response = file_get_contents($apiUrl);

if ($response === false) {
    die("Erro ao conectar com a API.");
}

$data = json_decode($response, true);

if (!$data) {
    die("Resposta inválida da API.");
}

if (isset($data['erro'])) {
    die("<h3>" . htmlspecialchars($data['erro']) . "</h3>");
}

$dataEvento = date("d/m/Y", strtotime($data['data_hora']));

?>

<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8" />
  <title>Certificado de Participação</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      max-width: 700px;
      margin: 50px auto;
      padding: 30px;
      border: 1px solid #ccc;
      text-align: center;
    }
    h2 {
      margin-bottom: 40px;
    }
    .assinatura {
      margin-top: 80px;
      font-weight: bold;
    }
  </style>
</head>
<body>
  <h2>Certificado de Participação</h2>
  <p>Certificamos que <strong><?= htmlspecialchars($data['aluno']) ?></strong> participou do evento <strong><?= htmlspecialchars($data['evento']) ?></strong>, realizado em <strong><?= htmlspecialchars($data['local']) ?></strong> no dia <strong><?= $dataEvento ?></strong>.</p>

  <div class="assinatura">
    ____________________________________<br>
    Organização do Evento
  </div>
</body>
</html>
