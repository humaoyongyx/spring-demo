package issac.study.springdemo.core.config.audit;

import issac.study.springdemo.core.context.ContextHolder;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * @author humy6
 * @Date: 2019/7/19 15:42
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditAware implements AuditorAware<Integer> {
    @Override
    public Optional<Integer> getCurrentAuditor() {
        return ContextHolder.getUser().map(it -> it.getId());
    }
}
