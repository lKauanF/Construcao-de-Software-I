import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PedidoService {

    private final PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository nao pode ser nulo.");
        }
        this.repository = repository;
    }

    public Pedido cadastrarPedido(String cliente, String descricao, double valor) {
        Pedido pedido = new Pedido(cliente, descricao, valor);
        return repository.salvar(pedido);
    }

    public List<Pedido> listarPedidos() {
        return repository.listarTodos()
                .stream()
                .sorted(Comparator.comparingInt(Pedido::getId))
                .collect(Collectors.toList());
    }

    public Optional<Pedido> buscarPedidoPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser maior que zero.");
        }
        return repository.buscarPorId(id);
    }

    public boolean atualizarPedido(int id, String cliente, String descricao, double valor) {
        Pedido pedido = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido nao encontrado para o ID: " + id));

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new IllegalStateException("Nao e permitido atualizar um pedido CANCELADO.");
        }

        pedido.setCliente(cliente);
        pedido.setDescricao(descricao);
        pedido.setValor(valor);

        return repository.atualizar(pedido);
    }

    public boolean removerPedido(int id) {
        Pedido pedido = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido nao encontrado para o ID: " + id));

        if (pedido.getStatus() == StatusPedido.ENVIADO) {
            throw new IllegalStateException("Nao e permitido remover um pedido ENVIADO.");
        }

        return repository.deletar(id);
    }

    public Pedido marcarComoPago(int id) {
        Pedido pedido = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido nao encontrado para o ID: " + id));

        if (pedido.getStatus() != StatusPedido.CRIADO) {
            throw new IllegalStateException("So e permitido marcar como PAGO se estiver CRIADO.");
        }

        pedido.setStatus(StatusPedido.PAGO);
        repository.atualizar(pedido);
        return pedido;
    }

    public Pedido marcarComoEnviado(int id) {
        Pedido pedido = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido nao encontrado para o ID: " + id));

        if (pedido.getStatus() != StatusPedido.PAGO) {
            throw new IllegalStateException("So e permitido marcar como ENVIADO se estiver PAGO.");
        }

        pedido.setStatus(StatusPedido.ENVIADO);
        repository.atualizar(pedido);
        return pedido;
    }

    public Pedido cancelarPedido(int id) {
        Pedido pedido = repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido nao encontrado para o ID: " + id));

        if (pedido.getStatus() == StatusPedido.ENVIADO) {
            throw new IllegalStateException("Nao e permitido cancelar um pedido ENVIADO.");
        }

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            return pedido;
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        repository.atualizar(pedido);
        return pedido;
    }
}