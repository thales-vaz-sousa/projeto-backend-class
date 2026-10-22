// Define o pacote dos repositórios.
package GameStoreClassLibrary.Produto.Repositories;

// Importa o repositório do Spring Data JPA que já traz o CRUD pronto.
import org.springframework.data.jpa.repository.JpaRepository;

// Importa a entidade que este repositório acessa.
import GameStoreClassLibrary.Produto.Entities.Produto;

// CAMADA: Repository
// RESPONSABILIDADE: ler e gravar registros de Produto no banco.
// SITUAÇÃO: implementada. save, findById, findAll e delete vêm do JpaRepository.
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
