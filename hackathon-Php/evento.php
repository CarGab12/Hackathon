<?php
$evento_id = $_GET['evento'] ?? null;

if (empty($evento_id)) {
    die("<h3 style='text-align: center; margin-top: 100px;'>Evento não especificado. Use ?evento=1 na URL.</h3>");
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
        $mensagem = "Inscrição realizada com sucesso!";
        header("Location: evento.php?evento=$evento_id&inscrito=" . urlencode($mensagem));
        exit;
    } elseif ($httpCode === 400) {
        $erro = json_decode($resposta, true);
        $mensagemApi = $erro['mensagem'] ?? "";

        $mensagem = $mensagemApi !== "" ? htmlspecialchars($mensagemApi) : "Este CPF já está inscrito nesse evento ou em um evento neste horário";
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
    <link rel="stylesheet" href="styleEvento.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-custom fixed-top shadow-sm">
    <div class="container">
        <a class="navbar-brand" href="index.php">
            <img src="imagens/logo.png" height="40" alt="UniALFA Logo">
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link" href="index.php#inicio">Início</a></li>
                <li class="nav-item"><a class="nav-link" href="index.php#eventos">Eventos</a></li>
                <li class="nav-item"><a class="nav-link" href="consulta.php?tipo=inscricao">Consultar</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container my-5">
    <div class="card-evento">
        <?php if (!empty($evento['fotos'])): ?>
            <img src="imagens/<?php echo htmlspecialchars($evento['fotos']); ?>" alt="Imagem do Evento" class="img-evento mb-4" />
        <?php endif; ?>

        <h1 class="mb-3"><?php echo htmlspecialchars($evento['nomeEvento']); ?></h1>

        <p><strong>Data:</strong> <?php echo date("d/m/Y H:i", strtotime($evento['data_hora'])); ?></p>
        <p><strong>Local:</strong> <?php echo htmlspecialchars($evento['local']); ?></p>
        <p><?php echo nl2br(htmlspecialchars($evento['descricao'])); ?></p>
        <p><strong>Palestrante:</strong>
            <?php 
            echo !empty($evento['palestrante_nome']) 
                ? htmlspecialchars($evento['palestrante_nome']) . " - " . htmlspecialchars($evento['minicurriculo']) 
                : "Sem palestrante principal";
            ?>
        </p>
        <p><strong>Curso:</strong>
            <?php 
            echo !empty($evento['curso_nome']) 
                ? htmlspecialchars($evento['curso_nome']) 
                : "Não vinculado a um curso.";
            ?>
        </p>

        <hr class="my-4" />

        <h2 class="mb-3">Inscreva-se</h2>
        <form method="POST" action="">
            <input type="hidden" name="evento_id" value="<?php echo htmlspecialchars($evento_id); ?>" />

            <div class="mb-3">
                <label for="nome" class="form-label">Nome</label>
                <input type="text" id="nome" name="nome" class="form-control" required />
            </div>
            <div class="mb-3">
                <label for="cpf" class="form-label">CPF</label>
                <input type="text" id="cpf" name="cpf" class="form-control" required />
            </div>
            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" id="email" name="email" class="form-control" required />
            </div>
            <div class="mb-3">
                <label for="telefone" class="form-label">Telefone</label>
                <input type="text" id="telefone" name="telefone" class="form-control" required />
            </div>

            <button type="submit" class="btn btn-primary">Enviar Inscrição</button>
        </form>
    </div>
</div>

<div class="modal fade" id="mensagemModal" tabindex="-1" aria-labelledby="mensagemModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="mensagemModalLabel">Inscrição</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body" id="mensagemConteudo"></div>
            <div class="modal-footer">
                <button type="button" id="botaoVoltar" class="btn btn-primary">Voltar</button>
            </div>
        </div>
    </div>
</div>

<footer class="bg-primary text-white text-center py-3 mt-5">
    UniALFA &copy; <?php echo date('Y'); ?> - Sistema de Eventos
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const mensagem = <?php echo json_encode($mensagem, JSON_HEX_TAG | JSON_HEX_AMP | JSON_HEX_APOS | JSON_HEX_QUOT); ?>;

        if (mensagem && mensagem.trim() !== "") {
            document.getElementById("mensagemConteudo").innerHTML = mensagem;

            document.getElementById("botaoVoltar").addEventListener("click", function () {
                window.location.href = "index.php#eventos";
            });

            const modalEl = document.getElementById("mensagemModal");
            const modal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
            modal.show();

            const url = new URL(window.location);
            url.searchParams.delete("inscrito");
            window.history.replaceState(null, "", url.toString());
        }
    });
</script>
</body>
</html>
