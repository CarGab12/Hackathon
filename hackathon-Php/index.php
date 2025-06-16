<?php
$eventosJson = @file_get_contents("http://localhost:3000/api/eventos");

if ($eventosJson === false) {
    $eventos = [];
    $erroConexao = "Não foi possível conectar ao servidor de eventos.";
} else {
    $eventos = json_decode($eventosJson, true);
    if (!is_array($eventos)) {
        $eventos = [];
        $erroConexao = "Resposta inválida do servidor de eventos.";
    }
}

$cursoSelecionado = $_GET['curso'] ?? '';

$cursos = [];
foreach ($eventos as $evento) {
    if (!empty($evento['curso_nome']) && !in_array($evento['curso_nome'], $cursos)) {
        $cursos[] = $evento['curso_nome'];
    }
}

if ($cursoSelecionado === 'sem-curso') {
    $eventosFiltrados = array_filter($eventos, function ($evento) {
        return empty($evento['curso_nome']);
    });
} elseif ($cursoSelecionado !== '' && $cursoSelecionado !== 'todos') {
    $eventosFiltrados = array_filter($eventos, function ($evento) use ($cursoSelecionado) {
        return ($evento['curso_nome'] ?? '') === $cursoSelecionado;
    });
} else {
    $eventosFiltrados = $eventos;
}
?>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>UniALFA Eventos</title>
    <link rel="stylesheet" href="styleIndex.css" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body class="bg-light">

<nav class="navbar navbar-expand-lg navbar-custom fixed-top shadow-sm">
    <div class="container">
        <a class="navbar-brand" href="#inicio">
            <img src="imagens/logo.png" height="50" alt="UniALFA Logo" />
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link" href="#inicio">Início</a></li>
                <li class="nav-item"><a class="nav-link" href="#eventos">Eventos</a></li>
                <li class="nav-item"><a class="nav-link" href="consulta.php?tipo=inscricao">Consultar</a></li>
            </ul>
        </div>
    </div>
</nav>

<?php if (!empty($erroConexao)): ?>
    <div class="alert alert-warning text-center mt-5" role="alert" style="margin-top: 80px;">
        <?php echo htmlspecialchars($erroConexao); ?>
    </div>
<?php endif; ?>

<section id="inicio" style="margin-top: 60px;">
    <div id="carouselEventos" class="carousel slide" data-bs-ride="carousel">
        <div class="carousel-inner rounded-3 shadow-sm">
            <?php foreach ($eventos as $index => $evento): ?>
                <div class="carousel-item <?php echo $index === 0 ? 'active' : ''; ?>">
                    <a href="evento.php?evento=<?php echo htmlspecialchars($evento['id']); ?>" style="display: block; position: relative; text-decoration: none; color: inherit;">
                        <img src="imagens/<?php echo htmlspecialchars($evento['fotos']); ?>" class="d-block w-100" alt="<?php echo htmlspecialchars($evento['nomeEvento']); ?>" />
                        <div class="carousel-caption">
                            <?php echo htmlspecialchars(strtoupper($evento['nomeEvento'])); ?>
                        </div>
                    </a>
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

<div class="separator-line"></div>

<section id="eventos" class="container mt-5">
    <h2 class="text-center section-title">Eventos Disponíveis</h2>

    <form method="GET" class="mb-4" role="search" aria-label="Filtro de cursos">
        <label for="curso" class="form-label">Filtrar por Curso:</label>
        <select name="curso" id="curso" class="form-select" onchange="this.form.submit()">
            <option value="todos" <?php if ($cursoSelecionado === 'todos' || $cursoSelecionado === '') echo 'selected'; ?>>Todos</option>
            <option value="sem-curso" <?php if ($cursoSelecionado === 'sem-curso') echo 'selected'; ?>>Sem Curso: Não vinculado a um curso</option>
            <?php foreach ($cursos as $curso): ?>
                <option value="<?php echo htmlspecialchars($curso); ?>" <?php if ($cursoSelecionado === $curso) echo 'selected'; ?>>
                    <?php echo htmlspecialchars($curso); ?>
                </option>
            <?php endforeach; ?>
        </select>
    </form>

    <div class="row">
        <?php if (count($eventosFiltrados) === 0): ?>
            <p class="text-center fs-5 text-muted">Nenhum evento encontrado para o curso selecionado.</p>
        <?php endif; ?>

        <?php foreach ($eventosFiltrados as $evento): ?>
            <div class="col-md-4 mb-4">
                <div class="card card-evento shadow-sm rounded">
                    <img src="imagens/<?php echo htmlspecialchars($evento['fotos']); ?>" class="card-img-top rounded-top" alt="<?php echo htmlspecialchars($evento['nomeEvento']); ?>" />
                    <div class="card-body">
                        <h5 class="card-title"><?php echo htmlspecialchars($evento['nomeEvento']); ?></h5>
                        <p class="card-text"><?php echo htmlspecialchars(substr($evento['descricao'], 0, 100)); ?>...</p>
                        <a href="evento.php?evento=<?php echo htmlspecialchars($evento['id']); ?>" class="btn btn-primary fw-semibold">Ver mais</a>
                    </div>
                </div>
            </div>
        <?php endforeach; ?>
    </div>
</section>

<footer class="bg-primary text-white text-center py-3 mt-5">
    UniALFA &copy; <?php echo date('Y'); ?> - Sistema de Eventos
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    window.addEventListener('load', () => {
        const eventosSection = document.getElementById('eventos');
        eventosSection.classList.add('visible');
    });
</script>
</body>
</html>
