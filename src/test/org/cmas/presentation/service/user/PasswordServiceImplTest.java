package org.cmas.presentation.service.user;

import org.junit.Test;




public class PasswordServiceImplTest {

    private final PasswordServiceImpl sut = new PasswordServiceImpl();

    @Test
    public void testGeneratePassword(){
        String password = sut.generatePassword();
        System.out.println(password);
        for(int i=33; i< 127 ;i++){
            System.out.print((char)i);
        }
		System.out.println();
		for(int i=48; i< 58 ;i++){
            System.out.print((char)i);
        }
		System.out.println();
		for(int i=65; i< 91 ;i++){
            System.out.print((char)i);
        }
		System.out.println();
		for(int i=97; i< 123 ;i++){
            System.out.print((char)i);
        }
        //StringUtils
        System.out.println();
        System.out.print((char)0x9);
        System.out.print((char)0x3c);
        System.out.print((char)0x04);
        System.out.print((char)0x84);
        System.out.print((char)0xaf);
    }
}
