package polimorfismo;

public class FreteSedex implements Frete {
    @Override
    public double calcular(double peso) {
        if (peso <= 0) {
            throw new IllegalArgumentException("Peso deve ser maior que zero.");
        }
        return peso * 10;
    }
}
