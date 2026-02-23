public class LojaApp {

    public static void main(String[] args) {
        PedidoRepository repository = new PedidoRepositoryMemoria();
        PedidoService service = new PedidoService(repository);
        PedidoController controller = new PedidoController(service);

        controller.executar();
    }
}