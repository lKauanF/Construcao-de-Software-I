import java.time.LocalDateTime;
import java.util.Objects;

public class Pedido {

    private static int nextId = 1;

    private final int id;
    private String cliente;
    private String descricao;
    private double valor;
    private StatusPedido status;
    private final LocalDateTime dataCriacao;

    public Pedido(String cliente, String descricao, double valor) {
        this.id = nextId++;
        this.dataCriacao = LocalDateTime.now();
        this.status = StatusPedido.CRIADO;

        setCliente(cliente);
        setDescricao(descricao);
        setValor(valor);
    }

    public int getId() {
        return id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        String value = (cliente == null) ? "" : cliente.trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Cliente nao pode ser nulo ou vazio.");
        }
        if (value.length() < 3) {
            throw new IllegalArgumentException("Cliente deve ter no minimo 3 caracteres.");
        }
        this.cliente = value;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        String value = (descricao == null) ? "" : descricao.trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Descricao nao pode ser nula ou vazia.");
        }
        if (value.length() < 5) {
            throw new IllegalArgumentException("Descricao deve ter no minimo 5 caracteres.");
        }
        this.descricao = value;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero.");
        }
        this.valor = valor;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setStatus(StatusPedido novoStatus) {
        if (novoStatus == null) {
            throw new IllegalArgumentException("Status nao pode ser nulo.");
        }

        if (this.status == StatusPedido.CANCELADO) {
            throw new IllegalStateException("Nao e permitido alterar status de um pedido CANCELADO.");
        }

        if (novoStatus == StatusPedido.ENVIADO && this.status != StatusPedido.PAGO) {
            throw new IllegalStateException("Nao e permitido marcar como ENVIADO se nao estiver PAGO.");
        }

        this.status = novoStatus;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", cliente='" + cliente + '\'' +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", status=" + status +
                ", dataCriacao=" + dataCriacao +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pedido)) return false;
        Pedido pedido = (Pedido) o;
        return id == pedido.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}