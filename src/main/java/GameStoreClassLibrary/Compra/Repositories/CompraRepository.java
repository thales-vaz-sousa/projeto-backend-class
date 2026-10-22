// Define o pacote dos repositórios.
package GameStoreClassLibrary.Compra.Repositories;

// Importa o repositório do Spring Data JPA que já traz o CRUD pronto.
import org.springframework.data.jpa.repository.JpaRepository;

// Importa a entidade que este repositório acessa.
import GameStoreClassLibrary.Compra.Entities.Compra;

// CAMADA: Repository
// RESPONSABILIDADE: ler e gravar registros de Compra no banco.
// SITUAÇÃO: implementada. save, findById, findAll e delete vêm do JpaRepository.
public interface CompraRepository extends JpaRepository<Compra, Long> {
}
