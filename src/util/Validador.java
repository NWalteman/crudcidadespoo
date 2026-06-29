package util;

import java.util.Set;

public class Validador {

  private static final Set<String> UFS_VALIDAS =
      Set.of(
          "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA",
          "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO");

  // retorna true se o cpf for valido ou estiver em branco (campo opcional)
  public static boolean cpfValido(String cpf) {
    if (cpf == null || cpf.isBlank()) return true;
    String nums = cpf.replaceAll("[^0-9]", "");
    if (nums.length() != 11) return false;
    // rejeita sequencias como 111.111.111-11
    if (nums.chars().distinct().count() == 1) return false;

    int soma = 0;
    for (int i = 0; i < 9; i++) soma += (nums.charAt(i) - '0') * (10 - i);
    int dig1 = 11 - (soma % 11);
    if (dig1 >= 10) dig1 = 0;
    if (dig1 != (nums.charAt(9) - '0')) return false;

    soma = 0;
    for (int i = 0; i < 10; i++) soma += (nums.charAt(i) - '0') * (11 - i);
    int dig2 = 11 - (soma % 11);
    if (dig2 >= 10) dig2 = 0;
    return dig2 == (nums.charAt(10) - '0');
  }

  // retorna true se o telefone for valido ou estiver em branco (campo opcional)
  // aceita 10 digitos (fixo) ou 11 digitos (celular), ignorando formatacao
  public static boolean telefoneValido(String telefone) {
    if (telefone == null || telefone.isBlank()) return true;
    String nums = telefone.replaceAll("[^0-9]", "");
    return nums.length() == 10 || nums.length() == 11;
  }

  // retorna true se o email for valido ou estiver em branco (campo opcional)
  public static boolean emailValido(String email) {
    if (email == null || email.isBlank()) return true;
    return email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
  }

  // retorna true apenas se a uf corresponder a uma sigla de estado brasileiro
  public static boolean ufValida(String uf) {
    return uf != null && UFS_VALIDAS.contains(uf.toUpperCase());
  }
}
