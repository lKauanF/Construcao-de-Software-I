import java.util.List;
import java.util.Optional;

public interface PedidoRepository {

    Pedido salvar(Pedido pedido);

    List<Pedido> listarTodos();

    Optional<Pedido> buscarPorId(int id);

    boolean atualizar(Pedido pedido);

    boolean deletar(int id);

    int contar();
}