// Define o pacote dos repositórios.
package api.repository;

// Importa o repositório do Spring Data JPA que já traz o CRUD pronto.
import org.springframework.data.jpa.repository.JpaRepository;

// Importa a entidade que este repositório acessa.
import api.model.Cliente;

// CAMADA: Repository
// RESPONSABILIDADE: ler e gravar registros de Cliente no banco.
// SITUAÇÃO: implementada. save, findById, findAll e delete vêm do JpaRepository.
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
