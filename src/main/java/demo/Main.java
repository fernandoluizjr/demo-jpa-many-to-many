package demo;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.stereotype.Component;

import demo.domain.A;
import demo.domain.B;
import demo.repository.As;
import demo.repository.Bs;

@SpringBootApplication
public class Main implements CommandLineRunner {

	@Autowired
	private As as;

	@Autowired
	private Bs bs;

	@Component
	public static class ExposeEntityIdRestMvcConfiguration implements RepositoryRestConfigurer {

		@Override
		public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
			config.exposeIdsFor(A.class, B.class);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// 3 bs jah previamente inseridos
		// se tentar salvar em a um b nao salvo previamente da transient exception
		final B b1 = bs.save(new B("um B"));
		final B b2 = bs.save(new B("dois B"));
		final B b3 = bs.save(new B("tres B"));

		// modifica os 3 bs pra garantir q nao seram atualizados
		b1.setDesc(b1.getDesc() + " updated");
		b2.setDesc(b2.getDesc() + " updated");
		b3.setDesc(b3.getDesc() + " updated");

		// salva a1 com 2 bs
		final A aCom2Bs = new A("um A com 2 bs", b1, b2);
		final A savedA1 = as.save(aCom2Bs);

		// atualiza a1 com o terceiro b
		savedA1.getB().add(b3);
		savedA1.setDesc("a1 com 3 bs");
		as.save(savedA1);

		// atualiza a1 tirando um dos bs (nao usar remove do set, deve criar outro)
		savedA1.setB(savedA1.getB().stream().limit(2).collect(Collectors.toSet()));
		savedA1.setDesc("a1 com o 2 bs novamente");
		as.save(savedA1);

		// atualiza a1 tirando mais um dos bs
		savedA1.setB(savedA1.getB().stream().limit(1).collect(Collectors.toSet()));
		savedA1.setDesc("a1 com soh 1 b");
		as.save(savedA1);

		// remove a1
		as.delete(savedA1);
	}
}
