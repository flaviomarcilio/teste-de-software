package br.com.flaviomarcilio.exceptions;

public class ProdutoNaoCadastradoException extends RuntimeException {

    public String getMessage() {
        return "O produto não está cadastrado.";
    }
}
