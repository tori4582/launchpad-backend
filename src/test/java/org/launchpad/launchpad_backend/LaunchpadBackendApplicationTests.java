package org.launchpad.launchpad_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LaunchpadBackendApplicationTests {

	@Autowired
	TestService testService;

	@Test
	void testAop() {
		System.out.println(testService.userService_createNewUser().toString());
		System.out.println(testService.productService_createNewProduct().toString());
	}

}
