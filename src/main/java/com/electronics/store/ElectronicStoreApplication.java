package com.electronics.store;
import com.electronics.store.entities.Role;
import com.electronics.store.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.UUID;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public void run(String... args) throws Exception {
		// ROLe admin Logic
		// role admin logic agar role ek baar database me rahega to baar baar declare nhi hoga role iske liye if condition me daala
		Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").orElse(null);
		if(roleAdmin==null){
			Role admin = Role.builder().roleId(UUID.randomUUID().toString()).name("ROLE_ADMIN").build();
			roleRepository.save(admin);
		}

		// role normal logic
		// role admin logic agar role ek baar database me rahega to baar baar declare nhi hoga role iske liye if condition me daala
		Role roleNormal = roleRepository.findByName("ROLE_NORMAL").orElse(null);
		if(roleNormal==null){
			Role normal = Role.builder().roleId(UUID.randomUUID().toString()).name("ROLE_NORMAL").build();
		}

	}

}