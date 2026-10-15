// Define o pacote da camada de serviço.
package api.service;

// CAMADA: Service
// RESPONSABILIDADE: avisar que um carrinho não existe. Herda de
//                   RecursoNaoEncontradoException, por isso o ApiExceptionHandler
//                   já devolve 404 sem precisar mudar nada.
// SITUAÇÃO: implementada.
public class CarrinhoNaoEncontradoException extends RecursoNaoEncontradoException {

    // Cria a exceção usando o id que não foi encontrado.
    public CarrinhoNaoEncontradoException(Long id) {
        // Envia a mensagem que vai aparecer no campo "mensagem" do ErroDTO.
        super("Carrinho não encontrado: " + id);
    }
}
