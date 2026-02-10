

import controller.LivroController;
import entity.Livro;
import repository.LivroRepository;
import repository.LivroRepositoryMemoria;
import service.LivroService;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        LivroRepository livroRepository = new LivroRepositoryMemoria();
        LivroService livroService = new LivroService(livroRepository);
        LivroController livroController = new LivroController(livroService);

        while (true) {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Salvar livro");
            System.out.println("2. Listar livros");
            System.out.println("3. Buscar livro por ID");
            System.out.println("4. Deletar livro");
            System.out.println("5. Atualizar livro");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao;
            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Digite um número.");
                continue;
            }

            switch (opcao) {
                case 1:
                    cadastrarLivro(livroController);
                    break;
                case 2:
                    listarLivros(livroController);
                    break;
                case 3:
                    buscarLivroPorId(livroController);
                    break;
                case 4:
                    deletarLivro(livroController);
                    break;
                case 5:
                    atualizarLivro(livroController);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void cadastrarLivro(LivroController livroController) {
        try {
            System.out.print("Digite o título do livro: ");
            String titulo = scanner.nextLine().trim();

            System.out.print("Digite o autor do livro: ");
            String autor = scanner.nextLine().trim();

            System.out.print("Digite o ISBN (XXX-X-XXXX-XXXX-X): ");
            String isbn = scanner.nextLine().trim();

            System.out.print("Digite o ano de publicação: ");
            String anoStr = scanner.nextLine().trim();
            int ano = Integer.parseInt(anoStr);

            Livro livro = livroController.salvarLivro(titulo, autor, isbn, ano);
            System.out.println("Livro salvo: " + livro);

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar livro: " + e.getMessage());
        }
    }

    private static void listarLivros(LivroController livroController) {
        System.out.println("\nLista de livros:");
        List<Livro> livros = livroController.listarLivros();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }

        for (Livro livro : livros) {
            System.out.println(livro);
        }
    }

    private static void buscarLivroPorId(LivroController livroController) {
        try {
            System.out.print("Digite o ID do livro: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            Livro livro = livroController.buscarPorId(id);
            System.out.println("Livro encontrado: " + livro);

        } catch (Exception e) {
            System.out.println("Erro ao buscar livro: " + e.getMessage());
        }
    }

    private static void deletarLivro(LivroController livroController) {
        try {
            System.out.print("Digite o ID do livro a deletar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            boolean ok = livroController.deletarLivro(id);
            if (ok) System.out.println("Livro deletado com sucesso.");
            else System.out.println("Livro não encontrado.");

        } catch (Exception e) {
            System.out.println("Erro ao deletar livro: " + e.getMessage());
        }
    }

    private static void atualizarLivro(LivroController livroController) {
        try {
            System.out.print("Digite o ID do livro a atualizar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Digite o novo título (ou Enter para manter): ");
            String titulo = scanner.nextLine();
            if (titulo != null && titulo.trim().isEmpty()) titulo = null;

            System.out.print("Digite o novo autor (ou Enter para manter): ");
            String autor = scanner.nextLine();
            if (autor != null && autor.trim().isEmpty()) autor = null;

            System.out.print("Digite o novo ano (ou Enter para manter): ");
            String anoStr = scanner.nextLine().trim();
            Integer ano = null;
            if (!anoStr.isEmpty()) {
                ano = Integer.parseInt(anoStr);
            }

            System.out.print("Disponível? (s/n ou Enter para manter): ");
            String dispStr = scanner.nextLine().trim().toLowerCase();
            Boolean disponivel = null;
            if (!dispStr.isEmpty()) {
                if (dispStr.equals("s")) disponivel = true;
                else if (dispStr.equals("n")) disponivel = false;
                else throw new IllegalArgumentException("Valor inválido. Use s, n ou Enter.");
            }

            boolean atualizado = livroController.atualizarLivro(id, titulo, autor, ano, disponivel);
            if (atualizado) System.out.println("Livro atualizado com sucesso.");
            else System.out.println("Livro não encontrado ou não foi possível atualizar.");

        } catch (Exception e) {
            System.out.println("Erro ao atualizar livro: " + e.getMessage());
        }
    }
}
