package abstracao;

public class RelatorioPDF implements Relatorio {
    @Override
    public void gerar() {
        System.out.println("Gerando relatório em PDF");
    }
}
