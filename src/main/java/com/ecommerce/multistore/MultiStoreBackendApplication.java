
/* ---- File: src/main/java/com/ecommerce/multistore/MultiStoreBackendApplication.java ---- */

package com.ecommerce.multistore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableTransactionManagement
public class MultiStoreBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiStoreBackendApplication.class, args);
    }
}

