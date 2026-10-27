import java.util.Random;

public class TabelaRehash {

    static final int TAMANHO = 100;
    static final int QUANTIDADE = 100;
    static final int SEED = 1090;
    static final int LIMITE = 100000;
    static final int VAZIO = -1;
    static final int LIMITE_OCUPACAO = (TAMANHO * 90) / 100; // 90 posicoes

    public static void main(String[] args) {
        int[] matriz = new int[TAMANHO];
        for (int i = 0; i < TAMANHO; i++) {
            matriz[i] = VAZIO;
        }

        Random random = new Random(SEED);

        int ocupadas = 0;
        int totalColisoes = 0;
        int rejeitados = 0;

        // Os 3 elementos que mais colidiram ate serem inseridos
        int top1Valor = -1, top1Colisoes = -1, top1Posicao = -1;
        int top2Valor = -1, top2Colisoes = -1, top2Posicao = -1;
        int top3Valor = -1, top3Colisoes = -1, top3Posicao = -1;

        System.out.println("===== INSERCAO =====");

        for (int i = 0; i < QUANTIDADE; i++) {
            int valor = random.nextInt(LIMITE + 1); // 0 a 100 000

            // Rehash impede a insercao quando a matriz chega a 90%
            if (ocupadas >= LIMITE_OCUPACAO) {
                rejeitados++;
                System.out.println("Elemento " + (i + 1) + ": " + valor
                        + " -> NAO INSERIDO (matriz com 90% de ocupacao)");
            } else {
                int posicao = valor % TAMANHO;
                int colisoes = 0;

                // Rehash linear: enquanto a posicao estiver ocupada, tenta a proxima
                while (matriz[posicao] != VAZIO) {
                    colisoes++;
                    posicao = (posicao + 1) % TAMANHO;
                }

                matriz[posicao] = valor;
                ocupadas++;
                totalColisoes = totalColisoes + colisoes;

                System.out.println("Elemento " + (i + 1) + ": " + valor
                        + " -> posicao " + posicao + " (colisoes: " + colisoes + ")");

                // Atualiza o ranking dos 3 que mais colidiram
                if (colisoes > top1Colisoes) {
                    top3Valor = top2Valor; top3Colisoes = top2Colisoes; top3Posicao = top2Posicao;
                    top2Valor = top1Valor; top2Colisoes = top1Colisoes; top2Posicao = top1Posicao;
                    top1Valor = valor;     top1Colisoes = colisoes;     top1Posicao = posicao;
                } else if (colisoes > top2Colisoes) {
                    top3Valor = top2Valor; top3Colisoes = top2Colisoes; top3Posicao = top2Posicao;
                    top2Valor = valor;     top2Colisoes = colisoes;     top2Posicao = posicao;
                } else if (colisoes > top3Colisoes) {
                    top3Valor = valor;     top3Colisoes = colisoes;     top3Posicao = posicao;
                }
            }
        }

        // ---------- Resultados ----------
        System.out.println();
        System.out.println("===== RESULTADOS =====");
        System.out.println("Elementos inseridos: " + ocupadas);
        System.out.println("Elementos bloqueados pelo limite de 90%: " + rejeitados);
        System.out.println("Numero total de colisoes: " + totalColisoes);

        System.out.println();
        System.out.println("3 elementos que mais colidiram ate a insercao:");
        System.out.println("1o: " + top1Valor + " (" + top1Colisoes + " colisoes, posicao final " + top1Posicao + ")");
        System.out.println("2o: " + top2Valor + " (" + top2Colisoes + " colisoes, posicao final " + top2Posicao + ")");
        System.out.println("3o: " + top3Valor + " (" + top3Colisoes + " colisoes, posicao final " + top3Posicao + ")");

        System.out.println();
        System.out.print("Posicoes livres na matriz: ");
        int livres = 0;
        for (int i = 0; i < TAMANHO; i++) {
            if (matriz[i] == VAZIO) {
                if (livres > 0) {
                    System.out.print(", ");
                }
                System.out.print(i);
                livres++;
            }
        }
        System.out.println();
        System.out.println("Total de posicoes livres: " + livres);

        // ---------- Teste de busca ----------
        System.out.println();
        System.out.println("===== TESTE DE BUSCA =====");
        buscar(matriz, top1Valor);
        buscar(matriz, top2Valor);
        buscar(matriz, top3Valor);

        System.out.println();
        System.out.println("===== MATRIZ FINAL =====");
        for (int i = 0; i < TAMANHO; i++) {
            if (matriz[i] == VAZIO) {
                System.out.println("[" + i + "] (livre)");
            } else {
                System.out.println("[" + i + "] " + matriz[i]);
            }
        }
    }

    // Busca usando o mesmo rehash da insercao
    static void buscar(int[] matriz, int valor) {
        int posicao = valor % TAMANHO;
        int tentativas = 1;

        while (matriz[posicao] != VAZIO && matriz[posicao] != valor && tentativas < TAMANHO) {
            posicao = (posicao + 1) % TAMANHO;
            tentativas++;
        }

        if (matriz[posicao] == valor) {
            System.out.println("Valor " + valor + " ENCONTRADO na posicao " + posicao
                    + " (hash inicial " + (valor % TAMANHO) + ", " + tentativas + " tentativas)");
        } else {
            System.out.println("Valor " + valor + " NAO encontrado (" + tentativas + " tentativas)");
        }
    }
}