<?php
$tipo = $_GET['tipo'] ?? null;
$mensagem = "";
$resultados = [];

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $usuariosJson = file_get_contents("http://localhost:3000/api/usuarios");
    $usuarios = json_decode($usuariosJson, true);

    $eventosJson = file_get_contents("http://localhost:3000/api/eventos");
    $eventos = json_decode($eventosJson, true);

    if ($tipo === 'inscricao') {
        $cpf = $_POST['cpf'] ?? '';

        foreach ($usuarios as $usuario) {
            if ($usuario['cpf'] === $cpf) {
                $eventoUsuario = null;
                foreach ($eventos as $evento) {
                    if ($evento['id'] == $usuario['evento_id']) {
                        $eventoUsuario = $evento;
                        break;
                    }
                }
                $usuario['evento'] = $eventoUsuario;
                $resultados[] = $usuario;
            }
        }

        $mensagem = empty($resultados)
            ? "Nenhuma inscrição encontrada para este CPF."
            : count($resultados) . " inscrição(ões) encontrada(s) para este CPF.";
    } elseif ($tipo === 'certificado') {
        $nome = $_POST['nome'] ?? '';
        $cpf = $_POST['cpf'] ?? '';
        $inscricao = $_POST['inscricao'] ?? '';

        foreach ($usuarios as $usuario) {
            if (
                strtolower(trim($usuario['nome'])) === strtolower(trim($nome)) &&
                $usuario['cpf'] === $cpf &&
                $usuario['inscricao'] === $inscricao
            ) {
                $eventoUsuario = null;
                foreach ($eventos as $evento) {
                    if ($evento['id'] == $usuario['evento_id']) {
                        $eventoUsuario = $evento;
                        break;
                    }
                }
                $usuario['evento'] = $eventoUsuario;
                $resultados[] = $usuario;
                $mensagem = "Cadastro localizado.";
                break;
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
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Consulta de Inscrição e Certificados</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="styleConsulta.css" />
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-custom fixed-top">
    <div class="container">
        <a class="navbar-brand" href="index.php">
            <img src="imagens/logo.png" height="40" alt="UniALFA Logo" />
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

<div class="container mt-5 pt-5">
    <h1 class="mb-4 text-center">CONSULTA</h1>
    <ul class="nav nav-tabs mb-4 justify-content-center">
        <li class="nav-item">
            <a class="nav-link <?php echo $tipo === 'certificado' ? '' : 'active'; ?>" href="?tipo=inscricao">Consultar por CPF</a>
        </li>
        <li class="nav-item">
            <a class="nav-link <?php echo $tipo === 'certificado' ? 'active' : ''; ?>" href="?tipo=certificado">Consultar Certificado</a>
        </li>
    </ul>

    <?php if ($tipo === 'certificado'): ?>
        <form method="POST">
            <div class="mb-3">
                <label for="nome">Nome completo</label>
                <input type="text" class="form-control" name="nome" required />
            </div>
            <div class="mb-3">
                <label for="cpf">CPF</label>
                <input type="text" class="form-control" name="cpf" required />
            </div>
            <div class="mb-3">
                <label for="inscricao">Número da Inscrição</label>
                <input type="text" class="form-control" name="inscricao" required />
            </div>
            <button type="submit" class="btn btn-primary">Buscar</button>
        </form>
    <?php else: ?>
        <form method="POST">
            <div class="mb-3">
                <label for="cpf">CPF</label>
                <input type="text" class="form-control" name="cpf" required />
            </div>
            <button type="submit" class="btn btn-primary">Buscar</button>
        </form>
    <?php endif; ?>

    <?php if (!empty($mensagem)): ?>
        <div class="alert alert-info mt-4"><?php echo htmlspecialchars($mensagem); ?></div>
    <?php endif; ?>

    <?php if (!empty($resultados)): ?>
        <div class="mt-4">
            <h4>Resultado(s) Encontrado(s)</h4>
            <ul class="list-group">
                <?php foreach ($resultados as $usuario): ?>
                    <li class="list-group-item">
                        <strong>Nome:</strong> <?php echo htmlspecialchars($usuario['nome']); ?><br />
                        <strong>CPF:</strong> <?php echo htmlspecialchars($usuario['cpf']); ?><br />
                        <strong>Email:</strong> <?php echo htmlspecialchars($usuario['email']); ?><br />
                        <strong>Telefone:</strong> <?php echo htmlspecialchars($usuario['telefone']); ?><br />

                        <?php if (!empty($usuario['evento'])): ?>
                            <strong>Evento:</strong> <?php echo htmlspecialchars($usuario['evento']['nomeEvento']); ?><br />
                            <strong>Data:</strong> <?php echo date("d/m/Y H:i", strtotime($usuario['evento']['data_hora'])); ?><br />
                            <strong>Local:</strong> <?php echo htmlspecialchars($usuario['evento']['local']); ?><br />
                        <?php else: ?>
                            <em>Evento não encontrado.</em><br />
                        <?php endif; ?>

                        <strong>Inscrição:</strong> <?php echo htmlspecialchars($usuario['inscricao']); ?><br />

                        <?php if ($tipo === 'certificado'): ?>
                            <a href="certificado.php?inscricao=<?php echo urlencode($usuario['inscricao']); ?>" class="btn btn-sm btn-success mt-2">Gerar Certificado</a>
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
