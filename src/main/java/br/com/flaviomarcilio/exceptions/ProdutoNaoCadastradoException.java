package br.com.flaviomarcilio.exceptions;

public class ProdutoNaoCadastradoException extends RuntimeException {

    public ProdutoNaoCadastradoException(String message) {
        super(message);
    }

    public ProdutoNaoCadastradoException() {
        super("O produto não está cadastrado.");
    }
}
