package com.srnrit.BMS;

import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.srnrit.BMS.dao.UserDao;
import com.srnrit.BMS.repository.UserRepository;


@SpringBootTest
class BmsApplicationTests {
	

	@Autowired
	UserDao userDAO;
	
	@Mock
	UserRepository userRepository ;
	
}
	


