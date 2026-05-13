package cinematch.util;

/**
 * Contrato para geração de números aleatórios. Mockável nos testes para
 * garantir resultados determinísticos.
 */
public interface GeradorAleatorio {

	int sortearInteiro(int min, int max);
}
