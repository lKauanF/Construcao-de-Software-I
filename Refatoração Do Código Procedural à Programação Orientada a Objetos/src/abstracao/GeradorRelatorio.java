package abstracao;

public class GeradorRelatorio {
    public static void main(String[] args) {
        Relatorio relatorio1 = new RelatorioPDF();
        Relatorio relatorio2 = new RelatorioHTML();

        relatorio1.gerar();
        relatorio2.gerar();
    }
}
