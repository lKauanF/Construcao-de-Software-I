import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class PedidoController {

    private final PedidoService service;
    private final Scanner scanner;

    public PedidoController(PedidoService service) {
        if (service == null) {
            throw new IllegalArgumentException("Service nao pode ser nulo.");
        }
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void executar() {
        int opcao;
        do {
            mostrarMenu();
            opcao = lerInteiro("Escolha uma opcao: ");

            try {
                switch (opcao) {
                    case 1:
                        cadastrarPedido();
                        break;
                    case 2:
                        listarTodos();
                        break;
                    case 3:
                        buscarPorId();
                        break;
                    case 4:
                        atualizarPedido();
                        break;
                    case 5:
                        marcarComoPago();
                        break;
                    case 6:
                        marcarComoEnviado();
                        break;
                    case 7:
                        cancelarPedido();
                        break;
                    case 8:
                        removerPedido();
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opcao invalida.");
                        break;
                }
            } catch (Exception ex) {
                System.out.println("Erro: " + ex.getMessage());
            }

            System.out.println();
        } while (opcao != 0);
    }

    private void mostrarMenu() {
        System.out.println("=== SISTEMA DE GESTAO DE PEDIDOS ===");
        System.out.println("1. Cadastrar Pedido");
        System.out.println("2. Listar Todos os Pedidos");
        System.out.println("3. Buscar Pedido por ID");
        System.out.println("4. Atualizar Pedido");
        System.out.println("5. Marcar como Pago");
        System.out.println("6. Marcar como Enviado");
        System.out.println("7. Cancelar Pedido");
        System.out.println("8. Remover Pedido");
        System.out.println("0. Sair");
    }

    private void cadastrarPedido() {
        String cliente = lerString("Cliente: ");
        String descricao = lerString("Descricao: ");
        double valor = lerDouble("Valor: ");

        Pedido pedido = service.cadastrarPedido(cliente, descricao, valor);
        System.out.println("Pedido cadastrado com sucesso. ID: " + pedido.getId());
    }

    private void listarTodos() {
        List<Pedido> pedidos = service.listarPedidos();

        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido cadastrado.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        System.out.println("ID | Cliente | Valor | Status | Data");
        for (Pedido p : pedidos) {
            System.out.printf(
                    "%d | %s | %.2f | %s | %s%n",
                    p.getId(),
                    p.getCliente(),
                    p.getValor(),
                    p.getStatus(),
                    p.getDataCriacao().format(formatter)
            );
        }
    }

    private void buscarPorId() {
        int id = lerInteiro("ID do pedido: ");
        Optional<Pedido> pedidoOpt = service.buscarPedidoPorId(id);

        if (!pedidoOpt.isPresent()) {
            System.out.println("Pedido nao encontrado.");
            return;
        }

        System.out.println(pedidoOpt.get());
    }

    private void atualizarPedido() {
        int id = lerInteiro("ID do pedido: ");
        String cliente = lerString("Novo cliente: ");
        String descricao = lerString("Nova descricao: ");
        double valor = lerDouble("Novo valor: ");

        boolean ok = service.atualizarPedido(id, cliente, descricao, valor);
        System.out.println(ok ? "Pedido atualizado com sucesso." : "Nao foi possivel atualizar o pedido.");
    }

    private void marcarComoPago() {
        int id = lerInteiro("ID do pedido: ");
        Pedido pedido = service.marcarComoPago(id);
        System.out.println("Pedido marcado como PAGO. Status atual: " + pedido.getStatus());
    }

    private void marcarComoEnviado() {
        int id = lerInteiro("ID do pedido: ");
        Pedido pedido = service.marcarComoEnviado(id);
        System.out.println("Pedido marcado como ENVIADO. Status atual: " + pedido.getStatus());
    }

    private void cancelarPedido() {
        int id = lerInteiro("ID do pedido: ");
        Pedido pedido = service.cancelarPedido(id);
        System.out.println("Pedido cancelado. Status atual: " + pedido.getStatus());
    }

    private void removerPedido() {
        int id = lerInteiro("ID do pedido: ");
        String confirmacao = lerString("Confirmar remocao? (s/n): ").trim().toLowerCase();

        if (!confirmacao.equals("s")) {
            System.out.println("Remocao cancelada.");
            return;
        }

        boolean ok = service.removerPedido(id);
        System.out.println(ok ? "Pedido removido com sucesso." : "Nao foi possivel remover o pedido.");
    }

    private int lerInteiro(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException ex) {
                System.out.println("Valor invalido. Digite um numero inteiro.");
            }
        }
    }

    private double lerDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input.trim().replace(",", "."));
            } catch (NumberFormatException ex) {
                System.out.println("Valor invalido. Digite um numero decimal.");
            }
        }
    }

    private String lerString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}