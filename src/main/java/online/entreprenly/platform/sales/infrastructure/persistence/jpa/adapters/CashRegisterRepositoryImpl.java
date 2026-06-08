package online.entreprenly.platform.sales.infrastructure.persistence.jpa.adapters;

import online.entreprenly.platform.sales.domain.model.aggregates.CashRegister;
import online.entreprenly.platform.sales.domain.repositories.CashRegisterRepository;
import online.entreprenly.platform.sales.infrastructure.persistence.jpa.assemblers.CashRegisterPersistenceAssembler;
import online.entreprenly.platform.sales.infrastructure.persistence.jpa.repositories.CashRegisterPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository adapter that bridges the cash register domain repository port with Spring Data JPA.
 */
@Repository
public class CashRegisterRepositoryImpl implements CashRegisterRepository {

    private final CashRegisterPersistenceRepository cashRegisterPersistenceRepository;

    public CashRegisterRepositoryImpl(CashRegisterPersistenceRepository cashRegisterPersistenceRepository) {
        this.cashRegisterPersistenceRepository = cashRegisterPersistenceRepository;
    }

    @Override
    public List<CashRegister> findAllByOwnerEmail(String ownerEmail) {
        return cashRegisterPersistenceRepository.findAllByOwnerEmail(ownerEmail).stream()
                .map(CashRegisterPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<CashRegister> findByIdAndOwnerEmail(Long id, String ownerEmail) {
        return cashRegisterPersistenceRepository.findByIdAndOwnerEmail(id, ownerEmail)
                .map(CashRegisterPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<CashRegister> findByDateAndOwnerEmail(LocalDate date, String ownerEmail) {
        return cashRegisterPersistenceRepository.findByDateAndOwnerEmail(date, ownerEmail)
                .map(CashRegisterPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsByDateAndOwnerEmail(LocalDate date, String ownerEmail) {
        return cashRegisterPersistenceRepository.existsByDateAndOwnerEmail(date, ownerEmail);
    }

    @Override
    public CashRegister save(CashRegister cashRegister) {
        var saved = cashRegisterPersistenceRepository.save(
                CashRegisterPersistenceAssembler.toPersistenceFromDomain(cashRegister));
        return CashRegisterPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
