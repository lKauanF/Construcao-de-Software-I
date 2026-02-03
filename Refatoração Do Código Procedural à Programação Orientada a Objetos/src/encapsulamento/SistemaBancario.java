package encapsulamento;

public class SistemaBancario {
    public static void main(String[] args) {
        ContaBancaria conta = new ContaBancaria(1000.0);

        conta.depositar(500);
        conta.sacar(200);

        System.out.println("Saldo: " + conta.getSaldo());
    }
}
