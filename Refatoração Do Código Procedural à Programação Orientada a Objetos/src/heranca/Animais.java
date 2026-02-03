package heranca;

public class Animais {
    public static void main(String[] args) {
        Animal cachorro = new Cachorro();
        Animal gato = new Gato();
        Animal passaro = new Passaro();

        cachorro.emitirSom();
        gato.emitirSom();
        passaro.emitirSom();

        cachorro.dormir();
    }
}
