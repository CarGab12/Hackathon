<?php
$inscricao = $_GET['inscricao'] ?? null;

if (!$inscricao) {
  die("Número de inscrição não informado.");
}

$conn = new mysqli("localhost", "root", "", "hackathon");
if ($conn->connect_error) {
  die("Erro ao conectar: " . $conn->connect_error);
}

$sql = "
  SELECT u.nome AS aluno, e.nomeEvento AS evento, e.local, e.data_hora
  FROM usuarios u
  JOIN eventos e ON e.id = u.evento_id
  WHERE u.inscricao = ?
";

$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $inscricao);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 0) {
  echo "<h3>Inscrição não encontrada.</h3>";
  exit;
}

$dados = $result->fetch_assoc();
$data = date("d/m/Y", strtotime($dados['data_hora']));

echo "
  <div style='max-width: 700px; margin: 50px auto; padding: 30px; border: 1px solid #ccc; font-family: sans-serif;'>
    <h2 style='text-align: center;'>Certificado de Participação</h2>
    <p style='text-align: center; margin-top: 40px;'>Certificamos que <strong>{$dados['aluno']}</strong> participou do evento <strong>{$dados['evento']}</strong>, realizado em <strong>{$dados['local']}</strong> no dia <strong>{$data}</strong>.</p>
    <br><br><br>
    <p style='text-align: center;'>____________________________________<br>Organização do Evento</p>
  </div>
";

$conn->close();
?>
