package polimorfismo;

public class CalculadoraFrete {
    public static void main(String[] args) {
        Frete sedex = new FreteSedex();
        Frete pac = new FretePac();

        System.out.println(sedex.calcular(10));
        System.out.println(pac.calcular(10));
    }
}
