package polimorfismo;

public class FretePac implements Frete {
    @Override
    public double calcular(double peso) {
        if (peso <= 0) {
            throw new IllegalArgumentException("Peso deve ser maior que zero.");
        }
        return peso * 5;
    }
}
