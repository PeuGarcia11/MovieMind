package moviemind.util;

/**
 * Contrato para geração de números aleatórios.
 * Mockável nos testes para garantir resultados determinísticos.
 */
public interface GeradorAleatorio {

    /**
     * Sorteia um inteiro no intervalo fechado [min, max].
     *
     * @param min limite inferior (inclusive)
     * @param max limite superior (inclusive)
     * @return número sorteado
     */
    int sortearInteiro(int min, int max);
}
